package com.example.keepnotes.ui.note

import android.os.Build
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
import java.util.Date
import javax.inject.Inject

class NoteDetailFragment : Fragment() {

    private lateinit var binding: FragmentNoteDetailBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: NoteViewModel

    var note: Note? = null

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
        getNote()
        binding.buttonBack.setOnClickListener {
            addNote()
            findNavController().navigateUp()
        }
    }

    private fun getNote() {
        val type = arguments?.getString("type")
        type.let {
            if (type == "update") {
                note = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable("note", Note::class.java)
                } else {
                    @Suppress("DEPRECATION")
                    arguments?.getParcelable("note") as? Note
                }

                binding.titleEditText.setText(note?.title)
                binding.noteEditText.setText(note?.text)
            }
        }
    }

    private fun setupOnBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isEnabled) {
                    addNote()
                    findNavController().navigateUp()
                }
            }
        })
    }

    private fun addNote() {
        if (validation()) {
            if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable("note", Note::class.java) == null
                } else {
                    @Suppress("DEPRECATION")
                    arguments?.getParcelable("note") as? Note == null
                }
            ) {
                viewModel.addNote(
                    Note(
                        id = "",
                        title = binding.titleEditText.text.toString(),
                        text = binding.noteEditText.text.toString(),
                        date = Date()
                    )
                )
            } else {
                viewModel.updateNote(
                    Note(
                        id = note?.id ?: "",
                        title = binding.titleEditText.text.toString(),
                        text = binding.noteEditText.text.toString(),
                        date = Date()
                    )
                )
            }
        }
    }

    private fun validation(): Boolean {
        var isValid = true

        if (binding.titleEditText.text.toString().isEmpty() and binding.noteEditText.text.toString()
                .isEmpty()
        ) {
            isValid = false
            Toast.makeText(context, "Empty note discarded", Toast.LENGTH_LONG).show()
        }
        return isValid
    }
}