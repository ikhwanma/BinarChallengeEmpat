package ikhwan.binar.binarchallengeempat.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import ikhwan.binar.binarchallengeempat.R
import ikhwan.binar.binarchallengeempat.database.user.User
import ikhwan.binar.binarchallengeempat.database.user.UserDatabase
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.regex.Pattern

class RegisterFragment : Fragment() {

    private var userDatabase: UserDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDatabase = UserDatabase.getInstance(requireContext())

        btn_register.setOnClickListener {
            val nama = input_nama.text.toString()
            val email = input_email.text.toString()
            val password = input_password.text.toString()
            val cek = isValidEmail(email)
            val user = User(null, nama, email, password)

            if (nama.isEmpty() || email.isEmpty() || password.isEmpty() || !cek
                || input_konf_password.text.toString() != password || password.length < 6
            ) {
                if (nama.isEmpty()) {
                    input_nama.setError("Username Tidak Boleh Kosong")
                    input_nama.requestFocus()
                }
                if (email.isEmpty()) {
                    input_email.setError("Email Tidak Boleh Kosong")
                    input_email.requestFocus()
                }
                if (password.isEmpty()) {
                    input_password.setError("Password Tidak Boleh Kosong")
                    input_password.requestFocus()
                }
                if (!cek) {
                    input_email.setError("Email Tidak Sesuai Format")
                    input_email.requestFocus()
                }
                if (input_konf_password.text.toString() != password) {
                    input_konf_password.setError("Password Tidak Sama")
                    input_konf_password.requestFocus()
                }
                if (password.length < 6) {
                    input_password.setError("Password minimal 6 karakter")
                    input_password.requestFocus()
                }
                return@setOnClickListener
            }

            GlobalScope.async {
                val cekUser = userDatabase?.userDao()?.getUserRegistered(email)
                Log.d("cekuser", cekUser.toString())
                if (cekUser != null) {
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "User dengan email ${user.email} sudah terdaftar",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    val result = userDatabase?.userDao()?.registerUser(user)
                    requireActivity().runOnUiThread {
                        if (result != 0.toLong()) {
                            Toast.makeText(
                                requireContext(),
                                "Sukses mendaftarkan ${user.email}, silakan mencoba untuk login",
                                Toast.LENGTH_LONG
                            ).show()
                            it.findNavController()
                                .navigate(R.id.action_registerFragment_to_loginFragment)
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Gagal mendaftarkan ${user.email}, silakan coba lagi",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }

            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }
}