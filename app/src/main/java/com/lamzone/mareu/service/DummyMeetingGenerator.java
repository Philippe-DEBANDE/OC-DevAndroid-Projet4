package com.lamzone.mareu.service;

import com.lamzone.mareu.model.Meeting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class DummyMeetingGenerator {

    public static List<String> Rooms = new ArrayList<>(Arrays.asList(
       "Sélectionner une salle :",
            "Salle 01", "Salle 02", "Salle 03", "Salle 04", "Salle 05", "Salle 06 ", "Salle 07", "Salle 08", "Salle 09 ",  "Salle 10"
    ));

    public static List<String> Attendees = new ArrayList<>(Arrays.asList(
            "Sélectionner le(s) participants :","alex@lamzone.com","amandine@lamzone.com", "paul@lamzone.com", "maxime@lamzone.com", "karine@lamzone.com", "phil@lamzone.com")
    );

    public static List<Meeting> Meetings = new ArrayList<>();



    static List<Meeting> generateMeeting() {return new ArrayList<>(Meetings);}

    static List<String> generateRooms() {return new ArrayList<>(Rooms);}

    static List<String> generateAttendees() {return new ArrayList<>(Attendees);}
}
