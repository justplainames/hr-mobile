package edu.singaporetech.hr

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class PayslipNavigationTest {

    @Test
    fun testPayslipNavigation(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // check navigation to attendance page
        onView(withId(R.id.card_payday_countdown)).perform(click())
        onView(withId((R.id.fragment_payslip))).check(matches(isDisplayed()))

        // check navigation to payslip detail
        onView(withId(R.id.payslipMoreDetailsButton)).perform(click())
        onView(withId((R.id.fragment_payslip_detail))).check(matches(isDisplayed()))
        pressBack()

        // check navigation to view all payslip
        onView(withId(R.id.payslipListButton)).perform(click())
        onView(withId((R.id.fragment_payslip_list))).check(matches(isDisplayed()))

        // check button to specific details from list of payslip
        onView(withId(R.id.payslipListRecyclerView))
            .perform(clickRecyclerViewItem(0,R.id.nextButton))
        onView(withId((R.id.fragment_payslip_detail))).check(matches(isDisplayed()))
        pressBack()

        // check navigation to consolidated payment
        onView(withId(R.id.payslipDownloadConsoButton)).perform(click())
        onView(withId((R.id.fragment_payslip_conso))).check(matches(isDisplayed()))



    }
}
private fun clickRecyclerViewItem(position: Int, id: Int? = null): ViewAction {
    return RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position,
        id?.let {
            object : ViewAction {
                override fun getConstraints() = null
                override fun getDescription() = "Click on an item view with specified id."
                override fun perform(uiController: UiController?, view: View?) {
                    val itemView = view?.findViewById<View>(id)
                        ?: throw Exception("Item view is not found")
                    itemView.performClick()
                }
            }
        } ?: click())
}


