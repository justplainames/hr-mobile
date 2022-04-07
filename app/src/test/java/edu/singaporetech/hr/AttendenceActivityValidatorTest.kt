package edu.singaporetech.hr

import com.google.common.truth.Truth
import edu.singaporetech.hr.validator.AttendenceActivityValidator
import org.junit.Test

class AttendenceActivityValidatorTest{
    /*
    Unit testing on AttendenceClockFragment :
    - - submit submit issue form (submitBtn)
    */
    /*
       empty reason input for reasonET
     */
    @Test
    fun emptytextViewLeaveStartDate() {
        val result= AttendenceActivityValidator.validateAttendenceInput(
            ""
        )
        return Truth.assertThat(result).isFalse()
    }
    /*
       successfully submitted with  input for reasonET
     */
    @Test
    fun submitIssueSuccess() {
        val result= AttendenceActivityValidator.validateAttendenceInput(
            "System Crashed due to peak hours, clock in is missed out"
        )
        return Truth.assertThat(result).isTrue()
    }
}