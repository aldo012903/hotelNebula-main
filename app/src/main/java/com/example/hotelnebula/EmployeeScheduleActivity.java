package com.example.hotelnebula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class EmployeeScheduleActivity extends AppCompatActivity {

    TextView tvMonday, tvTuesday, tvWednesday, tvThursday, tvFriday, tvSaturday, tvSunday;
    String username;
    DatabaseReference workersRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_schedule);
        initialize();
    }

    private void initialize() {
        tvMonday = findViewById(R.id.tvMonday);
        tvTuesday = findViewById(R.id.tvTuesday);
        tvWednesday = findViewById(R.id.tvWednesday);
        tvThursday = findViewById(R.id.tvThursday);
        tvFriday = findViewById(R.id.tvFriday);
        tvSaturday = findViewById(R.id.tvSaturday);
        tvSunday = findViewById(R.id.tvSunday);

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        username = preferences.getString("username", "No UserAvailable");

        workersRef = FirebaseDatabase.getInstance().getReference("employees");
        Query query = workersRef.child(username);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                DataSnapshot schedule = snapshot.child("schedule");

                String test = schedule.child("monday").getValue(String.class).toString();
                tvMonday.setText(schedule.child("monday").getValue(String.class).toString());
                tvTuesday.setText(schedule.child("tuesday").getValue(String.class).toString());
                tvWednesday.setText(schedule.child("wednesday").getValue(String.class).toString());
                tvThursday.setText(schedule.child("thursday").getValue(String.class).toString());
                tvFriday.setText(schedule.child("friday").getValue(String.class).toString());
                tvSaturday.setText(schedule.child("saturday").getValue(String.class).toString());
                tvSunday.setText(schedule.child("sunday").getValue(String.class).toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}