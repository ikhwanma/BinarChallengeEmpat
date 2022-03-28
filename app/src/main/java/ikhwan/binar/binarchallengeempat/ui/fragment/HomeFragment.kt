package ikhwan.binar.binarchallengeempat.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import ikhwan.binar.binarchallengeempat.R
import ikhwan.binar.binarchallengeempat.adapter.NoteAdapter
import ikhwan.binar.binarchallengeempat.database.note.Note
import ikhwan.binar.binarchallengeempat.database.note.NoteDatabase
import ikhwan.binar.binarchallengeempat.database.user.User
import ikhwan.binar.binarchallengeempat.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.dialog_add.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteDatabase: NoteDatabase
    private lateinit var user: User

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        user = arguments?.getParcelable<User>(EXTRA_USER) as User
        val name = user.nama.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val txtName =
            " $name!"

        noteDatabase = NoteDatabase.getInstance(requireContext())!!

        binding.apply {
            tvUsername.append(txtName)
            btnLogout.setOnClickListener(this@HomeFragment)
            btnFloatAdd.setOnClickListener(this@HomeFragment)
        }

        binding.rvNotes.layoutManager = LinearLayoutManager(activity)
        fetchData()

    }

    fun fetchData() {
        GlobalScope.launch {
            val listNote = noteDatabase.noteDao().getNote(user.email)

            requireActivity().runOnUiThread {
              listNote.let {
                  val adapter = NoteAdapter(it, this@HomeFragment)
                  binding.rvNotes.adapter = adapter
              }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_logout -> {
                Toast.makeText(requireContext(), "logout", Toast.LENGTH_SHORT).show()
            }
            R.id.btn_float_add -> {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add, null, false)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(view)

        val dialog = dialogBuilder.create()

        view.btn_input.setOnClickListener {
            add(view, dialog)
        }

        dialog.show()

    }

    private fun add(view: View, dialog: AlertDialog) {
        val judul = view.input_judul.text.toString()
        val catatan = view.input_catatan.text.toString()

        if (checkInput(judul,catatan,view)){
            val note = Note(null, judul, catatan, user.email)
            addNote(dialog, note)
        }

    }

    private fun addNote(dialog: AlertDialog, note: Note) {
        GlobalScope.async {
            val result = noteDatabase.noteDao().addNote(note)
            requireActivity().runOnUiThread {
                if (result != 0.toLong()) {
                    dialog.dismiss()
                    Toast.makeText(requireContext(), "Note Ditambahkan", Toast.LENGTH_SHORT).show()

                } else {
                    Toast.makeText(requireContext(), "Gagal Menambahkan", Toast.LENGTH_SHORT).show()
                }
            }
            fetchData()
        }
    }

    private fun checkInput(judul: String, catatan: String, view: View): Boolean {
        if (judul.isEmpty() || catatan.isEmpty()){
            if (catatan.isEmpty()){
                view.apply {
                    input_catatan.setError("Catatan tidak boleh kosong")
                    input_catatan.requestFocus()
                }
            }
            if (judul.isEmpty()){
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

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}