package com.example.hotelnebula;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Model.User;
import Model.UserDatabaseModel;

public class UserInfoActivity extends AppCompatActivity {

    User userToUpdate;

    TextView lbTitle, lbPassword, lbUsername;
    EditText txtEmail, txtName, txtRole, txtPassword, txtUsername;
    Button btnAction;
    DatabaseReference userDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initialize();
    }

    private void initialize() {
        lbTitle = findViewById(R.id.lbTitle);
        lbPassword = findViewById(R.id.lbPassword);
        lbUsername = findViewById(R.id.lbUsername);

        txtEmail = findViewById(R.id.txtEmail);
        txtName = findViewById(R.id.txtName);
        txtRole = findViewById(R.id.txtRole);
        txtPassword = findViewById(R.id.txtPassword);
        txtUsername = findViewById(R.id.txtUsername);

        btnAction = findViewById(R.id.btnAction);

        userDatabase = FirebaseDatabase.getInstance().getReference("users");

        Intent intent = getIntent();

        if (intent.hasExtra("userToUpdate")) {
            userToUpdate = (User) intent.getExtras().getSerializable("userToUpdate");
            lbTitle.setText(userToUpdate.getUsername());

            txtEmail.setText(userToUpdate.getEmail());
            txtName.setText(userToUpdate.getName());
            txtRole.setText(userToUpdate.getRole());

            lbPassword.setVisibility(View.INVISIBLE);
            txtPassword.setVisibility(View.INVISIBLE);
            lbUsername.setVisibility(View.INVISIBLE);
            txtUsername.setVisibility(View.INVISIBLE);
            btnAction.setText("Update");

            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {

                        if (txtEmail.getText().toString().trim().isEmpty() || txtRole.getText().toString().trim().isEmpty() || txtName.getText().toString().trim().isEmpty()) {
                            Snackbar.make(v, "Please fill all the fields", Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
                        Matcher matcher = pattern.matcher(txtEmail.getText().toString().trim());

                        if (!matcher.find()) {
                            Snackbar.make(v, "Please insert a valid email", Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        userToUpdate.setEmail(txtEmail.getText().toString().trim());
                        userToUpdate.setName(txtName.getText().toString().trim());
                        userToUpdate.setRole(txtRole.getText().toString().trim());

                        UserDatabaseModel person = new UserDatabaseModel(userToUpdate.getEmail(), userToUpdate.getName(), userToUpdate.getPassword(), userToUpdate.getRole());
                        userDatabase.child(userToUpdate.getUsername()).setValue(person);
                        finish();
                    }
                    catch (Exception e ) {
                        Snackbar.make(v, e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                }
            });

        }
        else {
            btnAction.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        String fullName = txtName.getText().toString();
                        String username = txtUsername.getText().toString();
                        String email = txtEmail.getText().toString();
                        String password = txtPassword.getText().toString();

                        if (fullName.isEmpty() || username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                            Snackbar.make(v, "Please fill all the fields", Snackbar.LENGTH_LONG).show();
                            return;
                        }
                        Pattern pattern = Pattern.compile("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
                        Matcher matcher = pattern.matcher(email);

                        if (!matcher.find()) {
                            Snackbar.make(v, "Please insert a valid email", Snackbar.LENGTH_LONG).show();
                            return;
                        }

                        DatabaseReference usernameRef = userDatabase.child(username);

                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (!dataSnapshot.exists()) {
                                    UserDatabaseModel user = new UserDatabaseModel(email, fullName, password, "Customer");
                                    userDatabase.child(username).setValue(user);
                                    Snackbar.make(v, "The user with username: " + user + " has been added successfully", Snackbar.LENGTH_LONG).show();
                                    finish();
                                } else {
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
    }
}