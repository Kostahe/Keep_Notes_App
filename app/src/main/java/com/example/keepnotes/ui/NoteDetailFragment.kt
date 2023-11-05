package com.example.keepnotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.keepnotes.appComponent
import com.example.keepnotes.data.model.Note
import com.example.keepnotes.databinding.FragmentNoteDetailBinding
import com.example.keepnotes.di.ViewModelFactory
import com.example.keepnotes.domain.repository.State
import java.util.Date
import javax.inject.Inject

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: NoteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.appComponent?.injectToNoteDetailFragment(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setupOnBackPressed()
        binding = FragmentNoteDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonBack.setOnClickListener {
            if (validation()) {
                viewModel.addNote(
                    Note(
                        id = "",
                        title = binding.titleEditText.text.toString(),
                        text = binding.noteEditText.text.toString(),
                        date = Date()
                    )
                )
            }
            findNavController().navigateUp()
        }
        viewModel.addedNote.observe(viewLifecycleOwner) { state ->
            when(state) {
                is State.Loading -> {

                }
                is State.Success -> {

                }
                is State.Error -> {

                }
            }
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.titleEditText.text.toString().isEmpty() and binding.noteEditText.text.toString().isEmpty()) {
            isValid = false
            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_LONG).show()
        }
        return isValid
    }
}