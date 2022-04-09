package edu.singaporetech.hr

import com.google.common.truth.Truth
import edu.singaporetech.hr.validator.PayslipActivityValidator
import org.junit.Test
/*
    PayslipActivityValidatorTest: Payslip Activity Validator Test
        - state all the test cases of the unit testing
        - state the condition based on the validator and its corresponding output
 */
class PayslipActivityValidatorTest {

    /*
    Unit testing on PayslipConsoFragment :
    - - click on submit button to download consolidated payslip (submitButton)
    --
    */
    /*
    empty input is filled and submitted for consolidated payslip form
    */
    @Test
    fun noInput() {
        val result= PayslipActivityValidator.validatePayslipInput(
            "",
            ""
        )
        return Truth.assertThat(result).isFalse()
    }
    /*
    empty input is filled for payslipDatePickerFromInput
    */
    @Test
    fun emptyStartDate() {
        val result= PayslipActivityValidator.validatePayslipInput(
            "",
            "Feb 2022"
        )
        return Truth.assertThat(result).isFalse()
    }
    /*
   empty input is filled for payslipDatePickerToInput
   */
    @Test
    fun emptyEndDate() {
        val result= PayslipActivityValidator.validatePayslipInput(
            "Jan 2022",
            ""
        )
        return Truth.assertThat(result).isFalse()
    }
    /*
    successfully submitted consolidated form
    */
    @Test
    fun submitConsoFormSuccess() {
        val result= PayslipActivityValidator.validatePayslipInput(
            "Jan 2022",
            "Feb 2022",

        )
        return Truth.assertThat(result).isTrue()
    }
    /*
    datepickerFrom is later than datepickerTo
    */
    @Test
    fun datePickerFromLater() {
        val result= PayslipActivityValidator.validatePayslipInput(
            "Feb 2022",
            "Jan 2022",

            )
        return Truth.assertThat(result).isFalse()
    }
        /*
    laterThanNow is later than current date
    */
    @Test
    fun laterThanNow() {
        val result= PayslipActivityValidator.validatePayslipInput(
            "Apr 2022",
            "May 2022",
            )
        return Truth.assertThat(result).isFalse()
    }
}
