package com.lamzone.mareu;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TimePicker;

import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.service.MeetingApiService;
import com.lamzone.mareu.ui.ListMeetingActivity_Main;
import com.lamzone.mareu.utils.AddFakeMeetingData;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Calendar;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.lamzone.mareu.ui.ListMeetingActivity_Main.sApiService;
import static com.lamzone.mareu.utils.RecyclerViewItemCountAssertion.withItemCount;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.core.AllOf.allOf;

@RunWith(AndroidJUnit4.class)
public class AddMeetingTest {
    private List<Meeting> meetings;
    private MeetingApiService mMeetingApiService;
    private static int ITEMS_COUNT = 6;

    @Rule
    public ActivityTestRule<ListMeetingActivity_Main> mActivityTestRule = new ActivityTestRule<ListMeetingActivity_Main>(ListMeetingActivity_Main.class)

    {
        @Override
        protected void beforeActivityLaunched() {
            insertFakeMeetings();
    }
    };

    // Check if click on the addBtn show the AddMeetingActivity
    @Test
    public void addActivity_LaunchTest() {
        //mApiService = DI.getMeetingApiService();
        //meetings = mApiService.getMeetings(null, null);
        insertFakeMeetings();

        ViewInteraction recyclerList = onView( allOf(withId(R.id.List_Meeting_rv),isDisplayed()));
        ViewInteraction fab_addMeeting = onView( allOf(withId(R.id.fab_addMeeting),isDisplayed()));
        fab_addMeeting.perform(click());

        // If click is not perform the test fail
        onView(withId(R.id.roomName_spinner)).check(matches(isDisplayed()));
        sApiService.deleteMeetings();
    }

