package edu.singaporetech.firstapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import edu.singaporetech.firstapp.databinding.FragmentSignUserBinding

class SignUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<FragmentSignUserBinding>(inflater,
            R.layout.fragment_sign_user,container,false)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_user, container, false)
    }
}