package com.example.keepnotes.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.keepnotes.R
import com.example.keepnotes.appComponent
import com.example.keepnotes.databinding.FragmentWelcomeBinding
import com.example.keepnotes.di.ViewModelFactory
import javax.inject.Inject

class WelcomeFragment : Fragment() {

    private lateinit var binding: FragmentWelcomeBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var authenticationViewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.appComponent?.injectToWelcomeFragment(this)
        authenticationViewModel = ViewModelProvider(this, viewModelFactory)[AuthenticationViewModel::class.java]
    }

    override fun onStart() {
        super.onStart()
        authenticationViewModel.getSession()?.let {
            findNavController().navigate(R.id.action_welcomeFragment_to_noteListingFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setupOnBackPressed()
        binding = FragmentWelcomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            registerButton.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_registrationFragment)
            }

            loginButton.setOnClickListener {
                findNavController().navigate(R.id.action_welcomeFragment_to_loginFragment)
            }
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    requireActivity().finish()
                }
            }
        })
    }

}