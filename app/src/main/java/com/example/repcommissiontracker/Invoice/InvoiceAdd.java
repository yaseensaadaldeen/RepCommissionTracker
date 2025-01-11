package com.example.repcommissiontracker.Invoice;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.repcommissiontracker.Classes.Invoice;
import com.example.repcommissiontracker.Classes.Location;
import com.example.repcommissiontracker.Classes.SalesRepresentative;
import com.example.repcommissiontracker.DatabaseHelper;
import com.example.repcommissiontracker.MainActivity;
import com.example.repcommissiontracker.R;
import com.example.repcommissiontracker.RepManagement.RepManagementActivity;
import com.example.repcommissiontracker.Settings.SettingsActivity;
import com.example.repcommissiontracker.search.ui.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InvoiceAdd extends AppCompatActivity {

    private EditText edtInvNo, edtDate, edtTotalPrice;
    private Spinner spnSalesRep, spnLocation;
    private Button btnSave, btnCancel;
    BottomNavigationView bottomNavigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_add);

        // Initialize UI elements
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        edtDate = findViewById(R.id.edt_date);
        edtTotalPrice = findViewById(R.id.edt_total_price);
        spnSalesRep = findViewById(R.id.spn_sales_rep);
        spnLocation = findViewById(R.id.spn_location);
        btnSave = findViewById(R.id.btn_save);
        btnCancel = findViewById(R.id.btn_cancel);
// Get the current date as a string in the format YYYY-MM-DD
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        setupBottomNavigation();
        edtDate.setText(currentDate);

        // Populate Spinners
        setupSalesRepSpinner();
        setupLocationSpinner();
        setupToolbar();
        // Button Listeners
        btnSave.setOnClickListener(v -> saveInvoice());
        btnCancel.setOnClickListener(v -> finish());
    }

    private void setupSalesRepSpinner() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        List<SalesRepresentative> salesReps = dbHelper.getAllSalesReps();
        List<String> salesRepNames = new ArrayList<>();
        for (SalesRepresentative rep : salesReps) {
            salesRepNames.add(rep.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, salesRepNames);
        spnSalesRep.setAdapter(adapter);
    }
    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> navigateToMainActivity());
    }
    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        navigateToActivity(intent);
    }

    private void setupLocationSpinner() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Location> locations = dbHelper.getAllLocations();
        List<String> locationNames = new ArrayList<>();
        for (Location loc : locations) {
            locationNames.add(loc.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, locationNames);
        spnLocation.setAdapter(adapter);
    }

    private void saveInvoice() {
        String date = edtDate.getText().toString().trim();
        String totalPriceStr = edtTotalPrice.getText().toString().trim();

        if (TextUtils.isEmpty(totalPriceStr)) {
            Toast.makeText(this, "Please enter the total price", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validate date format and value
        if (!isValidDate(date)) {
            Toast.makeText(this, "Please enter a valid date in the format YYYY-MM-DD.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the date is not in the future
        if (isFutureDate(date)) {
            Toast.makeText(this, "Date cannot be in the future.", Toast.LENGTH_SHORT).show();
            return;
        }


        DatabaseHelper dbHelper = new DatabaseHelper(this);

        double totalPrice = Double.parseDouble(totalPriceStr);

        // Get selected Sales Rep and Location
        String selectedSalesRep = (String) spnSalesRep.getSelectedItem();
        String selectedLocation = (String) spnLocation.getSelectedItem();

        // Retrieve IDs
        int salesRepId = dbHelper.getSalesRepIdByName(selectedSalesRep);
        int locId = dbHelper.getLocationIdByName(selectedLocation);

        // Create and save Invoice
        Invoice invoice = new Invoice(date, totalPrice, salesRepId, locId);
        long result = dbHelper.addInvoice(invoice);

        if (result != -1) {
            // Show success dialog
            new androidx.appcompat.app.AlertDialog.Builder(this)
                    .setTitle("Success")
                    .setMessage("Invoice added successfully")
                    .setPositiveButton("OK", (dialog, which) -> clearInputs())
                    .show();
        } else {
            Toast.makeText(this, "Failed to add invoice", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        sdf.setLenient(false); // Disallow invalid dates like 2023-13-32
        try {
            sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    private boolean isFutureDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        try {
            Date inputDate = sdf.parse(date);
            Date currentDate = new Date();
            return inputDate.after(currentDate);
        } catch (Exception e) {
            return false;
        }
    }

    private void clearInputs() {
        edtDate.setText(""); // Optionally, reset to the current date
        edtTotalPrice.setText("");
        spnSalesRep.setSelection(0);
        spnLocation.setSelection(0);

        // Reset date to the current date if desired
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        edtDate.setText(sdf.format(new Date()));
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.placeholder);
        bottomNavigationView.setBackground(null); // Remove background for transparency
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = null;

            switch (item.getItemId()) {
                case R.id.home:
                    intent = new Intent(this, MainActivity.class);
                    break;
                case R.id.search:
                    intent = new Intent(this, SearchActivity.class);
                    break;
                case R.id.rep_management:
                    intent = new Intent(this, RepManagementActivity.class);
                    break;
                case R.id.settings:
                    intent = new Intent(this, SettingsActivity.class);
                    break;
            }
            if (intent != null) {
                navigateToActivity(intent);
            }
            return true; // Return true to indicate item selection
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void navigateToActivity(Intent intent) {
        bottomNavigationView.setOnNavigationItemSelectedListener(null);
        bottomNavigationView.setSelectedItemId(R.id.placeholder);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0); // No animation between activities
    }
}
