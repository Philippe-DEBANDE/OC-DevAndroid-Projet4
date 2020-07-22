package com.lamzone.mareu.service;

import com.lamzone.mareu.model.Meeting;
import com.lamzone.mareu.utils.MeetingUtils;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static com.lamzone.mareu.utils.MeetingUtils.getMeetingsMatchDate;

public class DummyMeetingApiService implements MeetingApiService {

    private List<Meeting> meetings = DummyMeetingGenerator.generateMeeting();
    private List<String> Rooms = DummyMeetingGenerator.generateRooms();
    private List<String> Attendees = DummyMeetingGenerator.generateAttendees();

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Meeting> getMeetings(Calendar date, String roomName) {

        if (roomName != null && ! roomName.isEmpty())
            return MeetingUtils.getMeetingsMatchRoomName(roomName, meetings);
        else if (date != null)
            return getMeetingsMatchDate(date, meetings);
        // Sorting by startTime
        Collections.sort(meetings, Meeting::compareTo);
        return meetings;
    }


    /**
     * {@inheritDoc}
     * @param meeting
     */
    @Override
    public void addMeeting(Meeting meeting) { meetings.add(meeting); }

    /**
     * {@inheritDoc}
     * @param meeting
     */
    @Override
    public void deleteMeeting(Meeting meeting) { meetings.remove(meeting); }

    /**
     * {@inheritDoc}
     * Emptying meetings list
     */
    public void deleteMeetings() { meetings.clear(); }


    /**
     * Get all meeting rooms
     *
     * @return list of meeting rooms
     */
    public List<String> getRooms() {

        return Rooms;
    }

    /**
     * Get all attendees
     *
     * @return list of meeting rooms
     */
    public List<String> getAttendees() {
        Collections.sort(Attendees);
        return Attendees;
    }

}
