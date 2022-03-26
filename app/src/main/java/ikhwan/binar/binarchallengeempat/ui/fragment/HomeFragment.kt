package ikhwan.binar.binarchallengeempat.ui.fragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import ikhwan.binar.binarchallengeempat.R
import ikhwan.binar.binarchallengeempat.database.user.User
import ikhwan.binar.binarchallengeempat.databinding.FragmentHomeBinding
import kotlinx.android.synthetic.main.dialog_add.view.*
import java.util.*

class HomeFragment : Fragment(), View.OnClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val user = arguments?.getParcelable<User>(EXTRA_USER) as User
        val txtName =
            " ${user.nama.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }}!"

        binding.apply {
            tvUsername.append(txtName)
            btnLogout.setOnClickListener(this@HomeFragment)
            btnFloatAdd.setOnClickListener(this@HomeFragment)
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
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_add,null,false)
        val dialogBuilder = AlertDialog.Builder(requireContext())
        dialogBuilder.setView(view)

        view.btn_input.setOnClickListener{
            val catatan = view.input_catatan.text.toString()
            Toast.makeText(requireContext(), catatan, Toast.LENGTH_SHORT).show()
        }

        val dialog = dialogBuilder.create()

        dialog.show()
    }

    companion object {
        const val EXTRA_USER = "extra_user"
    }
}