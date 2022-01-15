package com.example.yosef;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yosef.R;
import com.example.yosef.mapsactivity.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SendOrderActivity extends AppCompatActivity {
    public List<Order> order1=new ArrayList<>();
    /**
     * Location symbols
     **/
    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    String latitude, longitude;
    Button getlocationbtn;

    /**
     * Firebase symbols
     **/
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    /**
     * Timer symbols
     **/
    private EditText mEditTextInput;
    private TextView mTextViewCountDown;
    private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    String finalAddress = null;
     String  link="https://www.google.com/maps/@";
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    private String s1,s2,s3,s4,s5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.send_order_activity);


        /**Database**/
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        /**Location**/
        //Add permission

        ActivityCompat.requestPermissions(this, new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        getlocationbtn = findViewById(R.id.getlocationbtn);
        getlocationbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                //Check gps is enable or not

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    //Write Function To enable gps
                    OnGPS();
                } else {
                    //GPS is already On then

                    getLocation();
                }


            }

        });
        //check phone numbers
        SharedPreferences sp = getSharedPreferences("Contacts", Context.MODE_PRIVATE);
         s1 = sp.getString("1", "click here to choose contact1");
         s2 = sp.getString("2", "click here to choose contact2");
         s3 = sp.getString("3", "click here to choose contact3");
         s4 = sp.getString("4", "click here to choose contact4");
         s5 = sp.getString("5", "click here to choose contact5");
        if(s1.equals("click here to choose contact1") && s2.equals("click here to choose contact2") && s3.equals("click here to choose contact3") && s4.equals("click here to choose contact4") && s5.equals("click here to choose contact5"))
        {
            AlertDialog.Builder detect = new AlertDialog.Builder(SendOrderActivity.this);
            detect.setMessage("Add atleast one emergency contact to continue.").setPositiveButton("OK", new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                    Intent in = new Intent(SendOrderActivity.this,Updatenumbers.class);
                    startActivity(in);
                }
            });
            detect.setCancelable(false);
            AlertDialog alert=detect.create();
            alert.show();


        }

     /*   s1 = s1.replaceAll("\\s+","");
        s2 = s2.replaceAll("\\s+","");
        s3 = s3.replaceAll("\\s+","");
        s4 = s4.replaceAll("\\s+","");
        s5 = s5.replaceAll("\\s+","");*/
        Log.d("hehe","*2121212****************************************************");
        myRef = database.getReference("orders");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                order1.clear();
                List<String> keys=new ArrayList<>();
                for (DataSnapshot KeyNode : dataSnapshot.getChildren()){
                    keys.add(KeyNode.getKey());
                    Order order = KeyNode.getValue(Order.class);
                    Log.d("hehe","*****************************************************");
                    order1.add(order);
                       /*    Order order = dataSnapshot1.getValue(Order.class);
                            Log.d("hehe",order.latit);
                            Log.d("hehe",order.longit);
                            double latit = Double.parseDouble(order.latit);
                            double longit=Double.parseDouble(order.longit);
                            LatLng location=new LatLng(latit,longit);
                            mMap.addMarker(new MarkerOptions().position(location).title(order.email)).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
*/
                }
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

           /*      String latit = dataSnapshot.child("latit").getValue().toString();
                String  longit = dataSnapshot.child("longit").getValue().toString();*/
            /*    double latit = Double.parseDouble(dataSnapshot.child("latit").getValue().toString());
                double longit=Double.parseDouble(dataSnapshot.child("longit").getValue().toString());
                LatLng location=new LatLng(latit,longit);
                mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("test"));*/

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Toast.makeText(SendOrderActivity.this ,"error",Toast.LENGTH_SHORT).show();
            }
        });


        /**Timer**/
        mEditTextInput = findViewById(R.id.edit_text_input);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonSet = findViewById(R.id.button_set);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(SendOrderActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(SendOrderActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }

                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });

    }

    private void getLocation() {
        //Check Permissions again
        finalAddress="Hey.. I am in Danger. Please, help me ASAP!!";
        if (ActivityCompat.checkSelfPermission(SendOrderActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(SendOrderActivity.this,

                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location LocationGps = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps != null) {
                double lat = LocationGps.getLatitude();
                double longi = LocationGps.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                /*showLocationTxt.setText("Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude);*/


            } else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                /* showLocationTxt.setText("Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude);*/


            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double longi = LocationPassive.getLongitude();

                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);

                /* showLocationTxt.setText("Your Location:" + "\n" + "Latitude= " + latitude + "\n" + "Longitude= " + longitude);*/

            } else {
                Toast.makeText(this, "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }
            link = link +latitude+","+longitude+",18.12z";

            sendtextmessage();
            callemergance();
            //Thats All Run Your App
            saveLocation();
        }

    }
    public void sendtextmessage() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }


        //
        }


    public void saveLocation() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDataformat1 = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat simpleDataformat2 = new SimpleDateFormat("dd:MM:yyyy");
        String id = (FirebaseAuth.getInstance().getCurrentUser().getUid());
        String email = (FirebaseAuth.getInstance().getCurrentUser().getEmail());
        String latit = latitude;
        String longit = longitude;
        String oTime = simpleDataformat1.format(calendar.getTime());
        String oDate = simpleDataformat2.format(calendar.getTime());
        Intent intent = getIntent();

        final String Emergency_type = intent.getStringExtra("Emergency Type");
        switch (Emergency_type) {
            case "Police":
                myRef =  database.getInstance().getReference("Orders").child("orders").child("Police");

                Order order1 = new Order(Emergency_type,email, latit, longit, oTime, oDate);
                myRef.push().setValue(order1);
                Toast.makeText(SendOrderActivity.this, "Police On The Way", Toast.LENGTH_SHORT).show();
                break;
            case "Ambulance":
                myRef =  database.getInstance().getReference("Orders").child("orders").child("Ambulance");

                Order order2 = new Order(Emergency_type,email, latit, longit, oTime, oDate);
                myRef.push().setValue(order2);
                Toast.makeText(SendOrderActivity.this, "Ambulance On The Way", Toast.LENGTH_SHORT).show();
                break;
            case "FireFight":
                myRef = database.getInstance().getReference("Orders").child("orders").child("FireFight");
                Order order3 = new Order(Emergency_type,email, latit, longit, oTime, oDate);
                myRef.push().setValue(order3);
                Toast.makeText(SendOrderActivity.this, "FireFight On The Way", Toast.LENGTH_SHORT).show();
                break;
            case "ClosePeople":
                Toast.makeText(SendOrderActivity.this, "TEST the close poeple", Toast.LENGTH_SHORT).show();
                myRef = database.getReference("Orders").child("Close People");
                Order order4 = new Order(Emergency_type,email, latit, longit, oTime, oDate);
                myRef.push().setValue(order4);
                Toast.makeText(SendOrderActivity.this, "close people On The Way", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void OnGPS() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void setTime(long milliseconds) {
        mStartTimeInMillis = milliseconds;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Pause");
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Start");

            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
                getlocationbtn.callOnClick();
                saveLocation();
                resetTimer();
            }
            else {
                mButtonStartPause.setVisibility(View.VISIBLE);

            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }

        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }

    private void callemergance() {
        Intent intent = getIntent();
        final String Emergency_type = intent.getStringExtra("Emergency Type");
        switch (Emergency_type) {
            case "Police":
                Intent intent1 = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:0502847637"));
                startActivity(intent);
                break;
            case "Ambulance":

                break;
            case "FireFight":

                break;
            case "ClosePeople":

                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    SmsManager sms = SmsManager.getDefault();
                    if(!s1.equals("click here to choose contact1")) {
                        Toast.makeText(SendOrderActivity.this,"this is contact one "+ finalAddress,Toast.LENGTH_SHORT).show();
                        Log.d("hehe",finalAddress+"this is sms 1");

                        sms.sendTextMessage(s1,null,finalAddress+link,null,null);
                        Log.d("hehe","sending sms"+finalAddress);
                    } Toast.makeText(SendOrderActivity.this,s1,Toast.LENGTH_SHORT).show();
                    if (!s2.equals("click here to choose contact2"))
                        sms.sendTextMessage(s2, null,finalAddress+link, null, null); //must be uncommented
                    if (!s3.equals("click here to choose contact3"))
                        sms.sendTextMessage(s3, null,finalAddress+link, null, null);
                    if (!s4.equals("click here to choose contact4"))
                        sms.sendTextMessage(s4, null,finalAddress+link, null, null);
                    if (!s5.equals("click here to choose contact5"))
                        sms.sendTextMessage(s5, null,finalAddress+link, null, null);




                    Toast.makeText(getApplicationContext(), "SMS sent.",
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(),
                            "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

    }
}

