package com.example.hotelnebula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Model.Menu;
import Model.ReservationAdapter;
import Model.Reservations;
import Model.UserAdapter;

public class UserReservationActivity extends AppCompatActivity {

    ListView lvReservation;
    ArrayList<Reservations> reservationsList;

    ReservationAdapter reservationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_reservation);
        try {
            //setContentView(R.layout.activity_search);
            Menu.setupBottomNavigationBar(this);
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        }
        initialize();
    }

    private void initialize() {
        /*
        reservationsList = new ArrayList<>();
        lvReservation = findViewById(R.id.lvReservations);
        this.checkReservation();
        reservationAdapter = new ReservationAdapter(this,reservationsList);
        lvReservation.setAdapter(reservationAdapter);
        */
        reservationsList = new ArrayList<>();
        lvReservation = findViewById(R.id.lvReservations);
        reservationAdapter = new ReservationAdapter(this, new ArrayList<>());
        lvReservation.setAdapter(reservationAdapter);
        checkReservation();
    }

    /*public void checkReservation() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reservations");
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        Query checkReservationData = reference.orderByChild("User").equalTo(username);

        checkReservationData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                        String userFromDB = reservationSnapshot.child("User").getValue(String.class);

                        if (userFromDB.equals(username)) {

                            int RoomNumber = reservationSnapshot.child("RoomNumber").getValue(Integer.class);
                            String CheckInDate = reservationSnapshot.child("CheckInDate").getValue(String.class);
                            String CheckOutDate = reservationSnapshot.child("CheckOutDate").getValue(String.class);

                            Reservations reservation = new Reservations(userFromDB, RoomNumber,CheckInDate,CheckOutDate);
                            reservationsList.add(reservation);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Log.e("ReservationAdapter", "Database Error: " + error.getMessage());
            }
        });
    }*/

    public void checkReservation() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Reservations");
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        Query checkReservationData = reference.orderByChild("User").equalTo(username);
        ArrayList<Reservations> tempReservationsList = new ArrayList<>();

        checkReservationData.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                        String userFromDB = reservationSnapshot.child("User").getValue(String.class);

                        if (userFromDB.equals(username)) {
                            int RoomNumber = reservationSnapshot.child("RoomNumber").getValue(Integer.class);
                            String CheckInDate = reservationSnapshot.child("CheckInDate").getValue(String.class);
                            String CheckOutDate = reservationSnapshot.child("CheckOutDate").getValue(String.class);

                            Reservations reservation = new Reservations(userFromDB, RoomNumber, CheckInDate, CheckOutDate);
                            tempReservationsList.add(reservation);
                        }
                    }

                    reservationAdapter.reloadData(tempReservationsList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("UserReservationActivity", "Database Error: " + error.getMessage());
            }
        });
    }
}