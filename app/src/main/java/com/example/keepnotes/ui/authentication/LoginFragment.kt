package com.example.keepnotes.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.keepnotes.R
import com.example.keepnotes.appComponent
import com.example.keepnotes.databinding.FragmentLoginBinding
import com.example.keepnotes.di.ViewModelFactory
import com.example.keepnotes.domain.repository.State
import javax.inject.Inject


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.appComponent?.injectToLoginFragment(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthenticationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            loginButton.setOnClickListener {
                if (validation()) {
                    viewModel.login(
                        email = emailEditText.text.toString(),
                        password = passwordEditText.text.toString()
                    )
                }

                viewModel.login.observe(viewLifecycleOwner) { state ->
                    when (state) {
                        is State.Loading -> {
                            progressBar.visibility = View.VISIBLE
                        }

                        is State.Error -> {
                            progressBar.visibility = View.INVISIBLE
                        }

                        is State.Success -> {
                            progressBar.visibility = View.INVISIBLE
                            findNavController().navigate(R.id.action_loginFragment_to_noteListingFragment)
                        }
                    }
                }
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        binding.apply {
            if (emailEditText.text.toString().trim().isEmpty()) {
                isValid = false
            } else if (" " in emailEditText.text.toString()) {
                isValid = false
            } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString())
                    .matches()
            ) {
                isValid = false
            }

            if (passwordEditText.text.toString().trim().isEmpty()) {
                isValid = false
            } else if (" " in passwordEditText.text.toString()) {
                isValid = false
            } else if (passwordEditText.text.toString().length < 8) {
                isValid = false
            }

            return isValid
        }
    }
}