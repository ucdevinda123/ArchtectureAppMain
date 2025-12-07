
#!/usr/bin/env python3
import json
import re
from typing import Optional

# ---------- file paths (string-based, relative) ----------

COLOR_BASE = "../data/00_base/color.json"
COLOR_LIGHT = "../data/01_system/color_light.json"
COLOR_DARK = "../data/01_system/color_dark.json"
DATA_VIS_LIGHT = "../data/01_system/data_vis_light.json"
DATA_VIS_DARK = "../data/01_system/data_vis_dark.json"
OPACITY_BASE = "../data/00_base/opacity.json"
OPACITY_SYS = "../data/01_system/opacity2.json"
THEME_FILE = "ThemeV1.kt"
OUT_FILE = "ThemeV1.generated.kt"

# ---------- helpers ----------

def hex_to_argb(hex_str: str) -> str:
    h = hex_str.strip().lstrip("#")
    if len(h) == 6:
        return "0xFF" + h.upper()
    if len(h) == 8:
        return "0x" + h.upper()
    raise ValueError(f"Unexpected hex length for {hex_str}")


def camel_name(parts):
    if not parts:
        return ""
    first = parts[0].lower()
    rest = [p.capitalize() for p in parts[1:]]
    return first + "".join(rest)


def flatten_base_colors(base: dict):
    flat = {}
    for family, payload in base.items():
        if family == "$type":
            continue
        for key, v in payload.items():
            if key == "$type":
                continue
            val = v.get("$value")
            name = f"{family}{key}"
            flat[name] = val
    return flat


def build_base_colors_object(base_colors: dict) -> str:
    lines = []
    lines.append("object BaseColors {")
    for name in sorted(
        base_colors.keys(),
        key=lambda n: (re.sub(r"\d+", "", n), int(re.search(r"\d+", n).group(0)) if re.search(r"\d+", n) else 0),
    ):
        hex_val = base_colors[name]
        argb = hex_to_argb(hex_val)
        prop_name = re.sub(r"[^A-Za-z0-9]", "", name)
        lines.append(f"    val {prop_name}: Color = Color({argb})")
    lines.append("}")
    return "\n".join(lines)


def flatten_values(tree, prefix=None):
    if prefix is None:
        prefix = []
    flat = {}
    if isinstance(tree, dict):
        if "$value" in tree:
            key = ".".join(prefix)
            flat[key] = tree["$value"]
        else:
            for k, v in tree.items():
                if k.startswith("$"):
                    continue
                flat.update(flatten_values(v, prefix + [k]))
    return flat


BASE_REF_RE = re.compile(r"^\{base\.([a-zA-Z0-9_]+)\.([0-9]+)\}$")
OPACITY_REF_RE = re.compile(r"^\{opacity\.([0-9]+)\}$")
RGBA_REF_RE = re.compile(r"^rgba\(\s*(\{base\.[^,]+?\})\s*,\s*(.+)\)$", re.IGNORECASE)
PERCENT_RE = re.compile(r"^\s*([0-9]+(?:\.[0-9]+)?)\s*%\s*$")


def base_ref_to_kotlin(ref: str) -> str:
    ref = ref.strip()
    m = BASE_REF_RE.match(ref)
    if not m:
        return "BaseColors.neutral200"
    family, num = m.groups()
    return f"BaseColors.{family}{num}"


def format_single_decimal(value: float) -> str:
    # produces e.g. 0.0f, 0.1f, 0.2f, 1.0f
    return f"{value:.1f}f"


def percent_to_float_literal(s: str) -> Optional[str]:
    m = PERCENT_RE.match(s)
    if not m:
        return None
    value = float(m.group(1)) / 100.0
    return format_single_decimal(value)


def opacity_ref_to_kotlin(ref: str) -> Optional[str]:
    ref = ref.strip()
    m = OPACITY_REF_RE.match(ref)
    if not m:
        return None
    num = m.group(1)
    return f"BaseOpacity.opacity{num}"


def path_to_field_name(path: str) -> str:
    parts = []
    for segment in path.split("."):
        segment = segment.replace("_", " ").replace("-", " ")
        parts.extend(segment.split())
    return camel_name(parts[:4])


# ---------- opacity generation ----------

