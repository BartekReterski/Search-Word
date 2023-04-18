package com.searchdirectly.searchword.presentation.adapters

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Context.CLIPBOARD_SERVICE
import android.content.Intent
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.searchdirectly.searchword.R
import com.searchdirectly.searchword.databinding.SavedLinksItemBinding
import com.searchdirectly.searchword.domain.model.SavedLinks
import com.searchdirectly.searchword.presentation.activities.SavedWebsitesActivity

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
                popupMenuSetup(it, savedLinks)
            }
        }
    }

    @SuppressLint("DiscouragedPrivateApi")
    private fun popupMenuSetup(it: View, savedLinks: SavedLinks) {
        val wrapper: Context = ContextThemeWrapper(it.context, R.style.CustomPopUpStyle)
        val popupMenu = PopupMenu(wrapper, it)
        popupMenu.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.menu_saved_share_link -> {
                    shareLink(it, savedLinks)
                    true
                }
                R.id.menu_saved_copy_link -> {
                    copyLinkToClipboard(it, savedLinks)
                    true
                }
                R.id.menu_saved_delete_link -> {
                    deleteLinkWithDialog(it, savedLinks)

                    true
                }
                else -> false
            }
        }

        popupMenu.inflate(R.menu.popup_menu_saved_links)

        try {
            val fieldMPopup = PopupMenu::class.java.getDeclaredField("mPopup")
            fieldMPopup.isAccessible = true
            val mPopup = fieldMPopup.get(popupMenu)
            mPopup.javaClass
                .getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(mPopup, true)
        } catch (e: Exception) {
            Log.e("Main", "Error showing menu icons.", e)
        } finally {
            popupMenu.show()
        }
    }

    private fun shareLink(it: View, savedLinks: SavedLinks) {
        val shareUrl = savedLinks.hyperLink
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareUrl)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        it.context.startActivity(shareIntent)
    }

    private fun copyLinkToClipboard(it: View, savedLinks: SavedLinks) {
        (it.context.getSystemService(CLIPBOARD_SERVICE) as ClipboardManager).apply {
            setPrimaryClip(
                ClipData.newPlainText(
                    it.context.getString(R.string.copied_link_id),
                    savedLinks.hyperLink
                )
            )
        }
        Toast.makeText(
            it.context,
            it.context.getString(R.string.copied_to_clipboard),
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun deleteLinkWithDialog(
        it: View,
        savedLinks: SavedLinks
    ) {
        val builder = AlertDialog.Builder(it.context)
        builder.setTitle(it.context.getString(R.string.delete_dialog_title))
        builder.setMessage(it.context.getString(R.string.delete_dialog_confirmation) + savedLinks.title)

        builder.setPositiveButton(R.string.delete_string) { _, _ ->
            (it.context as SavedWebsitesActivity).removeLinkFromDatabase(savedLinks)
            // Toast.makeText(it.context, it.context.getString(R.string.deleted_info), Toast.LENGTH_SHORT).show()
        }

        builder.setNegativeButton(android.R.string.cancel) { _, _ -> }

        builder.show()
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