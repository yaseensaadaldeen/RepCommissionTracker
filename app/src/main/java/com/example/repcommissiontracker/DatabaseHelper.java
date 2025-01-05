package com.example.repcommissiontracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.repcommissiontracker.Classes.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "SalesManagement.db";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_SALES_REP = "SalesRepresentative";
    public static final String TABLE_LOCATION = "Location";
    public static final String TABLE_INVOICE = "Invoice";
    public static final String TABLE_MONTHLY_SALES = "MonthlyRepSales";
    public static final String TABLE_MONTHLY_COMMISSION = "MonthlyRepCommission";

    // SalesRepresentative Table Columns
    public static final String SR_ID = "ID";
    public static final String SR_NAME = "Name";
    public static final String SR_IMAGE = "ImagePath";
    public static final String SR_PHONE = "PhoneNumber";
    public static final String SR_START_DATE = "StartDate";
    public static final String SR_SUPERVISED_LOC = "SupervisedLocID";

    // Location Table Columns
    public static final String LOC_ID = "LocID";
    public static final String LOC_NAME = "Name";
    public static final String LOC_ADDRESS = "Address";

    // Invoice Table Columns
    public static final String INV_NO = "InvNo";
    public static final String INV_DATE = "CreatedDate";
    public static final String INV_TOTAL_PRICE = "TotalPrice";
    public static final String INV_SALES_REP_ID = "SalesRepID";
    public static final String INV_LOC_ID = "LocID";

    // MonthlyRepSales Table Columns
    public static final String MRS_ID = "ID";
    public static final String MRS_MONTH = "Month";
    public static final String MRS_SALES_VALUE = "SalesValue";
    public static final String MRS_REP_ID = "RepID";
    public static final String MRS_LOC_ID = "LocID";

    // MonthlyRepCommission Table Columns
    public static final String MRC_ID = "ID";
    public static final String MRC_MONTH = "Month";
    public static final String MRC_COMMISSION_VALUE = "CommissionValue";
    public static final String MRC_REP_ID = "RepID";
    public static final String MRC_LOC_ID = "LocID";

    // Create Table Statements
    private static final String CREATE_TABLE_SALES_REP = "CREATE TABLE " + TABLE_SALES_REP + "("
            + SR_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + SR_NAME + " TEXT NOT NULL, "
            + SR_IMAGE + " TEXT, "
            + SR_PHONE + " TEXT, "
            + SR_START_DATE + " TEXT, "
            + SR_SUPERVISED_LOC + " INTEGER, "
            + "FOREIGN KEY(" + SR_SUPERVISED_LOC + ") REFERENCES " + TABLE_LOCATION + "(" + LOC_ID + ")"
            + ");";

    private static final String CREATE_TABLE_LOCATION = "CREATE TABLE " + TABLE_LOCATION + "("
            + LOC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + LOC_NAME + " TEXT NOT NULL, "
            + LOC_ADDRESS + " TEXT"
            + ");";

    private static final String CREATE_TABLE_INVOICE = "CREATE TABLE " + TABLE_INVOICE + "("
            + INV_NO + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + INV_DATE + " TEXT, "
            + INV_TOTAL_PRICE + " REAL, "
            + INV_SALES_REP_ID + " INTEGER, "
            + INV_LOC_ID + " INTEGER, "
            + "FOREIGN KEY(" + INV_SALES_REP_ID + ") REFERENCES " + TABLE_SALES_REP + "(" + SR_ID + "), "
            + "FOREIGN KEY(" + INV_LOC_ID + ") REFERENCES " + TABLE_LOCATION + "(" + LOC_ID + ")"
            + ");";

    private static final String CREATE_TABLE_MONTHLY_SALES = "CREATE TABLE " + TABLE_MONTHLY_SALES + "("
            + MRS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MRS_MONTH + " TEXT, "
            + MRS_SALES_VALUE + " REAL, "
            + MRS_REP_ID + " INTEGER, "
            + MRS_LOC_ID + " INTEGER, "
            + "FOREIGN KEY(" + MRS_REP_ID + ") REFERENCES " + TABLE_SALES_REP + "(" + SR_ID + "), "
            + "FOREIGN KEY(" + MRS_LOC_ID + ") REFERENCES " + TABLE_LOCATION + "(" + LOC_ID + ")"
            + ");";

    private static final String CREATE_TABLE_MONTHLY_COMMISSION = "CREATE TABLE " + TABLE_MONTHLY_COMMISSION + "("
            + MRC_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + MRC_MONTH + " TEXT, "
            + MRC_COMMISSION_VALUE + " REAL, "
            + MRC_REP_ID + " INTEGER, "
            + MRC_LOC_ID + " INTEGER, "
            + "FOREIGN KEY(" + MRC_REP_ID + ") REFERENCES " + TABLE_SALES_REP + "(" + SR_ID + "), "
            + "FOREIGN KEY(" + MRC_LOC_ID + ") REFERENCES " + TABLE_LOCATION + "(" + LOC_ID + ")"
            + ");";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_LOCATION); // Create Location first because SalesRep references it
        db.execSQL(CREATE_TABLE_SALES_REP);
        db.execSQL(CREATE_TABLE_INVOICE);
        db.execSQL(CREATE_TABLE_MONTHLY_SALES);
        db.execSQL(CREATE_TABLE_MONTHLY_COMMISSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older tables if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONTHLY_COMMISSION);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MONTHLY_SALES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INVOICE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALES_REP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);

        // Create tables again
        onCreate(db);
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
        db.close();
        return locId;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true);
    }

    // CRUD Operations (To be implemented below)
    public long addSalesRep(SalesRepresentative rep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SR_NAME, rep.getName());
        values.put(SR_IMAGE, rep.getImagePath());
        values.put(SR_PHONE, rep.getPhoneNumber());
        values.put(SR_START_DATE, rep.getStartDate());
        values.put(SR_SUPERVISED_LOC, rep.getSupervisedLocId());

        long id = db.insert(TABLE_SALES_REP, null, values);
        db.close();
        return id;
    }
    public List<SalesRepresentative> getAllSalesReps() {
        List<SalesRepresentative> reps = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_SALES_REP;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                SalesRepresentative rep = new SalesRepresentative();
                rep.setId(cursor.getInt(cursor.getColumnIndexOrThrow(SR_ID)));
                rep.setName(cursor.getString(cursor.getColumnIndexOrThrow(SR_NAME)));
                rep.setImagePath(cursor.getString(cursor.getColumnIndexOrThrow(SR_IMAGE)));
                rep.setPhoneNumber(cursor.getString(cursor.getColumnIndexOrThrow(SR_PHONE)));
                rep.setStartDate(cursor.getString(cursor.getColumnIndexOrThrow(SR_START_DATE)));
                rep.setSupervisedLocId(cursor.getInt(cursor.getColumnIndexOrThrow(SR_SUPERVISED_LOC)));
                reps.add(rep);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return reps;
    }
    public int updateSalesRep(SalesRepresentative rep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SR_NAME, rep.getName());
        values.put(SR_IMAGE, rep.getImagePath());
        values.put(SR_PHONE, rep.getPhoneNumber());
        values.put(SR_START_DATE, rep.getStartDate());
        values.put(SR_SUPERVISED_LOC, rep.getSupervisedLocId());

        // updating row
        int result = db.update(TABLE_SALES_REP, values, SR_ID + " = ?",
                new String[]{String.valueOf(rep.getId())});
        db.close();
        return result;
    }
    public void deleteSalesRep(int repId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SALES_REP, SR_ID + " = ?",
                new String[]{String.valueOf(repId)});
        db.close();
    }
    public long addLocation(Location location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(LOC_NAME, location.getName());
        values.put(LOC_ADDRESS, location.getAddress());

        long id = db.insert(TABLE_LOCATION, null, values);
        db.close();
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
        db.close();
        return id;
    }
    public List<Invoice> getInvoicesByRepAndMonth(int repId, String month, String year) {
        List<Invoice> invoices = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_INVOICE + " WHERE "
                + INV_SALES_REP_ID + " = ? AND strftime('%m', " + INV_DATE + ") = ? AND strftime('%Y', " + INV_DATE + ") = ?";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, new String[]{
                String.valueOf(repId),
                month, // Ensure month is in two-digit format, e.g., "09"
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
        db.close();
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
        db.close();
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
        db.close();
        return id;
    }
    public double calculateCommission(int repId, String month, String year) {
        List<Invoice> invoices = getInvoicesByRepAndMonth(repId, month, year);
        double totalCommission = 0.0;

        // Assuming you have a method to get the supervised location of a rep
        int supervisedLocId = getSupervisedLocation(repId);

        // Group sales by location
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
                // Primary region commission
                if (amount <= 100_000_000) {
                    commission += amount * 0.05;
                } else {
                    commission += 100_000_000 * 0.05;
                    commission += (amount - 100_000_000) * 0.07;
                }
            } else {
                // Other regions commission
                if (amount <= 100_000_000) {
                    commission += amount * 0.03;
                } else {
                    commission += 100_000_000 * 0.03;
                    commission += (amount - 100_000_000) * 0.04;
                }
            }

            totalCommission += commission;

            // Optionally, store individual commission per location
            // Add to MonthlyRepCommission table
            MonthlyRepCommission commissionRecord = new MonthlyRepCommission();
            commissionRecord.setMonth(month);
            commissionRecord.setCommissionValue(commission);
            commissionRecord.setRepId(repId);
            commissionRecord.setLocId(locId);
            addMonthlyRepCommission(commissionRecord);
        }

        return totalCommission;
    }

    public List<Location> getAllLocations() {
        List<Location> locations = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_LOCATION;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

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
        db.close();
        return locations;
    }

}
