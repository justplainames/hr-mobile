package edu.singaporetech.hr

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import edu.singaporetech.hr.databinding.FragmentPayslipBinding
import edu.singaporetech.hr.databinding.FragmentPayslipDetailBinding


class PayslipDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel1: PayslipViewModel
    private lateinit var viewModel2: PayslipViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPayslipDetailBinding>(
            inflater,R.layout.fragment_payslip_detail, container, false
        )
        val adapterEarning = PayslipEarningDetailAdapter()
        binding.recyclerViewPaySlipDetailEarning.adapter=adapterEarning
        binding.recyclerViewPaySlipDetailEarning.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewPaySlipDetailEarning.setHasFixedSize(true)
        val digitListObserverEarning = Observer<List<Payslip>> { payslip ->
                adapterEarning.setDigitData(payslip)
            }

        viewModel2 = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        viewModel2.getLatestMth.observe(this, digitListObserverEarning)

        val adapterDeduction = PayslipDeductionDetailAdapter()
        binding.recyclerViewPaySlipDetailDeduction.adapter=adapterDeduction
        binding.recyclerViewPaySlipDetailDeduction.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewPaySlipDetailDeduction.setHasFixedSize(true)
        val digitListObserverDeduction = Observer<List<Payslip>> { payslip ->
                adapterDeduction.setDigitData(payslip)
        }

        viewModel1 = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        viewModel1.getLatestMth.observe(this, digitListObserverDeduction)
        viewModel1.getLatestMth.observe(this, digitListObserverDeduction)
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

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,object:
            OnBackPressedCallback(true){

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "Payslip"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, PayslipFragment())
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

