package com.lamzone.mareu.service;

import com.lamzone.mareu.model.Meeting;

import java.util.Calendar;
import java.util.List;

public interface MeetingApiService {


    /**
     * Get all my Meeting
     * @return {@link List}
     */

    List<Meeting> getMeetings(Calendar date, String RoomName);

     /**
     * Add a Meeting
     * @param meeting
     */
    void addMeeting(Meeting meeting);

    /**
     * Delete a Meeting
     * @param meeting
     */
    void deleteMeeting(Meeting meeting);

    /**
     * Delete all Meeting
     *
     */
    void deleteMeetings();

     /**
     * Get all meeting Rooms
     * @return list of meeting rooms
     */
    List<String> getRooms();

    /**
     * Get all meeting Attendees
     * @return list of meeting rooms
     */
    List<String> getAttendees();


}
