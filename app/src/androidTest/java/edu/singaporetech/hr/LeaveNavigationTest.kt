package edu.singaporetech.hr

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class LeaveNavigationTest {

    @Test
    fun testLeaveNavigation(){
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)

        // check navigation to leave page
        onView(withId(R.id.card_remaining_leave)).perform(click())
        onView(withId((R.id.fragment_leave))).check(matches(isDisplayed()))

        // check navigation to leave application
        onView(withId(R.id.buttonApplyLeave)).perform(click())
        onView(withId((R.id.fragment_leave_apply))).check(matches(isDisplayed()))

        // check navigation back to leave page
        onView(withId(R.id.buttonCancelLeave)).perform(click())
        onView(withId((R.id.fragment_leave))).check(matches(isDisplayed()))

        // check view all leave page
        onView(withId(R.id.buttonViewAllLeave)).perform(click())
        onView(withId((R.id.fragment_leave_record))).check(matches(isDisplayed()))

    }
}