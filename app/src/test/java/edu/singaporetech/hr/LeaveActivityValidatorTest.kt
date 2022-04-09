package edu.singaporetech.hr
import com.google.common.truth.Truth.assertThat
import edu.singaporetech.hr.validator.LeaveActivityValidator
import org.junit.Test
/*
    LeaveActivityValidatorTest: Leave Activity Validator Test
        - state all the test cases of the unit testing
        - state the condition based on the validator and its corresponding output
 */
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
        val result= LeaveActivityValidator.validateApplyLeaveInput(
            "",
            "8/4/2022",
            "Annual Leave",
            "Half Day",
            "Mary Tan",
            "null"
        )
        return assertThat(result).isFalse()
    }
    /*
       empty datepicker input for textViewLeaveEndDate
     */
    @Test
    fun emptytextViewLeaveEndDate() {
        val result= LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "",
            "Annual Leave",
            "Half Day",
            "Mary Tan",
            null
        )
        return assertThat(result).isFalse()
    }
    /*
   empty leave type input for autoCompleteTextViewLeaveType
    */
    @Test
    fun emptyLeaveType() {
        val result= LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "8/4/2022",
            "",
            "Half Day",
            "Mary Tan",
            null
        )
        return assertThat(result).isFalse()
    }
    /*
    empty selected day input for selectedDay
    */
    @Test
    fun emptySelectedDay() {
        val result= LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "8/4/2022",
            "Maternity Leave",
            "0",
            "Mary Tan",
            null
        )
        return assertThat(result).isFalse()
    }
    /*
    empty supervisor day input for autoCompleteTextViewLeaveSupervisor
    */
    @Test
    fun emptyLeaveSupervisor() {
        val result= LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "8/4/2022",
            "Sick Leave",
            "Full Day",
            "",
            null
        )
        return assertThat(result).isFalse()
    }
    /*
    empty input is filled and submitted for apply leave form
    */
    @Test
    fun noInput() {
        val result= LeaveActivityValidator.validateApplyLeaveInput(
            "",
            "",
            "",
            "",
            "",
            null
        )
        return assertThat(result).isFalse()
    }
    /*
    successfully submitted apply leave form
    */
    @Test
    fun submitLeaveFormSuccess() {
        val result= LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "8/4/2022",
            "Annual Leave",
            "Half Day",
            "Mary Tan",
            null
        )
        return assertThat(result).isTrue()
    }
    /*
    start date is later than end date
    */
    @Test
    fun startDateLater() {
        val result= LeaveActivityValidator.validateApplyLeaveInput(
            "10/4/2022",
            "8/4/2022",
            "Annual Leave",
            "Half Day",
            "Mary Tan",
            null
        )
        return assertThat(result).isFalse()
    }
    /*
    emptyImage Uri when the leave type is sick leave
    */
    @Test
    fun emptyImageUri() {
        val result= LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "8/4/2022",
            "Sick Leave",
            "Half Day",
            "Mary Tan",
            null
        )
        return assertThat(result).isFalse()
    }
    /*
    submit sick leave application successfully with imageuri
    */
    @Test
    fun submitSickLeave() {
        val result= LeaveActivityValidator.validateApplyLeaveInput(
            "4/4/2022",
            "8/4/2022",
            "Sick Leave",
            "Half Day",
            "Mary Tan",
            "android.resource://edu.singaporetech.hr/SickLeaveattachment.png"
        )
        return assertThat(result).isTrue()
    }
}