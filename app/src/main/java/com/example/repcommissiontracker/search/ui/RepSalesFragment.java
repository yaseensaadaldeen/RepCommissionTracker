package com.example.repcommissiontracker.search.ui;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.repcommissiontracker.Classes.MonthlyRepSales;
import com.example.repcommissiontracker.DatabaseHelper;
import com.example.repcommissiontracker.R;

public class RepSalesFragment extends Fragment {

    public class SalesFragment extends Fragment {
        private Spinner spinnerRep;
        private EditText editTextMonth, editTextYear;
        private Button buttonSubmit;
        private TextView textViewResult;

        private DatabaseHelper dbHelper;

        public SalesFragment() {
            // Required empty public constructor
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_rep_sales, container, false);

            // Initialize UI components
            spinnerRep = view.findViewById(R.id.spinnerRep);
            editTextMonth = view.findViewById(R.id.editTextMonth);
            editTextYear = view.findViewById(R.id.editTextYear);
            buttonSubmit = view.findViewById(R.id.buttonSubmit);
            textViewResult = view.findViewById(R.id.textViewResult);

            dbHelper = new DatabaseHelper(getContext());

            // Set up Spinner for selecting Sales Rep (This can be dynamically populated)
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                    android.R.layout.simple_spinner_item, getRepNames());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerRep.setAdapter(adapter);

            // Handle button click
            buttonSubmit.setOnClickListener(v -> handleSubmit());

            return view;
        }

        private void handleSubmit() {
            String monthStr = editTextMonth.getText().toString();
            String yearStr = editTextYear.getText().toString();

            // Ensure the user entered both a valid month and year
            if (monthStr.isEmpty() || yearStr.isEmpty()) {
                textViewResult.setText("Please enter both Month and Year.");
                return;
            }

            int month = Integer.parseInt(monthStr);
            int year = Integer.parseInt(yearStr);
            long repId = getSelectedRepId();

            // Check if the data already exists
            if (dbHelper.isMonthlySalesExist(repId, month, year)) {
                // Ask the user if they want to edit existing data
                new AlertDialog.Builder(getContext())
                        .setTitle("Data Already Exists")
                        .setMessage("Sales data for this month already exists. Do you want to edit it?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            // If yes, update the data
                            updateMonthlySales(repId, month, year);
                        })
                        .setNegativeButton("No", null)
                        .show();
            } else {
                // If no data exists, insert new data
                insertMonthlySales(repId, month, year);
            }
        }

        private void insertMonthlySales(long repId, int month, int year) {
            double totalSales = dbHelper.calculateTotalSalesForRep(repId, month, year);

            // Create MonthlyRepSales object and insert
            MonthlyRepSales sales = new MonthlyRepSales();
            sales.setRepId((int) repId);
            sales.setMonth(String.valueOf(month));
            sales.setSalesValue(totalSales);
            sales.setYear((year)); // Set year
            sales.setLocId(getLocationIdForRep(repId)); // Optional, if location is needed

            long id = dbHelper.addMonthlyRepSales(sales);

            // Show result
            textViewResult.setText("Inserted Monthly Sales: " + totalSales);
        }

        private void updateMonthlySales(long repId, int month, int year) {
            double totalSales = dbHelper.calculateTotalSalesForRep(repId, month, year);

            // Create MonthlyRepSales object and update
            MonthlyRepSales sales = new MonthlyRepSales();
            sales.setRepId((int) repId);
            sales.setMonth(String.valueOf(month));
            sales.setSalesValue(totalSales);
            sales.setYear((year)); // Set year
            sales.setLocId(getLocationIdForRep(repId)); // Optional, if location is needed

            dbHelper.updateMonthlyRepSales(sales);

            // Show result
            textViewResult.setText("Updated Monthly Sales: " + totalSales);
        }

        private long getSelectedRepId() {
            // Get the ID of the selected sales rep from the Spinner
            // Assuming you're saving the rep names and their IDs together in the spinner
            String selectedRepName = spinnerRep.getSelectedItem().toString();
            return dbHelper.getRepIdByName(selectedRepName);
        }

        private String[] getRepNames() {
            // This method should return an array of sales representative names
            return dbHelper.getAllRepNames();
        }

        private int getLocationIdForRep(long repId) {
            // This method should return the location ID based on the rep
            return dbHelper.getLocationIdForRep(repId);
        }
    }
}