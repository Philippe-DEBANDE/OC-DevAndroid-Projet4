package com.lamzone.mareu;

import com.lamzone.mareu.di.DI;
import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.service.DummyMeetingGenerator;
import com.lamzone.mareu.service.MeetingApiService;
import com.lamzone.mareu.utils.AddFakeMeetingData;
import com.lamzone.mareu.utils.MeetingUtils;

import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class MeetingServiceTest {

    private MeetingApiService mMeetingApiService;

    @Before
    public void setup() {mMeetingApiService = DI.getNewInstanceApiService();


    }

    @Test
    public void getMeetingWithSuccess() {
        List<Meeting> meetings = mMeetingApiService.getMeetings(null, null);
        // Putting 6 Fake Meetings
        addFakeMeetingData();
        assertTrue(meetings.size() > 0);

    }

    @Test
    public void getRoomNamesWithSuccess() {
        List<String> roomNames = mMeetingApiService.getRooms();
        List<String> expectedRoomNames = DummyMeetingGenerator.Rooms;
        assertThat(roomNames, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedRoomNames.toArray()));

    }

    @Test
    public void getAttendeesWithSuccess() {
        List<String> attendees = mMeetingApiService.getAttendees();
        List<String> expectedAttendees = DummyMeetingGenerator.Attendees;
        assertThat(attendees, IsIterableContainingInAnyOrder.containsInAnyOrder(expectedAttendees.toArray()));

    }

    @Test
    public void deleteMeetingWithSuccess() {
        List<Meeting> meetings = mMeetingApiService.getMeetings(null, null);
        addFakeMeetingData();
        Meeting meetingToDelete = AddFakeMeetingData.generateFakeMeeting().get(0);
        mMeetingApiService.deleteMeeting(meetingToDelete);
        assertFalse(meetings.contains(meetingToDelete));
    }

    @Test
    public void addMeetingWithSuccess() {
        List<Meeting> meetings = mMeetingApiService.getMeetings(null, null);
        Meeting meetingToAdd = AddFakeMeetingData.generateFakeMeeting().get(0);
        mMeetingApiService.addMeeting(meetingToAdd);
        assertTrue(mMeetingApiService.getMeetings(null, null).contains(meetingToAdd));

    }

    @Test
    public void addMeetinWithError() {
        List<Meeting> meetings = mMeetingApiService.getMeetings(null, null);
        addFakeMeetingData();
        Meeting meetingToAdd = AddFakeMeetingData.generateFakeMeeting().get(3);
        List<Meeting> tmp = MeetingUtils.getMeetingsMatchRoomName(meetingToAdd.getRoomName(),meetings );
        assertFalse(MeetingUtils.goodMeetingroomName(meetingToAdd.getRoomName(), meetingToAdd.getStartTime(),meetingToAdd.getEndTime(),tmp ));
    }

    @Test
    public void returnfilteroomNamesSuccess() {
        List<String> roomNames = mMeetingApiService.getRooms();
        List<Meeting> meetings = mMeetingApiService.getMeetings(null, null);
        addFakeMeetingData();
        String expectedRoomName=roomNames.get(3);
        meetings = mMeetingApiService.getMeetings(null, expectedRoomName);

        for (int i=0; i<meetings.size();i++) {
            //assertSame(meetings.get(i).getRoomName(), true, equals(expectedRoomName));
            assertTrue((meetings.get(i).getRoomName().equals(expectedRoomName)));
        }
    }

    @Test
    public void returnfilterDateSuccess() {

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar expectedDate = Calendar.getInstance();
        List<Meeting> meetings = mMeetingApiService.getMeetings(null, null);
        addFakeMeetingData();
        meetings = mMeetingApiService.getMeetings(expectedDate, null);

        String sexpectedDate= (dateFormat.format(expectedDate.getTime()));
        for (int i=0; i<meetings.size();i++) {
           String meetingDate= (dateFormat.format(meetings.get(i).getStartTime().getTime()));
        assertTrue((meetingDate.equals(sexpectedDate)));
        }
    }

    // Putting 6 Meetings
    public void addFakeMeetingData() {
        for (int i=0; i<6;i++) {
            mMeetingApiService.addMeeting(AddFakeMeetingData.generateFakeMeeting().get(i));
        }
    }

}
