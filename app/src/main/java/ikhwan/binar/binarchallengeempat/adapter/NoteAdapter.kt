package ikhwan.binar.binarchallengeempat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ikhwan.binar.binarchallengeempat.database.note.Note
import ikhwan.binar.binarchallengeempat.databinding.ItemNoteBinding

class NoteAdapter(val listNote : List<Note>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {
    class ViewHolder (var binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = listNote[position]
        holder.binding.apply {
            tvJudul.text = note.judul
            tvNote.text = note.catatan
        }
    }

    override fun getItemCount(): Int {
        return listNote.size
    }
}