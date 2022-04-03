package edu.singaporetech.hr
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class LeaveActivityValidatorTest {
    /*
    Unit testing on LeaveApplyFragment :
    - - submit apply leave form (buttonSubmitLeave)
    */

    /*
       empty datepicker input for textViewLeaveStartDate
     */
    @Test
    fun emptytextViewLeaveStartDate() {
        val result=LeaveActivityValidator.validateApplyLeaveInput(
            "",
            "8/4/2022",
            "Annual Leave",
            "Half Day",
            "Mary Tan"
        )
        return assertThat(result).isFalse()
    }
    /*
       empty datepicker input for textViewLeaveEndDate
     */
    @Test
    fun emptytextViewLeaveEndDate() {
        val result=LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "",
            "Annual Leave",
            "Half Day",
            "Mary Tan"
        )
        return assertThat(result).isFalse()
    }
    /*
   empty leave type input for autoCompleteTextViewLeaveType
    */
    @Test
    fun emptyLeaveType() {
        val result=LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "8/4/2022",
            "",
            "Half Day",
            "Mary Tan"
        )
        return assertThat(result).isFalse()
    }
    /*
    empty selected day input for selectedDay
    */
    @Test
    fun emptySelectedDay() {
        val result=LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "8/4/2022",
            "Sick Leave",
            "0",
            "Mary Tan"
        )
        return assertThat(result).isFalse()
    }
    /*
    empty supervisor day input for autoCompleteTextViewLeaveSupervisor
    */
    @Test
    fun emptyLeaveSupervisor() {
        val result=LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "8/4/2022",
            "Sick Leave",
            "Full Day",
            ""
        )
        return assertThat(result).isFalse()
    }
    /*
    empty input is filled and submitted for apply leave form
    */
    @Test
    fun noInput() {
        val result=LeaveActivityValidator.validateApplyLeaveInput(
            "",
            "",
            "",
            "",
            ""
        )
        return assertThat(result).isFalse()
    }
    /*
    successfully submitted apply leave form
    */
    @Test
    fun submitLeaveFormSuccess() {
        val result=LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "8/4/2022",
            "Annual Leave",
            "Half Day",
            "Mary Tan"
        )
        return assertThat(result).isTrue()
    }
    /*
    start date is later than end date
    */
    @Test
    fun startDateLater() {
        val result=LeaveActivityValidator.validateApplyLeaveInput(
            "10/4/2022",
            "8/4/2022",
            "Annual Leave",
            "Half Day",
            "Mary Tan"
        )
        return assertThat(result).isFalse()
    }

}