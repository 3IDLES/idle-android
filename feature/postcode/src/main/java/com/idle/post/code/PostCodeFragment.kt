package com.idle.post.code

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.idle.postcode.databinding.FragmentPostCodeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostCodeFragment : DialogFragment() {

    private var _binding: FragmentPostCodeBinding? = null
    private val binding get() = _binding!!
    var onDismissCallback: (() -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPostCodeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this.viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupWebView()
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissCallback?.invoke()
        super.onDismiss(dialog)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun setupWebView() {
        binding.postcodeWV.apply {
            settings.javaScriptEnabled = true
            clearCache(true)
            settings.cacheMode = WebSettings.LOAD_NO_CACHE

            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    view?.evaluateJavascript("execDaumPostcode();", null)
                }
            }
            webChromeClient = WebChromeClient()

            addJavascriptInterface(AndroidBridge(), "android")
            loadUrl("https://tgyuuan.github.io/DaumAddressApi")
        }
    }

    private inner class AndroidBridge {
        @JavascriptInterface
        fun onPostCodeReceived(roadNameAddress: String, lotNumberAddress: String) {
            lifecycleScope.launch(Dispatchers.Main) {
                findNavController().currentBackStackEntry?.savedStateHandle?.set(
                    key = "roadNameAddress",
                    value = roadNameAddress,
                )
                findNavController().currentBackStackEntry?.savedStateHandle?.set(
                    key = "lotNumberAddress",
                    value = lotNumberAddress,
                )

                dismiss()
            }
        }
    }
}