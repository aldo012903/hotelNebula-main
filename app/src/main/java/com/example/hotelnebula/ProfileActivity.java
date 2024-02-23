package com.example.hotelnebula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import Model.Menu;

public class ProfileActivity extends AppCompatActivity implements  View.OnClickListener{

    TextView profilename,profileEmail,profilePass,profileusername;
    TextView titlename,titleusername;
    Button siginout,editprofile,deleteProfile, btnSchedule;
    String nameuser,emailuser,usernameuser,passuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        try {
            //setContentView(R.layout.activity_search);
            Menu.setupBottomNavigationBar(this);
        }catch(Exception e){
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        }
        initialize();
    }

    private void initialize(){
        profilename = findViewById(R.id.profilename);
        profileEmail = findViewById(R.id.profileemail);
        profilePass =  findViewById(R.id.profilepass);
        profileusername = findViewById(R.id.profileusername);
        titlename = findViewById(R.id.titlename);
        titleusername =findViewById(R.id.titleusername);
        deleteProfile = findViewById(R.id.delete);
        siginout = findViewById(R.id.siginout);
        editprofile = findViewById(R.id.editprofile);
        btnSchedule = findViewById(R.id.btnSchedule);

        editprofile.setOnClickListener(this);
        siginout.setOnClickListener(this);
        deleteProfile.setOnClickListener(this);
        showAllData();

        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String role = preferences.getString("role", "No UserAvailable");

        if (role.equals("Employee")) {
            btnSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this,EmployeeScheduleActivity.class);
                    startActivity(intent);
                }
            });
        } else if (role.equals("Admin")) {

            btnSchedule.setText("Check users");
            btnSchedule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ProfileActivity.this,UserListViewActivity.class);
                    startActivity(intent);
                }
            });

        } else {

            btnSchedule.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.editprofile)
            passUserData();
        if (id == R.id.siginout)
            singOut();
        if (id == R.id.delete)
            deleteProfile();
    }
    public void deleteProfile(){
        String usernameMain = profileusername.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        //Query checkuserdata = reference.orderByChild("username").equalTo(usernameMain);
        Query checkuserdata = reference.orderByKey().equalTo(usernameMain);

        checkuserdata.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    snapshot.child(usernameMain).getRef().removeValue();
                    Intent intent = new Intent(ProfileActivity.this,WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void singOut(){
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }
    /*public void showAllData() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        String name = preferences.getString("name", "Guest");
        String username = preferences.getString("username", "No Username Available");
        String email = preferences.getString("email", "No Email Available");
        String password = preferences.getString("password", "No Password Available");

        titlename.setText("Welcome " + name);
        titleusername.setText(username);
        profilename.setText(name);
        profileEmail.setText(email);
        profilePass.setText(password);
        profileusername.setText(username);
    }*/
    public void showAllData() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        usernameuser = preferences.getString("username", "No Username Available");


        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(usernameuser);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nameuser = snapshot.child("name").getValue(String.class);
                    emailuser = snapshot.child("email").getValue(String.class);
                    passuser = snapshot.child("password").getValue(String.class);

                    titlename.setText("Welcome " + nameuser);
                    titleusername.setText(usernameuser);

                    profilename.setText(nameuser);
                    profileEmail.setText(emailuser);
                    profileusername.setText(usernameuser);
                    profilePass.setText(passuser);
                } else {
                    Toast.makeText(ProfileActivity.this, "User not found in Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void passUserData(){
        Intent intent = new Intent(this,EditProfileActivity.class);
        startActivity(intent);
        finish();
    }


}