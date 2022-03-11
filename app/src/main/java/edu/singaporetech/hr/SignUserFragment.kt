package edu.singaporetech.hr

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import edu.singaporetech.hr.databinding.FragmentSignUserBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class  SignUserFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val user = Firebase.auth.currentUser
        if (user != null) {
            val intent = Intent(activity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

        val binding = DataBindingUtil.inflate<FragmentSignUserBinding>(
            inflater,
            R.layout.fragment_sign_user, container, false
        )

        auth = FirebaseAuth.getInstance()

        val currentUser = auth.currentUser
        if(currentUser != null){

        }

        binding.btnLogin.setOnClickListener { view: View ->
            hideKeyboard()
            val email = binding.etUser.text.toString()
            val password = binding.etPass.text.toString()
            signIn(email, password)
        }

        binding.btnForgot.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signUserFragment_to_forgetUserFragment)
        }

        binding.ibFaceId.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signUserFragment_to_signFacialFragment)
        }

        return binding.root


    }

    private fun signIn(email: String, password: String) {

        if (!validateForm()) {
            return
        }

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val intent = Intent(activity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(context, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun validateForm(): Boolean {
        val email = view?.findViewById<EditText>(R.id.etUser)
        val pass = view?.findViewById<EditText>(R.id.etPass)

        var valid = true
        email?.text.toString()
        if (TextUtils.isEmpty(email?.text)) {
            email?.error = "Required."
            valid = false
        } else {
            email?.error = null
        }

        pass?.text.toString()
        if (TextUtils.isEmpty(pass?.text)) {
            pass?.error = "Required."
            valid = false
        } else {
            pass?.error = null
        }
        return valid
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }




}

