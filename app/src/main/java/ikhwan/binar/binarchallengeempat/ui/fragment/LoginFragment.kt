package ikhwan.binar.binarchallengeempat.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import ikhwan.binar.binarchallengeempat.R
import ikhwan.binar.binarchallengeempat.database.user.UserDatabase
import kotlinx.android.synthetic.main.fragment_login.*
import kotlinx.android.synthetic.main.fragment_login.btn_register
import kotlinx.android.synthetic.main.fragment_login.input_email
import kotlinx.android.synthetic.main.fragment_login.input_password
import kotlinx.android.synthetic.main.fragment_register.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.regex.Pattern


class LoginFragment : Fragment() {

    private var userDatabase: UserDatabase? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userDatabase = UserDatabase.getInstance(requireContext())

        btn_login.setOnClickListener {
            login()
        }
        btn_register.setOnClickListener {
            it.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    private fun login() {
        val email = input_email.text.toString()
        val password = input_password.text.toString()
        val cek = isValidEmail(email)

        if (email.isEmpty() || password.isEmpty() || !cek
        ) {
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
            return
        }

        loginUser(email, password)
    }

    private fun loginUser(email : String, password : String) {
        GlobalScope.async {
            val user = userDatabase?.userDao()?.getUserRegistered(email)
            val mBundle = bundleOf(HomeFragment.EXTRA_USER to user)
            requireActivity().runOnUiThread{
                if (user != null) {
                    if (email == user.email && password == user.password){
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment, mBundle)
                    }else{
                        Toast.makeText(requireContext(), "Password yang anda masukkan salah", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Akun dengan email ${email} belum terdaftar", Toast.LENGTH_SHORT).show()
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