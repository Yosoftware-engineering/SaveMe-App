package com.example.yosef;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class Updatenumbers extends AppCompatActivity {
    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    TextView contact1,contact2,contact3 ,contact4 ,contact5;
    Button add1,update,accept;
    ImageButton delete;
    TextView t1;
    int numcontact=0;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_updatenumbers);
        contact1=(TextView) findViewById(R.id.contact1);
        contact2=(TextView) findViewById(R.id.contact2);
        contact3=(TextView) findViewById(R.id.contact3);
        contact4=(TextView) findViewById(R.id.contact4);
        contact5=(TextView) findViewById(R.id.contact5);
        add1=(Button) findViewById(R.id.add1);
        delete=(ImageButton) findViewById(R.id.delete);
        accept=(Button) findViewById(R.id.accept);

        Invisble();
        loadinfo();//loding the information from database
        Checkper();
        contact1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Visble();
                numcontact=1;
            }
        });
        contact2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Visble();
                numcontact=2;
            }
        });
        contact3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Visble();
                numcontact=3;
            }
        });
        contact4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Visble();
                numcontact=4;
            }
        });
        contact5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Visble();
                numcontact=5;
            }
        });
        add1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
                pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
                startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              DeleteContact();

            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                CheckChanges();
                Intent intent = getIntent();
                final String lastpostion = intent.getStringExtra("last postion");
                if(lastpostion.equals("Userinterface")){
                    Intent intent1 = new Intent(Updatenumbers.this, Userinterface.class);
                    startActivity(intent1);
                }
                else{
                    Intent intent2 = new Intent(Updatenumbers.this, Signup.class);
                    startActivity(intent2);
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent resultIntent) {
        super.onActivityResult(requestCode, resultCode, resultIntent);
        String name = null;
        String number = null;
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = resultIntent.getData();
                // We only need the NUMBER column, because there will be only one row in the result
                String[] PROJECTION_MAIN = new String[]{

                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.Contacts.DISPLAY_NAME
                };
                // Perform the query on the contact to get the NUMBER column
                // We don't need a selection or sort order (there's only one result for the given URI)
                // CAUTION: The query() method should be called from a separate thread to avoid blocking
                // your app's UI thread. (For simplicity of the sample, this code doesn't do that.)
                // Consider using <code><a href="/reference/android/content/CursorLoader.html">CursorLoader</a></code> to perform the query.
                Cursor cursor = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        PROJECTION_MAIN, null, null, null);
                cursor.moveToFirst();

                // Retrieve the phone number from the NUMBER column
                int column = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                number = cursor.getString(column);

                name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));


                storeData(number);
                // Do something with the phone number...
                switch (numcontact) {
                    case 1:
                        contact1.setText(number);
//                            t1.setText(name);
                        break;
                    case 2:
                        contact2.setText(number);
                        break;
                    case 3:
                        contact3.setText(number);
                        break;
                    case 4:
                        contact4.setText(number);
                        break;
                    case 5:
                        contact5.setText(number);

                }
            }
        }

    }
    public void storeData(String number)
    {
        sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        SharedPreferences.Editor ed= sp.edit();
        ed.putString(Integer.toString(numcontact),number);

        ed.commit();
        Toast.makeText(this,"Data saved successfully",Toast.LENGTH_SHORT).show();
        Invisble();
        accept.setVisibility(View.VISIBLE);
    }
    public void loadinfo()
    {
        sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        String s1= sp.getString("1","click here to choose contact1");
        String s2= sp.getString("2","click here to choose contact2");
        String s3= sp.getString("3","click here to choose contact3");
        String s4= sp.getString("4","click here to choose contact4");
        String s5= sp.getString("5","click here to choose contact5");

        contact1.setText(s1);
        contact2.setText(s2);
        contact3.setText(s3);
        contact4.setText(s4);
        contact5.setText(s5);
        Toast.makeText(this,"Data loading successfully",Toast.LENGTH_SHORT).show();
        return;
    }
    public void DeleteContact(){
        String s;
        SharedPreferences.Editor ed = sp.edit();
        switch (numcontact){
            case 1 :
                s = sp.getString("1","click here to choose contact1");
                if(s!="choose Contact 1"){
                    ed.remove("1");
                    ed.commit();
                    ed.remove("1");
                    ed.commit();
                    contact1.setText("click here to choose contact1");
                }
                break;
            case 2 :
                s = sp.getString("2","click here to choose contact2");
                if(s!="click here to choose contact2"){
                    ed.remove("2");
                    ed.commit();
                    ed.remove("2");
                    ed.commit();
                    contact2.setText("click here to choose contact2");
                }
                break;
            case 3 :
                s = sp.getString("3","click here to choose contact3");
                if(s!="click here to choose contact3"){
                    ed.remove("3");
                    ed.commit();
                    ed.remove("3");
                    ed.commit();
                    contact3.setText("click here to choose contact3");
                }
                break;
            case 4 :
                s = sp.getString("4","click here to choose contact4");
                if(s!="click here to choose contact4"){
                    ed.remove("4");
                    ed.commit();
                    ed.remove("4");
                    ed.commit();
                    contact4.setText("click here to choose contact4");
                }
                break;
            case 5 :
                s = sp.getString("5","click here to choose contact5");
                if(s!="click here to choose contact5"){
                    ed.remove("5");
                    ed.commit();
                    ed.remove("5");
                    ed.commit();
                    contact5.setText("click here to choose contact5");
                }
                break;
        }
        Toast.makeText(this,"Data deleteing successfully",Toast.LENGTH_SHORT).show();
    }
    public void CheckChanges()
    {
        int i=0;
        sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
        if(sp.contains("1")) i++;
        if(sp.contains("2")) i++;
        if(sp.contains("3")) i++;
        if(sp.contains("4")) i++;
        if(sp.contains("5")) i++;
        if(i>=1) {
            Toast.makeText(this, "save changes successfully", Toast.LENGTH_SHORT).show();
        }
        else
            Toast.makeText(this, "please put at least phone number for your close people", Toast.LENGTH_SHORT).show();
        return;
    }
    public void Invisble()
    {
        add1.setVisibility(View.INVISIBLE);
        delete.setVisibility(View.INVISIBLE);

    }
    public void Visble()
    {
        add1.setVisibility(View.VISIBLE);
        delete.setVisibility(View.VISIBLE);

    }
    public static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 99;
    public void Checkper()
    {
        if (ContextCompat.checkSelfPermission(Updatenumbers.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) + ContextCompat
                .checkSelfPermission(Updatenumbers.this,
                        Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (Updatenumbers.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (Updatenumbers.this, Manifest.permission.READ_CONTACTS)) {
                Snackbar.make(findViewById(android.R.id.content),
                        "Please Grant Permissions",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(Updatenumbers.this,
                                        new String[]{Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
                            }
                        }).show();
            } else {
                ActivityCompat.requestPermissions(Updatenumbers.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);
            }
        }
    }
}
