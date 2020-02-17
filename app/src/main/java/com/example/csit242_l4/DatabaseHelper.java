package com.example.csit242_l4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Lab4";
    private static final String TABLE_CONTACTS = "Contacts";
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_EMAIL = "email";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = String.format(
                "CREATE TABLE %s (%s INTEGER PRIMARY KEY, %s TEXT, %s TEXT, %s TEXT)",
                TABLE_CONTACTS, KEY_ID, KEY_NAME, KEY_MOBILE, KEY_EMAIL);
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(String.format("DROP TABLE IF EXISTS %s", TABLE_CONTACTS));
    }



    // list all contacts
    public ArrayList<Contact> getAllContacts() {
        ArrayList<Contact> contactsRetrieved = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s", TABLE_CONTACTS);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Contact c = new Contact();
                c.setID(Integer.parseInt(cursor.getString(0)));
                c.setName(cursor.getString(1));
                c.setMobile(cursor.getString(2));
                c.setEmail(cursor.getString(3));
                contactsRetrieved.add(c);
            } while (cursor.moveToNext());
        }
        return contactsRetrieved;
    }

    // add contact
    public void addContact(Contact c) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, c.getName());
        values.put(KEY_MOBILE, c.getMobile());
        values.put(KEY_EMAIL, c.getEmail());
        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    //    public int updateContact(com.example.csit242_l4.Contact c) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        valu
//    }

    // delete contact
    public void deleteContact(Contact c) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_NAME + " = ?", new String[] { String.valueOf(c.getName())});
        db.close();

    }

    // search by name
    public ArrayList<Contact> searchContacts(String name) {
        ArrayList<Contact> contactsRetrieved = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = String.format("SELECT * FROM %s WHERE %s='%s'", TABLE_CONTACTS, KEY_NAME, name);
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Contact c = new Contact();
                c.setID(Integer.parseInt(cursor.getString(0)));
                c.setName(cursor.getString(1));
                c.setMobile(cursor.getString(2));
                c.setEmail(cursor.getString(3));
                contactsRetrieved.add(c);
            } while (cursor.moveToNext());
        }
        return contactsRetrieved;
    }

}