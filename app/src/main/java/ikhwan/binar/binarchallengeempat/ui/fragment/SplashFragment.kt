package ikhwan.binar.binarchallengeempat.ui.fragment

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import ikhwan.binar.binarchallengeempat.R
import ikhwan.binar.binarchallengeempat.databinding.FragmentSplashBinding


class SplashFragment : Fragment() {

    private var _binding : FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ObjectAnimator.ofPropertyValuesHolder(
            binding.imgNote,
            PropertyValuesHolder.ofFloat("scaleX", 1.2f),
            PropertyValuesHolder.ofFloat("scaleY", 1.2f)
        ).setDuration(2000).start()

        Handler(Looper.getMainLooper()).postDelayed({

            Navigation.findNavController(view).navigate(R.id.action_splashFragment_to_loginFragment)
        }, 3000)
    }

}