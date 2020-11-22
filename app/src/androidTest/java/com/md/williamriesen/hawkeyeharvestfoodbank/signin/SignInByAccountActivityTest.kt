package com.md.williamriesen.hawkeyeharvestfoodbank.signin

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.md.williamriesen.hawkeyeharvestfoodbank.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SignInByAccountActivityTest {

    @get: Rule
    val activityScenario = ActivityScenarioRule(SignInByAccountActivity::class.java)

    @Test
    fun test_that_title_is_in_view() {
        onView(withText("Hawkeye Harvest")).check(matches(isDisplayed()))
        onView(withText("FOOD BANK")).check(matches(isDisplayed()))
    }

    @Test
    fun test_that_account_entry_box_is_visible() {
        onView(withText("Please enter your account number:"))
            .check(matches(isDisplayed()))

        onView(withId(R.id.editTextAccountID))
            .check(matches(isDisplayed()))
            .check(matches(isEnabled()))
    }

    @Test
    fun test_that_correct_login_navigates_to_next_activity() {
        onView(withId(R.id.editTextAccountID))
            .perform(click())
            .perform(typeText("AAA-NOSHOW"))
            .perform(pressImeActionButton())

        Thread.sleep(15000)

        onView(withId(R.id.textViewNotPickedUpMessage))
            .check(matches(isDisplayed()))
    }

    @Test
    fun test_that_entering_staff_navigates_to_the_staff_login_page() {
        onView(withId(R.id.editTextAccountID))
            .perform(click())
            .perform(typeText("STAFF"))
            .perform(pressImeActionButton())

        Thread.sleep(1000)

        onView(withText("Sign in with email"))
            .check(matches(isDisplayed()))
    }
}

