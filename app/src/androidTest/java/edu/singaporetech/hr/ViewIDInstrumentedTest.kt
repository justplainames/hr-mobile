package edu.singaporetech.hr

import android.util.Log
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.MethodSorters

/**
 * The instrumented blackbox test suite for the MainActivity.
 * Note that the ListActivity is not tested here and its associated view ids (listViewFourDigits,
 * switchGrid and resetDataButton) and are not included here. You should create your own test cases
 * for those.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING) // run in alphabet order of method name
@RunWith(AndroidJUnit4::class)
@LargeTest
class ViewIDInstrumentedTest {
    // UiAutomator device
    private lateinit var device: UiDevice

    @get:Rule
    var activityRule = activityScenarioRule<MainActivity>()

    @Before
    fun setupUiAutomator() {
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation())
    }

    @Test
    fun onStartMainActivity_uiMatches() {
        Log.i(TAG, """
            
            ### Activity layout and UI elements are similar to screenshot
                - has textViewFourDigitNum and generateButton
                - generateButton can be clicked
            """.trimIndent())
        
        onView(withId(R.id.textViewFourDigitNum))
            .check(matches(isDisplayed()))
        onView(withId(R.id.generateButton))
            .check(matches(isDisplayed()))
        onView(withId(R.id.generateButton)).perform(click())
    }

    companion object {
        private val TAG = ViewIDInstrumentedTest::class.simpleName
    }
}