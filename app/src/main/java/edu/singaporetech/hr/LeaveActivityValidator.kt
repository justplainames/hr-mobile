package edu.singaporetech.hr

import android.text.TextUtils
import android.widget.EditText
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
object LeaveActivityValidator {
    /*
    -- input for apply leave is empty (not all field is filled up)
        - textViewLeaveStartDate is null
        - textViewLeaveEndDate is null
        - autoCompleteTextViewLeaveType is null
        - selectedDay is null
        - autoCompleteTextViewLeaveSupervisor is null
    -- textViewLeaveEndDate is earlier than textViewLeaveStartDate
    -- textViewLeaveStartDate is later than textViewLeaveEndDate
    -- password is not the same as the one stored in the database
    -- annual leave balance = 0, apply leave will not be successful
     */
    fun validateApplyLeaveInput(
        textViewLeaveStartDate:String,
        textViewLeaveEndDate:String,
        autoCompleteTextViewLeaveType:String,
        selectedDay:String,
        autoCompleteTextViewLeaveSupervisor:String):Boolean{

        if (textViewLeaveStartDate.isNullOrBlank() || textViewLeaveEndDate.isNullOrBlank()
            || autoCompleteTextViewLeaveType.isNullOrBlank() || selectedDay == "0" || autoCompleteTextViewLeaveSupervisor.isNullOrBlank()) {
            return false
        }
        if ((textViewLeaveEndDate.split("/")[0].toInt() < textViewLeaveStartDate.split("/")[0].toInt() ) && (textViewLeaveEndDate.split("/")[1].toInt()
                    <= textViewLeaveStartDate.split("/")[1].toInt()  )) {
            return false
        }
        return true
    }

}