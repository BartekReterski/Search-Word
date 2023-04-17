package com.searchdirectly.searchword.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.searchdirectly.searchword.databinding.SavedLinksItemBinding
import com.searchdirectly.searchword.domain.model.SavedLinks

class SavedLinkListAdapter(var savedLinks: ArrayList<SavedLinks>) :
    RecyclerView.Adapter<SavedLinkListAdapter.SavedLinksViewHolder>() {

    inner class SavedLinksViewHolder(binding: SavedLinksItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val imageMore = binding.imageMore
        private val linkTitle = binding.title
        private val linkContentValue = binding.content
        private val linkDate = binding.date

        fun bind(savedLinks: SavedLinks) {
            linkTitle.text = savedLinks.title
            linkContentValue.text = savedLinks.hyperLink
            linkDate.text = savedLinks.creationTime

            imageMore.setOnClickListener {
            }

//            layout.setOnLongClickListener {
//                action.onLongClick(note)
//                notifyItemRemoved(note.id.toInt())
//                true
//            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedLinksViewHolder {
        val binding =
            SavedLinksItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return SavedLinksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavedLinksViewHolder, position: Int) {
        holder.bind(savedLinks[position])
    }

    override fun getItemCount() = savedLinks.size

    // when is a new information(update) from database
    fun updateHyperLink(newNotes: List<SavedLinks>) {
        savedLinks.clear()
        savedLinks.addAll(newNotes)
        notifyDataSetChanged()
    }

}