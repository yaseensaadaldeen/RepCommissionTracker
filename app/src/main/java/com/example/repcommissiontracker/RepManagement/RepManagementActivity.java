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

import com.example.repcommissiontracker.Invoice.InvoiceAdd;
import com.example.repcommissiontracker.MainActivity;
import com.example.repcommissiontracker.R;
import com.example.repcommissiontracker.Settings.SettingsActivity;
import com.example.repcommissiontracker.search.ui.SearchActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class RepManagementActivity extends AppCompatActivity  implements rep_add_dialog.ImageSelectionListener{
    FloatingActionButton fab,addRepButton;
    Intent intent;
    SearchView searchView ;
    Context context;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rep_management);
         bottomNavigationView = findViewById(R.id.bottomNavigationView);
        context = getBaseContext();
         searchView=findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.setIconified(false);
            }
        });
        fab = findViewById(R.id.fab);
        addRepButton = findViewById(R.id.addRepButton);
        intent = null;
        addRepButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the rep_add_dialog when the button is clicked
                listDialog = new rep_add_dialog(RepManagementActivity.this, RepManagementActivity. this);

                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(listDialog.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = 1700;
                listDialog.show();
                listDialog.getWindow().setAttributes(lp);
                listDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        });
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
    private rep_add_dialog listDialog; // Declare the dialog here
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
        if (requestCode == rep_add_dialog.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                // Pass the selected image to the dialog through the callback
                onImageSelected(bitmap);
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