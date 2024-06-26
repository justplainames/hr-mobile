package edu.singaporetech.hr.fragment.signin

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import edu.singaporetech.hr.R
import edu.singaporetech.hr.databinding.FragmentRecoverSuccessBinding

class RecoverSuccessFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val binding = DataBindingUtil.inflate<FragmentRecoverSuccessBinding>(
            inflater,
            R.layout.fragment_recover_success, container, false
        )
        binding.ivMail.animate().apply {
            duration = 1200
            rotationYBy(360f)
        }
        binding.btnOk.setOnClickListener { view: View ->
            view.findNavController()
                .navigate(R.id.action_recoverSuccessFragment_to_signUserFragment)
        }
        return binding.root
    }
}