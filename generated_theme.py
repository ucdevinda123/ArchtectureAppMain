import json
import re

BASE_FILES = [
    "color.json",
    "color_light.json",
    "color_dark.json",
    "data_vis_light.json",
    "data_vis_dark.json"
]
THEME_KT_IN = "ThemeV1.kt"
THEME_KT_OUT = "Theme2.generated.kt"

BASE_REF_RE = re.compile(r"^\{base\.([a-zA-Z0-9_]+)\.([0-9]+)\}$")

def argb_from_hex(hex_color: str) -> str:
    hex_color = hex_color.strip()
    return "0xFF" + hex_color[1:].upper()

def flatten_base_colors(base_json):
    result = {}
    for hue, entries in base_json["base"].items():
        if hue == "$type": continue
        for step, meta in entries.items():
            if step == "$type": continue
            key = f"{hue}{step}"
            hex_color = meta["$value"]
            result[key] = argb_from_hex(hex_color)
    return result

def collect_color_tokens(data, prefix=""):
    items = []
    if isinstance(data, dict):
        for k, v in data.items():
            if k.startswith("$"): continue
            new_prefix = f"{prefix}.{k}" if prefix else k
            if isinstance(v, dict) and "$value" in v:
                items.append((new_prefix, v["$value"]))
            else:
                items.extend(collect_color_tokens(v, new_prefix))
    return items

def to_camel_name(path: str) -> str:
    # Split by dots/underscores/hyphens, convert to valid Kotlin lowerCamelCase, and remove invalid chars.
    segments = re.split(r"[\.\-_]+", path)
    cleaned = []
    for i, seg in enumerate(segments):
        seg = re.sub(r"[^a-zA-Z0-9]", "", seg)
        if seg == "":
            continue
        if i == 0:
            cleaned.append(seg[:1].lower() + seg[1:])
        else:
            cleaned.append(seg[:1].upper() + seg[1:])
    prop = ''.join(cleaned)
    if not prop: prop = "colorUnknown"
    if re.match(r"^[0-9]", prop):
        prop = "color" + prop
    return prop

def ensure_unique_names(props):
    name_counts = {}
    result = []
    for prop, expr, value in props:
        orig_prop = prop
        count = name_counts.get(prop, 0)
        if count != 0:
            # if duplicate, append running number to guarantee uniqueness
            prop = f"{prop}{count+1}"
        name_counts[orig_prop] = count + 1
        result.append((prop, expr, value))
    return result

def map_token_to_base_ref(token_value: str):
    m = BASE_REF_RE.match(token_value.strip())
    if not m: return None
    hue, step = m.group(1), m.group(2)
    return f"{hue}{step}"

def build_color_sets(light_sources, dark_sources, base_colors):
    all_items = []
    for src_name, data in light_sources.items():
        for path, value in collect_color_tokens(data):
            all_items.append(("light", src_name, path, value))
    all_items.sort(key=lambda x: x[2])
    seen_paths, unique_items = set(), []
    for kind, src, path, value in all_items:
        if path not in seen_paths:
            seen_paths.add(path)
            unique_items.append((kind, src, path, value))
    unique_items = unique_items[:300]
    def build_data_class_props(items_slice):
        props = []
        for _, _, path, value in items_slice:
            prop_name = to_camel_name(path)
            base_ref_key = map_token_to_base_ref(value)
            val_expr = f"BaseColors.{base_ref_key}" if (base_ref_key and base_ref_key in base_colors) else "Color(0x00000000)"
            props.append((prop_name, val_expr, value))
        props = ensure_unique_names(props)
        while len(props) < 100:
            idx = len(props) + 1
            props.append((f"placeholder{idx}", "Color(0x00000000)", None))
        return props[:100]
    # 100, 100, 100
    set1 = build_data_class_props(unique_items[:100])
    set2 = build_data_class_props(unique_items[100:200])
    set3 = build_data_class_props(unique_items[200:300])
    # Now collect dark versions for the exact same paths if possible
    dark_items_map = {}
    for src_name, data in dark_sources.items():
        for path, value in collect_color_tokens(data):
            dark_items_map[path] = value
    def dark_props(items_slice):
        props = []
        for _, _, path, _ in items_slice:
            prop_name = to_camel_name(path)
            dval = dark_items_map.get(path)
            base_ref_key = map_token_to_base_ref(dval) if dval else None
            val_expr = f"BaseColors.{base_ref_key}" if (base_ref_key and base_ref_key in base_colors) else "Color(0x00000000)"
            props.append((prop_name, val_expr, dval))
        props = ensure_unique_names(props)
        while len(props) < 100:
            idx = len(props) + 1
            props.append((f"placeholder{idx}", "Color(0x00000000)", None))
        return props[:100]
    set1_dark = dark_props(unique_items[:100])
    set2_dark = dark_props(unique_items[100:200])
    set3_dark = dark_props(unique_items[200:300])
    return (set1, set2, set3, set1_dark, set2_dark, set3_dark)

