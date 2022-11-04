package com.example.familymapclient.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.familymapclient.R;
import com.example.familymapclient.net.DataCache;

@SuppressLint("UseSwitchCompatOrMaterialCode")
public class SettingsActivity extends AppCompatActivity {

    Switch lifeStorySwitch;
    Switch familyTreeSwitch;
    Switch spouseSwitch;
    Switch fatherSwitch;
    Switch motherSwitch;
    Switch maleSwitch;
    Switch femaleSwitch;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Family Map: Settings");

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_24);
        actionBar.setDisplayHomeAsUpEnabled(true);

        lifeStorySwitch = findViewById(R.id.lifeStorySwitch);
        familyTreeSwitch = findViewById(R.id.familyTreeSwitch);
        spouseSwitch = findViewById(R.id.spouseSwitch);
        fatherSwitch = findViewById(R.id.fatherSwitch);
        motherSwitch = findViewById(R.id.motherSwitch);
        maleSwitch = findViewById(R.id.maleSwitch);
        femaleSwitch = findViewById(R.id.femaleSwitch);
        logoutButton = findViewById(R.id.logoutButton);

        if (DataCache.getDataCache().isLifeStorySwitch()) { lifeStorySwitch.setChecked(true); }
        if (DataCache.getDataCache().isFamilyTreeSwitch()) { familyTreeSwitch.setChecked(true); }
        if (DataCache.getDataCache().isSpouseSwitch()) { spouseSwitch.setChecked(true); }
        if (DataCache.getDataCache().isFatherSwitch()) { fatherSwitch.setChecked(true); }
        if (DataCache.getDataCache().isMotherSwitch()) { motherSwitch.setChecked(true); }
        if (DataCache.getDataCache().isMaleSwitch()) { maleSwitch.setChecked(true); }
        if (DataCache.getDataCache().isFemaleSwitch()) { femaleSwitch.setChecked(true); }

        lifeStorySwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getDataCache().setLifeStorySwitch(((Switch) v).isChecked());
            }
        });
        familyTreeSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getDataCache().setFamilyTreeSwitch(((Switch) v).isChecked());
            }
        });
        spouseSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getDataCache().setSpouseSwitch(((Switch) v).isChecked());
            }
        });
        fatherSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getDataCache().setFatherSwitch(((Switch) v).isChecked());
            }
        });
        motherSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getDataCache().setMotherSwitch(((Switch) v).isChecked());
            }
        });
        maleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getDataCache().setMaleSwitch(((Switch) v).isChecked());
            }
        });
        femaleSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DataCache.getDataCache().setFemaleSwitch(((Switch) v).isChecked());
            }
        });
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}