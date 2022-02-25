package edu.singaporetech.hr


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import edu.singaporetech.hr.databinding.FragmentSignFacialBinding

class SignFacialFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentSignFacialBinding>(
            inflater,
            R.layout.fragment_sign_facial, container, false
        )
        binding.btnUserLogin.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signFacialFragment_to_signUserFragment)
        }

        return binding.root

    }
}