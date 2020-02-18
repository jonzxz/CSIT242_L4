package com.example.csit242_l4;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public DatabaseHelper dbHelper = new DatabaseHelper(this);
    ArrayList<Contact> allContacts;
    String[] namesOfContacts;
    ArrayAdapter<String> adapter;
    ListView allContactDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allContacts = dbHelper.getAllContacts();
        namesOfContacts = new String[allContacts.size()];

        for (int i = 0; i < allContacts.size(); i ++) {
            namesOfContacts[i] = allContacts.get(i).getName();
        }

        final Button addContact = (Button) findViewById(R.id.addContactBtn);
        final Button updateContact = (Button) findViewById(R.id.updateContactBtn);
        final Button deleteContact = (Button) findViewById(R.id.deleteContactBtn);
        final Button searchContact = (Button) findViewById(R.id.searchContactBtn);
        final Button showAll = (Button) findViewById(R.id.showAllBtn);
        allContactDisplay = (ListView) findViewById(R.id.allContactDisplay);

        adapter = new ArrayAdapter<String>(this, R.layout.listview_item_row, namesOfContacts);
        allContactDisplay.setAdapter(adapter);
        allContactDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup viewGroup = findViewById(R.id.content);
                final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.result_contact_dialog, viewGroup, false);
                EditText nameField = (EditText) dialogView.findViewById(R.id.resultNameField);
                EditText mobileField = (EditText) dialogView.findViewById(R.id.resultMobileField);
                EditText emailField = (EditText) dialogView.findViewById(R.id.resultEmailField);
                nameField.setText(allContacts.get(position).getName());
                mobileField.setText(allContacts.get(position).getMobile());
                emailField.setText(allContacts.get(position).getEmail());
                AlertDialog.Builder secBuilder = new AlertDialog.Builder(MainActivity.this);
                secBuilder.setView(dialogView)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = secBuilder.create();
                dialog.show();
            }
        });


        addContact.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(R.id.content);
                final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.add_contact_dialog, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

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
                                refreshList();

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
        });

        deleteContact.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                ViewGroup viewGroup = findViewById(R.id.content);
                final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.delete_contact_dialog, viewGroup, false);
                final Spinner contactPicker = (Spinner) dialogView.findViewById(R.id.contactPicker);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                ArrayList<String> contactNameString = new ArrayList<>();
                ArrayList<Contact> allContacts = dbHelper.getAllContacts();


                for (Contact c : allContacts) {
                    contactNameString.add(c.getName());
                }

                // Sets contactPicker adapter to show contactNameString
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, contactNameString);
                contactPicker.setAdapter(adapter);
                contactPicker.setPadding(70, 50, 50, 50);


                //Builder settings
                builder.setView(dialogView)
                        .setTitle("Delete Contact")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteContact(new Contact(contactPicker.getSelectedItem().toString()));
                                refreshList();
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
        });

        searchContact.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ViewGroup viewGroup = findViewById(R.id.content);
                final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.search_contact_dialog, viewGroup, false);
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                builder.setView(dialogView)
                        .setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText searchNameField = (EditText) dialogView.findViewById(R.id.searchNameField);
                        String name = searchNameField.getText().toString();

                        final ArrayList<Contact> contactsFound = dbHelper.searchContacts(name);
                        String[] contactsNames = new String[contactsFound.size()];
                        for (int i = 0; i < contactsFound.size(); i++) {
                            contactsNames[i] = contactsFound.get(i).getName();
                        }

                        adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.listview_item_row, contactsNames);
                        allContactDisplay.setAdapter(adapter);
                        allContactDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.result_contact_dialog, viewGroup, false);
                                EditText nameField = (EditText) dialogView.findViewById(R.id.resultNameField);
                                EditText mobileField = (EditText) dialogView.findViewById(R.id.resultMobileField);
                                EditText emailField = (EditText) dialogView.findViewById(R.id.resultEmailField);
                                nameField.setText(contactsFound.get(position).getName());
                                mobileField.setText(contactsFound.get(position).getMobile());
                                emailField.setText(contactsFound.get(position).getEmail());
                                AlertDialog.Builder secBuilder = new AlertDialog.Builder(MainActivity.this);
                                secBuilder.setView(dialogView)
                                .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                AlertDialog dialog = secBuilder.create();
                                dialog.show();
                            }
                        });

                    }
                }).setNegativeButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });


        updateContact.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("t", "yoohoo");
            }
        });


        showAll.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                refreshList();
            }
        });
    }

    //Clears existing arraylist and replace with newly updated DB
    // replaces string array passed into listview with updated arraylist
    // overwrite adapter and set new adapter
    // Sadly this is not a sustainable way because if the contact list is huge enough this probably
    // will post a memory issue
    private void refreshList() {
        allContacts.clear();
        allContacts = dbHelper.getAllContacts();
        namesOfContacts = new String[allContacts.size()];
        for (int i = 0; i < allContacts.size(); i ++) {
            namesOfContacts[i] = allContacts.get(i).getName();
        }
        adapter = new ArrayAdapter<String>(MainActivity.this, R.layout.listview_item_row, namesOfContacts);
        allContactDisplay.setAdapter(adapter);
        allContactDisplay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ViewGroup viewGroup = findViewById(R.id.content);
                final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.result_contact_dialog, viewGroup, false);
                EditText nameField = (EditText) dialogView.findViewById(R.id.resultNameField);
                EditText mobileField = (EditText) dialogView.findViewById(R.id.resultMobileField);
                EditText emailField = (EditText) dialogView.findViewById(R.id.resultEmailField);
                nameField.setText(allContacts.get(position).getName());
                mobileField.setText(allContacts.get(position).getMobile());
                emailField.setText(allContacts.get(position).getEmail());
                AlertDialog.Builder secBuilder = new AlertDialog.Builder(MainActivity.this);
                secBuilder.setView(dialogView)
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                AlertDialog dialog = secBuilder.create();
                dialog.show();
            }
        });


    }


}
