package com.example.yosef;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.yosef.mapsactivity.MapsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Userinterface extends AppCompatActivity {


    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;
    private String uid;
    String Emergency_Type;

    private Button ambulancebtn, firefightbtn, closepplbtn, mapbtn,closepeopleupdate;
    private Button policebtn, heartrate,testactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.userinterface_activity);


        /**Database**/
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Orders");
        mAuth = FirebaseAuth.getInstance();


        /** Emergency Buttons Activites**/
        policebtn = findViewById(R.id.policebtn);
        ambulancebtn = findViewById(R.id.ambulancebtn);
        firefightbtn = findViewById(R.id.firefightbtn);
        closepplbtn = findViewById(R.id.closepplbtn);
        mapbtn = findViewById(R.id.mapbtn);
        heartrate = findViewById(R.id.heartrate);
        closepeopleupdate = findViewById(R.id.updateclosepeople);
        closepeopleupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String lastpostion="Userinterface";
                Intent intent = new Intent(Userinterface.this, Updatenumbers.class);
                intent.putExtra("last postion", lastpostion);
                startActivity(intent);

            }
        });
        policebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Emergency_Type = "Police";
                Intent intent = new Intent(Userinterface.this, SendOrderActivity.class);
                intent.putExtra("Emergency Type", Emergency_Type);
                startActivity(intent);

            }
        });


        ambulancebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Emergency_Type = "Ambulance";
                Intent intent = new Intent(Userinterface.this, SendOrderActivity.class);
                intent.putExtra("Emergency Type", Emergency_Type);
                startActivity(intent);
            }
        });

        firefightbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Emergency_Type = "FireFight";
                Intent intent = new Intent(Userinterface.this, SendOrderActivity.class);
                intent.putExtra("Emergency Type", Emergency_Type);
                startActivity(intent);
            }
        });

        closepplbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Emergency_Type = "ClosePeople";
                Intent intent = new Intent(Userinterface.this, SendOrderActivity.class);
                intent.putExtra("Emergency Type", Emergency_Type);
                startActivity(intent);
            }
        });

        mapbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MapsActivity.class));
            }
        });

        heartrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Userinterface.this, MesureActivity.class);
                startActivity(intent);

            }
        });

    }




}





