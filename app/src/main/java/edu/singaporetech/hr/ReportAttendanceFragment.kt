package edu.singaporetech.hr



import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.singaporetech.hr.databinding.ReportAttendanceFragmentBinding

class ReportAttendanceFragment(private var position: Int, var selectedDate: String, var id: String) : Fragment() {

//    companion object {
//        fun newInstance() = ReportAttendanceFragment()
//    }

    private lateinit var viewModel: AttendanceModel
    private lateinit var reportAttendanceAdapter: ReportAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ReportAttendanceFragmentBinding>(
            inflater,R.layout.report_attendance_fragment, container, false
        )

      //  val binding = inflater.inflate(R.layout.report_attendance_fragment, container, false)

        viewModel = ViewModelProvider(this).get(AttendanceModel::class.java)

        reportAttendanceAdapter = ReportAdapter(selectedDate,id)

     //   binding.findViewById<TextView>(R.id.selectedDateTV)
        binding.selectedDateTV.text = selectedDate
        val submitBtn = binding.submitBtn
        val reasonET = binding.reasonET
        val testeT = binding.testET
        val reason = binding.reasonET.text
        val testT = binding.testET.text


        Log.d("ggg" , "at report frag: " + selectedDate + " and " + id)
        val cancelBtn= binding.cancelBtn
        cancelBtn.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, AttendanceFragment())
                .commitNow()
        }

        submitBtn.setOnClickListener{
            Log.d("reportBtn", "this is" + reason.toString() + reason)
            var isSubmitted = viewModel.updateAttendanceRecord(id,reason)
            if(isSubmitted){
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, AttendanceFragment())
                    .commitNow()
            }else{

            }
        }

        return binding.root
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {
                (requireActivity() as MainActivity).supportActionBar?.title = "View All Attendance"
                requireActivity()
                    .supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentContainerView, AttendanceFragment())
                    .commitNow()
            }
        })



    }

}