package Model;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.hotelnebula.ProfileActivity;
import com.example.hotelnebula.R;
import com.example.hotelnebula.SearchActivity;
import com.example.hotelnebula.UserReservationActivity;

import androidx.appcompat.app.AppCompatActivity;

public class Menu {
    public static void setupBottomNavigationBar(Activity activity) {
        Button btnHome = activity.findViewById(R.id.btn_home);
        Button btnReservations = activity.findViewById(R.id.btn_reservations);
        Button btnProfile = activity.findViewById(R.id.btn_profile);
/*
        btnHome.setOnClickListener(v -> navigateToHome());
        btnReservations.setOnClickListener(v -> navigateToReservations());
        btnProfile.setOnClickListener(v -> navigateToProfile());*/
        btnHome.setOnClickListener(v -> navigateTo(activity, SearchActivity.class));
        btnReservations.setOnClickListener(v -> navigateTo(activity, UserReservationActivity.class));
        btnProfile.setOnClickListener(v -> navigateTo(activity, ProfileActivity.class));
    }
    /*
        private void navigateToHome() {
            Intent intent = new Intent(this, SearchActivity.class);
            startActivity(intent);
        }

        private void navigateToReservations() {
            // Implementa la navegación a Reservations
        }

        private void navigateToProfile() {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        }*/
    private static void navigateTo(Activity currentActivity, Class<?> targetActivityClass) {
        if (currentActivity.getClass() != targetActivityClass) {
            try {
                Intent intent = new Intent(currentActivity, targetActivityClass);
                currentActivity.startActivity(intent);
            }catch(Exception e){
                Toast.makeText(currentActivity, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("NavigationError", "Error navigating to " + targetActivityClass.getSimpleName(), e);
            }
        }
    }
}
