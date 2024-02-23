package com.example.hotelnebula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.User;
import Model.UserDatabaseModel;

public class RegisterActivity extends AppCompatActivity {

    TextView txtFullName, txtUsername, txtEmail, txtPassword, txtConfirmPassword;
    Button btnCreateAccount, btnGoTOLogIn;
    DatabaseReference usersDatabase;
    DatabaseReference usernameRef;

    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }

    private void initialize() {
        txtFullName = findViewById(R.id.edFullName);
        txtUsername = findViewById(R.id.edUsername);
        txtEmail = findViewById(R.id.edEmail);
        txtPassword = findViewById(R.id.edPassword);
        txtConfirmPassword = findViewById(R.id.edConfirmPassword);

        btnGoTOLogIn = findViewById(R.id.btnLogin);
        btnCreateAccount = findViewById(R.id.btnSingUp);

        usersDatabase = FirebaseDatabase.getInstance().getReference("users");

        btnGoTOLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        btnCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String fullName = txtFullName.getText().toString();
                    String username = txtUsername.getText().toString();
                    String email = txtEmail.getText().toString();
                    String password = txtPassword.getText().toString();
                    String confirmPassword = txtConfirmPassword.getText().toString();

                    if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                        Snackbar.make(v, "Please fill all the fields", Snackbar.LENGTH_LONG).show();
                        return;
                    }
                    Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
                    Matcher matcher = pattern.matcher(email);

                    if (!matcher.find()){
                        Snackbar.make(v, "Please insert a valid email", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    if (!password.equals(confirmPassword)) {
                        Snackbar.make(v, "Passwords doesn't match", Snackbar.LENGTH_LONG).show();
                        return;
                    }

                    DatabaseReference usernameRef = usersDatabase.child(username);

                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()) {
                                UserDatabaseModel user = new UserDatabaseModel(email,fullName,password,"Customer");
                                usersDatabase.child(username).setValue(user);
                                Snackbar.make(v, "The user with username: " + user + " has been added successfully", Snackbar.LENGTH_LONG).show();
                                clearWidgets();
                            }
                            else {
                                Snackbar.make(v, "The username " + username + " is already used", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.d("DATABASE ERROR", error.getMessage()); //
                        }
                    };
                    usernameRef.addListenerForSingleValueEvent(eventListener);


                } catch (Exception e) {
                    Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }
    private void goToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void clearWidgets() {
        txtFullName.setText("");
        txtUsername.setText("");
        txtEmail.setText("");
        txtPassword.setText("");
        txtConfirmPassword.setText("");
        txtFullName.requestFocus();
    }
}