package com.lamzone.mareu.events;

public class MyCustomEvent {
String eventAttendee;

    public MyCustomEvent(String eventAttendee) {
    this.eventAttendee = eventAttendee;
    }

    public String getEventAttendee() {

        return eventAttendee;
    }

}


