package com.example.repcommissiontracker.search.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.repcommissiontracker.Invoice.InvoiceAdd;
import com.example.repcommissiontracker.MainActivity;
import com.example.repcommissiontracker.R;
import com.example.repcommissiontracker.RepManagement.RepManagementActivity;
import com.example.repcommissiontracker.Settings.SettingsActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class SearchActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private BottomNavigationView bottomNavigationView;
    private Intent intent;
    private Button btnSales, btnCommission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initializeViews();
        setupFab();
        setupBottomNavigation();
    }


    private void initializeViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        btnSales = findViewById(R.id.btnSales);
        btnCommission = findViewById(R.id.btnCommission);

        // Remove fragment transactions from buttons
        btnSales.setOnClickListener(v -> loadFragment(new RepSalesFragment(), "مبيعات المندوبين"));

        // Navigate to the RepCommFragment
        btnCommission.setOnClickListener(v -> loadFragment(new RepCommFragment(), "عمولات المندوبين"));

        setupToolbar();

        intent = null;
    }
    private void loadFragment(Fragment fragment, String title) {
        // Hide buttons when navigating to a fragment
        btnSales.setVisibility(View.GONE);
        btnCommission.setVisibility(View.GONE);

        // Update toolbar title
        Toolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setTitle(title);

        // Set back button behavior to mimic the physical back button
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // Replace the fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null); // Add to back stack for proper navigation
        transaction.commit();
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
    private void setupFab() {
        fab.setOnClickListener(v -> {
            intent = new Intent(this, InvoiceAdd.class);
            navigateToActivity(intent);
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setBackground(null);
        bottomNavigationView.setSelectedItemId(R.id.search);
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
            return true;
        });
    }

    private void navigateToActivity(Intent intent) {
        bottomNavigationView.setOnNavigationItemSelectedListener(null);
        bottomNavigationView.setSelectedItemId(R.id.placeholder);
        if (intent != null) {
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            // If a fragment is in the back stack, pop it
            getSupportFragmentManager().popBackStack();

            // Show buttons and reset toolbar for SearchActivity
            btnSales.setVisibility(View.VISIBLE);
            btnCommission.setVisibility(View.VISIBLE);

            Toolbar toolbar = findViewById(R.id.topAppBar);
            toolbar.setTitle(("Search"));
            toolbar.setNavigationIcon(null);
        } else {
            // If no fragments are in the back stack, navigate to MainActivity
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }
}