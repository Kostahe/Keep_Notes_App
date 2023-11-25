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
import com.example.keepnotes.appComponent
import com.example.keepnotes.data.model.Note
import com.example.keepnotes.databinding.FragmentNoteDetailBinding
import com.example.keepnotes.di.ViewModelFactory
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
        authViewModel =
            ViewModelProvider(this, viewModelFactory)[AuthenticationViewModel::class.java]
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
//            viewModel.
        }
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
        if (validation()) {
            if (getUpdatedNote() == null) {
                viewModel.addNote(
                    Note(
                        id = "",
                        title = binding.titleEditText.text.toString(),
                        text = binding.noteEditText.text.toString(),
                        date = Date(),
                        userId = authViewModel.getSession()?.id ?: ""
                    )
                )
            } else if(isNoteUnchanged()) {
                Toast.makeText(context, "Note didn't changed", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.updateNote(
                    Note(
                        id = note?.id ?: "",
                        title = binding.titleEditText.text.toString(),
                        text = binding.noteEditText.text.toString(),
                        date = Date(),
                        userId = authViewModel.getSession()?.id ?: ""
                    )
                )
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.titleEditText.text.toString().isEmpty() and binding.noteEditText.text.toString().isEmpty()) {
            isValid = false
            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_LONG).show()
        }
        return isValid
    }

    private fun isNoteUnchanged(): Boolean = binding.titleEditText.text.toString() == note?.title && binding.noteEditText.text.toString() == note?.text
}