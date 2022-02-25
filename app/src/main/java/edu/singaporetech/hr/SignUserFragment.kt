package edu.singaporetech.hr

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import edu.singaporetech.hr.databinding.FragmentSignUserBinding

class SignUserFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentSignUserBinding>(
            inflater,
            R.layout.fragment_sign_user, container, false
        )
        binding.btnForgot.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signUserFragment_to_forgetUserFragment)
        }

        binding.ibFaceId.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signUserFragment_to_signFacialFragment)
        }

        binding.btnGoMenu.setOnClickListener {
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
        return binding.root
    }
}