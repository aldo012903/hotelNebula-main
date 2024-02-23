package com.example.hotelnebula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import Model.Reservations;

public class SearchResults extends AppCompatActivity implements View.OnClickListener{

    ImageButton btnBack;
    Button btnReserve;
    TextView tvDescription;
    ImageView ivRoom;
    String roomTypeTitle,roomId;
    final boolean[] roomFound = {false};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        initilize();
    }

    private void initilize() {
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        btnReserve = findViewById(R.id.btnReserve);
        btnReserve.setOnClickListener(this);
        tvDescription = findViewById(R.id.tvDescription);
        ivRoom = findViewById(R.id.ivRoom);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("RoomType") ) {
            String roomType = intent.getStringExtra("RoomType");
            int resID = getResources().getIdentifier(roomType, "drawable", getPackageName());
            ivRoom.setImageResource(resID);
            roomTypeTitle = intent.getStringExtra("RoomTypeTitle");
            tvDescription.setText("Room reserved!, look into your reservations.");
            findRoom(intent);
        }
    }

    private void findRoom(Intent intent) {
        String arrivalDate = intent.getStringExtra("ArrivalDate");
        String departureDate = intent.getStringExtra("DepartureDate");
        String roomTypeTitle = intent.getStringExtra("RoomType");

        DatabaseReference roomsRef = FirebaseDatabase.getInstance().getReference("Rooms");
        Query checkReservationData = roomsRef.orderByChild("photo").equalTo(roomTypeTitle);

        final String[] foundRoomId = {""};
        checkReservationData.addValueEventListener(new ValueEventListener()  {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                            roomId = roomSnapshot.getKey();

                            DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference("Reservations");
//                            Query checkReservationsRoom = reservationsRef.orderByChild("RoomNumber").equalTo(roomId);
                            reservationsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            boolean roomAvailable = true;
                                            for (DataSnapshot reservationSnapshot : snapshot.getChildren()) {
                                                String reservationArrivalDate = reservationSnapshot.child("CheckInDate").getValue(String.class);
                                                String reservationDepartureDate = reservationSnapshot.child("CheckOutDate").getValue(String.class);

                                                if ((arrivalDate.compareTo(reservationDepartureDate) < 0 && departureDate.compareTo(reservationArrivalDate) > 0)) {
                                                    roomAvailable = false;
                                                    break;
                                                }
                                            }

                                            if (roomAvailable) {
                                                roomFound[0] = true;
                                                foundRoomId[0] = roomId;
                                                tvDescription.setText(roomId);
                                                btnReserve.setVisibility(View.VISIBLE);
                                                return;
                                            }
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                        }
                                    });
                            if (roomFound[0]) {
                                break;
                            }
                        }
                        if (roomFound[0]) {
                            finish();
                        } else {
                            tvDescription.setText("No rooms found with those parameters.");
                            btnReserve.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if(id == R.id.btnBack){
            finish();
        }
        if(id == R.id.btnReserve){
            reserveRoom();
        }
    }

    private void reserveRoom() {
        String arrivalDate = getIntent().getStringExtra("ArrivalDate");
        String departureDate = getIntent().getStringExtra("DepartureDate");
        DatabaseReference reservationsRef = FirebaseDatabase.getInstance().getReference("Reservations");

        if (roomFound[0]) {
            String roomNumber = roomId;
            if (!roomNumber.isEmpty()) {
                SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");

                Map<String, Object> reservationData = new HashMap<>();
                reservationData.put("User", username);
                reservationData.put("RoomNumber", Integer.parseInt(roomNumber));
                reservationData.put("CheckInDate", arrivalDate);
                reservationData.put("CheckOutDate", departureDate);

                reservationsRef.push().setValue(reservationData)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Room reserved successfully!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(this, "Failed to reserve the room. Please try again.", Toast.LENGTH_SHORT).show();
                            }
                        });
            } else {
                Toast.makeText(SearchResults.this, "Room number is empty!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(SearchResults.this, "No available room found to reserve.", Toast.LENGTH_SHORT).show();
        }
    }
}