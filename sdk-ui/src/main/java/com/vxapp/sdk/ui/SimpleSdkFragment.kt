package com.vxapp.sdk.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.vxapp.sdk.session.SimpleSessionManager
import com.vxapp.sdk.session.SessionState

/**
 * Simple Fragment for SDK UI demonstration
 */
class SimpleSdkFragment : Fragment() {
    
    private lateinit var sessionManager: SimpleSessionManager
    private var onResultCallback: ((String) -> Unit)? = null
    
    private lateinit var inputText: EditText
    private lateinit var submitButton: Button
    private lateinit var statusText: TextView
    private lateinit var resultText: TextView
    
    companion object {
        fun newInstance(): SimpleSdkFragment {
            return SimpleSdkFragment()
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_simple_sdk, container, false)
        
        sessionManager = SimpleSessionManager(requireContext())
        
        inputText = view.findViewById(R.id.input_text)
        submitButton = view.findViewById(R.id.submit_button)
        statusText = view.findViewById(R.id.status_text)
        resultText = view.findViewById(R.id.result_text)
        
        setupUI()
        
        return view
    }
    
    private fun setupUI() {
        submitButton.setOnClickListener {
            val input = inputText.text.toString()
            if (input.isBlank()) {
                statusText.text = "Please enter some text"
                statusText.setTextColor(android.graphics.Color.RED)
                return@setOnClickListener
            }
            
            // Simulate processing
            statusText.text = "Processing..."
            statusText.setTextColor(android.graphics.Color.BLUE)
            
            // Update session last used
            sessionManager.updateLastUsed()
            
            // Simulate async processing
            submitButton.postDelayed({
                statusText.text = "Success!"
                statusText.setTextColor(android.graphics.Color.GREEN)
                resultText.text = "Processed: $input"
                onResultCallback?.invoke(input)
            }, 1000)
        }
        
        // Initialize status
        when (sessionManager.sessionState.value) {
            is SessionState.Authenticated -> {
                statusText.text = "Ready - User authenticated"
                statusText.setTextColor(android.graphics.Color.GREEN)
            }
            is SessionState.Unauthenticated -> {
                statusText.text = "Please login to use this feature"
                statusText.setTextColor(android.graphics.Color.RED)
            }
            is SessionState.Error -> {
                statusText.text = "Error: ${(sessionManager.sessionState.value as SessionState.Error).message}"
                statusText.setTextColor(android.graphics.Color.RED)
            }
            is SessionState.Loading -> {
                statusText.text = "Loading..."
                statusText.setTextColor(android.graphics.Color.BLUE)
            }
        }
    }
    
    fun setOnResultCallback(callback: (String) -> Unit) {
        this.onResultCallback = callback
    }
}
