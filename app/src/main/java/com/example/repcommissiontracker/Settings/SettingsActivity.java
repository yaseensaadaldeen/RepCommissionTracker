package com.example.repcommissiontracker.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.repcommissiontracker.Classes.CommissionConfig;
import com.example.repcommissiontracker.Invoice.InvoiceAdd;
import com.example.repcommissiontracker.MainActivity;
import com.example.repcommissiontracker.R;
import com.example.repcommissiontracker.RepManagement.RepManagementActivity;
import com.example.repcommissiontracker.search.ui.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SettingsActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;

    private TextView supervisedLowRateText;
    private TextView supervisedHighRateText;
    private TextView nonSupervisedLowRateText;
    private TextView nonSupervisedHighRateText;
    private TextView commissionThresholdText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initializeUIComponents();
        setupToolbar();
        setupBottomNavigation();
        setCommissionValues();
    }

    private void initializeUIComponents() {
        fab = findViewById(R.id.fab);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        supervisedLowRateText = findViewById(R.id.supervisedLowRate);
        supervisedHighRateText = findViewById(R.id.supervisedHighRate);
        nonSupervisedLowRateText = findViewById(R.id.nonSupervisedLowRate);
        nonSupervisedHighRateText = findViewById(R.id.nonSupervisedHighRate);
        commissionThresholdText = findViewById(R.id.commissionThreshold);

        fab.setOnClickListener(v -> openInvoiceAddActivity());
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> navigateToMainActivity());
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setBackground(null); // Remove background for transparency
        bottomNavigationView.setSelectedItemId(R.id.settings);

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Intent intent = getNavigationIntent(item.getItemId());
            if (intent != null) {
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation between activities
            }
            return true;
        });
    }

    private Intent getNavigationIntent(int itemId) {
        Intent intent = null;
        switch (itemId) {
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
        return intent;
    }

    private void setCommissionValues() {
        supervisedLowRateText.setText(String.format("نسبة العمولة للموقع الخاضع للإشراف (منخفضة): %.2f%%", CommissionConfig.SUPERVISED_LOCATION_RATE_LOW * 100));
        supervisedHighRateText.setText(String.format("نسبة العمولة للموقع الخاضع للإشراف (عالية): %.2f%%", CommissionConfig.SUPERVISED_LOCATION_RATE_HIGH * 100));
        nonSupervisedLowRateText.setText(String.format("نسبة العمولة للموقع غير الخاضع للإشراف (منخفضة): %.2f%%", CommissionConfig.NON_SUPERVISED_LOCATION_RATE_LOW * 100));
        nonSupervisedHighRateText.setText(String.format("نسبة العمولة للموقع غير الخاضع للإشراف (عالية): %.2f%%", CommissionConfig.NON_SUPERVISED_LOCATION_RATE_HIGH * 100));
        commissionThresholdText.setText(String.format("الحد الأدنى للعمولة: %.2f", CommissionConfig.COMMISSION_THRESHOLD));
    }



    private void openInvoiceAddActivity() {
        Intent intent = new Intent(this, InvoiceAdd.class);
        bottomNavigationView.setOnNavigationItemSelectedListener(null);
        bottomNavigationView.setSelectedItemId(R.id.placeholder);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0); // No animation between activities
    }

    private void navigateToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        navigateToMainActivity();
    }
}
