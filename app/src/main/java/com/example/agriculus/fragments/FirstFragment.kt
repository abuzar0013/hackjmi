package com.example.agriculus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.agriculus.databinding.FirstFragmentBinding
import com.example.agriculus.R
import com.google.firebase.auth.FirebaseAuth

class FirstFragment : BaseFragment() {
    private val binding by lazy { FirstFragmentBinding.inflate(layoutInflater) }
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSignIn.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment3_to_logInFragment2)
        }
        binding.btnSignUp.setOnClickListener {
            findNavController().navigate(R.id.action_firstFragment3_to_signUpFragment2)
        }
    }

}
