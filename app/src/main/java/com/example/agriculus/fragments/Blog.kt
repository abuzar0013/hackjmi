package com.example.agriculus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.agriculus.databinding.BlogBinding


class Blog :BaseFragment() {
    private val binding by lazy { BlogBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationVisibility(false)
        binding.wvMain.settings.javaScriptEnabled
        binding.wvMain.loadUrl("https://agriculusedu.netlify.app/")
        binding.wvMain.webViewClient = WebViewClient()
    }
}