package com.example.agriculus.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.agriculus.databinding.HomeFragmentBinding
import com.example.agriculus.R

class HomeFragment: BaseFragment(){
    private val binding by lazy { HomeFragmentBinding.inflate(layoutInflater) }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigationVisibility(true)
        binding.btnhmgrdn.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_blogHomeGarden)
        }
        binding.btncashcrp.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_cashCropFragment)
        }

        binding.btnpredict.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_camFragment2)
        }
        binding.btnfertilizer.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment2_to_fertilizerFragment)
        }
        binding.btnChat.setOnClickListener {
            openBot()
        }
    }


}