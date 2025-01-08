package com.example.repcommissiontracker.RepManagement;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.repcommissiontracker.Classes.Location;
import com.example.repcommissiontracker.DatabaseHelper;
import com.example.repcommissiontracker.R;

import java.util.ArrayList;
import java.util.List;

public abstract class rep_add_dialog extends Dialog {
    Context context;

    public rep_add_dialog(@NonNull Context context) {
        super(context);
    }

    public rep_add_dialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected rep_add_dialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState != null ? savedInstanceState : new Bundle());
        context = getContext();

        // Inflate the layout for the dialog
        view = LayoutInflater.from(getContext()).inflate(R.layout.rep_add, null);
        setContentView(view);  // Ensure the dialog displays the inflated layout

        // Setup the location spinner
        setupLocationSpinner();
    }

    public void setupLocationSpinner() {
        // Fetch locations from the database
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        List<Location> locations = dbHelper.getAllLocations();

        // Create a list of location names for the Spinner
        List<String> locationNames = new ArrayList<>();
        for (Location location : locations) {
            locationNames.add(location.getName());  // Assuming getName() returns the location name
        }

        // Find the Spinner in the layout
        Spinner locationSpinner = findViewById(R.id.spinner_locations);

        // Create an ArrayAdapter using the location names and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, locationNames);

        // Set the dropdown layout for the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner
        locationSpinner.setAdapter(adapter);
    }
}
