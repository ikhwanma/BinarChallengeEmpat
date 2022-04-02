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
import kotlinx.android.synthetic.main.dialog_add.view.*
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

    override fun getItemCount(): Int {
        return listNote.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val note = listNote[itemCount-1-position]
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

        holder.binding.btnEdit.setOnClickListener {
            val view = LayoutInflater.from(it.context).inflate(R.layout.dialog_add, null, false)
            val dialogBuilder = AlertDialog.Builder(it.context)
            dialogBuilder.setView(view)

            val dialog = dialogBuilder.create()
            val txtKeterangan = "Edit Data"
            val txtButton = "EDIT"

            view.tv_keterangan.setText(txtKeterangan)
            view.input_judul.setText(note.judul)
            view.input_catatan.setText(note.catatan)
            view.btn_input.setText(txtButton)
            view.btn_input.setOnClickListener {

                val judul = view.input_judul.text.toString()
                val catatan = view.input_catatan.text.toString()

                if (checkInput(judul, catatan, view)){
                    val noteUpdated = Note(note.id, judul, catatan, note.email)
                    editNote(holder, noteUpdated, view, dialog)
                }
            }

            dialog.show()
        }
    }

    private fun checkInput(judul: String, catatan: String, view: View) : Boolean{
        if (judul.isEmpty() || catatan.isEmpty()) {
            if (catatan.isEmpty()) {
                view.apply {
                    input_catatan.setError("Catatan tidak boleh kosong")
                    input_catatan.requestFocus()
                }
            }
            if (judul.isEmpty()) {
                view.apply {
                    input_judul.setError("Judul tidak boleh kosong")
                    input_judul.requestFocus()
                }
            }
            return false
        }else{
            return true
        }
    }

    private fun editNote(
        holder: ViewHolder,
        noteUpdated: Note,
        view: View,
        dialog: AlertDialog
    ) {
        val noteDatabase = NoteDatabase.getInstance(holder.itemView.context)

        GlobalScope.async {
            val result = noteDatabase?.noteDao()?.updateNote(noteUpdated)
            context!!.requireActivity().runOnUiThread {
                if (result != 0) {
                    Toast.makeText(
                        view.context,
                        "Note berhasil diupdate",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        view.context,
                        "Note Gagal diupdate",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            dialog.dismiss()
            context!!.fetchData()
        }
    }

    private fun deleteData(note: Note, holder: ViewHolder, view: View) {
        val noteDatabase = NoteDatabase.getInstance(holder.itemView.context)

        GlobalScope.async {
            val result = noteDatabase?.noteDao()?.deleteNote(note)

            context!!.requireActivity().runOnUiThread {
                if (result != 0) {
                    Toast.makeText(
                        view.context,
                        "Note berhasil dihapus",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        view.context,
                        "Note Gagal dihapus",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            context!!.fetchData()
        }

    }


}