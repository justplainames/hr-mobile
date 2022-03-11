package edu.singaporetech.hr

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.databinding.DataBindingUtil.setContentView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import edu.singaporetech.hr.databinding.FragmentPayslipDetailBinding
import kotlin.properties.Delegates


class PayslipDetailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: PayslipViewModel
    private lateinit var adapterEarning : PayslipEarningDetailAdapter
    private lateinit var adapterDeduction : PayslipDeductionDetailAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPayslipDetailBinding>(
            inflater,R.layout.fragment_payslip_detail, container, false
        )

        //EARNING
        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        val payslipListEarningObserver = Observer<ArrayList<Payslip>> { items->
            adapterEarning=PayslipEarningDetailAdapter(items) // add items to adapter
            binding.recyclerViewPaySlipDetailEarning.adapter=adapterEarning
        }
        viewModel.payslip.observe(requireActivity(), payslipListEarningObserver)

        binding.recyclerViewPaySlipDetailEarning.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewPaySlipDetailEarning.setHasFixedSize(true)

        //DEDUCTION
        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        val payslipListDeductionObserver = Observer<ArrayList<Payslip>> { items->
            adapterDeduction=PayslipDeductionDetailAdapter(items) // add items to adapter
            binding.recyclerViewPaySlipDetailDeduction.adapter=adapterDeduction
        }
        viewModel.payslip.observe(requireActivity(), payslipListDeductionObserver)

        binding.recyclerViewPaySlipDetailDeduction.layoutManager=LinearLayoutManager(activity)
        binding.recyclerViewPaySlipDetailDeduction.setHasFixedSize(true)

        viewModel.payslip.observe(viewLifecycleOwner, Observer { payslip->
            binding.latestPayslipMthYr.setText("${android.text.format.DateFormat.format("MMM yyyy", payslip[0].dateOfPayDay).toString()}")
            binding.textViewNetPay.setText("NetPay: ${payslip[0].netPay.toString()}")
            binding.payslipEarning.setText("(+)Earning:$${payslip[0].totalEarning.toString()}")
            binding.payslipDeduction.setText("(-)Deduction:$${payslip[0].totalDeduction.toString()}")
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

