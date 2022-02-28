package edu.singaporetech.hr.leave

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import edu.singaporetech.hr.R
import java.util.*

class LeaveApplyLeave : AppCompatActivity() {

    lateinit var optionLeaveType:Spinner
    lateinit var optionLeaveSupervisor:Spinner
    // lateinit var resultLeaveType:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView((R.layout.leave_apply_leave))

        optionLeaveType = findViewById<Spinner>(R.id.spinnerLeaveType)
        val textViewLeaveStartDate = findViewById<TextView>(R.id.textViewLeaveStartDate)
        val textViewLeaveEndDate = findViewById<TextView>(R.id.textViewLeaveEndDate)
        val radioGroupLeaveDay = findViewById<RadioGroup>(R.id.radioGroupLeaveDay)
        optionLeaveSupervisor = findViewById<Spinner>(R.id.spinnerLeaveSupervisor)
        val editTextLeaveReason = findViewById<EditText>(R.id.editTextLeaveReason)
        val textViewAvailableLeave = findViewById<TextView>(R.id.textViewAvailableLeave)
        val buttonCancelLeave = findViewById<Button>(R.id.buttonCancelLeave)
        val buttonSubmitLeave = findViewById<Button>(R.id.buttonSubmitLeave)

        // Spinner for leave type
        val optionsLeaveType = arrayOf("Annual","Sick","Maternity")
        optionLeaveType.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, optionsLeaveType)

        optionLeaveType.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                Toast.makeText(this@LeaveApplyLeave,optionsLeaveType[position], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        // Date picker for leave start date
        val calender = Calendar.getInstance()
        val myYear = calender.get(Calendar.YEAR)
        val myMonth = calender.get(Calendar.MONTH)
        val myDay = calender.get(Calendar.DAY_OF_MONTH)

        textViewLeaveStartDate.setOnClickListener{
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth  ->
                textViewLeaveStartDate.text = " " + dayOfMonth + " / " + (month + 1) + " / " + year
            }, myYear, myMonth, myDay)
            datePickerDialog.show()
        }

        // Date picker for leave end date
        textViewLeaveEndDate.setOnClickListener{
            val datePickerDialog = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth  ->
                textViewLeaveStartDate.text = " " + dayOfMonth + " / " + (month + 1) + " / " + year
            }, myYear, myMonth, myDay)
            datePickerDialog.show()
        }




    }
}