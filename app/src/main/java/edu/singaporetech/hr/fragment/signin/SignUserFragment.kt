package edu.singaporetech.hr.fragment.signin

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_WEAK
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import edu.singaporetech.hr.MainActivity
import edu.singaporetech.hr.R
import edu.singaporetech.hr.SignActivity
import edu.singaporetech.hr.databinding.FragmentSignUserBinding


class SignUserFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("Authentication success!")
                    startActivity(Intent(activity, MainActivity::class.java))
                }
            }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val actionBar = (activity as SignActivity).supportActionBar
        actionBar!!.setDisplayShowTitleEnabled(true)
        actionBar.title = "Sign In"

        val binding = DataBindingUtil.inflate<FragmentSignUserBinding>(
            inflater,
            R.layout.fragment_sign_user, container, false
        )

        val user = Firebase.auth.currentUser

        if (user != null) {
            binding.tvFaceId.visibility = View.VISIBLE
            binding.ibFaceId.visibility = View.VISIBLE
            biometricAuthentication()
        } else {
            binding.tvFaceId.visibility = View.INVISIBLE
            binding.ibFaceId.visibility = View.INVISIBLE
        }

        auth = FirebaseAuth.getInstance()


        binding.btnLogin.setOnClickListener {
            hideKeyboard()
            val email = binding.etUser.text.toString()
            val password = binding.etPass.text.toString()
            signIn(email, password)
        }

        binding.btnForgot.setOnClickListener { view: View ->
            view.findNavController().navigate(R.id.action_signUserFragment_to_forgetUserFragment)
        }

        binding.ibFaceId.setOnClickListener {
            biometricAuthentication()
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
                    Toast.makeText(
                        context, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
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
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun notifyUser(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager =
            requireActivity().getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint authentication has not been enabled in settings")
            return false
        }

        if (activity?.let {
                ActivityCompat.checkSelfPermission(
                    it.applicationContext,
                    android.Manifest.permission.USE_BIOMETRIC
                )
            } != PackageManager.PERMISSION_GRANTED) {
            notifyUser("Fingerprint authentication permission is not enabled")
            return false
        }

        return if (requireContext().packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.P)
    private fun biometricAuthentication() {
        checkBiometricSupport()
        activity?.let { it1 ->
            BiometricPrompt.Builder(activity)
                .setAllowedAuthenticators(BIOMETRIC_STRONG or BIOMETRIC_WEAK)
                .setTitle("You were not fully logged out!")
                .setSubtitle("Sign in quickly using your biometric credentials")
                .setNegativeButton(
                    "Cancel",
                    it1.mainExecutor
                ) { _, _ ->
                    notifyUser("Authentication Cancelled")
                }.build()
        }?.authenticate(
            getCancellationSignal(),
            requireContext().mainExecutor,
            authenticationCallback
        )
    }

}

