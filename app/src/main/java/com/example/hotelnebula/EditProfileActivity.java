package com.example.hotelnebula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity implements  View.OnClickListener{

    EditText editname,editemail,editpass;
    Button savebtn,profilebtn;
    TextView editusername;
    DatabaseReference reference;

    String nameuser,emailuser,usernameuser,passuser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initialize();
    }

    private void initialize(){
        editname = findViewById(R.id.editname);
        editemail = findViewById(R.id.editemail);
        editpass =  findViewById(R.id.editpassword);
        profilebtn=  findViewById(R.id.profile);
        savebtn = findViewById(R.id.save);

        savebtn.setOnClickListener(this);
        profilebtn.setOnClickListener(this);
        editusername = findViewById(R.id.editusername);

        reference = FirebaseDatabase.getInstance().getReference("users");

        showAllData();
    }
    @Override
    public void onClick(View v) {

        int id = v.getId();
        if (id == R.id.profile)
            goToProfile();
        if (id == R.id.save)
            save();

    }

    private void goToProfile() {
        Intent intent =new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    private void save() {
        if(isNameChanged()){
            Toast.makeText(EditProfileActivity.this, "named changed", Toast.LENGTH_SHORT).show();
        }
        if (ispassChanged()){
            Toast.makeText(EditProfileActivity.this, "pass changed", Toast.LENGTH_SHORT).show();
        }
        if (isemailChanged()){
            Toast.makeText(EditProfileActivity.this, "email changed", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(EditProfileActivity.this, "No change", Toast.LENGTH_SHORT).show();
        }
    }
    private boolean isNameChanged(){
        if(!nameuser.equals(editname.getText().toString())){
            reference.child(usernameuser).child("name").setValue(editname.getText().toString());
            return true;
        }else{
            return false;
        }
    }


    private boolean isemailChanged(){
        if(!emailuser.equals(editemail.getText().toString())){
            reference.child(usernameuser).child("email").setValue(editemail.getText().toString());
            return true;
        }else{
            return false;
        }
    }

    private boolean ispassChanged(){
        if(!passuser.equals(editpass.getText().toString())){
            reference.child(usernameuser).child("password").setValue(editpass.getText().toString());
            return true;
        }else{
            return false;
        }
    }


    public void showAllData() {
        SharedPreferences preferences = getSharedPreferences("user_prefs", MODE_PRIVATE);
        usernameuser = preferences.getString("username", "No Username Available");

        editusername.setText(usernameuser);

        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users").child(usernameuser);

        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    nameuser = snapshot.child("name").getValue(String.class);
                    emailuser = snapshot.child("email").getValue(String.class);
                    passuser = snapshot.child("password").getValue(String.class);

                    editname.setText(nameuser);
                    editemail.setText(emailuser);
                    editpass.setText(passuser);
                } else {
                    Toast.makeText(EditProfileActivity.this, "User not found in Firebase", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(EditProfileActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}