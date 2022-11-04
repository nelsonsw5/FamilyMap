package com.example.familymapclient.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.familymapclient.R;
import com.example.familymapclient.net.DataCache;
import com.joanzapata.iconify.IconDrawable;
import com.joanzapata.iconify.fonts.FontAwesomeIcons;

import java.util.List;
import java.util.Objects;

import Models.EventModel;
import Models.PersonModel;

public class SearchActivity extends AppCompatActivity {

    private static final int PEOPLE_ITEM_VIEW_TYPE = 0;
    private static final int EVENT_ITEM_VIEW_TYPE = 1;
    EditText searchTextBar;
    String searchTextBarString;
    List<PersonModel> searchedPeople;
    List<EventModel> searchedEvents;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        setTitle("Family Map: Search Activity");
        setUpMenuBar();

        searchedPeople = DataCache.getDataCache().getSearchedPeopleDataCache(searchTextBarString);
        searchedEvents =  DataCache.getDataCache().getSearchedEventsDataCache(searchTextBarString);

        searchTextBar = findViewById(R.id.SearchTextInput);
        searchTextBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchTextBarString = s.toString();
                searchedPeople = DataCache.getDataCache().getSearchedPeopleDataCache(searchTextBarString);
                searchedEvents =  DataCache.getDataCache().getSearchedEventsDataCache(searchTextBarString);
                RecyclerView recyclerView = findViewById(R.id.recycler_view_widget);
                recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));

                SearchAdapter adapter = new SearchAdapter(searchedPeople, searchedEvents);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void afterTextChanged(Editable s) { }
        });

    }
    public void switchToPersonActivity() {
        Intent intent = new Intent(this,PersonActivity.class);
        startActivity(intent);
    }
    public void switchToEventActivity() {
        Intent intent = new Intent(this,EventActivity.class);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void setUpMenuBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
    private class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder> {

        private final List<PersonModel> searchedPeople;
        private final List<EventModel> searchedEvents;
        SearchAdapter(List<PersonModel> searchedPeople, List<EventModel> searchedEvents) {
            this.searchedPeople = searchedPeople;
            this.searchedEvents = searchedEvents;
        }

        @Override
        public int getItemViewType(int position) {
            return position < searchedPeople.size() ? PEOPLE_ITEM_VIEW_TYPE : EVENT_ITEM_VIEW_TYPE;
        }

        @NonNull
        @Override
        public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view;
            if (viewType == PEOPLE_ITEM_VIEW_TYPE) {
                view = getLayoutInflater().inflate(R.layout.person_item, parent, false);
            } else {
                view = getLayoutInflater().inflate(R.layout.event_item, parent, false);
            }
            return new SearchViewHolder(view, viewType);
        }

        @Override
        public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
            if(position < searchedPeople.size()) {
                holder.bind(searchedPeople.get(position));
            } else {
                holder.bind(searchedEvents.get(position - searchedPeople.size()));
            }
        }

        @Override
        public int getItemCount() {
            return searchedPeople.size() + searchedEvents.size();
        }
    }
    private class SearchViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView personTextView;
        private TextView eventTextView;
        private ImageView personImageView;

        SearchViewHolder(View view, int viewType) {
            super(view);

            itemView.setOnClickListener(this);

            if (viewType == PEOPLE_ITEM_VIEW_TYPE) {
                personTextView = itemView.findViewById(R.id.personDetailsText);
                personImageView = itemView.findViewById(R.id.genderImage);
            }
            else {
                eventTextView = itemView.findViewById(R.id.eventDetailsText);
            }
        }

        private void bind(PersonModel searchedPerson) {
            String gender;
            Drawable genderIcon;
            if (searchedPerson.getGender().equals("f")) {
                gender = "female";
                genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_female).
                        colorRes(R.color.purple_200).sizeDp(5);
            }
            else {
                gender = "male";
                genderIcon = new IconDrawable(SearchActivity.this, FontAwesomeIcons.fa_male).
                        colorRes(R.color.black).sizeDp(5);
            }
            String personText = searchedPerson.getFirstName() + " " + searchedPerson.getLastName()
                    + "\n" + gender.toUpperCase();
            personTextView.setText(personText);
            personTextView.setTag(searchedPerson);
            personTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonModel clickedPerson = (PersonModel) personTextView.getTag();
                    DataCache.getDataCache().setCurrentPerson(clickedPerson);
                    switchToPersonActivity();
                }
            });
            personImageView.setImageDrawable(genderIcon);
        }

        private void bind(EventModel searchedEvent) {
            String eventPersonFirstName = Objects.requireNonNull(DataCache.getDataCache().getPeopleMap().
                    get(searchedEvent.getPersonID())).getFirstName();
            String eventPersonLastName = Objects.requireNonNull(DataCache.getDataCache().getPeopleMap().
                    get(searchedEvent.getPersonID())).getLastName();
            String eventText = searchedEvent.getEventType().toUpperCase() + ": " +
                    searchedEvent.getCity() + ", " + searchedEvent.getCountry() +
                    " (" + searchedEvent.getYear() + ")\n" + eventPersonFirstName +
                    " " + eventPersonLastName;
            eventTextView.setText(eventText);
            eventTextView.setTag(searchedEvent);
            eventTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataCache.getDataCache().setCurrentEvent((EventModel) eventTextView.getTag());
                    switchToEventActivity();
                }
            });
        }

        @Override
        public void onClick(View v) { }
    }
}