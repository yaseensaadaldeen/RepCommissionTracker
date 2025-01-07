package com.example.repcommissiontracker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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

    @Override
    public void onBackPressed() {
        // Show a confirmation dialog instead of closing the app
        super.onBackPressed();
        new AlertDialog.Builder(this)
                .setTitle("Exit App")
                .setMessage("Are you sure you want to exit the app?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Exit the app
                    finishAffinity(); // This closes all activities in the stack
                })
                .setNegativeButton("No", (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                })
                .setCancelable(true)
                .show();
    }

}