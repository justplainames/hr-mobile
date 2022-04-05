package edu.singaporetech.hr

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Collections.list
import java.util.regex.Matcher

@RunWith(AndroidJUnit4::class)
class AttendanceNavigationTest {

    @Test
    fun testAttendanceNavigation(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // check navigation to attendance page
        onView(withId(R.id.card_view_attendance)).perform(click())
        onView(withId((R.id.attendance_overview))).check(matches(isDisplayed()))

        // check navigation to all attendance
        onView(withId(R.id.allAttendanceBtn)).perform(click())
        onView(withId((R.id.fragment_attendance))).check(matches(isDisplayed()))

        // check navigation to attendance summary
        onView(withId(R.id.attendanceSummaryBtn)).perform(click())
        onView(withId((R.id.attendance_overview))).check(matches(isDisplayed()))

        // check view all leave page
        onView(withId(R.id.allAttendanceBtn)).perform(click())
        onView(withId(R.id.recyclerViewAttendence))
            .perform(clickRecyclerViewItem(0,R.id.reportBtn))
        onView(withId((R.id.report_attendance_fragment))).check(matches(isDisplayed()))

    }
}

private fun clickRecyclerViewItem(position: Int, id: Int? = null): ViewAction {
    return actionOnItemAtPosition<RecyclerView.ViewHolder>(position,
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
