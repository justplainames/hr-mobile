package edu.singaporetech.hr

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import edu.singaporetech.hr.databinding.FragmentForgetUserBinding

class ForgetUserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentForgetUserBinding>(
            inflater,
            R.layout.fragment_forget_user, container, false
        )
        binding.btnSubmit.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_forgetUserFragment_to_recoverSuccessFragment)
        }
        return binding.root
    }
}