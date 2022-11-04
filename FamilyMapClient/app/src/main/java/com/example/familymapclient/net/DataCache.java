package com.example.familymapclient.net;

import com.example.familymapclient.model.RelationshipModel;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Models.EventModel;
import Models.PersonModel;
import Results.AllEventsResult;
import Results.AllPersonsResult;

import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_AZURE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_CYAN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_GREEN;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_MAGENTA;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ORANGE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_RED;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_ROSE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_VIOLET;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;

public class DataCache {

    private static final int NUM_COLORS = 11;
    public int colorIterator;
    private String curAuthToken;
    private Float[] colors = {HUE_AZURE,HUE_BLUE,HUE_CYAN,HUE_GREEN,
            HUE_MAGENTA,HUE_ORANGE,HUE_RED,HUE_ROSE,HUE_VIOLET,HUE_YELLOW};
    private boolean fatherSwitch = true;
    private boolean motherSwitch = true;
    private boolean maleSwitch = true;
    private boolean femaleSwitch = true;
    private boolean lifeStorySwitch = true;
    private boolean familyTreeSwitch = true;
    private boolean spouseSwitch = true;
    private Map<String, PersonModel> peopleMap;
    private Map<String, EventModel> eventMap;
    private Map<String, List<EventModel>> peopleEvents;
    private Map<String, Float> eventColors;
    private Map<String, PersonModel> peopleMapCopy;
    private Map<String, EventModel> eventMapCopy;
    private Map<String, List<EventModel>> peopleEventsCopy;
    private String currentUserPersonID;
    private EventModel currentEvent;
    private PersonModel currentPerson;
    private GoogleMap currentMap;

    public PersonModel getCurrentPerson() {
        return currentPerson;
    }
    public void setCurrentPerson(PersonModel currentPerson) {
        this.currentPerson = currentPerson;
    }
    public EventModel getCurrentEvent() { return currentEvent; }
    public void setCurrentEvent(EventModel currentEvent) { this.currentEvent = currentEvent; }
    public String getCurrentUserPersonID() {
        return currentUserPersonID;
    }
    public void setCurrentUserPersonID(String currentUserPersonID) { this.currentUserPersonID = currentUserPersonID; }
    public boolean isFatherSwitch() {
        return fatherSwitch;
    }
    public void setFatherSwitch(boolean fatherSwitch) {
        this.fatherSwitch = fatherSwitch;
    }
    public boolean isMotherSwitch() {
        return motherSwitch;
    }
    public void setMotherSwitch(boolean motherSwitch) {
        this.motherSwitch = motherSwitch;
    }
    public boolean isMaleSwitch() {
        return maleSwitch;
    }
    public void setMaleSwitch(boolean maleSwitch) {
        this.maleSwitch = maleSwitch;
    }
    public boolean isFemaleSwitch() {
        return femaleSwitch;
    }
    public void setFemaleSwitch(boolean femaleSwitch) {
        this.femaleSwitch = femaleSwitch;
    }
    public boolean isLifeStorySwitch() {
        return lifeStorySwitch;
    }
    public void setLifeStorySwitch(boolean lifeStorySwitch) { this.lifeStorySwitch = lifeStorySwitch; }
    public boolean isFamilyTreeSwitch() {
        return familyTreeSwitch;
    }
    public void setFamilyTreeSwitch(boolean familyTreeSwitch) { this.familyTreeSwitch = familyTreeSwitch; }
    public boolean isSpouseSwitch() {
        return spouseSwitch;
    }
    public void setSpouseSwitch(boolean spouseSwitch) {
        this.spouseSwitch = spouseSwitch;
    }
    public String getCurAuthToken () { return curAuthToken; }
    public Map<String, PersonModel> getPeopleMap() { return peopleMap; }
    public Map<String, EventModel> getEventMap() { return eventMap; }
    public Map<String, List<EventModel>> getPeopleEvents() { return peopleEvents; }
    public Map<String, Float> getEventColors() { return eventColors; }
    public Map<String, PersonModel> getPeopleMapCopy() {
        return peopleMapCopy;
    }
    public Map<String, EventModel> getEventMapCopy() {
        return eventMapCopy;
    }
    public Map<String, List<EventModel>> getPeopleEventsCopy() {
        return peopleEventsCopy;
    }
    public GoogleMap getCurrentMap() { return currentMap; }
    public void setCurrentMap(GoogleMap currentMap) { this.currentMap = currentMap; }


