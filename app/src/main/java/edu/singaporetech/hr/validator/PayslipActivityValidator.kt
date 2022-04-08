package edu.singaporetech.hr.validator

import org.junit.runner.RunWith
import org.junit.runners.JUnit4
/*
    PayslipActivityValidator: Payslip Activity Validator
        --place the code from the Fragment and check against the
        PayslipActivityValidatorTest test cases
 */
@RunWith(JUnit4::class)
object PayslipActivityValidator {

    /*
    -- input for consolidated payslip datepicker  is empty
        - datepickerFrom is empty
        - datepickerTo is empty
    -- datepickerFrom is later than datepickerTo
     */
    fun getMonthInt(month: String): Int? {
        val result = when (month) {
            "Jan" -> 0
            "Feb" -> 1
            "Mar" ->2
            "Apr" ->3
            "May" ->4
            "Jun" ->5
            "Jul" ->6
            "Aug" ->7
            "Sept"->8
            "Oct"->9
            "Nov" ->10
            "Dec" -> 11

            else -> null
        }
        return result
    }
    fun validatePayslipInput(
        datepickerFrom:String,
        datepickerTo:String)
    :Boolean{
        var valid = true

        if (datepickerFrom.isEmpty() && datepickerTo.isEmpty() ) {
            valid = false
            return valid
        }
        if (datepickerFrom.isEmpty() || datepickerTo.isEmpty()) {
            valid = false

            return valid
        }
        if(((datepickerFrom.split(" ")[1].toInt() > datepickerTo.split(" ")[1].toInt()) &&
                    (getMonthInt(datepickerFrom.split(" ")[0])!! > getMonthInt(datepickerTo.split(" ")[0])!!))||((datepickerFrom.split(" ")[1].toInt() > datepickerTo.split(" ")[1].toInt()) &&
                    (getMonthInt(datepickerFrom.split(" ")[0])!! < getMonthInt(datepickerTo.split(" ")[0])!!))||(((datepickerFrom.split(" ")[1].toInt() == datepickerTo.split(" ")[1].toInt()) &&
                    (getMonthInt(datepickerFrom.split(" ")[0])!! > getMonthInt(datepickerTo.split(" ")[0])!!)))) {
            return false
        }
        return valid
    }

}