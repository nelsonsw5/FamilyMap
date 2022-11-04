package com.example.familymapclient.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.model.RelationshipModel;
import com.example.familymapclient.net.DataCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;
import java.util.Objects;

import Models.EventModel;
import Models.PersonModel;

public class PersonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);
        setTitle("Family Map: Person Details");
        setUpBackButton();
        setFirstNameText();
        setLastNameText();
        setGenderText();
        List<EventModel> orderedEvents = DataCache.getDataCache().getOrderedEvents();
        List<RelationshipModel> familyMembers = DataCache.getDataCache().getFamilyMembers();
        ExpandableListView expandableListView = findViewById(R.id.expandableListView);
        expandableListView.setAdapter(new PersonExpandableListAdapter(orderedEvents,familyMembers));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(PersonActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setUpBackButton() {
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    public void setFirstNameText() {
        TextView firstName = findViewById(R.id.firstNameDisplayed);
        firstName.setText(Objects.requireNonNull(DataCache.getDataCache().getPeopleMap()
                .get(DataCache.getDataCache()
                .getCurrentPerson().getPersonID())).getFirstName());
    }
    public void setLastNameText() {
        TextView lastName = findViewById(R.id.lastNameDisplayed);
        lastName.setText(Objects.requireNonNull(DataCache.getDataCache().getPeopleMap()
                .get(DataCache.getDataCache()
                .getCurrentPerson().getPersonID())).getLastName());
    }
    @SuppressLint("SetTextI18n")
    public void setGenderText() {
        TextView gender = findViewById(R.id.genderDisplayed);
        String genderToSet = Objects.requireNonNull(DataCache.getDataCache().getPeopleMap()
                .get(DataCache.getDataCache().
                getCurrentPerson().getPersonID())).getGender();
        if (genderToSet.equals("f")) { gender.setText("Female"); }
        if (genderToSet.equals("m")) { gender.setText("Male"); }
    }

    public void setGenderImage(String gender, View view) {
        Drawable genderIcon;
        if (gender.equals("f")) {
            genderIcon = new IconDrawable(this, FontAwesomeIcons.fa_female).
                    colorRes(R.color.purple_200).sizeDp(5);
        }
        else {
            genderIcon = new IconDrawable(this, FontAwesomeIcons.fa_male).
                    colorRes(R.color.black).sizeDp(5);
        }
        ImageView imageView = view.findViewById(R.id.genderImage);
        imageView.setImageDrawable(genderIcon);
    }
    public void switchToPersonActivity() {
        Intent intent = new Intent(this,PersonActivity.class);
        startActivity(intent);
    }
    public void switchToEventActivity() {
        Intent intent = new Intent(this,EventActivity.class);
        startActivity(intent);
    }

    private class PersonExpandableListAdapter extends BaseExpandableListAdapter {
        private static final int EVENT_GROUP_POSITION = 0;
        private static final int PEOPLE_GROUP_POSITION = 1;
        private final List<EventModel> orderedEvents;
        private final List<RelationshipModel> familyMembers;
        public PersonExpandableListAdapter(List<EventModel> orderedEvents,
                                           List<RelationshipModel> familyMembers) {
            this.orderedEvents = orderedEvents;
            this.familyMembers = familyMembers;
        }
        @Override
        public int getGroupCount() { return 2; }
        @Override
        public int getChildrenCount(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return orderedEvents.size();
                case PEOPLE_GROUP_POSITION:
                    return familyMembers.size();
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }
        @Override
        public Object getGroup(int groupPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return "EVENTS";
                case PEOPLE_GROUP_POSITION:
                    return "PEOPLE";
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }
        @Override
        public Object getChild(int groupPosition, int childPosition) {
            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    return orderedEvents.get(childPosition);
                case PEOPLE_GROUP_POSITION:
                    return familyMembers.get(childPosition);
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
        }
        @Override
        public long getGroupId(int groupPosition) { return groupPosition; }
        @Override
        public long getChildId(int groupPosition, int childPosition) { return childPosition; }
        @Override
        public boolean hasStableIds() { return false; }
        @SuppressLint("SetTextI18n")
        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
                                 ViewGroup parent) {
            if(convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item_group, parent,
                        false);
            }

            TextView titleView = convertView.findViewById(R.id.listTitle);

            switch (groupPosition) {
                case EVENT_GROUP_POSITION:
                    titleView.setText("EVENTS");
                    break;
                case PEOPLE_GROUP_POSITION:
                    titleView.setText("PEOPLE");
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }

            return convertView;
        }
        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            View itemView;
            switch(groupPosition) {
                case EVENT_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.event_item, parent, false);
                    initializeEventView(itemView, childPosition);
                    break;
                case PEOPLE_GROUP_POSITION:
                    itemView = getLayoutInflater().inflate(R.layout.person_item, parent, false);
                    initializePeopleView(itemView, childPosition);
                    break;
                default:
                    throw new IllegalArgumentException("Unrecognized group position: " + groupPosition);
            }
            return itemView;
        }
        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) { return true; }

        private void initializeEventView(View eventsView, final int childPosition) {
            TextView eventDetailsView = eventsView.findViewById(R.id.eventDetailsText);
            EventModel e = orderedEvents.get(childPosition);
            String firstName = Objects.requireNonNull(DataCache.getDataCache().
                    getPeopleMap().get(e.getPersonID())).getFirstName();
            String lastName = Objects.requireNonNull(DataCache.getDataCache().
                    getPeopleMap().get(e.getPersonID())).getLastName();
            String eventDetails = e.getEventType().toUpperCase() + ": " + e.getCity() + ", "
                    + e.getCountry() + " (" + e.getYear() + ")\n" + firstName + " " + lastName;

            eventDetailsView.setText(eventDetails);
            eventDetailsView.setTag(e);
            eventsView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EventModel event = (EventModel) eventDetailsView.getTag();
                    DataCache.getDataCache().setCurrentEvent(event);
                    switchToEventActivity();
                }
            });
        }
        private void initializePeopleView(View peopleView, final int childPosition) {
            TextView personDetailsView = peopleView.findViewById(R.id.personDetailsText);
            RelationshipModel relation = familyMembers.get(childPosition);
            if (relation.getPerson() != null) {
                String personDetails = relation.getPerson().getFirstName() + " " +
                        relation.getPerson().getLastName() + "\n" + relation.getRelationship();
                personDetailsView.setText(personDetails);
                personDetailsView.setTag(relation.getPerson());
                String genderToPass = relation.getPerson().getGender();
                setGenderImage(genderToPass,peopleView);
            }
            peopleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonModel clickedPerson = (PersonModel) personDetailsView.getTag();
                    DataCache.getDataCache().setCurrentPerson(clickedPerson);
                    switchToPersonActivity();
                }
            });
        }
    }
}

