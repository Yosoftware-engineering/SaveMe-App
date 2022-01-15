 package com.example.yosef;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    /**
     * Firebase symbols
     **/
    private DatabaseReference myRef;
    private FirebaseDatabase database;
    private FirebaseAuth mAuth;

    private ListView myListView;
    private ArrayList<String> myArraylist = new ArrayList<>();
    private ArrayAdapter<String> adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        /**Database**/
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users");
     /*  String id = mAuth.getUid();
        Order order1 = new Order("dsada@.3213.com ","120.3132" ,"1230.21","dasdasd","dasdas");
        myRef.push().setValue(order1);*/

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(TestActivity.this,android.R.layout.simple_list_item_1, myArraylist);
        myListView = (ListView) findViewById(R.id.myListView);
        myListView.setAdapter(arrayAdapter);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                String email = dataSnapshot.getValue(Order.class).getEmail();
                String lat = dataSnapshot.getValue(Order.class).getLatit();
                String lag = dataSnapshot.getValue(Order.class).getLongit();
                myArraylist.add(email) ;
                myArraylist.add(lat) ;
                myArraylist.add(lag) ;
                arrayAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}