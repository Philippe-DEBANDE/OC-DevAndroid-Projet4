package com.lamzone.mareu.events;

import com.lamzone.mareu.model.Meeting;

public class DeleteMeetingEvent {

    /**
     * Delete event for a meeting
     */
    public Meeting meeting;

    /**
     * Constructor
     * @param meeting
     */
    public DeleteMeetingEvent(Meeting meeting) {this.meeting = meeting;}


}

