package com.example.keepnotes.ui.note

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.keepnotes.R
import com.example.keepnotes.appComponent
import com.example.keepnotes.data.model.Note
import com.example.keepnotes.databinding.FragmentNoteDetailBinding
import com.example.keepnotes.di.ViewModelFactory
import com.example.keepnotes.domain.repository.State
import com.example.keepnotes.ui.authentication.AuthenticationViewModel
import com.example.keepnotes.util.NavigationConstants
import java.util.Date
import javax.inject.Inject

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: NoteViewModel
    private lateinit var authViewModel: AuthenticationViewModel

    var note: Note? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.appComponent?.injectToNoteDetailFragment(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]
        authViewModel = ViewModelProvider(this, viewModelFactory)[AuthenticationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        setupOnBackPressed()
        binding = FragmentNoteDetailBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateNoteFields()
        binding.backButton.setOnClickListener {
            saveOrUpdateNote()
            findNavController().navigateUp()
        }

        binding.deleteButton.setOnClickListener {
            note?.let { note ->
                viewModel.deleteNote(note)
            } ?: findNavController().navigateUp()
        }
        observe()
    }

    private fun updateNoteFields() {
        getUpdatedNote()?.let {
            binding.titleEditText.setText(note?.title)
            binding.noteEditText.setText(note?.text)
        }
    }

    private fun getUpdatedNote(): Note? {
        val type = arguments?.getString(NavigationConstants.TYPE)
        type?.let {
            if (type == NavigationConstants.UPDATE) {
                note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable(NavigationConstants.NOTE, Note::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    arguments?.getParcelable(NavigationConstants.NOTE) as? Note
                }
                return note
            }
        }
        return null
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    saveOrUpdateNote()
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun saveOrUpdateNote() {
        var updatedNote = getUpdatedNote()
        if (validation()) {
            if (updatedNote == null) {
                val newNote = Note(
                    id = "",
                    title = binding.titleEditText.text.toString(),
                    text = binding.noteEditText.text.toString(),
                    date = Date(),
                    userId = authViewModel.getSession()?.id ?: ""
                )
                viewModel.addNote(newNote)
            } else if (!isNoteUnchanged()) {
                updatedNote = updatedNote.copy(
                    title = binding.titleEditText.text.toString(),
                    text = binding.noteEditText.text.toString(),
                    date = Date()
                )
                viewModel.updateNote(updatedNote)
            }
        } else {
            if (!isNoteUnchanged() && updatedNote != null) {
                Toast.makeText(context, getString(R.string.changes_were_discarded), Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, getString(R.string.empty_note_was_discarded), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun validation(): Boolean = binding.titleEditText.text.toString().isNotEmpty() || binding.noteEditText.text.toString().isNotEmpty()

    private fun isNoteUnchanged(): Boolean = binding.titleEditText.text.toString() == note?.title && binding.noteEditText.text.toString() == note?.text

    private fun observe() {
        viewModel.addedNote.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.Success -> {
                    findNavController().navigateUp()
                }
                is State.Error -> {
                    findNavController().navigateUp()
                    Toast.makeText(context, "Error happened note didn't create", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.updatedNote.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.Success -> {
                    findNavController().navigateUp()
                }
                is State.Error -> {
                    findNavController().navigateUp()
                    Toast.makeText(context, "Error happened note didn't update", Toast.LENGTH_SHORT).show()
                }
            }
        }

        viewModel.deletedNote.observe(viewLifecycleOwner) { state ->
            when (state) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.Success -> {
                    findNavController().navigateUp()
                }
                is State.Error -> {
                    findNavController().navigateUp()
                    Toast.makeText(context, "Error happened note didn't delete", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}