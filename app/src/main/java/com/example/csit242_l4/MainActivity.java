package com.example.csit242_l4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
    }


    private void showAddContactDialog() {
        ViewGroup viewGroup = findViewById(R.id.content);
        final View dialogView = LayoutInflater.from(this).inflate(R.layout.add_contact_dialog, viewGroup, false);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView)
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
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ArrayList<Contact> retrieved = dbHelper.getAllContacts();
                for (Contact c : retrieved) {
                    Log.d("Contact", String.format("%d, %s, %s, %s", c.getID(), c.getName(), c.getMobile(), c.getEmail()));
                }
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
