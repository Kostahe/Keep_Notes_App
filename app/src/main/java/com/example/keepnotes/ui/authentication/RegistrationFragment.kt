package com.example.keepnotes.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.keepnotes.R
import com.example.keepnotes.appComponent
import com.example.keepnotes.data.model.User
import com.example.keepnotes.databinding.FragmentRegistrationBinding
import com.example.keepnotes.di.ViewModelFactory
import com.example.keepnotes.domain.repository.State
import javax.inject.Inject

class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.appComponent?.injectToRegistrationFragment(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthenticationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegistrationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            usernameEditText.addTextChangedListener {
                usernameEditTextLayout.error = null
            }

            emailEditText.addTextChangedListener {
                emailEditTextLayout.error = null
            }

            passwordEditText.addTextChangedListener {
                passwordEditTextLayout.error = null
            }

            viewModel.register.observe(viewLifecycleOwner) { state ->
                when (state) {
                    is State.Loading -> {
                        progressBar.visibility = View.VISIBLE
                    }

                    is State.Success -> {
                        progressBar.visibility = View.GONE
                        findNavController().navigate(R.id.action_registrationFragment_to_welcomeFragment2)
                    }

                    is State.Error -> {
                        progressBar.visibility = View.GONE
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                    }
                }

            }


            registrationButton.setOnClickListener {

                if (validation()) {
                    viewModel.register(
                        email = emailEditText.text.toString(),
                        password = passwordEditText.text.toString(),
                        user = User(
                            id = "",
                            username = usernameEditText.text.toString(),
                            email = emailEditText.text.toString()
                        )
                    )
                }

            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true
        binding.apply {

            if (usernameEditText.text.toString().trim().isEmpty()) {
                isValid = false
                usernameEditTextLayout.error = "Username can not be empty"
            } else if (usernameEditText.text.toString().trim().length < 3) {
                usernameEditTextLayout.error = "Username can not be less than 3 characters long"
            }

            if (emailEditText.text.isNullOrEmpty()) {
                isValid = false
                emailEditTextLayout.error = "Email can not be empty"

            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                    emailEditText.text.toString().trim()
                ).matches()
            ) {
                isValid = false
                emailEditTextLayout.error = "The email format is not correct"
            }

            if (passwordEditText.text.isNullOrEmpty()) {
                isValid = false
                passwordEditTextLayout.error = "Password can not be empty"
            } else if (passwordEditText.text.toString().length < 8) {
                isValid = false
                passwordEditTextLayout.error = "Password can not be less than 8 characters long"
            }
            return isValid
        }
    }


}