package edu.singaporetech.hr

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import edu.singaporetech.hr.databinding.FragmentHomepageBinding

class HomepageFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = DataBindingUtil.inflate<FragmentHomepageBinding>(
            inflater,
            R.layout.fragment_homepage, container, false
        )

        binding.goPayslip.setOnClickListener {
            val intent = Intent(activity, PayslipActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}