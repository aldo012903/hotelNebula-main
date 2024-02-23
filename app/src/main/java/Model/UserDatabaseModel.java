package Model;

import androidx.annotation.NonNull;

public class UserDatabaseModel {

    private String email;
    private String name;
    private String password;
    private String role;

    public UserDatabaseModel( String email, String name, String password, String role) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
