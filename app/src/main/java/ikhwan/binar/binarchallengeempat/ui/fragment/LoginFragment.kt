package ikhwan.binar.binarchallengeempat.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputType
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import ikhwan.binar.binarchallengeempat.R
import ikhwan.binar.binarchallengeempat.database.AppDatabase
import ikhwan.binar.binarchallengeempat.databinding.FragmentLoginBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import java.util.regex.Pattern


@DelicateCoroutinesApi
class LoginFragment : Fragment() , View.OnClickListener{

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private var appDatabase : AppDatabase? = null
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var email: String
    private lateinit var password: String
    private var viewPass : Boolean = false
    private var cek: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedPreferences = requireActivity().getSharedPreferences(HomeFragment.PREF_USER, Context.MODE_PRIVATE)

        appDatabase = AppDatabase.getInstance(requireContext())

        if (sharedPreferences.contains(HomeFragment.EMAIL)){
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_homeFragment)
        }
        binding.btnLogin.setOnClickListener(this)
        binding.btnRegister.setOnClickListener(this)
        binding.btnViewPass.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        when(p0?.id){
            R.id.btn_login ->{
                login()
            }
            R.id.btn_register -> {
                p0.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            }
            R.id.btn_view_pass -> {
                if (viewPass == false){
                    binding.apply {
                        btnViewPass.setImageResource(R.drawable.ic_green_eye_24)
                        inputPassword.transformationMethod = HideReturnsTransformationMethod.getInstance()
                    }
                    viewPass = true
                }else{
                    binding.apply {
                        btnViewPass.setImageResource(R.drawable.ic_outline_remove_green_eye_24)
                        inputPassword.transformationMethod = PasswordTransformationMethod.getInstance()
                    }
                    viewPass = false
                }
            }
        }
    }

    private fun login() {
        binding.apply {
            email = inputEmail.text.toString()
            password = inputPassword.text.toString()
            cek = isValidEmail(email)
        }

        if (inputCheck(email,password,cek)){
            loginUser(email, password)
        }
    }

    private fun inputCheck(email: String, password: String, cek: Boolean) : Boolean{
        if (email.isEmpty() || password.isEmpty() || !cek
        ) {
            if (email.isEmpty()) {
                binding.apply {
                    inputEmail.setError("Email Tidak Boleh Kosong")
                    inputEmail.requestFocus()
                }

            }
            if (password.isEmpty()) {
                binding.apply {
                    inputPassword.setError("Password Tidak Boleh Kosong")
                    inputPassword.requestFocus()
                }
            }
            if (!cek) {
                binding.apply {
                    inputEmail.setError("Email Tidak Sesuai Format")
                    inputEmail.requestFocus()
                }
            }
            return false
        }else{
            return true
        }
    }

    private fun loginUser(email : String, password : String) {
        GlobalScope.async {
            val user = appDatabase?.appDao()?.getUserRegistered(email)
            requireActivity().runOnUiThread{
                if (user != null) {
                    if (email == user.email && password == user.password){
                        val editor = sharedPreferences.edit()
                        editor.putString(HomeFragment.EMAIL, email)
                        editor.apply()
                        Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_homeFragment)
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
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}