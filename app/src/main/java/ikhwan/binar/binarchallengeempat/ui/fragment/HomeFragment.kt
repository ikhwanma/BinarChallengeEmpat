package ikhwan.binar.binarchallengeempat.ui.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ikhwan.binar.binarchallengeempat.R
import ikhwan.binar.binarchallengeempat.database.user.User

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getParcelable<User>(EXTRA_USER) as User
        Log.d(EXTRA_USER, user.toString())
    }

    companion object{
        const val EXTRA_USER = "extra_user"
    }
}