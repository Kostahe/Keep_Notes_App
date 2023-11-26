package com.example.keepnotes.ui.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.keepnotes.R
import com.example.keepnotes.appComponent
import com.example.keepnotes.databinding.FragmentForgotPasswordBinding
import com.example.keepnotes.di.ViewModelFactory
import com.example.keepnotes.domain.repository.State
import javax.inject.Inject


class ForgotPasswordFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: AuthenticationViewModel

    private lateinit var binding: FragmentForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.appComponent?.injectToForgotPasswordFragment(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[AuthenticationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            emailEditText.addTextChangedListener {
                emailEditTextLayout.error = null
            }

            sendButton.setOnClickListener {
                if (validation()) {
                    viewModel.forgotPassword(
                        email = emailEditText.text.toString()
                    )
                }
            }
        }
        observer()
    }

    private fun validation(): Boolean {
        var isValid = true
        binding.apply {
            if (emailEditText.text.toString().trim().isEmpty()) {
                isValid = false
                emailEditTextLayout.error = getString(R.string.email_can_not_be_empty)

            } else if (" " in emailEditText.text.toString()) {
                isValid = false
                emailEditTextLayout.error = getString(R.string.email_can_not_contain_spaces)
            } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(
                    emailEditText.text.toString().trim()
                ).matches()
            ) {
                isValid = false
                emailEditTextLayout.error = getString(R.string.the_email_format_is_not_correct)
            }
        }
        return isValid
    }

    private fun observer() {
        viewModel.forgotPassword.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is State.Error -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(context, state.message, Toast.LENGTH_SHORT).show()
                }

                is State.Success -> {
                    binding.progressBar.visibility = View.INVISIBLE
                    Toast.makeText(context, state.data, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}