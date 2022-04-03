package edu.singaporetech.hr



import com.google.common.truth.Truth.assertThat
import org.junit.Test

class SignActivityTest {
    /*
    Unit testing on SignUserFragment :validateForm()
    */
    /*
    empty email return false
     */
    @Test
    fun emptyemail() {
        val result=SignActivityValidator.validateLoginInput(
            "",
            "qwerty"
        )
        return assertThat(result).isFalse()
    }
    /*
    empty password return false
     */
    @Test
    fun emptypass() {
        val result=SignActivityValidator.validateLoginInput(
            "mary@csc2008.com",
            ""
        )
        return assertThat(result).isFalse()
    }
    /*
    email and password match the HR database records
     */
    @Test
    fun validateSuccess() {
        val result=SignActivityValidator.validateLoginInput(
            "mary@csc2008.com",
            "qwerty"
        )
        return assertThat(result).isTrue()
    }
    /*
    email match but password is incorrect
     */
    @Test
    fun wrongPassword() {
        val result=SignActivityValidator.validateLoginInput(
            "mary@csc2008.com",
            "qwerty1001"
        )
        return assertThat(result).isFalse()
    }
        /*
    password match but email is incorrect
     */
    @Test
    fun wrongEmail() {
        val result=SignActivityValidator.validateLoginInput(
            "mary1001@csc2008.com",
            "qwerty"
        )
        return assertThat(result).isFalse()
    }

        /*
    password match but email is incorrect
    */
    @Test
    fun noUserInput() {
        val result=SignActivityValidator.validateLoginInput(
            "",
            ""
        )
        return assertThat(result).isFalse()
    }
}