package com.lamzone.mareu;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.service.MeetingApiService;
import com.lamzone.mareu.ui.ListMeetingActivity_Main;
import com.lamzone.mareu.utils.AddFakeMeetingData;
import com.lamzone.mareu.utils.DeleteViewAction;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static com.lamzone.mareu.ui.ListMeetingActivity_Main.sApiService;
import static com.lamzone.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MeetingListTest {
    /**
     * Test class for list of Meeting
     */
         // This is fixed
        private static int ITEMS_COUNT = 6;

        private ListMeetingActivity_Main mActivity;

        @Rule
        public ActivityTestRule<ListMeetingActivity_Main> mActivityRule =
                new ActivityTestRule(ListMeetingActivity_Main.class) {
                    @Override
                    protected void beforeActivityLaunched() {
                        addingFakeMeetings();
                    }
    };
    private MeetingApiService mMeetingApiService;
    private List<Meeting> meetings;

    @Before
        public void setUp() {
            //addingFakeMeetings();
            mActivity = mActivityRule.getActivity();
            assertThat(mActivity, notNullValue());
        }

        /**
         * We ensure that our recyclerview is displaying at least on item
         */
        @Test
        public void MeetingsList_shouldNotBeEmpty() {
            //addingFakeMeetings();
            onView(allOf(withId(R.id.List_Meeting_rv), isDisplayed()))
                    .check(matches(hasMinimumChildCount(1)));
            sApiService.deleteMeetings();
        }

    /**
     * When we delete an item, the item is no more shown
     */
    @Test
    public void MeetingsList_deleteAction_shouldRemoveItem() {
        // List Display
        onView(allOf(withId(R.id.List_Meeting_rv), isDisplayed())).check((ViewAssertion) withItemCount(ITEMS_COUNT));
        // Given : We remove the element at position 2
        // When perform a click on a delete icon
        onView(allOf(withId(R.id.List_Meeting_rv), isDisplayed()))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, (ViewAction) new DeleteViewAction()));
        // Then : the number of element is 5
        onView(allOf(withId(R.id.List_Meeting_rv), isDisplayed())).check((ViewAssertion) withItemCount(ITEMS_COUNT-1));
        sApiService.deleteMeetings();
    }

    // Filter Test
    @Test
    public void filterLaunchingTest()  {
    ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.filter), withContentDescription("Filtering"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction imageView = onView(
                allOf(withId(R.id.dateFilter_Iv),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class),
                                        0),
                                1),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction linearLayout = onView(
                allOf(withId(R.id.title_template),
                        childAtPosition(
                                allOf(withId(R.id.topPanel),
                                        childAtPosition(
                                                withId(R.id.parentPanel),
                                                0)),
                                0),
                        isDisplayed()));
        linearLayout.check(matches(isDisplayed()));
        sApiService.deleteMeetings();
}

    @Test
    public void listMeetingActivity_FilterDateToday() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(R.id.filter), withContentDescription("Filtering"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.action_bar),
                                        1),
                                0),
                        isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withId(R.id.dateFilter_Iv),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                1),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction materialButton = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                2),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction materialButton2 = onView(
                allOf(withId(android.R.id.button1), withText("Valider"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.buttonPanel),
                                        0),
                                3)));
        materialButton2.perform(scrollTo(), click());

        // Check the filter is Active with fakeMeeting it must be less
        onView(allOf(withId(R.id.List_Meeting_rv), isDisplayed())).check((ViewAssertion) withItemCount(lessThan( ITEMS_COUNT)));
        sApiService.deleteMeetings();
    }

    @Test
    public void listMeetingActivity_filterByRoomName() {
        ViewInteraction actionMenuItemView = onView(allOf(withId(R.id.filter),isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatSpinner = onView(allOf(withId(R.id.roomNameFilter_spinner),isDisplayed()));
        appCompatSpinner.perform(click());

        onData(anything()).inRoot(RootMatchers.isPlatformPopup()).atPosition(4).perform(click());
        onView(withId(R.id.roomNameFilter_spinner)).check(matches(withSpinnerText(containsString("Salle 04"))));

        ViewInteraction materialButton = onView(allOf(withId(android.R.id.button1), withText("Valider")));
        materialButton.perform(scrollTo(), click());

        // Check the filter is Active with fakeMeeting it must be less
        onView(allOf(withId(R.id.List_Meeting_rv), isDisplayed())).check((ViewAssertion) withItemCount(lessThan( ITEMS_COUNT)));
        sApiService.deleteMeetings();
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }


    public void addingFakeMeetings() {
        mMeetingApiService = DI.getNewInstanceApiService();
        sApiService = DI.getMeetingApiService();
        meetings = mMeetingApiService.getMeetings(null, null);
        // Adding 6 Meetings
        for (int i=0; i<6;i++) {
            sApiService.addMeeting(AddFakeMeetingData.generateFakeMeeting().get(i));
        }
    }





}