    DataCache() {
        peopleMap = new HashMap<String, PersonModel>();
        eventMap = new HashMap<String, EventModel>();
        peopleEvents = new HashMap<String, List<EventModel>>();
        eventColors = new HashMap<String, Float>();
        peopleMapCopy = new HashMap<String, PersonModel>();
        eventMapCopy = new HashMap<String, EventModel>();
        peopleEventsCopy = new HashMap<String, List<EventModel>>();
    }
    public static DataCache getDataCache() {
        return dataCache;
    }
    private static final DataCache dataCache = new DataCache();

    public void initialMapCopyFill() {
        for (Map.Entry<String,PersonModel> entry : peopleMap.entrySet()) {
            peopleMapCopy.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String,EventModel> entry : eventMap.entrySet()) {
            eventMapCopy.put(entry.getKey(), entry.getValue());
        }
        for (Map.Entry<String,List<EventModel>> entry : peopleEvents.entrySet()) {
            if (!peopleEventsCopy.containsKey(entry.getKey())) {
                peopleEventsCopy.put(entry.getKey(),new ArrayList<EventModel>());
            }
            if (!entry.getValue().isEmpty()) {
                for (EventModel e : entry.getValue()) {
                    peopleEventsCopy.get(entry.getKey()).add(e);
                }
            }
        }
        String breakpoint = "breakpoint";
    }

    public void fillPeopleMap (AllPersonsResult allPersonsResult) {
        PersonModel[] data = allPersonsResult.getData();
        for (PersonModel pm : data) {
            peopleMap.put(pm.getPersonID(), pm);
        }
    }
    public void fillEventMap (AllEventsResult allEventsResult) {
        EventModel[] data = allEventsResult.getData();
        for (EventModel em : data) {
            eventMap.put(em.getEventID(), em);
        }
    }
    public void fillPeopleEvents (AllEventsResult allEventsResult) {
        EventModel[] data = allEventsResult.getData();
        for (EventModel e : data) {
            if (!peopleEvents.containsKey(e.getPersonID())) {
                peopleEvents.put(e.getPersonID(), new ArrayList<EventModel>());
            }
            peopleEvents.get(e.getPersonID()).add(e);
        }
        int i = 0;
    }
    public void fillEventColors(AllEventsResult allEventsResult) {
        EventModel[] myEvents = allEventsResult.getData();
        for (EventModel e : myEvents) {
            if (!eventColors.containsKey(e.getEventType())) {
                if (colorIterator == 9) { colorIterator = 0; }
                eventColors.put(e.getEventType(),colors[colorIterator]);
                colorIterator++;
            }
        }
    }

    public void reloadMapCopies () {
        peopleMapCopy.clear();
        eventMapCopy.clear();
        peopleEventsCopy.clear();
        addPersonToMapCopies(currentUserPersonID);
        addPersonToMapCopies(peopleMap.get(currentUserPersonID).getSpouseID());
        if (motherSwitch) {
            fillMomSide();
        }
        if (fatherSwitch) {
            fillDadSide();
        }
        if (!femaleSwitch) {
            removeFemales();
        }
        if (!maleSwitch) {
            removeMales();
        }
    }
    public void fillMomSide() {
        addPersonToMapCopies(currentUserPersonID);
        String curUserMomID = null;
        PersonModel curPerson = peopleMap.get(currentUserPersonID);

        if (curPerson != null) {
            if (curPerson.getMotherID() != null) {
                curUserMomID = curPerson.getMotherID();
            }
            PersonModel curMom = peopleMap.get(curUserMomID);
            if (curMom != null) {
                addPersonToMapCopies(curUserMomID);
                fill_Helper(curMom.getMotherID(), curMom.getFatherID());
            }
        }
    }
    public void fillDadSide() {
        addPersonToMapCopies(currentUserPersonID);
        String curUserDadID = null;
        PersonModel curPerson = peopleMap.get(currentUserPersonID);

        if (curPerson != null) {
            if (curPerson.getMotherID() != null) {
                curUserDadID = curPerson.getFatherID();
            }
            PersonModel curDad = peopleMap.get(curUserDadID);
            if (curDad != null) {
                addPersonToMapCopies(curUserDadID);
                fill_Helper(curDad.getMotherID(), curDad.getFatherID());
            }
        }
    }
    public void fill_Helper(String momID, String dadID) {
        PersonModel mom = peopleMap.get(momID);
        if (mom != null) {
            addPersonToMapCopies(momID);
            fill_Helper(mom.getMotherID(), mom.getFatherID());
        }
        PersonModel dad = peopleMap.get(momID);
        if (dad != null) {
            addPersonToMapCopies(dadID);
            fill_Helper(dad.getMotherID(), dad.getFatherID());
        }
    }

