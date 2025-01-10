package com.example.repcommissiontracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.repcommissiontracker.Classes.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SalesManagement.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    private static final String TABLE_SALES_REP = "SalesRepresentative";
    private static final String TABLE_LOCATION = "Location";
    private static final String TABLE_INVOICE = "Invoice";
    private static final String TABLE_MONTHLY_SALES = "MonthlyRepSales";
    private static final String TABLE_MONTHLY_COMMISSION = "MonthlyRepCommission";

    // Column Names for SalesRepresentative Table
    private static final String SR_ID = "ID";
    private static final String SR_NAME = "Name";
    private static final String SR_IMAGE = "ImagePath";
    private static final String SR_PHONE = "PhoneNumber";
    private static final String SR_START_DATE = "StartDate";
    private static final String SR_SUPERVISED_LOC = "SupervisedLocID";

    // Column Names for Location Table
    private static final String LOC_ID = "LocID";
    private static final String LOC_NAME = "Name";
    private static final String LOC_ADDRESS = "Address";

    // Column Names for Invoice Table
    private static final String INV_NO = "InvNo";
    private static final String INV_DATE = "CreatedDate";
    private static final String INV_TOTAL_PRICE = "TotalPrice";
    private static final String INV_SALES_REP_ID = "SalesRepID";
    private static final String INV_LOC_ID = "LocID";

    // Column Names for MonthlyRepSales Table
    private static final String MRS_ID = "ID";
    private static final String MRS_MONTH = "Month";
    private static final String MRS_SALES_VALUE = "SalesValue";
    private static final String MRS_REP_ID = "RepID";
    private static final String MRS_LOC_ID = "LocID";

    // Column Names for MonthlyRepCommission Table
    private static final String MRC_ID = "ID";
    private static final String MRC_MONTH = "Month";
    private static final String MRC_COMMISSION_VALUE = "CommissionValue";
    private static final String MRC_REP_ID = "RepID";
    private static final String MRC_LOC_ID = "LocID";

    // Table Creation Statements
    private static final String CREATE_TABLE_SALES_REP = "CREATE TABLE " + TABLE_SALES_REP + "(" +
            SR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            SR_NAME + " TEXT NOT NULL, " +
            SR_IMAGE + " BLOB, " +
            SR_PHONE + " TEXT, " +
            SR_START_DATE + " TEXT, " +
            SR_SUPERVISED_LOC + " INTEGER, " +
            "FOREIGN KEY(" + SR_SUPERVISED_LOC + ") REFERENCES " + TABLE_LOCATION + "(" + LOC_ID + ")" +
            ");";

    private static final String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_LOCATION + "(" +
            LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            LOC_NAME + " TEXT NOT NULL, " +
            LOC_ADDRESS + " TEXT" +
            ");";

    private static final String CREATE_TABLE_INVOICE = "CREATE TABLE " + TABLE_INVOICE + "(" +
            INV_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            INV_DATE + " TEXT DEFAULT CURRENT_DATE, "  +
            INV_TOTAL_PRICE + " REAL, " +
            INV_SALES_REP_ID + " INTEGER, " +
            INV_LOC_ID + " INTEGER, " +
            "FOREIGN KEY(" + INV_SALES_REP_ID + ") REFERENCES " + TABLE_SALES_REP + "(" + SR_ID + "), " +
            "FOREIGN KEY(" + INV_LOC_ID + ") REFERENCES " + TABLE_LOCATION + "(" + LOC_ID + ")" +
            ");";

    private static final String CREATE_TABLE_MONTHLY_SALES = "CREATE TABLE " + TABLE_MONTHLY_SALES + "(" +
            MRS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MRS_MONTH + " TEXT, " +
            MRS_SALES_VALUE + " REAL, " +
            MRS_REP_ID + " INTEGER, " +
            MRS_LOC_ID + " INTEGER, " +
            "FOREIGN KEY(" + MRS_REP_ID + ") REFERENCES " + TABLE_SALES_REP + "(" + SR_ID + "), " +
            "FOREIGN KEY(" + MRS_LOC_ID + ") REFERENCES " + TABLE_LOCATION + "(" + LOC_ID + ")" +
            ");";

    private static final String CREATE_TABLE_MONTHLY_COMMISSION = "CREATE TABLE " + TABLE_MONTHLY_COMMISSION + "(" +
            MRC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MRC_MONTH + " TEXT, " +
            MRC_COMMISSION_VALUE + " REAL, " +
            MRC_REP_ID + " INTEGER, " +
            MRC_LOC_ID + " INTEGER, " +
            "FOREIGN KEY(" + MRC_REP_ID + ") REFERENCES " + TABLE_SALES_REP + "(" + SR_ID + "), " +
            "FOREIGN KEY(" + MRC_LOC_ID + ") REFERENCES " + TABLE_LOCATION + "(" + LOC_ID + ")" +
            ");";

    private final Context context;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOCATION);
        db.execSQL(CREATE_TABLE_SALES_REP);
        db.execSQL(CREATE_TABLE_INVOICE);
        db.execSQL(CREATE_TABLE_MONTHLY_SALES);
        db.execSQL(CREATE_TABLE_MONTHLY_COMMISSION);
        insertDefaultLocations(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONTHLY_COMMISSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONTHLY_SALES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES_REP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    private void insertDefaultLocations(SQLiteDatabase db) {
        List<Location> defaultLocations = new ArrayList<>();
        defaultLocations.add(new Location(1, "دمشق", "مركز الشركة"));
        defaultLocations.add(new Location(2, "المنطقة الساحلية", "سوريا"));
        defaultLocations.add(new Location(3, "المنطقة الشمالية", "سوريا"));
        defaultLocations.add(new Location(4, "المنطقة الجنوبية", "سوريا"));
        defaultLocations.add(new Location(5, "المنطقة الشرقية", "سوريا"));
        defaultLocations.add(new Location(6, "لبنان", ""));

        for (Location loc : defaultLocations) {
            ContentValues values = new ContentValues();
            values.put(LOC_NAME, loc.getName());
            values.put(LOC_ADDRESS, loc.getAddress());
            db.insert(TABLE_LOCATION, null, values);
        }
    }

    // Utility methods for CRUD operations
    public long addSalesRep(SalesRepresentative rep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SR_NAME, rep.getName());

        // Convert the Bitmap image to byte[] and store it
        if (rep.getImageBitmap() != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            rep.getImageBitmap().compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] imageByteArray = byteArrayOutputStream.toByteArray();
            values.put(SR_IMAGE, imageByteArray);
        }

        values.put(SR_PHONE, rep.getPhoneNumber());
        values.put(SR_START_DATE, rep.getStartDate());
        values.put(SR_SUPERVISED_LOC, rep.getSupervisedLocId());

        long id = db.insert(TABLE_SALES_REP, null, values);
        ////db.close();
        return id;
    }



    public List<SalesRepresentative> getAllSalesReps() {
        List<SalesRepresentative> reps = new ArrayList<>();

        // Check if the SalesRepresentative table exists
        if (!isTableExists(TABLE_SALES_REP)) {
            return reps; // Return an empty list if the table doesn't exist
        }

        // Check if the table has any data
        if (isTableEmpty(TABLE_SALES_REP)) {
            return reps; // Return an empty list if the table is empty
        }
        SQLiteDatabase db = this.getReadableDatabase();

        // SQL query to join SalesRep table with Location table to get location names
        String query = "SELECT sr.*, loc." + LOC_NAME + " AS LocationName " +
                "FROM " + TABLE_SALES_REP + " sr " +
                "LEFT JOIN " + TABLE_LOCATION + " loc " +
                "ON sr." + SR_SUPERVISED_LOC + " = loc." + LOC_ID;

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                SalesRepresentative rep = new SalesRepresentative();
                rep.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SR_ID)));
                rep.setName(cursor.getString(cursor.getColumnIndexOrThrow(SR_NAME)));
                rep.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(SR_PHONE)));
                rep.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(SR_START_DATE)));
                rep.setSupervisedLocId(cursor.getInt(cursor.getColumnIndexOrThrow(SR_SUPERVISED_LOC)));

                // Get the location name instead of ID
                String locationName = cursor.getString(cursor.getColumnIndexOrThrow("LocationName"));
                rep.setSupervisedLocName(locationName);  // Assuming you add a method or field for location name in SalesRepresentative

                // Retrieve the image BLOB and convert it to Bitmap
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(SR_IMAGE));
                if (imageBytes != null) {
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    rep.setImageBitmap(bitmap);  // Assuming SalesRepresentative has a Bitmap field for image
                }

                reps.add(rep);
            } while (cursor.moveToNext());
        }

        cursor.close();
        ////db.close();
        return reps;
    }


    public int updateSalesRep(SalesRepresentative rep ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SR_NAME, rep.getName());
        values.put(SR_PHONE, rep.getPhoneNumber());
        values.put(SR_START_DATE, rep.getStartDate());
        values.put(SR_SUPERVISED_LOC, rep.getSupervisedLocId());
        Bitmap bitmap = rep.getImageBitmap();
        // Convert the Bitmap to a byte array
        if (bitmap != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            values.put(SR_IMAGE, byteArray);
        }

        int rowsAffected = db.update(TABLE_SALES_REP, values, SR_ID + " = ?", new String[]{String.valueOf(rep.getId())});
       // //db.close();
        return rowsAffected;
    }

    public int deleteSalesRep(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(TABLE_SALES_REP, SR_ID + " = ?", new String[]{String.valueOf(id)});
        ////db.close();
        return rowsDeleted;
    }

    public SalesRepresentative getSalesRepById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_SALES_REP, null, SR_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        SalesRepresentative rep = null;
        if (cursor != null && cursor.moveToFirst()) {
            rep = new SalesRepresentative();
            rep.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SR_ID)));
            rep.setName(cursor.getString(cursor.getColumnIndexOrThrow(SR_NAME)));

            // Retrieve the image stored as byte[] and convert it to Bitmap
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(SR_IMAGE));
            if (imageBytes != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                rep.setImageBitmap(bitmap); // Assuming you have a setImage(Bitmap) method in SalesRepresentative
            }

            rep.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(SR_PHONE)));
            rep.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(SR_START_DATE)));
            rep.setSupervisedLocId(cursor.getInt(cursor.getColumnIndexOrThrow(SR_SUPERVISED_LOC)));
            cursor.close();
        }
        ////db.close();
        return rep;
    }


    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_LOCATION, null);

        if (cursor.moveToFirst()) {
            do {
                Location loc = new Location();
                loc.setLocId(cursor.getInt(cursor.getColumnIndexOrThrow(LOC_ID)));
                loc.setName(cursor.getString(cursor.getColumnIndexOrThrow(LOC_NAME)));
                loc.setAddress(cursor.getString(cursor.getColumnIndexOrThrow(LOC_ADDRESS)));
                locations.add(loc);
            } while (cursor.moveToNext());
        }
        cursor.close();
        //db.close();
        return locations;
    }


    public long addLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOC_NAME, location.getName());
        values.put(LOC_ADDRESS, location.getAddress());

        long id = db.insert(TABLE_LOCATION, null, values);
        //db.close();
        return id;
    }
    public long addInvoice(Invoice invoice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(INV_DATE, invoice.getCreatedDate());
        values.put(INV_TOTAL_PRICE, invoice.getTotalPrice());
        values.put(INV_SALES_REP_ID, invoice.getSalesRepId());
        values.put(INV_LOC_ID, invoice.getLocId());

        long id = db.insert(TABLE_INVOICE, null, values);
        //db.close();
        return id;
    }
    public List<Invoice> getInvoicesByRepAndMonth(int repId, String month, String year) {
        List<Invoice> invoices = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_INVOICE + " WHERE "
                + INV_SALES_REP_ID + " = ? AND strftime('%m', " + INV_DATE + ") = ? AND strftime('%Y', " + INV_DATE + ") = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{
                String.valueOf(repId),
                month,
                year
        });

        if (cursor.moveToFirst()) {
            do {
                Invoice invoice = new Invoice();
                invoice.setInvNo(cursor.getInt(cursor.getColumnIndexOrThrow(INV_NO)));
                invoice.setCreatedDate(cursor.getString(cursor.getColumnIndexOrThrow(INV_DATE)));
                invoice.setTotalPrice(cursor.getDouble(cursor.getColumnIndexOrThrow(INV_TOTAL_PRICE)));
                invoice.setSalesRepId(cursor.getInt(cursor.getColumnIndexOrThrow(INV_SALES_REP_ID)));
                invoice.setLocId(cursor.getInt(cursor.getColumnIndexOrThrow(INV_LOC_ID)));
                invoices.add(invoice);
            } while (cursor.moveToNext());
        }

        cursor.close();
        //db.close();
        return invoices;
    }
    public long addMonthlyRepSales(MonthlyRepSales sales) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MRS_MONTH, sales.getMonth());
        values.put(MRS_SALES_VALUE, sales.getSalesValue());
        values.put(MRS_REP_ID, sales.getRepId());
        values.put(MRS_LOC_ID, sales.getLocId());

        long id = db.insert(TABLE_MONTHLY_SALES, null, values);
        //db.close();
        return id;
    }
    public long addMonthlyRepCommission(MonthlyRepCommission commission) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MRC_MONTH, commission.getMonth());
        values.put(MRC_COMMISSION_VALUE, commission.getCommissionValue());
        values.put(MRC_REP_ID, commission.getRepId());
        values.put(MRC_LOC_ID, commission.getLocId());

        long id = db.insert(TABLE_MONTHLY_COMMISSION, null, values);
        //db.close();
        return id;
    }
    public double calculateCommission(int repId, String month, String year) {
        List<Invoice> invoices = getInvoicesByRepAndMonth(repId, month, year);
        double totalCommission = 0.0;


        int supervisedLocId = getSupervisedLocation(repId);


        Map<Integer, Double> salesByLoc = new HashMap<>();
        for (Invoice invoice : invoices) {
            int locId = invoice.getLocId();
            double amount = invoice.getTotalPrice();
            if (salesByLoc.containsKey(locId)) {
                salesByLoc.put(locId, salesByLoc.get(locId) + amount);
            } else {
                salesByLoc.put(locId, amount);
            }
        }

        for (Map.Entry<Integer, Double> entry : salesByLoc.entrySet()) {
            int locId = entry.getKey();
            double amount = entry.getValue();
            double commission = 0.0;

            if (locId == supervisedLocId) {

                if (amount <= 100_000_000) {
                    commission += amount * 0.05;
                } else {
                    commission += 100_000_000 * 0.05;
                    commission += (amount - 100_000_000) * 0.07;
                }
            } else {

                if (amount <= 100_000_000) {
                    commission += amount * 0.03;
                } else {
                    commission += 100_000_000 * 0.03;
                    commission += (amount - 100_000_000) * 0.04;
                }
            }

            totalCommission += commission;



            MonthlyRepCommission commissionRecord = new MonthlyRepCommission();
            commissionRecord.setMonth(month);
            commissionRecord.setCommissionValue(commission);
            commissionRecord.setRepId(repId);
            commissionRecord.setLocId(locId);
            addMonthlyRepCommission(commissionRecord);
        }

        return totalCommission;
    }
    public int getSupervisedLocation(int repId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT " + SR_SUPERVISED_LOC + " FROM " + TABLE_SALES_REP + " WHERE " + SR_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(repId)});

        int locId = -1;
        if (cursor.moveToFirst()) {
            locId = cursor.getInt(cursor.getColumnIndexOrThrow(SR_SUPERVISED_LOC));
        }

        cursor.close();
        //db.close();
        return locId;
    }
    public int getSalesRepIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (!isTableExists(TABLE_SALES_REP)) {
            return 0; // Return an empty list if the table doesn't exist
        }

        // Check if the table has any data
        if (isTableEmpty(TABLE_SALES_REP)) {
            return 0; // Return an empty list if the table is empty
        }
        String query = "SELECT " + SR_ID + " FROM " + TABLE_SALES_REP + " WHERE " + SR_NAME + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{name});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1; // Not found
    }
    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name=?", new String[]{tableName});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        //db.close();
        return exists;
    }
    public boolean isTableEmpty(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + tableName, null);
        boolean isEmpty = true;
        if (cursor.moveToFirst()) {
            isEmpty = cursor.getInt(0) == 0;
        }
        cursor.close();
        //db.close();
        return isEmpty;
    }

    public int getLocationIdByName(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+LOC_ID+" FROM " + TABLE_LOCATION+" WHERE "+LOC_NAME+" =?", new String[]{name});
        if (cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1; // Not found
    }
}
