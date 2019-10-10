package com.example.contentresolver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    TextView tvContacts;
    EditText etDeleteId,etIdLookUp,etAddName;
    static final Uri CONTENT_URL = Uri.parse("content://com.example.lab12_contentprovider.ContactProvider/cpcontacts");

    ContentResolver resolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        tvContacts = findViewById(R.id.btnRetrieve);
        etDeleteId = findViewById(R.id.deleteIDEditText);
        etIdLookUp = findViewById(R.id.idLookUpEditText);
        etAddName = findViewById(R.id.addNameEditText);


        resolver = getContentResolver();
        // this method will be used  to access the database content

        getContacts();
    }

    private void getContacts() {
        String[] projection = new String[]{"id", "name"};

        Cursor cursor = resolver.query(CONTENT_URL, projection, null, null, null);

        String contactList = "";

        if (cursor.moveToFirst()) {
            do {
                String id = cursor.getString(cursor.getColumnIndex("id"));
                String name = cursor.getString(cursor.getColumnIndex("name"));

                contactList = contactList + id + " | " + name + "\n";

            } while (cursor.moveToFirst());
        }

        tvContacts.setText(contactList);

    }

    public void showContacts(View view) {
        getContacts();
    }

    public void deleteContact(View view) {
        String idToDelete = etDeleteId.getText().toString();

        long idDeleted = resolver.delete(CONTENT_URL, "id = ?", new String[]{idToDelete});


        etDeleteId.setText("");

        getContacts();
    }

    public void lookupContact(View view) {
String idToFind = etIdLookUp.getText().toString();
 String[] projection = new String[]{"id", "name"};
        Cursor cursor = resolver.query(CONTENT_URL, projection, "id = ?", new String[]{idToFind}, null);

        String contact = "";

        if(cursor.moveToFirst()){
            String id = cursor.getString(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));

            contact = contact + id + " | " + name + "\n";
        }else
        {
            Toast.makeText(this,"Contact not found", Toast.LENGTH_SHORT).show();
        }

    }

    public void addContact(View view) {
String nameToAdd = etAddName.getText().toString();

ContentValues values = new ContentValues();
values.put("name",nameToAdd);

resolver.insert(CONTENT_URL, values);
etAddName.setText("");
getContacts();

    }


}
