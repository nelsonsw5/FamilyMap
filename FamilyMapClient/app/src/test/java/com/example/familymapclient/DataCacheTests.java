package com.example.familymapclient;

import com.example.familymapclient.model.RelationshipModel;
import com.example.familymapclient.net.DataCache;
import com.example.familymapclient.net.ServerProxy;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import Models.EventModel;
import Models.PersonModel;
import Requests.LoginRequest;
import Results.AllEventsResult;
import Results.AllPersonsResult;

import static org.junit.jupiter.api.Assertions.*;

class DataCacheTests {

    String serverHost = "localhost";
    String serverPort = "8000";

    @BeforeEach
    void setUp() {
        ServerProxy.load(serverHost,serverPort);

        String authToken = ServerProxy.login(serverHost,serverPort,
                new LoginRequest("sheila","parker")).getAuthtoken();

        PersonModel sheila = new PersonModel("Sheila_Parker","sheila",
                "Sheila","Parker","f","Davis_Hyer",
                "Blaine_McGary","Betty_White");

        AllEventsResult aer = ServerProxy.getAllEvents(serverHost,serverPort,authToken);
        AllPersonsResult apr = ServerProxy.getAllPersons(serverHost,serverPort,authToken);

        DataCache.getDataCache().fillPeopleEvents(aer);
        DataCache.getDataCache().fillPeopleMap(apr);
        DataCache.getDataCache().fillEventMap(aer);

        DataCache.getDataCache().initialMapCopyFill();
        DataCache.getDataCache().setCurrentPerson(sheila);
        DataCache.getDataCache().setCurrentUserPersonID("Sheila_Parker");

        DataCache.getDataCache().setMotherSwitch(true);
        DataCache.getDataCache().setFatherSwitch(true);
        DataCache.getDataCache().setFemaleSwitch(true);
        DataCache.getDataCache().setMaleSwitch(true);
        DataCache.getDataCache().reloadMapCopies();
    }

    @Test
    @DisplayName("getFamilyMembers() Pass")
    void getFamilyMembersPass() {
        List<RelationshipModel> fam = DataCache.getDataCache().getFamilyMembers();
        for (RelationshipModel r : fam) {
            if (r.getRelationship().equals("Father")) {
                assertEquals(r.getPerson().getPersonID(),"Blaine_McGary");
            }
            if (r.getRelationship().equals("Mother")) {
                assertEquals(r.getPerson().getPersonID(),"Betty_White");
            }
            if (r.getRelationship().equals("Spouse")) {
                assertEquals(r.getPerson().getPersonID(),"Davis_Hyer");
            }
        }
    }

    @Test
    @DisplayName("getFamilyMembers() Fail")
    void getFamilyMembersFail() {
        PersonModel mrs_rodham = DataCache.getDataCache().getPeopleMap().get("Mrs_Rodham");
        DataCache.getDataCache().setCurrentPerson(mrs_rodham);
        List<RelationshipModel> fam = DataCache.getDataCache().getFamilyMembers();
        PersonModel father = null;
        for (RelationshipModel r : fam) {
            if (r.getRelationship().equals("Father")) {
                father = r.getPerson();
            }
        }
        assertNull(father);
    }

    @Test
    @DisplayName("Settings/Events Pass")
    void filterEventsPass() {
        DataCache.getDataCache().setMotherSwitch(true);
        DataCache.getDataCache().setFatherSwitch(true);
        DataCache.getDataCache().setFemaleSwitch(true);
        DataCache.getDataCache().setMaleSwitch(false);
        DataCache.getDataCache().reloadMapCopies();

        Map<String,EventModel> eventMap = DataCache.getDataCache().getEventMapCopy();

        for (EventModel e : eventMap.values()) {
            String gender = DataCache.getDataCache().getPeopleMap().get(e.getPersonID()).getGender();
            assertEquals("f",gender);
        }
    }

    @Test
    @DisplayName("Settings/Events Edge Case")
    void filterEventsEdgeCase() {
        DataCache.getDataCache().setMotherSwitch(true);
        DataCache.getDataCache().setFatherSwitch(true);
        DataCache.getDataCache().setFemaleSwitch(false);
        DataCache.getDataCache().setMaleSwitch(false);
        DataCache.getDataCache().reloadMapCopies();

        Map<String,EventModel> eventMap = DataCache.getDataCache().getEventMapCopy();

        assertEquals(0,eventMap.size());
    }

    @Test
    @DisplayName("Sorting Events #1")
    void sortEventsTest() {
        List<EventModel> sortedEvents = DataCache.getDataCache().getOrderedEvents();
        for (int i = 0; i < sortedEvents.size()-1; ++i) {
            assertTrue(sortedEvents.get(i).getYear() <= sortedEvents.get(i+1).getYear());
        }
    }

    @Test
    @DisplayName("Sorting Events #2")
    void sortEventsTest2() {
        // in this case, Betty has 1 event, just want to make sure the sortedEvents still works.
        DataCache.getDataCache().setCurrentPerson(DataCache.getDataCache().getPeopleMap().get("Betty_White"));
        List<EventModel> sortedEvents = DataCache.getDataCache().getOrderedEvents();
        assertEquals(1, sortedEvents.size());
    }

    @Test
    @DisplayName("Search Results: Events")
    void searchTestEvents() {
        List<EventModel> searchEvents = DataCache.getDataCache().getSearchedEventsDataCache("lear");
        boolean learnJava = false;
        boolean learnSurf = false;

        for (EventModel e : searchEvents) {
            if (e.getEventType().equals("learned Java")) {
                learnJava = true;
            }
            if (e.getEventType().equals("Learned to Surf")) {
                learnSurf = true;
            }
        }
        assertTrue(learnJava);
        assertTrue(learnSurf);
    }

    @Test
    @DisplayName("Search Results: People")
    void searchTestPeople() {
        List<PersonModel> searchPeople = DataCache.getDataCache().getSearchedPeopleDataCache("ar");
        boolean foundSheila = false;
        boolean foundBlaine = false;

        for (PersonModel p : searchPeople) {
            if (p.getPersonID().equals("Sheila_Parker")) {
                foundSheila = true;
            }
            if (p.getPersonID().equals("Blaine_McGary")) {
                foundBlaine = true;
            }
        }
        assertTrue(foundSheila);
        assertTrue(foundBlaine);
    }
}