package edu.singaporetech.hr

import android.content.Intent
import android.graphics.Color
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import edu.singaporetech.hr.databinding.FragmentPayslipBinding


class PayslipFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: PayslipViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPayslipBinding>(
            inflater,R.layout.fragment_payslip, container, false
        )
        val adapter = PayslipAdapter()
        binding.recyclerViewPaySlip.adapter=adapter
        binding.recyclerViewPaySlip.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewPaySlip.setHasFixedSize(true)

        val digitListObserver = Observer<List<Payslip>> { payslip ->
            adapter.setDigitData(payslip)
        }


        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        viewModel.getLatest3.observe(this, digitListObserver)
        val payslipMoreDetailsButton= binding.payslipMoreDetailsButton
        payslipMoreDetailsButton.setOnClickListener {
                //findNavController().navigate(R.id.action_payslipFragment_to_payslipDetailFragment)
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, PayslipDetailFragment())
                    .commitNow()

        }
        val payslipDownloadConsoButton= binding.payslipDownloadConsoButton
        payslipDownloadConsoButton.setOnClickListener {
            //findNavController().navigate(R.id.action_payslipFragment_to_payslipConsoFragment)
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, PayslipConsoFragment())
                .commitNow()
        }
        val payslipListButton= binding.payslipListButton
        payslipListButton.setOnClickListener {
            //findNavController().navigate(R.id.action_payslipFragment_to_payslipListFragment)
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, PayslipListFragment())
                .commitNow()
        }
        val circularProgressBar = binding.circularProgressBar
        circularProgressBar.apply {
            setProgressWithAnimation(180f, 1000) // =1s
            // Set Progress Max
            progressMax = 360f
            // Set ProgressBar Color
            progressBarColor = Color.GREEN
            // Set background ProgressBar Color
            backgroundProgressBarColor = Color.GRAY
            // or with gradient
            backgroundProgressBarColorStart = Color.BLUE
            backgroundProgressBarColorEnd = Color.BLUE
            backgroundProgressBarColorDirection =
                CircularProgressBar.GradientDirection.TOP_TO_BOTTOM

            // Set Width
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 7f // in DP

            // Other
            roundBorder = true
            startAngle = 0f
            progressDirection = CircularProgressBar.ProgressDirection.TO_RIGHT
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:OnBackPressedCallback(true){

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "HomePage"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, HomeFragment())
                    .commitNow()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
    }
    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
    }

}


