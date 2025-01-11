package com.example.repcommissiontracker.search.ui;

import android.app.AlertDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
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

import com.example.repcommissiontracker.Classes.MonthlyRepCommission;
import com.example.repcommissiontracker.Classes.SalesRepresentative;
import com.example.repcommissiontracker.DatabaseHelper;
import com.example.repcommissiontracker.R;

import java.util.ArrayList;
import java.util.List;

public class RepCommFragment extends Fragment {

    private Spinner spinnerRep;
    private EditText editTextMonth, editTextYear;
    private Button buttonSubmit;
    private TextView textViewResult;

    private DatabaseHelper dbHelper;
    private Ringtone ringtone;

    public RepCommFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rep_comm, container, false);

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
        if (dbHelper.isMonthlyCommExist(repId, month, year)) {
            // Ask the user if they want to edit existing data
            playNotificationSound(getContext());

            new AlertDialog.Builder(getContext())
                    .setTitle("البيانات موجودة بالفعل")
                    .setMessage("توجد بيانات عمولات لهذا الشهر. هل ترغب في تعديلها؟")
                    .setPositiveButton("نعم", (dialog, which) -> {
                        // If yes, update the data
                        updateMonthlyCommission(repId, month, year);
                        stopAlarmSound();
                    })
                    .setNegativeButton("لا", (dialog, which) -> {
                        stopAlarmSound();
                    })
                    .show();
        } else {
            // If no data exists, insert new data
            insertMonthlyCommission(repId, month, year);
        }
    }
    // Method to play the default alarm sound
    private void playNotificationSound(Context context) {
        // Get the default notification sound URI
        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Check if the URI is not null
        if (notificationUri != null) {
            // Create a Ringtone object and play it
            ringtone = RingtoneManager.getRingtone(context, notificationUri);
            ringtone.play();
        } else {
            // Fallback: If no notification sound is found, play a default sound
            // You can use a custom notification sound if necessary
            // Uri defaultSoundUri = Uri.parse("android.resource://com.example.repcommissiontracker/raw/your_custom_sound");
            ringtone = RingtoneManager.getRingtone(context, notificationUri);
            ringtone.play();
        }
    }

    private void insertMonthlyCommission(long repId, int month, int year) {
        double totalCommission = dbHelper.calculateCommission((int) repId, month, year);

        // Create MonthlyRepCommission object and insert
        MonthlyRepCommission commission = new MonthlyRepCommission();
        commission.setRepId((int) repId);
        commission.setMonth(month);
        commission.setCommissionValue(totalCommission);
        commission.setYear(year);
        commission.setLocId(getLocationIdForRep(repId)); // Optional, if location is needed

        long id = dbHelper.addMonthlyRepCommission(commission);

        // Show result
        textViewResult.setText("تم إدخال عمولات الشهر: " + totalCommission);
    }

    private void updateMonthlyCommission(long repId, int month, int year) {
        double totalCommission = dbHelper.calculateCommission((int) repId, month, year);

        // Create MonthlyRepCommission object and update
        MonthlyRepCommission commission = new MonthlyRepCommission();
        commission.setRepId((int) repId);
        commission.setMonth(month);
        commission.setCommissionValue(totalCommission);
        commission.setYear(year);
        commission.setLocId(getLocationIdForRep(repId)); // Optional, if location is needed

        dbHelper.updateMonthlyRepComm(commission);

        // Show result
        textViewResult.setText("تم تحديث عمولات الشهر: " + totalCommission);
    }

    private long getSelectedRepId() {
        // Get the ID of the selected sales rep from the Spinner
        String selectedRepName = spinnerRep.getSelectedItem().toString();
        return dbHelper.getRepIdByName(selectedRepName);
    }

    private int getLocationIdForRep(long repId) {
        // This method should return the location ID based on the rep
        return dbHelper.getLocationIdForRep(repId);
    }
    public void stopAlarmSound() {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
    }
}
