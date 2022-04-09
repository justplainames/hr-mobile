package edu.singaporetech.hr.validator

import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/*
    SignActivityValidator: Sign Activity Validator
        --place the code from the Fragment and check against the
        SignActivityTest test cases
 */
@RunWith(JUnit4::class)
object SignActivityValidator {
    /*
    Simulate database
    */
    private val existingEmployeeEmail = listOf("mary@csc2008.com")
    private val confirmedPassword = "qwerty"

    /*
    -- input for login is not valid is the email/password is empty
    -- email not registered in the HR database
    -- password is not the same as the one stored in the database

     */
    fun validateLoginInput(
        email: String,
        pass: String
    )
            : Boolean {
        var valid = true

        if (email.isEmpty() && pass.isEmpty()) {
            valid = false
            return valid
        }
        if (email.isEmpty() || pass.isEmpty()) {
            valid = false

            return valid
        }
        if (!(email in existingEmployeeEmail)) {
            return false
        }

        if (pass != confirmedPassword) {
            return false
        }
        return valid
    }

}