def build_opacity_base(opacity_json: dict) -> str:
    """
    From opacity.json -> object Opacity { val opacity20: Float = 0.2f ... }
    """
    payload = opacity_json["opacity"]
    lines = ["object Opacity {"]
    numeric_keys = [k for k in payload.keys() if k.isdigit()]
    for key in sorted(numeric_keys, key=lambda x: int(x)):
        v = payload[key]
        raw = v["$value"]  # "16%" etc.
        fl = percent_to_float_literal(raw)
        if fl is None:
            fl = "0.0f"
        lines.append(f"    val opacity{key}: Float = {fl}")
    lines.append("}")
    return "\n".join(lines)


def build_base_opacity(opacity_sys_json: dict) -> str:
    """
    From opacity2.json -> object BaseOpacity { val opacity40: Float = Opacity.opacity40 ... }
    """
    sys_op = opacity_sys_json["system"]["opacity"]
    levels = flatten_values(sys_op)
    lines = ["object BaseOpacity {"]
    for path, ref in sorted(levels.items()):
        op_ref = opacity_ref_to_kotlin(ref)
        if not op_ref:
            continue
        m = OPACITY_REF_RE.match(ref.strip())
        if m:
            key = m.group(1)
            field_name = f"opacity{key}"
        else:
            last = path.split(".")[-1]
            field_name = camel_name([last])
        lines.append(f"    val {field_name}: Float = {op_ref}")
    lines.append("}")
    return "\n".join(lines)


# ---------- color field collection (with opacity) ----------

def parse_value_to_kotlin(val_raw: str) -> str:
    """
    Handle:
      {base.xxx.yyy}
      rgba({base.xxx.yyy}, {opacity.nn})
      rgba({base.xxx.yyy}, 12%)
    """
    val = val_raw.strip()
    if val.lower().startswith("rgba"):
        m = RGBA_REF_RE.match(val)
        if not m:
            return base_ref_to_kotlin(val)
        base_part, alpha_part = m.groups()
        base_expr = base_ref_to_kotlin(base_part)

        op_ref = opacity_ref_to_kotlin(alpha_part)
        if op_ref:
            return f"{base_expr}.copy(alpha = {op_ref})"

        perc = percent_to_float_literal(alpha_part)
        if perc:
            return f"{base_expr}.copy(alpha = {perc})"

        return base_expr

    return base_ref_to_kotlin(val)


def collect_field_specs(
    light_json: dict,
    dark_json: dict,
    data_vis_light_json: dict,
    data_vis_dark_json: dict,
):
    sys_light_flat = flatten_values(light_json.get("system", {}))
    sys_dark_flat = flatten_values(dark_json.get("system", {}))
    dv_light_flat = flatten_values(
        data_vis_light_json.get("system", {}).get("data_vis", {})
    )
    dv_dark_flat = flatten_values(
        data_vis_dark_json.get("system", {}).get("data_vis", {})
    )

    all_keys = list(sys_light_flat.keys()) + list(dv_light_flat.keys())
    seen = set()
    ordered_paths = []
    for k in all_keys:
        if k in seen:
            continue
        seen.add(k)
        ordered_paths.append(k)
        if len(ordered_paths) >= 300:
            break

    field_specs = []
    for path in ordered_paths:
        fname = path_to_field_name(path)
        if not fname:
            continue

        light_val_raw = dv_light_flat.get(path) or sys_light_flat.get(path)
        dark_val_raw = dv_dark_flat.get(path) or sys_dark_flat.get(path)
        if not light_val_raw or not dark_val_raw:
            continue

        light_expr = parse_value_to_kotlin(light_val_raw)
        dark_expr = parse_value_to_kotlin(dark_val_raw)

        field_specs.append((fname, light_expr, dark_expr))

    return field_specs[:300]


def build_color_set_data_classes(field_specs):
    set1 = [fs[0] for fs in field_specs[0:100]]
    set2 = [fs[0] for fs in field_specs[100:200]]
    set3 = [fs[0] for fs in field_specs[200:300]]

    chunks = []

    def build_one(cls, names):
        if not names:
            return
        chunks.append(f"data class {cls}(")
        for i, name in enumerate(names):
            comma = "," if i < len(names) - 1 else ""
            chunks.append(f"    val {name}: Color{comma}")
        chunks.append(")")
        chunks.append("")

    build_one("ColorSetOne", set1)
    build_one("ColorSetTwo", set2)
    build_one("ColorSetThree", set3)

    return "\n".join(chunks).rstrip()


# ---------- Kotlin injection ----------

