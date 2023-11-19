package com.example.keepnotes.ui.note

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.keepnotes.R
import com.example.keepnotes.appComponent
import com.example.keepnotes.databinding.FragmentNoteListingBinding
import com.example.keepnotes.di.ViewModelFactory
import com.example.keepnotes.util.NavigationConstants
import com.example.keepnotes.domain.repository.State
import com.example.keepnotes.ui.authentication.AuthenticationViewModel
import javax.inject.Inject


class NoteListingFragment : Fragment() {

    private lateinit var binding: FragmentNoteListingBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: NoteViewModel
    private lateinit var authenticationViewModel: AuthenticationViewModel

    private val adapter by lazy {
        NoteListingAdapter(onItemCLicked = { _, item ->
            findNavController().navigate(R.id.action_noteListingFragment_to_noteDetailFragment,
                Bundle().apply {
                    putString(NavigationConstants.TYPE, NavigationConstants.UPDATE)
                    putParcelable(NavigationConstants.NOTE, item)
                })
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context?.appComponent?.injectToNoteListFragment(this)
        viewModel = ViewModelProvider(this, viewModelFactory)[NoteViewModel::class.java]
        authenticationViewModel =
            ViewModelProvider(this, viewModelFactory)[AuthenticationViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentNoteListingBinding.inflate(layoutInflater)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.searchBar)
        binding.searchBar.inflateMenu(R.menu.menu)
        binding.searchBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.log_out_button -> {
                    authenticationViewModel.logout()
                    findNavController().navigate(R.id.action_noteListingFragment_to_welcomeFragment)
                    true
                }

                else -> false
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.floatingActionButton.setOnClickListener {
            findNavController().navigate(R.id.action_noteListingFragment_to_noteDetailFragment,
                Bundle().apply {
                    putString(NavigationConstants.TYPE, NavigationConstants.CREATE)
                })
        }
        binding.recyclerViewOfNotes.adapter = adapter
        binding.recyclerViewOfNotes.layoutManager =
            StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        viewModel.getNotes()
        viewModel.note.observe(viewLifecycleOwner) { noteState ->
            when (noteState) {
                is State.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }

                is State.Error -> {
                    binding.progressBar.visibility = View.GONE
                }

                is State.Success -> {
                    binding.progressBar.visibility = View.GONE
                    adapter.updateList(noteState.data?.toMutableList() ?: mutableListOf())
                }
            }
        }
    }
}