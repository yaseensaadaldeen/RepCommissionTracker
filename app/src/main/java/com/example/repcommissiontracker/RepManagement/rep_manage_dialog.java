package com.example.repcommissiontracker.RepManagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import com.example.repcommissiontracker.Classes.Location;
import com.example.repcommissiontracker.Classes.SalesRepresentative;
import com.example.repcommissiontracker.DatabaseHelper;
import com.example.repcommissiontracker.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class rep_manage_dialog extends Dialog {
    private Context context;
    private View view;
    private FloatingActionButton btnCancel;
    private Button btnSave, btnDelete;
    private EditText edtName, edtPhone, edtStartDate;
    private TextView txtDialogTitle, txtError;
    private Spinner locationSpinner;
    public static final int PICK_IMAGE_REQUEST = 1;
    private ImageView imageViewRep;
    private Bitmap selectedImageBitmap;
    private SalesRepresentative currentRep; // For editing
    private boolean isEditMode = false; // To determine dialog mode

    // Callback interface to pass data back to the Activity
    public interface ImageSelectionListener {
        void onImageSelected(Bitmap image);
    }
    private Bitmap pendingImageBitmap; // Temporary storage for selected image

    public void setPendingImage(Bitmap image) {
        this.pendingImageBitmap = image;
        if (imageViewRep != null) {
            onImagePicked(image); // Apply the image if the dialog is initialized
        }
    }
    private ImageSelectionListener imageSelectionListener;

    public rep_manage_dialog(@NonNull Context context, ImageSelectionListener listener) {
        super(context);
        this.context = context;
        this.imageSelectionListener = listener;
    }

    public rep_manage_dialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public void setSalesRepresentative(SalesRepresentative rep) {
        this.currentRep = rep;
        this.isEditMode = true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState != null ? savedInstanceState : new Bundle());

        // Inflate the layout for the dialog
        view = LayoutInflater.from(context).inflate(R.layout.rep_manage, null);
        setContentView(view);

        // Initialize UI elements
        btnCancel = findViewById(R.id.btnCancel);
        btnSave = findViewById(R.id.btn_next); // Renamed "Add" button to a generic "Save"
        btnDelete = findViewById(R.id.btn_delete); // Add "Delete" button in the layout
        edtName = findViewById(R.id.user_name);
        edtPhone = findViewById(R.id.phone_no);
        edtStartDate = findViewById(R.id.start_date);
        txtDialogTitle = findViewById(R.id.dialog_title); // Add title TextView in the layout
        txtError = findViewById(R.id.error);
        locationSpinner = findViewById(R.id.spinner_locations);
        imageViewRep = findViewById(R.id.image_view_rep);
        Button selectImageButton = findViewById(R.id.select_image_button);

        selectImageButton.setOnClickListener(v -> openImageChooser());
        // Apply the pending image if available
        if (pendingImageBitmap != null) {
            onImagePicked(pendingImageBitmap);
            pendingImageBitmap = null; // Clear pending image after applying
        }
        // Cancel button listener
        btnCancel.setOnClickListener(v -> cancel());

        // Populate the location spinner
        setupLocationSpinner();

        // Update UI for edit mode
        if (isEditMode && currentRep != null) {
            populateFieldsForEditing();
        } else {
            resetFields();  // Reset fields and image if not in edit mode
        }

        // Save button listener
        btnSave.setOnClickListener(v -> {
            if (isEditMode) {
                updateSalesRep();
            } else {
                addSalesRep();
            }
        });

        // Delete button listener
        if (btnDelete != null) {
            btnDelete.setOnClickListener(v -> {
                if (isEditMode && currentRep != null) {
                    confirmAndDeleteSalesRep();
                }
            });
        } else {
            btnDelete.setVisibility(View.GONE);
        }

    }
    private RepManagementActivity.ImageUpdateListener imageUpdateListener;

    public void setImageUpdateListener(RepManagementActivity.ImageUpdateListener listener) {
        this.imageUpdateListener = listener;
    }


    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        if (context instanceof Activity) {
            ((Activity) context).startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
    }

    public void onImagePicked(Bitmap image) {
        if (image != null) {
            this.selectedImageBitmap = image;
            if (imageViewRep != null)
                imageViewRep.setImageBitmap(image);

            // Update the image in the SalesRepresentative object if editing
            if (isEditMode && currentRep != null) {
                currentRep.setImageBitmap(image);
            }
        }
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

    private void populateFieldsForEditing() {
        edtName.setText(currentRep.getName());
        edtPhone.setText(currentRep.getPhoneNumber());
        edtStartDate.setText(currentRep.getStartDate());

        // Set image if available
        if (currentRep.getImageBitmap() != null) {
            imageViewRep.setImageBitmap(currentRep.getImageBitmap());
            this.selectedImageBitmap = currentRep.getImageBitmap();
        }

        // Set spinner selection based on location
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        List<Location> locations = dbHelper.getAllLocations();
        for (int i = 0; i < locations.size(); i++) {
            if (locations.get(i).getLocId() == currentRep.getSupervisedLocId()) {
                locationSpinner.setSelection(i);
                break;
            }
        }

        // Update dialog title and button text for editing
        txtDialogTitle.setText("تعديل المندوب");
        btnSave.setText("تعديل");
        btnDelete.setVisibility(View.VISIBLE); // Make the "Delete" button visible
    }

    public void resetFields() {
        if (edtName != null) {
            edtName.setText("");
            edtPhone.setText("");
            edtStartDate.setText("");
            locationSpinner.setSelection(0); // Reset to the first option in the spinner
            imageViewRep.setImageResource(android.R.drawable.ic_menu_gallery); // Set a default placeholder image
            selectedImageBitmap = null; // Clear the image
            currentRep = null; // Clear the current representative object
            isEditMode = false; // Switch to add mode
            txtDialogTitle.setText("إضافة مندوب"); // Update dialog title for adding
            btnSave.setText("إضافة"); // Update button text for adding
            btnDelete.setVisibility(View.GONE); // Hide delete button in add mode
        }
    }


    private void addSalesRep() {
        handleSalesRepOperation(false);
    }

    private void updateSalesRep() {
        handleSalesRepOperation(true);
    }

    private void handleSalesRepOperation(boolean isUpdate) {
        // Clear previous error messages
        txtError.setVisibility(View.GONE);

        // Get input values
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String startDate = edtStartDate.getText().toString().trim();
        String selectedLocation = (String) locationSpinner.getSelectedItem();

        // Validate inputs
        boolean isValid = validateInputs(name, phone, startDate, selectedLocation);

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

        if (isUpdate) {
            // Update existing SalesRepresentative
            currentRep.setName(name);
            currentRep.setPhoneNumber(phone);
            currentRep.setStartDate(startDate);
            currentRep.setSupervisedLocId(locationId);

            if (selectedImageBitmap != null) {
                currentRep.setImageBitmap(selectedImageBitmap);
            }

            int success = dbHelper.updateSalesRep(currentRep);
            if (success > 0) dismiss();
            else showError("Failed to update the representative.");
        } else {
            // Create new SalesRepresentative
            SalesRepresentative rep = new SalesRepresentative();
            rep.setName(name);
            rep.setPhoneNumber(phone);
            rep.setStartDate(startDate);
            rep.setSupervisedLocId(locationId);

            if (selectedImageBitmap != null) {
                rep.setImageBitmap(selectedImageBitmap);
            }

            long result = dbHelper.addSalesRep(rep);
            if (result != -1) dismiss();
            else showError("Failed to add the representative.");
        }
    }

    private void confirmAndDeleteSalesRep() {
        new AlertDialog.Builder(context)
                .setTitle("تأكيد الحذف")
                .setMessage("هل أنت متأكد من حذف المندوب؟")
                .setPositiveButton("نعم", (dialog, which) -> {
                    DatabaseHelper dbHelper = new DatabaseHelper(context);
                    int success = dbHelper.deleteSalesRep(currentRep.getId());
                    if (success > 0) {
                        dismiss();
                    } else {
                        showError("فشل في حذف المندوب");
                    }
                })
                .setNegativeButton("إلغاء", null)
                .show();
    }

    private boolean validateInputs(String name, String phone, String startDate, String selectedLocation) {
        if (TextUtils.isEmpty(name)) {
            showError("اسم المندوب مطلوب");
            return false;
        }
        if (TextUtils.isEmpty(phone) || phone.length() != 10 || !phone.matches("\\d+")) {
            showError("رقم الهاتف غير صالح");
            return false;
        }
        if (TextUtils.isEmpty(startDate)) {
            showError("تاريخ البدء مطلوب");
            return false;
        }
        if (selectedLocation == null || selectedLocation.isEmpty()) {
            showError("يرجى اختيار الموقع");
            return false;
        }
        if (selectedImageBitmap == null) {
            showError("يرجى تحديد صورة المندوب");
            return false;
        }
        return true;
    }

    private void showError(String message) {
        txtError.setText(message);
        txtError.setVisibility(View.VISIBLE);
    }
}
