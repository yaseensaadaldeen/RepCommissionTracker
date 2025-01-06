package com.example.repcommissiontracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.repcommissiontracker.Invoice.InvoiceAdd;
import com.example.repcommissiontracker.RepManagement.RepManagementActivity;
import com.example.repcommissiontracker.Settings.SettingsActivity;
import com.example.repcommissiontracker.search.ui.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Intent intent;
    BottomNavigationView bottomNavigationView;
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        intent = null;
        fab.setOnClickListener(v ->{ intent = new Intent(this, InvoiceAdd.class) ;
            bottomNavigationView.setOnNavigationItemSelectedListener(null);
            bottomNavigationView.setSelectedItemId(R.id.placeholder);
            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation between activities
            }});


        bottomNavigationView.setBackground(null); // Remove background for transparency
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            intent = null;

            switch (item.getItemId()) {
                case R.id.home:
                    intent = new Intent(MainActivity.this, MainActivity.class);
                    break;
                case R.id.search:
                    intent = new Intent(MainActivity.this, SearchActivity.class);
                    break;
                case R.id.rep_management:
                    intent = new Intent(MainActivity.this, RepManagementActivity.class);
                    break;
                case R.id.settings:
                    intent = new Intent(MainActivity.this, SettingsActivity.class);
                    break;
            }

            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation between activities
            }

            return true; // Return true to indicate item selection
        });
        bottomNavigationView.setSelectedItemId(getLayoutResourceId());
    }

    protected int getLayoutResourceId() {
        return R.layout.activity_main;
    }
}