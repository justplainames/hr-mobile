package edu.singaporetech.hr

import android.text.TextUtils
import android.widget.EditText
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
object AttendenceActivityValidator {

    /*
    -- input for attendence submit issue is empty
        - reason is empty (reasonET)
     */
    fun validateAttendenceInput(
        reason:String)
    :Boolean{
        var valid = true

        if (reason.isEmpty()  ) {
            valid = false
            return valid
        }
        return valid
    }

}