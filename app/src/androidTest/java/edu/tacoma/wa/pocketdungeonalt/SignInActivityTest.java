package edu.tacoma.wa.pocketdungeonalt;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

import edu.tacoma.wa.pocketdungeonalt.authenticate.SignInActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.IsNot.not;

@RunWith(AndroidJUnit4.class)
// Tests the login and register functions of the SignInActivity
// MUST SET mTest to be true in SignInActivity
public class SignInActivityTest {


    @Rule
    public ActivityTestRule<SignInActivity> mActivityRule = new ActivityTestRule<>(
            SignInActivity.class);


    @Test
    public void testLogin() {
        String email = "justis5@uw.edu\t";

        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(email));
        onView(withId(R.id.password))
                .perform(typeText("password\t"));
        onView(withId(R.id.button_login))
                .perform(click());

        onView(withText("Login Successfully"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().
                        getWindow().getDecorView())))).check(matches(isDisplayed()));



    }

    @Test
    public void testLoginFail() {
        String email = "justis5@uw.edu\t";

        // Type text and then press the button.
        onView(withId(R.id.email))
                .perform(typeText(email));
        onView(withId(R.id.password))
                .perform(typeText("billisgreat\t"));
        onView(withId(R.id.button_login))
                .perform(click());

        onView(withText("Login Failed: invalid email/password."))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().
                        getWindow().getDecorView())))).check(matches(isDisplayed()));


    }


    @Test
    public void testRegisterSuccess() {
        Random random = new Random();
        //Generate an email address
        String email = "email" + (random.nextInt(10000) + 1)
                + (random.nextInt(900) + 1) + (random.nextInt(700) + 1)
                + (random.nextInt(400) + 1) + (random.nextInt(100) + 1)
                + "@uw.edu\t";

        // Type text and then press the button.
        onView(withId(R.id.text_register))
                .perform(click());
        onView(withId(R.id.register_email))
                .perform(typeText(email));
        onView(withId(R.id.register_password))
                .perform(typeText("test1@#\t"));
        onView(withId(R.id.button_register))
                .perform(click());

        onView(withText("Register Successfully"))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().
                        getWindow().getDecorView())))).check(matches(isDisplayed()));

    }

    @Test
    public void testRegisterFail() {
        Random random = new Random();
        //Generate an email address
        String email = "justis5@uw.edu\t";

        // Type text and then press the button.
        onView(withId(R.id.text_register))
                .perform(click());
        onView(withId(R.id.register_email))
                .perform(typeText(email));
        onView(withId(R.id.register_password))
                .perform(typeText("test1@#\t"));
        onView(withId(R.id.button_register))
                .perform(click());

        onView(withText("Register Failed: email already exists."))
                .inRoot(withDecorView(not(is(mActivityRule.getActivity().
                        getWindow().getDecorView())))).check(matches(isDisplayed()));

    }




}
