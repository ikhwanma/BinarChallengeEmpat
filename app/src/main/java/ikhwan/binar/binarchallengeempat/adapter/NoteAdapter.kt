package ikhwan.binar.binarchallengeempat.adapter

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import ikhwan.binar.binarchallengeempat.R
import ikhwan.binar.binarchallengeempat.database.AppDatabase
import ikhwan.binar.binarchallengeempat.database.Note
import ikhwan.binar.binarchallengeempat.databinding.DialogAddBinding
import ikhwan.binar.binarchallengeempat.databinding.DialogDeleteBinding
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
            btnDelete.setOnClickListener {
                val bindingDelete = DialogDeleteBinding.inflate(LayoutInflater.from(it.context))
                val dialogBuilder = AlertDialog.Builder(it.context)
                dialogBuilder.setView(bindingDelete.root)

                val dialog = dialogBuilder.create()
                dialog.show()

                bindingDelete.apply {
                    btnCancel.setOnClickListener {
                        dialog.dismiss()
                    }
                    btnHapus.setOnClickListener {
                        deleteData(note, holder, bindingDelete.root)
                        dialog.dismiss()
                    }
                }
            }

            btnEdit.setOnClickListener {
                val bindingEdit = DialogAddBinding.inflate(LayoutInflater.from(it.context))
                val dialogBuilder = AlertDialog.Builder(it.context)
                dialogBuilder.setView(bindingEdit.root)

                val dialog = dialogBuilder.create()
                val txtKeterangan = "Edit Data"
                val txtButton = "EDIT"

                bindingEdit.apply {
                    tvKeterangan.setText(txtKeterangan)
                    inputJudul.setText(note.judul)
                    inputCatatan.setText(note.catatan)
                    btnInput.setText(txtButton)
                    btnInput.setOnClickListener {

                        val judul = bindingEdit.inputJudul.text.toString()
                        val catatan = bindingEdit.inputCatatan.text.toString()

                        if (checkInput(judul, catatan, bindingEdit.root)){
                            val noteUpdated = Note(note.id, judul, catatan, note.email)
                            editNote(holder, noteUpdated, bindingEdit.root, dialog)
                        }
                    }
                }

                dialog.show()
            }
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
        val noteDatabase = AppDatabase.getInstance(holder.itemView.context)

        GlobalScope.async {
            val result = noteDatabase?.appDao()?.updateNote(noteUpdated)
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
        val noteDatabase = AppDatabase.getInstance(holder.itemView.context)

        GlobalScope.async {
            val result = noteDatabase?.appDao()?.deleteNote(note)

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