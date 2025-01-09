package com.example.repcommissiontracker.RepManagement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.repcommissiontracker.Adapters.RepAdapter;
import com.example.repcommissiontracker.Classes.SalesRepresentative;
import com.example.repcommissiontracker.DatabaseHelper;
import com.example.repcommissiontracker.Invoice.InvoiceAdd;
import com.example.repcommissiontracker.MainActivity;
import com.example.repcommissiontracker.R;
import com.example.repcommissiontracker.Settings.SettingsActivity;
import com.example.repcommissiontracker.search.ui.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.List;

public class RepManagementActivity extends AppCompatActivity implements rep_manage_dialog.ImageSelectionListener {
    FloatingActionButton fab, addRepButton;
    Intent intent;
    SearchView searchView;
    Context context;
    BottomNavigationView bottomNavigationView;
    private rep_manage_dialog listDialog; // Declare the dialog here

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_management);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        context = getBaseContext();
        searchView = findViewById(R.id.searchView);
        RecyclerView recyclerView = findViewById(R.id.repRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        fab = findViewById(R.id.fab);
        addRepButton = findViewById(R.id.addRepButton);
        intent = null;
        // Initialize listDialog here
        listDialog = new rep_manage_dialog(RepManagementActivity.this, RepManagementActivity.this);
        setupSearchView();
        setupFab();
        setupToolbar();
        setupBottomNavigation();
        setupAddRepButton(recyclerView);
        loadReps(recyclerView);
    }
    private void setupSearchView() {
        searchView.setOnClickListener(v -> searchView.setIconified(false));
    }
    private void setupFab() {
        fab.setOnClickListener(v -> {
            intent = new Intent(this, InvoiceAdd.class);
            navigateToActivity(intent);
        });
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
    private void setupBottomNavigation() {
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
                navigateToActivity(intent);
            }
            return true; // Return true to indicate item selection
        });
    }
    private void setupAddRepButton(RecyclerView recyclerView) {
        listDialog = new rep_manage_dialog(RepManagementActivity.this, RepManagementActivity.this);
        addRepButton.setOnClickListener(v -> {
            // Open the rep_add_dialog when the button is clicked
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(listDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height =  WindowManager.LayoutParams.WRAP_CONTENT;;
            listDialog.show();
            listDialog.getWindow().setAttributes(lp);
            listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            listDialog.setOnDismissListener(s -> {
                DatabaseHelper dbHelper2 = new DatabaseHelper(RepManagementActivity.this);
                List<SalesRepresentative> repList2 = dbHelper2.getAllSalesReps();
                RepAdapter adapter = (RepAdapter) recyclerView.getAdapter();
                if (adapter != null) {
                    adapter.updateData(repList2);
                }
            });
        });
    }
    private void loadReps(RecyclerView recyclerView) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<SalesRepresentative> repList = dbHelper.getAllSalesReps();
        RepAdapter adapter = new RepAdapter(this, repList);
        adapter.setOnRepClickListener(rep -> {
            // Open the dialog and pass the clicked SalesRepresentative object
            rep_manage_dialog dialog = new rep_manage_dialog(this, this);
            dialog.setSalesRepresentative(rep); // Ensure your dialog has a method to accept the SalesRepresentative
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(dialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.show();
            dialog.getWindow().setAttributes(lp);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setOnDismissListener(v -> {
                DatabaseHelper dbHelper2 = new DatabaseHelper(this);
                List<SalesRepresentative> repList2 = dbHelper2.getAllSalesReps();
                adapter.updateData(repList2);
            });
        });
        recyclerView.setAdapter(adapter);
    }
    private void navigateToActivity(Intent intent) {
        bottomNavigationView.setOnNavigationItemSelectedListener(null);
        bottomNavigationView.setSelectedItemId(R.id.placeholder);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        overridePendingTransition(0, 0); // No animation between activities
    }
    @Override
    public void onImageSelected(Bitmap image) {
        // Call onImagePicked directly in the dialog
        if (listDialog != null) {
            listDialog.onImagePicked(image);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == rep_manage_dialog.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                if (listDialog != null) {
                    listDialog.onImagePicked(bitmap);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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