    public void addPersonToMapCopies(String personID) {
        for (Map.Entry<String,PersonModel> entry : peopleMap.entrySet()) {
            if (entry.getKey().equals(personID)) {
                peopleMapCopy.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry<String,EventModel> entry : eventMap.entrySet()) {
            if (entry.getValue().getPersonID().equals(personID)) {
                eventMapCopy.put(entry.getKey(), entry.getValue());
            }
        }
        if (!peopleEventsCopy.containsKey(personID)) {
            peopleEventsCopy.put(personID,new ArrayList<EventModel>());
        }
        if (peopleEvents.get(personID) != null) {
            for (EventModel e : peopleEvents.get(personID)) {
                if (!peopleEventsCopy.get(personID).contains(e)) {
                    peopleEventsCopy.get(personID).add(e);
                }
            }
        }
    }

    public void removeMales() {
        if (peopleMapCopy != null) {
            Map<String,PersonModel> tempPeopleMap = new HashMap<String, PersonModel>();
            for (Map.Entry<String,PersonModel> entry : peopleMapCopy.entrySet()) {
                if (entry.getValue().getGender().equals("f")) {
                    tempPeopleMap.put(entry.getKey(), entry.getValue());
                }
            }
            peopleMapCopy.clear();
            peopleMapCopy = new HashMap<String,PersonModel>(tempPeopleMap);
        }
        if (eventMapCopy != null) {
            Map<String,EventModel> tempEventMap = new HashMap<String, EventModel>();
            for (Map.Entry<String, EventModel> entry : eventMapCopy.entrySet()) {
                if (peopleMap.get(entry.getValue().getPersonID()).getGender().equals("f")) {
                    tempEventMap.put(entry.getKey(), entry.getValue());
                }
            }
            eventMapCopy.clear();
            eventMapCopy = new HashMap<String,EventModel>(tempEventMap);
        }
        if (peopleEventsCopy != null) {
            Map<String,List<EventModel>> tempPeopleEvents = new HashMap<String, List<EventModel>>();
            for (String personID : peopleEventsCopy.keySet()) {
                tempPeopleEvents.put(personID, new ArrayList<EventModel>());
            }
            for (Map.Entry<String,List<EventModel>> entry : peopleEventsCopy.entrySet()) {
                if (peopleMap.get(entry.getKey()).getGender().equals("f")) {
                    if (!entry.getValue().isEmpty()) {
                        for (EventModel e : entry.getValue()) {
                            tempPeopleEvents.get(entry.getKey()).add(e);
                        }
                    }
                }
            }
            peopleEventsCopy.clear();
            peopleEventsCopy = new HashMap<String, List<EventModel>>(tempPeopleEvents);
        }
    }
    public void removeFemales() {
        if (peopleMapCopy != null) {
            Map<String,PersonModel> tempPeopleMap = new HashMap<String, PersonModel>();
            for (Map.Entry<String,PersonModel> entry : peopleMapCopy.entrySet()) {
                if (entry.getValue().getGender().equals("m")) {
                    tempPeopleMap.put(entry.getKey(), entry.getValue());
                }
            }
            peopleMapCopy.clear();
            peopleMapCopy = new HashMap<String,PersonModel>(tempPeopleMap);
        }
        if (eventMapCopy != null) {
            Map<String,EventModel> tempEventMap = new HashMap<String, EventModel>();
            for (Map.Entry<String, EventModel> entry : eventMapCopy.entrySet()) {
                if (peopleMap.get(entry.getValue().getPersonID()).getGender().equals("m")) {
                    tempEventMap.put(entry.getKey(), entry.getValue());
                }
            }
            eventMapCopy.clear();
            eventMapCopy = new HashMap<String,EventModel>(tempEventMap);
        }
        if (peopleEventsCopy != null) {
            Map<String,List<EventModel>> tempPeopleEvents = new HashMap<String, List<EventModel>>();
            for (Map.Entry<String,List<EventModel>> entry : peopleEventsCopy.entrySet()) {
                if (peopleMap.get(entry.getKey()).getGender().equals("m")) {
                    if (!entry.getValue().isEmpty()) {
                        for (EventModel e : entry.getValue()) {
                            if (tempPeopleEvents.get(entry.getKey())!= null) {
                                tempPeopleEvents.get(entry.getKey()).add(e);
                            }
                        }
                    }
                }
            }
            peopleEventsCopy.clear();
            peopleEventsCopy = new HashMap<String, List<EventModel>>(tempPeopleEvents);
        }
    }

    public List<PersonModel> getSearchedPeopleDataCache(String searchText) {
        List<PersonModel> peopleToReturn = new ArrayList<PersonModel>();
        if (searchText != null && searchText != "") {
            searchText = searchText.toLowerCase();
            for (PersonModel p : peopleMapCopy.values()) {
                if (p != null) {
                    if (p.getFirstName().toLowerCase().contains(searchText)) {
                        peopleToReturn.add(p);
                    }
                    if (p.getLastName().toLowerCase().contains(searchText)) {
                        peopleToReturn.add(p);
                    }
                }
            }
        }
        return peopleToReturn;
    }
    public List<EventModel> getSearchedEventsDataCache(String searchText) {
        List<EventModel> eventsToReturn = new ArrayList<EventModel>();
        if (searchText != null && searchText != "") {
            searchText = searchText.toLowerCase();
            for (EventModel e : eventMapCopy.values()) {
                if (e != null) {
                    if (e.getCountry().toLowerCase().contains(searchText)) {
                        eventsToReturn.add(e);
                    }
                    if (e.getCity().toLowerCase().contains(searchText)) {
                        eventsToReturn.add(e);
                    }
                    if (e.getEventType().toLowerCase().contains(searchText)) {
                        eventsToReturn.add(e);
                    }
                    if (String.valueOf(e.getYear()).toLowerCase().contains(searchText)) {
                        eventsToReturn.add(e);
                    }
                }
            }
        }
        return eventsToReturn;
    }
    public List<RelationshipModel> getFamilyMembers() {
        String currentPersonID = getCurrentPerson().getPersonID();
        List<RelationshipModel> familyMembers = new ArrayList<RelationshipModel>();

        String dadID = null;
        String momID = null;
        String spouseID = null;

        if (peopleMapCopy.containsKey(Objects.requireNonNull(peopleMapCopy.get(currentPersonID))
                .getFatherID())) {
            dadID =  Objects.requireNonNull(peopleMapCopy.get(currentPersonID)).getFatherID();
            familyMembers.add(new RelationshipModel(peopleMapCopy.get(dadID),"Father"));
        }
        if (peopleMapCopy.containsKey(Objects.requireNonNull(peopleMapCopy.get(currentPersonID))
                .getMotherID())) {
            momID = Objects.requireNonNull(peopleMapCopy.get(currentPersonID)).getMotherID();
            familyMembers.add(new RelationshipModel(peopleMapCopy.get(momID),"Mother"));
        }
        if (peopleMapCopy.containsKey(Objects.requireNonNull(peopleMapCopy.get(currentPersonID))
                .getSpouseID())) {
            spouseID = Objects.requireNonNull(peopleMapCopy.get(currentPersonID)).getSpouseID();
            familyMembers.add(new RelationshipModel(peopleMapCopy.get(spouseID),"Spouse"));
        }
        for (Map.Entry<String,PersonModel> entry : peopleMapCopy.entrySet()) {
            if (entry.getValue().getFatherID() != null) {
                if (entry.getValue().getFatherID().equals(currentPersonID)) {
                    familyMembers.add(new RelationshipModel(entry.getValue(),"Child"));
                }
            }
            if (entry.getValue().getMotherID() != null) {
                if (entry.getValue().getMotherID().equals(currentPersonID)) {
                    familyMembers.add(new RelationshipModel(entry.getValue(),"Child"));
                }
            }
        }
        return familyMembers;
    }

    public List<EventModel> getOrderedEvents() {
        List<EventModel> orderedEventsToReturn = new ArrayList<EventModel>();
        String currentPersonID = getCurrentPerson().getPersonID();

        List<EventModel> events = getPeopleEventsCopy().get(currentPersonID);
        if (events != null) {
            List<EventModel> eventsCopy = new ArrayList<EventModel>(events);
            while (eventsCopy.size() != 0) {
                orderedEventsToReturn.add(getEarliestEvent(eventsCopy));
                eventsCopy.remove(getEarliestEvent(eventsCopy));
            }
        }
        return orderedEventsToReturn;
    }

    public EventModel getEarliestEvent(List<EventModel> events) {
        int earliestEventYear = 10000;
        EventModel earliestEvent = null;
        for (EventModel e : events)  {
            if (e.getYear() < earliestEventYear) {
                earliestEventYear = e.getYear();
                earliestEvent = e;
            }
        }
        return earliestEvent;
    }
}