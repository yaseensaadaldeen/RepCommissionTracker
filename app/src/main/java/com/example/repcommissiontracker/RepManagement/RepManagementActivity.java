package com.example.repcommissiontracker.RepManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.widget.SearchView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.repcommissiontracker.Invoice.InvoiceAdd;
import com.example.repcommissiontracker.MainActivity;
import com.example.repcommissiontracker.R;
import com.example.repcommissiontracker.Settings.SettingsActivity;
import com.example.repcommissiontracker.search.ui.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class RepManagementActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Intent intent;
    SearchView searchView ;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_management);
         bottomNavigationView = findViewById(R.id.bottomNavigationView);
         searchView=findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
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
        Toolbar toolbar = findViewById(R.id.topAppBar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        bottomNavigationView.setBackground(null); // Remove background for transparency
        bottomNavigationView.setSelectedItemId(R.id.rep_management);

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
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(0, 0); // No animation between activities
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
    protected int getLayoutResourceId() {
        return R.layout.activity_rep_management;
    }
}