package ikhwan.binar.binarchallengeempat.ui.fragment

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import ikhwan.binar.binarchallengeempat.R
import ikhwan.binar.binarchallengeempat.adapter.NoteAdapter
import ikhwan.binar.binarchallengeempat.database.AppDatabase
import ikhwan.binar.binarchallengeempat.database.Note
import ikhwan.binar.binarchallengeempat.database.User
import ikhwan.binar.binarchallengeempat.databinding.*
import kotlinx.android.synthetic.main.dialog_add_edit.view.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.*

@DelicateCoroutinesApi
class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var appDatabase: AppDatabase
    private lateinit var user: User
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        appDatabase = AppDatabase.getInstance(requireContext())!!
        sharedPreferences =
            requireActivity().getSharedPreferences(PREF_USER, Context.MODE_PRIVATE)
        val email = sharedPreferences.getString(EMAIL, "").toString()

        Log.d("vvvvvvvvv", email)

        GlobalScope.async {
            user = appDatabase.appDao().getUserRegistered(email)
            val name =
                user.nama.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
            val txtName =
                " $name!"

            binding.apply {
                tvUsername.append(txtName)
            }

            binding.rvNotes.layoutManager = LinearLayoutManager(activity)
            fetchData()
        }

        binding.apply {
            btnLogout.setOnClickListener(this@HomeFragment)
            btnFloatAdd.setOnClickListener(this@HomeFragment)
        }

    }

    fun fetchData() {
        GlobalScope.launch {
            val listNote = appDatabase.appDao().getNote(user.email)

            requireActivity().runOnUiThread {
                listNote.let {
                    val adapter = NoteAdapter(listNote, this@HomeFragment)

                    if (adapter.itemCount == 0){
                        binding.llWarn.visibility = View.VISIBLE
                    }else{
                        binding.llWarn.visibility = View.GONE
                    }
                    binding.rvNotes.adapter = adapter
                }
            }
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_logout -> {
                val bindingLogout = DialogDeleteLogoutBinding.inflate(layoutInflater)
                val dialogBuilder = AlertDialog.Builder(requireContext()).setView(bindingLogout.root)
                val dialog = dialogBuilder.create()
                dialog.show()

                bindingLogout.apply {
                    val txtLogout = "Logout"
                    val txtKeterangan = "Yakin Ingin Logout?"
                    btnHapus.setText(txtLogout)
                    tvKeterangan.text = txtKeterangan
                }

                bindingLogout.btnHapus.setOnClickListener {
                    val editor = sharedPreferences.edit()
                    editor.clear()
                    editor.apply()

                    p0.findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                    Toast.makeText(requireContext(), "Anda telah logout", Toast.LENGTH_SHORT).show()
                    dialog.dismiss()
                }
                bindingLogout.btnCancel.setOnClickListener {
                    dialog.dismiss()
                }

            }
            R.id.btn_float_add -> {
                showDialog()
            }
        }
    }

    private fun showDialog() {
        val bindingAdd = DialogAddEditBinding.inflate(layoutInflater)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(bindingAdd.root)

        val dialog = dialogBuilder.create()

        bindingAdd.btnInput.setOnClickListener {
            add(bindingAdd.root, dialog)
        }

        dialog.show()

    }

    private fun add(view: View, dialog: AlertDialog) {
        val judul = view.input_judul.text.toString()
        val catatan = view.input_catatan.text.toString()

        if (checkInput(judul, catatan, view)) {
            val note = Note(null, judul, catatan, user.email)
            addNote(dialog, note)
        }

    }

    private fun addNote(dialog: AlertDialog, note: Note) {
        GlobalScope.async {
            val result = appDatabase.appDao().addNote(note)
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
        } else {
            return true
        }
    }

    companion object {

        const val PREF_USER = "user_preference"
        const val EMAIL = "email"

    }
}