package edu.singaporetech.hr.fragment.attendance



import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import edu.singaporetech.hr.MainActivity
import edu.singaporetech.hr.R
import edu.singaporetech.hr.ViewModel.AttendanceViewModel
import edu.singaporetech.hr.databinding.ReportAttendanceFragmentBinding
/*
    ReportAttendanceFragment :  Attendance Overview Fragment
        -- Submit of Reason for clock in/ out status
        -- Get the date based on the record user navigate to
        -- Update the reason in the firebase records
 */
class ReportAttendanceFragment(private var position: Int, var selectedDate: String, var id: String) : Fragment() {

    private lateinit var viewViewModel: AttendanceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ReportAttendanceFragmentBinding>(
            inflater, R.layout.report_attendance_fragment, container, false
        )
        viewViewModel = ViewModelProvider(this).get(AttendanceViewModel::class.java)

        binding.selectedDateTV.text = selectedDate
        val submitBtn = binding.submitBtn
        val reason = binding.reasonET.text

        val cancelBtn= binding.cancelBtn
        cancelBtn.setOnClickListener {
            requireActivity()
                .supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragmentContainerView, AttendanceOverviewFragment())
                .commitNow()
        }

        submitBtn.setOnClickListener{

            if (binding.reasonET.text.isNullOrBlank()) {
                Log.d("reportBtn", "this is empty")
                Toast.makeText(
                    this@ReportAttendanceFragment.requireActivity(),
                    "Please fill in the reason before you submit!", Toast.LENGTH_SHORT
                ).show()
            }else{
                val dialogView = layoutInflater.inflate(R.layout.confirm_report_dialog, null)
                val dialogBuilder = AlertDialog.Builder(context)
                    .setView(dialogView)
                    .setTitle("Confirmation Message")
                val alertDialog = dialogBuilder.show()
                val confirmYesButton = dialogView.findViewById<Button>(R.id.buttonYesSubmitReport)
                val confirmNoButton = dialogView.findViewById<Button>(R.id.buttonNoSubmitReport)

                val successDialogView = layoutInflater.inflate(R.layout.dialog_report_success, null)
                val okButton = successDialogView.findViewById<Button>(R.id.buttonOkReport)


                confirmYesButton.setOnClickListener {
                    alertDialog.dismiss()

                    var isSubmitted = viewViewModel.updateAttendanceRecord(id,reason)
                    Log.d("reportBtn", isSubmitted.toString())
                    if(isSubmitted){
                        val dialogSuccessBuilder = AlertDialog.Builder(context)
                            .setView(successDialogView)
                            .setTitle("Success Message")
                        val alertSuccessDialog = dialogSuccessBuilder.show()
                            okButton.setOnClickListener {
                                alertSuccessDialog.dismiss()
                                requireActivity()
                                    .supportFragmentManager
                                    .beginTransaction()
                                    .replace(R.id.fragmentContainerView, AttendanceOverviewFragment())
                                    .commitNow()
                            }

                    }else{
                        Toast.makeText(
                            this@ReportAttendanceFragment.requireActivity(),
                            "Submit Reason fail, Please try again", Toast.LENGTH_LONG
                        ).show()
                    }
                }
                confirmNoButton.setOnClickListener {
                    alertDialog.dismiss()
                    Toast.makeText(
                        this@ReportAttendanceFragment.requireActivity(),
                        "Submit Reason fail, Please try again", Toast.LENGTH_LONG
                    ).show()
                }
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
                    .replace(R.id.fragmentContainerView, AttendanceOverviewFragment())
                    .commitNow()
            }
        })
    }

}