package com.example.csit242_l4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public DatabaseHelper dbHelper = new DatabaseHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        final Button addContact = (Button) findViewById(R.id.addContactBtn);
        final Button updateContact = (Button) findViewById(R.id.addContactBtn);
        final Button deleteContact = (Button) findViewById(R.id.deleteContactBtn);
        final Button searchContact = (Button) findViewById(R.id.searchContactBtn);


        addContact.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddContactDialog();
            }
        });

        deleteContact.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDeleteContactDialog();
            }
        });
    }


    private void showAddContactDialog() {

        ViewGroup viewGroup = findViewById(R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.add_contact_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Builder settings
        builder.setView(dialogView)
                .setTitle("Add New Contact")
                .setPositiveButton("Add contact", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText nameInput = (EditText) dialogView.findViewById(R.id.name);
                EditText mobileInput = (EditText) dialogView.findViewById(R.id.mobile);
                EditText emailInput = (EditText) dialogView.findViewById(R.id.email);
                String name = nameInput.getText().toString();
                String mobile = mobileInput.getText().toString();
                String email = emailInput.getText().toString();
                dbHelper.addContact(new Contact(name, mobile, email));
                Log.d("ADDED", name);
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showDeleteContactDialog() {

        ViewGroup viewGroup = findViewById(R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.delete_contact_dialog, viewGroup, false);
        final Spinner contactPicker = (Spinner) dialogView.findViewById(R.id.contactPicker);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Populate AL<String> contactNameString with names of Contact in AL<Contact> allContacts
        ArrayList<Contact> allContacts = dbHelper.getAllContacts();
        ArrayList<String> contactNameString = new ArrayList<>();

        for (Contact c : allContacts) {
            contactNameString.add(c.getName());
        }

        // Sets contactPicker adapter to show contactNameString
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, contactNameString);
        contactPicker.setAdapter(adapter);
        contactPicker.setPadding(70, 50, 50, 50);

        //Builder settings
        builder.setView(dialogView)
        .setTitle("Delete Contact")
        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbHelper.deleteContact(new Contact(contactPicker.getSelectedItem().toString()));
                Log.d("DELETED", contactPicker.getSelectedItem().toString());
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
