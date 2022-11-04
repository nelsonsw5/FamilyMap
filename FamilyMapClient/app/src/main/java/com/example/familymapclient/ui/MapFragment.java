package com.example.familymapclient.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.net.DataCache;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import Models.EventModel;
import Models.PersonModel;

public class MapFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback {
    private GoogleMap map;
    List<Polyline> myPolyLines;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(layoutInflater, container, savedInstanceState);
        setHasOptionsMenu(true);
        DataCache.getDataCache().reloadMapCopies();
        View view = layoutInflater.inflate(R.layout.fragment_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);
        requireActivity().setTitle("Family Map");

        FragmentActivity fragmentActivity = this.getActivity();
        String fragmentName = fragmentActivity.getClass().getName();

        if (fragmentName.contains("EventActivity")) {
            getActivity().setTitle("Event Activity");
            EventModel markerEvent = DataCache.getDataCache().getCurrentEvent();
            DataCache.getDataCache().setCurrentEvent(markerEvent);
            TextView eventText = view.findViewById(R.id.mapTextView);
            eventText.setTag(DataCache.getDataCache().getCurrentEvent());
            eventText.setText(getEventString(markerEvent));
            eventText.setTextSize(15);
        }
        view.findViewById(R.id.mapTextView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PersonModel selectedPerson = DataCache.getDataCache().getPeopleMap().
                        get(DataCache.getDataCache().getCurrentEvent().getPersonID());
                DataCache.getDataCache().setCurrentPerson(selectedPerson);
                DataCache.getDataCache().reloadMapCopies();
                switchToPersonActivity();
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        FragmentActivity fragmentActivity = this.getActivity();
        assert fragmentActivity != null;
        String fragmentName = fragmentActivity.getClass().getName();
        if (!fragmentName.contains("EventActivity")) {
            inflater.inflate(R.menu.mapfragmentmenu, menu);
        }
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected (MenuItem menuItem) {
        FragmentActivity fragmentActivity = this.getActivity();
        assert fragmentActivity != null;
        String fragmentName = fragmentActivity.getClass().getName();
        if (fragmentName.contains("EventActivity")) {
            if (menuItem.getItemId() == R.id.home) {
                Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
                startActivity(searchIntent);
                return true;
            }
        }
        switch (menuItem.getItemId()) {
            case R.id.search:
                DataCache.getDataCache().reloadMapCopies();
                Intent searchIntent = new Intent(getActivity(),SearchActivity.class);
                startActivity(searchIntent);
                return true;
            case R.id.settings:
                Intent settingsIntent = new Intent(getActivity(),SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        myPolyLines = new ArrayList<Polyline>();
        map = googleMap;

        FragmentActivity fragmentActivity = this.getActivity();
        assert fragmentActivity != null;
        String fragmentName = fragmentActivity.getClass().getName();

        if (fragmentName.contains("EventActivity")) {
            EventModel markerEvent = DataCache.getDataCache().getCurrentEvent();
            if (DataCache.getDataCache().isSpouseSwitch()) { drawSpouseLine(markerEvent); }
            if (DataCache.getDataCache().isFamilyTreeSwitch()) { drawGenerationLines(markerEvent); }
            if (DataCache.getDataCache().isLifeStorySwitch()) { drawLifeLines(markerEvent); }
            CameraUpdate cameraUpdate = CameraUpdateFactory.
                    newLatLng(new LatLng(markerEvent.getLatitude(),markerEvent.getLongitude()));
            map.animateCamera(cameraUpdate);
            String gender = Objects.requireNonNull(DataCache.getDataCache().getPeopleMapCopy()
                    .get(markerEvent.getPersonID())).getGender();
            Drawable genderIcon;
            if (gender.equals("f")) {
                genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                        colorRes(R.color.purple_200).sizeDp(30);
            }
            else {
                genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                        colorRes(R.color.black).sizeDp(30);
            }
            ImageView genderImageView = getView().findViewById(R.id.imageView3);
            genderImageView.setImageDrawable(genderIcon);
        }

        map.setOnMapLoadedCallback(this);
        DataCache.getDataCache().setCurrentMap(map);

        DataCache.getDataCache().reloadMapCopies();
        Collection<EventModel> events = DataCache.getDataCache().getEventMapCopy().values();
        for (EventModel e : events) {
            LatLng event = new LatLng(e.getLatitude(),e.getLongitude());
            Float eventColor = DataCache.getDataCache().getEventColors().get(e.getEventType());
            Objects.requireNonNull(map.addMarker(new MarkerOptions().position(event)
                    .icon(BitmapDescriptorFactory.defaultMarker(eventColor)))).setTag(e);
        }

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                if (myPolyLines != null && myPolyLines.size() > 0) {
                    for (Polyline p : myPolyLines) {
                        p.remove();
                    }
                }
                EventModel markerEvent = (EventModel) marker.getTag();
                DataCache.getDataCache().setCurrentEvent(markerEvent);
                TextView eventText = getView().findViewById(R.id.mapTextView);
                if (DataCache.getDataCache().isSpouseSwitch()) {drawSpouseLine(markerEvent);}
                if (DataCache.getDataCache().isFamilyTreeSwitch()) {drawGenerationLines(markerEvent);}
                if (DataCache.getDataCache().isLifeStorySwitch()) {drawLifeLines(markerEvent);}
                moveCamera(markerEvent);
                eventText.setText(getEventString(markerEvent));
                eventText.setTextSize(15);
                setGenderImage(markerEvent);
                return true;
            }
        });
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onResume() {
        super.onResume();
        if (map != null) {
            map.clear();
            DataCache.getDataCache().reloadMapCopies();
            TextView eventText = requireView().findViewById(R.id.mapTextView);
            eventText.setText("Click on marker to see event details");
            ImageView imageView = getView().findViewById(R.id.imageView3);
            Drawable drawable = new IconDrawable(getActivity(),FontAwesomeIcons.fa_android)
                    .color(Color.GREEN).sizeDp(30);
            imageView.setImageDrawable(drawable);
            onMapReady(map);
        }
    }

    @Override
    public void onMapLoaded() { }

    public void switchToPersonActivity() {
        Intent intent = new Intent(getActivity(),PersonActivity.class);
        startActivity(intent);
    }
    public void drawLifeLines (EventModel markerEvent) {
        if (markerEvent != null) {
            String personID = markerEvent.getPersonID();
            List<EventModel> personEvents = DataCache.getDataCache().getPeopleEventsCopy()
                    .get(personID);
            if (personEvents != null && personEvents.size() != 0) {
                List<EventModel> personEventsCopy = new ArrayList<EventModel>(personEvents);
                List<EventModel> personEventsOrdered = new ArrayList<EventModel>();
                while (personEventsCopy.size() != 0) {
                    addEarliestEventList(personEventsCopy, personEventsOrdered);
                }
                if (personEventsOrdered.size() > 1) {
                    for (int i = 0; i < (personEventsOrdered.size() - 1); ++i) {
                        createPolyLine(personEventsOrdered.get(i), personEventsOrdered
                                .get(i + 1),Color.BLACK,5.0f);
                    }
                }
            }
        }
    }
    public void addEarliestEventList (List<EventModel> events, List<EventModel> orderedEvents) {
        int earliestEventYear = 10000;
        EventModel earliestEvent = null;
        for (EventModel e : events) {
           if (e.getYear() < earliestEventYear) {
               earliestEventYear = e.getYear();
               earliestEvent = e;
           }
        }
        events.remove(earliestEvent);
        orderedEvents.add(earliestEvent);
    }
    public void createPolyLine(EventModel event1, EventModel event2, int color, float width) {
        Polyline polyline = map.addPolyline(new PolylineOptions()
                .clickable(false)
                .add(
                        new LatLng(event1.getLatitude(),event1.getLongitude()),
                        new LatLng(event2.getLatitude(), event2.getLongitude()))
                .color(color)
                .width(width));
        myPolyLines.add(polyline);
    }
    public void drawSpouseLine (EventModel markerEvent) {
        if (getEarliestSpouseEvent(markerEvent) != null) {
            createPolyLine(markerEvent, getEarliestSpouseEvent(markerEvent), Color.RED, 20.0f);
        }
    }
    public void drawParentLines (EventModel markerEvent, float width) {
        if (markerEvent != null) {
            if (getEarliestDadEvent(markerEvent) != null) {
                createPolyLine(markerEvent, getEarliestDadEvent(markerEvent), Color.BLUE, width);
            }
            if (getEarliestMomEvent(markerEvent) != null) {
                createPolyLine(markerEvent, getEarliestMomEvent(markerEvent), Color.BLUE, width);
            }
        }
    }
    public void drawGenerationLines (EventModel markerEvent) {
        drawGenerationLines_Helper(markerEvent, 1);
    }
    public void drawGenerationLines_Helper (EventModel childEvent, int generationNum) {
        if (childEvent != null) {
            float width = 0;
            if (generationNum == 1) { width = 40.0f; }
            if (generationNum == 2) { width = 15.0f; }
            if (generationNum == 3) { width = 3.0f; }

            drawParentLines(childEvent, width);

            drawGenerationLines_Helper(getEarliestDadEvent(childEvent), generationNum+1);
            drawGenerationLines_Helper(getEarliestMomEvent(childEvent), generationNum+1);
        }
    }
    public void moveCamera(EventModel eventMarker) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.
                newLatLng(new LatLng(eventMarker.getLatitude(),eventMarker.getLongitude()));
        map.animateCamera(cameraUpdate);
    }
    public String getEventString (EventModel markerEvent) {
        String personFirstName = Objects.requireNonNull(DataCache.getDataCache().getPeopleMapCopy().
                get(markerEvent.getPersonID())).getFirstName();
        String personLastName = Objects.requireNonNull(DataCache.getDataCache().getPeopleMapCopy().
                get(markerEvent.getPersonID())).getLastName();
        return personFirstName + " " + personLastName + "\n"
                + markerEvent.getEventType().toUpperCase() + ": " + markerEvent.getCity()
                + ", " + markerEvent.getCountry() + " (" + markerEvent.getYear() + ")";
    }
    public void setGenderImage(EventModel markerEvent) {
        String gender = Objects.requireNonNull(DataCache.getDataCache().getPeopleMapCopy()
                .get(markerEvent.getPersonID())).getGender();
        Drawable genderIcon;
        if (gender.equals("f")) {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_female).
                    colorRes(R.color.purple_200).sizeDp(40);
        }
        else {
            genderIcon = new IconDrawable(getActivity(), FontAwesomeIcons.fa_male).
                    colorRes(R.color.black).sizeDp(40);
        }
        ImageView genderImageView = requireView().findViewById(R.id.imageView3);
        genderImageView.setImageDrawable(genderIcon);
    }
    public EventModel getEarliestSpouseEvent (EventModel markerEvent) {
        String spouseID = Objects.requireNonNull(DataCache.getDataCache().getPeopleMapCopy()
                .get(markerEvent.getPersonID())).getSpouseID();
        if (spouseID == null) { return null; }
        List<EventModel> spouseEvents = DataCache.getDataCache().getPeopleEventsCopy().get(spouseID);
        if (spouseEvents == null) { return null; }
        for (EventModel e : spouseEvents) {
            if (e.getEventType().equals("birth")) {
                return e;
            }
        }
        int earliestEventYear = 10000;
        EventModel earliestEvent = null;
        for (EventModel e : spouseEvents)  {
            if (e.getYear() < earliestEventYear) {
                earliestEventYear = e.getYear();
                earliestEvent = e;
            }
        }
        return earliestEvent;
    }
    public EventModel getEarliestDadEvent (EventModel markerEvent) {
        String fatherID = Objects.requireNonNull(DataCache.getDataCache().getPeopleMapCopy()
                .get(markerEvent.getPersonID())).getFatherID();
        if (fatherID == null) { return null; }
        List<EventModel> fatherEvents = DataCache.getDataCache().getPeopleEventsCopy().get(fatherID);
        if (fatherEvents == null) { return null; }
        for (EventModel e : fatherEvents) {
            if (e.getEventType().equals("birth")) {
                return e;
            }
        }
        int earliestEventYear = 10000;
        EventModel dadEarliestEvent = null;
        for (EventModel e : fatherEvents)  {
            if (e.getYear() < earliestEventYear) {
                earliestEventYear = e.getYear();
                dadEarliestEvent = e;
            }
        }
        return dadEarliestEvent;
    }
    public EventModel getEarliestMomEvent (EventModel markerEvent) {
        String motherID = Objects.requireNonNull(DataCache.getDataCache().getPeopleMapCopy()
                .get(markerEvent.getPersonID())).getMotherID();
        if (motherID == null) { return null; }
        List<EventModel> motherEvents = DataCache.getDataCache().getPeopleEventsCopy().get(motherID);
        if (motherEvents == null) { return null; }
        for (EventModel e : motherEvents) {
            if (e.getEventType().equals("birth")) {
                return e;
            }
        }
        int earliestEventYear = 10000;
        EventModel momEarliestEvent = null;
        for (EventModel e : motherEvents)  {
            if (e.getYear() < earliestEventYear) {
                earliestEventYear = e.getYear();
                momEarliestEvent = e;
            }
        }
        return momEarliestEvent;
    }

}