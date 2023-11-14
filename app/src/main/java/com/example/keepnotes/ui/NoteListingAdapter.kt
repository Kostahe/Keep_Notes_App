package com.example.keepnotes.ui


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.keepnotes.data.model.Note
import com.example.keepnotes.databinding.NoteItemBinding

class NoteListingAdapter(
    val onItemCLicked: (Int, Note) -> Unit
) : RecyclerView.Adapter<NoteListingAdapter.NoteViewHolder>() {

    private var list: MutableList<Note> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
        val itemView = NoteItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NoteViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: NoteViewHolder, position: Int) {
        val item = list[position]
        holder.bind(item)
    }

    fun removeItem(position: Int) {
        list.removeAt(position)
        notifyItemChanged(position)
    }

    fun updateList(list: MutableList<Note>) {
        this.list = list
        notifyDataSetChanged()
    }

    inner class NoteViewHolder(private val binding: NoteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Note) {
            binding.titleText.text = item.title
            binding.noteText.text = item.text
            binding.card.setOnClickListener {
                onItemCLicked(adapterPosition, item)
            }
        }
    }
}