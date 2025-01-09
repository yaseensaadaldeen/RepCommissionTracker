package com.example.repcommissiontracker.RepManagement;

import static android.app.Activity.RESULT_OK;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.repcommissiontracker.Classes.Location;
import com.example.repcommissiontracker.Classes.SalesRepresentative;
import com.example.repcommissiontracker.DatabaseHelper;
import com.example.repcommissiontracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class rep_add_dialog extends Dialog {
    private Context context;
    private View view;
    private FloatingActionButton btnCancel;
    private Button btnAdd;
    private EditText edtName, edtPhone, edtStartDate;
    private TextView addError;
    private Spinner locationSpinner;
    public static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewRep;
    private Bitmap selectedImageBitmap;

    // Callback interface to pass data back to the Activity
    public interface ImageSelectionListener {
        void onImageSelected(Bitmap image);
    }

    private ImageSelectionListener imageSelectionListener;

    public rep_add_dialog(@NonNull Context context, ImageSelectionListener listener) {
        super(context);
        this.context = context;
        this.imageSelectionListener = listener;
    }
    public rep_add_dialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState != null ? savedInstanceState : new Bundle());

        // Inflate the layout for the dialog
        view = LayoutInflater.from(context).inflate(R.layout.rep_add, null);
        setContentView(view);

        // Initialize UI elements
        btnCancel = findViewById(R.id.btnCancel);
        btnAdd = findViewById(R.id.btn_next);
        edtName = findViewById(R.id.user_name);
        edtPhone = findViewById(R.id.phone_no);
        edtStartDate = findViewById(R.id.start_date);
        addError = findViewById(R.id.error);
        locationSpinner = findViewById(R.id.spinner_locations);
        imageViewRep = findViewById(R.id.image_view_rep);
        Button selectImageButton = findViewById(R.id.select_image_button);

        selectImageButton.setOnClickListener(v -> openImageChooser());

        // Cancel button listener
        btnCancel.setOnClickListener(v -> cancel());

        // Populate the location spinner
        setupLocationSpinner();

        // Add button listener
        btnAdd.setOnClickListener(v -> addSalesRep());
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    public void onImagePicked(Bitmap image) {
        this.selectedImageBitmap = image;
        imageViewRep.setImageBitmap(image);
    }

    private void setupLocationSpinner() {
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        List<Location> locations = dbHelper.getAllLocations();
        List<String> locationNames = new ArrayList<>();
        for (Location location : locations) {
            locationNames.add(location.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, locationNames);
        locationSpinner.setAdapter(adapter);
    }

    private void addSalesRep() {
        // Clear previous error messages
        addError.setVisibility(View.GONE);

        // Get input values
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String startDate = edtStartDate.getText().toString().trim();
        String selectedLocation = (String) locationSpinner.getSelectedItem();

        // Validate inputs
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            addError.setText("اسم المندوب مطلوب");
            addError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (TextUtils.isEmpty(phone) || phone.length() != 10 || !phone.matches("\\d+")) {
            addError.setText("رقم الهاتف غير صالح");
            addError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (TextUtils.isEmpty(startDate)) {
            addError.setText("تاريخ البدء مطلوب");
            addError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (selectedLocation == null || selectedLocation.isEmpty()) {
            addError.setText("يرجى اختيار الموقع");
            addError.setVisibility(View.VISIBLE);
            isValid = false;
        }

        if (!isValid) {
            return;
        }

        // Retrieve location ID from the database
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        List<Location> locations = dbHelper.getAllLocations();
        int locationId = -1;
        for (Location location : locations) {
            if (location.getName().equals(selectedLocation)) {
                locationId = location.getLocId();
                break;
            }
        }

        // Create SalesRepresentative object
        SalesRepresentative rep = new SalesRepresentative();
        rep.setName(name);
        rep.setPhoneNumber(phone);
        rep.setStartDate(startDate);
        rep.setSupervisedLocId(locationId);

        // Store image if available
        if (selectedImageBitmap != null) {
            rep.setImagePath(selectedImageBitmap);
        }

        // Add to database
        long result = dbHelper.addSalesRep(rep);
        if (result != -1) {
            dismiss();
        } else {
            // Show error if insertion fails
            addError.setText("فشل في إضافة المندوب. حاول مرة أخرى.");
            addError.setVisibility(View.VISIBLE);
        }
    }

    // Method to save image to storage
    private String saveImageToStorage(Bitmap bitmap) {
        File file = new File(context.getFilesDir(), "rep_image_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getAbsolutePath();  // return the file path
    }
}
