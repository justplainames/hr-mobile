package edu.singaporetech.hr

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import edu.singaporetech.hr.databinding.FragmentPayslipBinding
import edu.singaporetech.hr.databinding.FragmentPayslipListBinding

class PayslipListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private lateinit var viewModel: PayslipViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentPayslipListBinding>(
            inflater,R.layout.fragment_payslip_list, container, false
        )
        val adapter = PayslipAdapter()
        binding.payslipListRecyclerView.adapter=adapter
        binding.payslipListRecyclerView.layoutManager=LinearLayoutManager(activity)
        binding.payslipListRecyclerView.setHasFixedSize(true)

        val digitListObserver = Observer<List<Payslip>> { payslip ->
            adapter.setDigitData(payslip)
        }

        viewModel = ViewModelProvider(requireActivity()).get(PayslipViewModel::class.java)
        viewModel.getAll.observe(this, digitListObserver)

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