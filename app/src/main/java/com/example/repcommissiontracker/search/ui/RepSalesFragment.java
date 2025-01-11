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
import com.example.repcommissiontracker.Classes.SalesRepresentative;
import com.example.repcommissiontracker.DatabaseHelper;
import com.example.repcommissiontracker.R;

import java.util.ArrayList;
import java.util.List;

public class RepSalesFragment extends Fragment {

    private Spinner spinnerRep;
    private EditText editTextMonth, editTextYear;
    private Button buttonSubmit;
    private TextView textViewResult;

    private DatabaseHelper dbHelper;

    public RepSalesFragment() {
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

        List<SalesRepresentative> salesReps = dbHelper.getAllSalesReps();
        List<String> salesRepNames = new ArrayList<>();
        for (SalesRepresentative rep : salesReps) {
            salesRepNames.add(rep.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, salesRepNames);
        spinnerRep.setAdapter(adapter);

        // Handle button click
        buttonSubmit.setOnClickListener(v -> handleSubmit());

        return view;
    }

    private void handleSubmit() {
        if (spinnerRep.getCount() == 0) {
            textViewResult.setText("لا يوجد مندوب مبيعات متاح. يرجى إضافة مندوب مبيعات أولاً.");
            return;
        }
        String monthStr = editTextMonth.getText().toString();
        String yearStr = editTextYear.getText().toString();

        // Ensure the user entered both a valid month and year
        if (monthStr.isEmpty() || yearStr.isEmpty()) {
            textViewResult.setText("يرجى إدخال الشهر والسنة.");
            return;
        }

        int month;
        int year;
        try {
            month = Integer.parseInt(monthStr);
            year = Integer.parseInt(yearStr);
        } catch (NumberFormatException e) {
            textViewResult.setText("يرجى إدخال قيم رقمية صحيحة للشهر والسنة.");
            return;
        }

        // Validate month (should be between 1 and 12)
        if (month < 1 || month > 12) {
            textViewResult.setText("يرجى إدخال شهر صالح بين 1 و 12.");
            return;
        }

        // Validate year (should be reasonable, e.g., between 1900 and the current year)
        int currentYear = java.util.Calendar.getInstance().get(java.util.Calendar.YEAR);
        if (year < 1900 || year > currentYear) {
            textViewResult.setText("يرجى إدخال سنة صالحة بين 1900 و " + currentYear + ".");
            return;
        }



        long repId = getSelectedRepId();

        // Check if the data already exists
        if (dbHelper.isMonthlySalesExist(repId, month, year)) {
            // Ask the user if they want to edit existing data
            new AlertDialog.Builder(getContext())
                    .setTitle("البيانات موجودة بالفعل")
                    .setMessage("توجد بيانات مبيعات لهذا الشهر. هل ترغب في تعديلها؟")
                    .setPositiveButton("نعم", (dialog, which) -> {
                        // If yes, update the data
                        updateMonthlySales(repId, month, year);
                    })
                    .setNegativeButton("لا", null)
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
        sales.setMonth(month);
        sales.setSalesValue(totalSales);
        sales.setYear((year)); // Set year
        sales.setLocId(getLocationIdForRep(repId)); // Optional, if location is needed

        long id = dbHelper.addMonthlyRepSales(sales);

        // Show result
        textViewResult.setText("تم إدخال مبيعات الشهر: " + totalSales);
    }

    private void updateMonthlySales(long repId, int month, int year) {
        double totalSales = dbHelper.calculateTotalSalesForRep(repId, month, year);

        // Create MonthlyRepSales object and update
        MonthlyRepSales sales = new MonthlyRepSales();
        sales.setRepId((int) repId);
        sales.setMonth(month);
        sales.setSalesValue(totalSales);
        sales.setYear((year)); // Set year
        sales.setLocId(getLocationIdForRep(repId)); // Optional, if location is needed

        dbHelper.updateMonthlyRepSales(sales);

        // Show result
        textViewResult.setText("تم تحديث مبيعات الشهر: " + totalSales);
    }

    private long getSelectedRepId() {
        // Get the ID of the selected sales rep from the Spinner
        // Assuming you're saving the rep names and their IDs together in the spinner
        String selectedRepName = spinnerRep.getSelectedItem().toString();
        return dbHelper.getRepIdByName(selectedRepName);
    }

    private int getLocationIdForRep(long repId) {
        // This method should return the location ID based on the rep
        return dbHelper.getLocationIdForRep(repId);
    }
}