def generate_base_colors_object(base_colors):
    lines = []
    lines.append("object BaseColors {")
    for key in sorted(base_colors.keys()):
        lines.append(f"    val {key}: Color = Color({base_colors[key]})")
    lines.append("}")
    return "\n".join(lines)

def generate_color_set_data_class(name, props):
    props_str = ",\n".join([f"    val {prop}: Color" for prop, _, _ in props])
    return f"data class {name}(\n{props_str}\n)\n"

def generate_color_set_ctor(name, props):
    lines = []
    for prop, expr, _ in props:
        lines.append(f"{prop} = {expr}")
    return f"{name}(\n    " + ",\n    ".join(lines) + "\n)"

def main():
    # Load JSON files
    with open("color.json") as f: color_base_json = json.load(f)
    with open("color_light.json") as f: color_light_json = json.load(f)
    with open("color_dark.json") as f: color_dark_json = json.load(f)
    with open("data_vis_light.json") as f: data_vis_light_json = json.load(f)
    with open("data_vis_dark.json") as f: data_vis_dark_json = json.load(f)

    base_colors = flatten_base_colors(color_base_json)

    (set1, set2, set3, set1_dark, set2_dark, set3_dark) = build_color_sets(
        {"color_light": color_light_json, "data_vis_light": data_vis_light_json},
        {"color_dark": color_dark_json, "data_vis_dark": data_vis_dark_json},
        base_colors
    )

    base_colors_block = generate_base_colors_object(base_colors)
    color_set_one_dc = generate_color_set_data_class("ColorSetOne", set1)
    color_set_two_dc = generate_color_set_data_class("ColorSetTwo", set2)
    color_set_three_dc = generate_color_set_data_class("ColorSetThree", set3)

    color_set_one_light_ctor = generate_color_set_ctor("ColorSetOne", set1)
    color_set_two_light_ctor = generate_color_set_ctor("ColorSetTwo", set2)
    color_set_three_light_ctor = generate_color_set_ctor("ColorSetThree", set3)

    color_set_one_dark_ctor = generate_color_set_ctor("ColorSetOne", set1_dark)
    color_set_two_dark_ctor = generate_color_set_ctor("ColorSetTwo", set2_dark)
    color_set_three_dark_ctor = generate_color_set_ctor("ColorSetThree", set3_dark)

    theme_src = open(THEME_KT_IN, encoding="utf-8").read()

    # Replace BaseColors object
    theme_src = re.sub(
        r"object BaseColors\s*\{[^}]*\}",
        base_colors_block,
        theme_src,
        flags=re.DOTALL,
    )
    # Add data classes if missing (place after RiskAnalytics)
    if "data class ColorSetOne" not in theme_src:
        insertion_point = theme_src.find("data class RiskAnalytics")
        theme_src = (
            theme_src[:insertion_point]
            + color_set_one_dc
            + "\n"
            + color_set_two_dc
            + "\n"
            + color_set_three_dc
            + "\n"
            + theme_src[insertion_point:]
        )
    # Replace colorSetOne/Two/Three in palettes (light first, then dark)
    theme_src = re.sub(
        r"colorSetOne\s*=\s*ColorSetOne\([^)]*\)",
        "colorSetOne = " + color_set_one_light_ctor,
        theme_src,
        flags=re.DOTALL,
        count=1,
    )
    theme_src = re.sub(
        r"colorSetTwo\s*=\s*ColorSetTwo\([^)]*\)",
        "colorSetTwo = " + color_set_two_light_ctor,
        theme_src,
        flags=re.DOTALL,
        count=1,
    )
    theme_src = re.sub(
        r"colorSetThree\s*=\s*ColorSetThree\([^)]*\)",
        "colorSetThree = " + color_set_three_light_ctor,
        theme_src,
        flags=re.DOTALL,
        count=1,
    )
    # For CFEDarkColorPalette, replace next occurrence only
    theme_src = re.sub(
        r"colorSetOne\s*=\s*ColorSetOne\([^)]*\)",
        "colorSetOne = " + color_set_one_dark_ctor,
        theme_src,
        flags=re.DOTALL,
        count=1,
    )
    theme_src = re.sub(
        r"colorSetTwo\s*=\s*ColorSetTwo\([^)]*\)",
        "colorSetTwo = " + color_set_two_dark_ctor,
        theme_src,
        flags=re.DOTALL,
        count=1,
    )
    theme_src = re.sub(
        r"colorSetThree\s*=\s*ColorSetThree\([^)]*\)",
        "colorSetThree = " + color_set_three_dark_ctor,
        theme_src,
        flags=re.DOTALL,
        count=1,
    )

    with open(THEME_KT_OUT, "w", encoding="utf-8") as f:
        f.write(theme_src)
    print(f"Wrote {THEME_KT_OUT}")

if __name__ == "__main__":
    main()
