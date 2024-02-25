package com.example.agriculus.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.agriculus.databinding.LoginFragmentBinding
import com.example.agriculus.R
import com.google.firebase.auth.FirebaseAuth

class LogInFragment:BaseFragment() {
    private val binding by lazy { LoginFragmentBinding.inflate(layoutInflater) }
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
        firebaseAuth = FirebaseAuth.getInstance()
        binding.Notuser.setOnClickListener {
            findNavController().navigate(R.id.action_logInFragment2_to_signUpFragment2)
        }
        binding.btnSignIn.setOnClickListener {
            showLoader()
            val email = binding.EmailAddress.text.toString()
            val pass = binding.TextPassword.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {
                firebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful){
                        hideLoader()
                        findNavController().navigate(R.id.action_logInFragment2_to_dashboard)
                    }else{
                        hideLoader()
                        showToast(it.exception.toString())
                    }
                }
            }else{
                hideLoader()
                showToast("Empty fields are not allowed")
            }
        }

    }

    private fun showLoader(){
        binding.progressBar.visibility = View.VISIBLE
        binding.text.visibility =View.GONE
    }
    private fun hideLoader(){
        binding.progressBar.visibility = View.GONE
        binding.text.visibility =View.VISIBLE
    }
}
