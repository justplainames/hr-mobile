package edu.singaporetech.hr.fragment.signin

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import edu.singaporetech.hr.R
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
        binding.btnSubmit.setOnClickListener {
            recover(binding.etSecondaryEmail.text.toString())
        }
        return binding.root
    }

    private fun recover(email: String) {
        val recoveryEmail = view?.findViewById<EditText>(R.id.etSecondaryEmail)
        if (!validateForm()) {
            return
        }else{
            FirebaseAuth.getInstance()
                .sendPasswordResetEmail(email)
                .addOnCompleteListener{task ->
                    if(task.isSuccessful){
                        view?.findNavController()
                            ?.navigate(R.id.action_forgetUserFragment_to_recoverSuccessFragment)
                    } else {
                        recoveryEmail?.error = "Email not in database."
                    }
                }
        }
    }

    /**
     * Validates form. Returns a boolean.
     */
    private fun validateForm(): Boolean {
        val email = view?.findViewById<EditText>(R.id.etSecondaryEmail)
        var valid = true
        email?.text.toString()
        if (TextUtils.isEmpty(email?.text)) {
            email?.error = "Required."
            valid = false
        }
        return valid
    }
}