    @Test
    public void addActivity_BeforeRoomNameChoiceClick() {

        //Launching AddMeetingActivity
        ViewInteraction recyclerList = onView( allOf(withId(R.id.List_Meeting_rv),isDisplayed()));
        ViewInteraction fab_addMeeting = onView( allOf(withId(R.id.fab_addMeeting),isDisplayed()));
        fab_addMeeting.perform(click());

        // After AddActivity Launched;
        ViewInteraction roomNameSpinner = onView(allOf(withId(R.id.roomName_spinner), isDisplayed()));
        String roomNameSpinnerErrorMsg="Sélectionner une salle !";
        // Click On datepicker
        ViewInteraction dateMeeting_Iv = onView( allOf(withId(R.id.date_imageView),isDisplayed()));
        dateMeeting_Iv.perform(click());
        // The spinner is set to Error with text and red color
        // Verify that roomNameSpinner is on error
        ViewInteraction checkedTextView = onView(
                Matchers.allOf(withId(android.R.id.text1),
                        childAtPosition(
                                Matchers.allOf(withId(R.id.roomName_spinner),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                0)),
                                0),
                        isDisplayed()));
        checkedTextView.check(matches(withText(roomNameSpinnerErrorMsg)));
        sApiService.deleteMeetings();
    }

    @Test
    public void addGoodMeeting() {
        //insertFakeMeetings();
        //Launching AddMeetingActivity
        ViewInteraction recyclerList = onView( allOf(withId(R.id.List_Meeting_rv),isDisplayed()));
        ViewInteraction fab_addMeeting = onView( allOf(withId(R.id.fab_addMeeting),isDisplayed()));
        fab_addMeeting.perform(click());

        // Filling fields
        ViewInteraction appCompatSpinner = onView(Matchers.allOf(withId(R.id.roomName_spinner),isDisplayed()));
        appCompatSpinner.perform(click());
        onData(anything()).inRoot(isPlatformPopup()).atPosition(3).perform(click());
        onView(withId(R.id.roomName_spinner)).check(matches(withSpinnerText(containsString("Salle 03"))));

        ViewInteraction date_ImgV = onView(Matchers.allOf(withId(R.id.date_imageView),isDisplayed()));
        date_ImgV.perform(click());
        ViewInteraction date_Ok_Btn = onView(Matchers.allOf(withId(android.R.id.button1), withText("OK"),isDisplayed()));
        date_Ok_Btn.perform(click());

        Calendar timeNow = Calendar.getInstance();
        int hourNow = timeNow.get(Calendar.HOUR_OF_DAY);

        ViewInteraction startTime_Imgv = onView(Matchers.allOf(withId(R.id.startTime_imageView),isDisplayed()));
        startTime_Imgv.perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hourNow+1, 0));



        ViewInteraction startTime_Ok_Btn = onView(Matchers.allOf(withId(android.R.id.button1), withText("OK"),isDisplayed()));
        startTime_Ok_Btn.perform(click());

        ViewInteraction endTime_ImgV = onView(Matchers.allOf(withId(R.id.endTime_imageView),isDisplayed()));
        endTime_ImgV.perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(hourNow+2, 0));

        ViewInteraction endTime_Ok_Btn = onView(Matchers.allOf(withId(android.R.id.button1), withText("OK"),isDisplayed()));
        endTime_Ok_Btn.perform(click());

        ViewInteraction aboutMeeting_It = onView(Matchers.allOf(withId(R.id.aboutMeeting_It),isDisplayed()));
        aboutMeeting_It.perform(click());
        aboutMeeting_It.perform(replaceText("Test application Maréu"),closeSoftKeyboard());

        ViewInteraction attendees_Spinner = onView(allOf((withId(R.id.attendees_spinner)), isDisplayed()));
        attendees_Spinner.perform(click());
        // Click On CheckBoxes
        onData(anything())
                .atPosition(1)
                .onChildView(withClassName(Matchers.containsString("CheckBox")))
                .perform(click());
        onData(anything())
                .atPosition(3)
                .onChildView(withClassName(Matchers.containsString("CheckBox")))
                .perform(click());
        //Assertions
        onData(anything())
                .atPosition(1)
                .onChildView(withClassName(Matchers.containsString("CheckBox")))
                .check(matches(isChecked()));
        onData(anything())
                .atPosition(3)
                .onChildView(withClassName(Matchers.containsString("CheckBox")))
                .check(matches(isChecked()));
         // Closing Spinner
        pressBack();
        //attendees_Spinner.perform(click());

        ViewInteraction total_Attendees = onView(Matchers.allOf(withId(R.id.totalAttendee_Tv),isDisplayed()));
        total_Attendees.check(matches(withText("Nombre de participants : 2")));

        ViewInteraction textInputEditText2 = onView(
        Matchers.allOf(withId(R.id.attendee_It),isDisplayed()));
        textInputEditText2.perform(replaceText("bozo56@orange.fr"), closeSoftKeyboard());

        ViewInteraction floatingActionButton2 = onView(Matchers.allOf(withId(R.id.addAttendeeFab),isDisplayed()));
        floatingActionButton2.perform(click());

        textInputEditText2.perform(replaceText("bozo67@orange.fr"), closeSoftKeyboard());
        floatingActionButton2.perform(click());

        total_Attendees.check(matches(withText("Nombre de participants : 4")));

        ViewInteraction add_Btn = onView(Matchers.allOf(withId(R.id.btnAdd), withText("Ajouter"),isDisplayed()));
        add_Btn.perform(click());

        onView(Matchers.allOf(withId(R.id.List_Meeting_rv), isDisplayed())).check((ViewAssertion) withItemCount(greaterThan( ITEMS_COUNT)));
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

    public void insertFakeMeetings() {
        mMeetingApiService = DI.getNewInstanceApiService();
        meetings = mMeetingApiService.getMeetings(null, null);
        sApiService = DI.getMeetingApiService();
        // Putting 6 Meetings
               for (int i=0; i<6;i++) {
                sApiService.addMeeting(AddFakeMeetingData.generateFakeMeeting().get(i));
               }
        }
}
