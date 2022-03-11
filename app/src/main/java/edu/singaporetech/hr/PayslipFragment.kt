package edu.singaporetech.hr

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.*
import com.mikhaellopez.circularprogressbar.CircularProgressBar

import edu.singaporetech.hr.databinding.FragmentPayslipBinding
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList
import kotlin.concurrent.schedule


class PayslipFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: PayslipViewModel
    private lateinit var adapter : PayslipAdapter
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPayslipBinding>(
            inflater,R.layout.fragment_payslip, container, false
        )
        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        val payslipListObserver = Observer<ArrayList<Payslip>> { items->
            adapter=PayslipAdapter(items) // add items to adapter
            binding.recyclerViewPaySlip.adapter=adapter
        }
        viewModel.payslip.observe(requireActivity(), payslipListObserver)

        binding.recyclerViewPaySlip.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewPaySlip.setHasFixedSize(true)

        val payslipMoreDetailsButton= binding.payslipMoreDetailsButton
        payslipMoreDetailsButton.setOnClickListener {
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, PayslipDetailFragment())
                    .commitNow()

        }
        val payslipDownloadConsoButton= binding.payslipDownloadConsoButton
        payslipDownloadConsoButton.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, PayslipConsoFragment())
                .commitNow()
        }
        val payslipListButton= binding.payslipListButton
        payslipListButton.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, PayslipListFragment())
                .commitNow()
        }


        viewModel.payslip.observe(viewLifecycleOwner, Observer { payslip->
            binding.selectedPayslipMthYr.setText("${android.text.format.DateFormat.format("MMM yyyy", payslip[0].dateOfPayDay).toString()}")
            binding.textViewNetPay.setText("NetPay: ${payslip[0].netPay.toString()}")
            var netpay: Float = payslip[0].netPay?.toFloat() ?: 0.0f
            var earning: Float =payslip[0].totalEarning?.toFloat() ?: 0.0f
            val value:Float= ((netpay) / (earning)) *360f
            val circularProgressBar = binding.circularProgressBar
            circularProgressBar.apply {

                setProgressWithAnimation(value, 2000) // =1s
                // Set Progress Max
                progressMax = 360f
                // Set ProgressBar Color
                progressBarColor = Color.GREEN
                // Set background ProgressBar Color
                backgroundProgressBarColor = Color.GRAY
                // or with gradient
                backgroundProgressBarColorStart = Color.RED
                backgroundProgressBarColorEnd = Color.RED
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
        })


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