def extend_cfe_palette_ctor(src: str) -> str:
    pattern = r"class\s+CFEColorPalette\s*\(\s*"
    m = re.search(pattern, src)
    if not m:
        return src

    start = m.end()
    depth = 1
    i = start
    while i < len(src) and depth > 0:
        if src[i] == "(":
            depth += 1
        elif src[i] == ")":
            depth -= 1
        i += 1

    ctor_body = src[start : i - 1]
    tail = src[i - 1 :]

    ctor_body = ctor_body.rstrip().rstrip(",")
    injection = (
        ctor_body
        + ",\n\nval colorSetOne: ColorSetOne,\n"
        + "val colorSetTwo: ColorSetTwo,\n"
        + "val colorSetThree: ColorSetThree\n"
    )

    return src[:start] + injection + tail


def inject_sets_into_palettes(src: str, field_specs):
    set1 = field_specs[0:100]
    set2 = field_specs[100:200]
    set3 = field_specs[200:300]

    def build_set_block(cls_name, specs, use_light: bool):
        lines = [f"    {cls_name} = {cls_name}("]
        for i, (fname, light_expr, dark_expr) in enumerate(specs):
            expr = light_expr if use_light else dark_expr
            comma = "," if i < len(specs) - 1 else ""
            lines.append(f"        {fname} = {expr}{comma}")
        lines.append("    ),")
        return "\n".join(lines)

    def inject_into_palette(palette_name: str, text: str, use_light: bool):
        pat = rf"private\s+val\s+{palette_name}\s*=\s*CFEColorPalette\("
        m = re.search(pat, text)
        if not m:
            return text
        start = m.end()
        depth = 1
        i = start
        while i < len(text) and depth > 0:
            if text[i] == "(":
                depth += 1
            elif text[i] == ")":
                depth -= 1
            i += 1

        body = text[start : i - 1]
        body = body.rstrip().rstrip(",")

        extra_blocks = []
        if set1:
            extra_blocks.append(build_set_block("ColorSetOne", set1, use_light))
        if set2:
            extra_blocks.append(build_set_block("ColorSetTwo", set2, use_light))
        if set3:
            extra_blocks.append(build_set_block("ColorSetThree", set3, use_light))

        new_body = body + ",\n\n" + "\n\n".join(extra_blocks)
        return text[:start] + new_body + text[i - 1 :]

    src = inject_into_palette("CFELightColorPalette", src, use_light=True)
    src = inject_into_palette("CFEDarkColorPalette", src, use_light=False)
    return src


# ---------- main ----------

def main():
    base_json = json.loads(open(COLOR_BASE, encoding="utf-8").read())
    light_json = json.loads(open(COLOR_LIGHT, encoding="utf-8").read())
    dark_json = json.loads(open(COLOR_DARK, encoding="utf-8").read())
    data_vis_light_json = json.loads(open(DATA_VIS_LIGHT, encoding="utf-8").read())
    data_vis_dark_json = json.loads(open(DATA_VIS_DARK, encoding="utf-8").read())
    opacity_json = json.loads(open(OPACITY_BASE, encoding="utf-8").read())
    opacity_sys_json = json.loads(open(OPACITY_SYS, encoding="utf-8").read())

    base_flat = flatten_base_colors(base_json["base"])
    base_obj = build_base_colors_object(base_flat)

    opacity_base_obj = build_opacity_base(opacity_json)
    base_opacity_obj = build_base_opacity(opacity_sys_json)

    field_specs = collect_field_specs(
        light_json, dark_json, data_vis_light_json, data_vis_dark_json
    )
    color_sets_code = build_color_set_data_classes(field_specs)

    theme_src = open(THEME_FILE, encoding="utf-8").read()

    inject_point = theme_src.find("class CFEColorPalette")
    if inject_point == -1:
        raise RuntimeError("class CFEColorPalette not found in ThemeV1.kt")

    prefix = theme_src[:inject_point]
    suffix = theme_src[inject_point:]

    header_block = (
        prefix.rstrip()
        + "\n\n"
        + base_obj
        + "\n\n"
        + opacity_base_obj
        + "\n\n"
        + base_opacity_obj
        + "\n\n"
        + color_sets_code
        + "\n\n"
    )

    suffix2 = extend_cfe_palette_ctor(suffix)
    suffix3 = inject_sets_into_palettes(suffix2, field_specs)

    with open(OUT_FILE, "w", encoding="utf-8") as f:
        f.write(header_block + suffix3)
    print(f"Generated {OUT_FILE}")


if __name__ == "__main__":
    main()
