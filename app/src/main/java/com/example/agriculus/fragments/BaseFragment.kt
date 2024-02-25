package com.example.agriculus.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.agriculus.activities.Dashboard
import com.example.agriculus.databinding.BaseFragmentBinding

open class BaseFragment :Fragment() {
    private val binding by lazy { BaseFragmentBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }
    fun showToast(string: String) {
        Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
    }

    fun navigationVisibility(isVisible :Boolean){
        (activity as Dashboard).navigationVisibility(isVisible)
    }

    fun openBot(){
        val urlIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://mediafiles.botpress.cloud/5c7134b1-85cf-4ab7-b7f2-0624975c35c1/webchat/bot.html"))
        startActivity(urlIntent)
    }
}