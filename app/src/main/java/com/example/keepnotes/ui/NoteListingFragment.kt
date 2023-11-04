package com.example.keepnotes.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.keepnotes.R
import com.example.keepnotes.appComponent
import com.example.keepnotes.databinding.FragmentNoteListingBinding
import com.example.keepnotes.di.ViewModelFactory
import com.example.keepnotes.domain.repository.State
import javax.inject.Inject


class NoteListingFragment : Fragment() {

    private lateinit var binding: FragmentNoteListingBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: NoteViewModel
    val adapter by lazy {
        NoteListingAdapter(
            onItemCLicked = { pos, item ->

            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.appComponent?.injectToNoteListFragment(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteListingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_noteListingFragment_to_noteDetailFragment)
        }
        binding.recyclerViewOfNotes.adapter = adapter
        viewModel.getNotes()
        viewModel.note.observe(viewLifecycleOwner) { noteState ->
            when(noteState) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is State.Error -> {
                    binding.progressBar.visibility = View.GONE
                }
                is State.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.updateList(noteState.data!!.toMutableList())
                }
            }
        }
    }
}