package ikhwan.binar.binarchallengeempat.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ikhwan.binar.binarchallengeempat.R
import ikhwan.binar.binarchallengeempat.database.note.Note
import ikhwan.binar.binarchallengeempat.database.note.NoteDatabase
import ikhwan.binar.binarchallengeempat.databinding.ItemNoteBinding
import ikhwan.binar.binarchallengeempat.ui.fragment.HomeFragment
import kotlinx.android.synthetic.main.dialog_delete.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


class NoteAdapter(val listNote: List<Note>) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    private var context: HomeFragment? = null

    class ViewHolder(var binding: ItemNoteBinding) : RecyclerView.ViewHolder(binding.root)

    constructor(listNote: List<Note>, context: HomeFragment) : this(listNote) {
        this.context = context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = listNote[position]
        holder.binding.apply {
            tvJudul.text = note.judul
            tvNote.text = note.catatan
        }

        holder.binding.btnDelete.setOnClickListener {
            val view = LayoutInflater.from(it.context).inflate(R.layout.dialog_delete, null, false)
            val dialogBuilder = AlertDialog.Builder(it.context)
            dialogBuilder.setView(view)

            val dialog = dialogBuilder.create()
            dialog.show()

            view.btn_cancel.setOnClickListener {
                dialog.dismiss()
            }

            view.btn_hapus.setOnClickListener {
                deleteData(note, holder, it)
                dialog.dismiss()
            }

        }

        holder.binding.btnEdit
    }

    private fun deleteData(note: Note, holder: ViewHolder, view: View) {
        val mDb = NoteDatabase.getInstance(holder.itemView.context)

        GlobalScope.async {
            val result = mDb?.noteDao()?.deleteNote(note)

            context!!.requireActivity().runOnUiThread {
                if (result != 0) {
                    Toast.makeText(
                        view.context,
                        "Data ${note.judul} berhasil dihapus",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        view.context,
                        "Data ${note.judul} Gagal dihapus",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            context!!.fetchData()
        }

    }

    override fun getItemCount(): Int {
        return listNote.size
    }
}