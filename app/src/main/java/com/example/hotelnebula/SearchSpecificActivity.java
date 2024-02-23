package com.example.hotelnebula;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public class SearchSpecificActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView selectedValueTextView;
    SeekBar optionsSeekBar;
    ImageView banner;
    TextView roomSubtitle;
    Button arrivalDateButton, departureDateButton, btnSearch;
    ImageButton btnBack;
    String roomType, imageResource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_specific);

        initialize();

    }
    private void initialize() {
        optionsSeekBar = findViewById(R.id.optionsSeekBar);
        selectedValueTextView = findViewById(R.id.selectedValueTextView);
        arrivalDateButton = findViewById(R.id.arrivalDateButton);
        departureDateButton = findViewById(R.id.departureDateButton);
        btnSearch = findViewById(R.id.btnSearch);
        banner = findViewById(R.id.imageView);
        btnBack = findViewById(R.id.btnBack);
        roomSubtitle = findViewById(R.id.roomSubtitle);


        optionsSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int selectedValue = progress + 1;
                selectedValueTextView.setText("Selected: " + selectedValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        arrivalDateButton.setOnClickListener(this);
        departureDateButton.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        btnBack.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("RoomType") && intent.hasExtra("ImageResource")) {
            roomType = intent.getStringExtra("RoomType");
            imageResource = intent.getStringExtra("ImageResource");
            int resID = getResources().getIdentifier(imageResource, "drawable", getPackageName());
            banner.setImageResource(resID);
            roomSubtitle.setText(roomType);
        }
    }

    private void showDatePicker(final Button dateButton) {
        MaterialDatePicker<Long> datePicker = MaterialDatePicker.Builder.datePicker().build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

                String selectedDate = sdf.format(new Date(selection));
                dateButton.setText(selectedDate);

            }
        });

        datePicker.show(getSupportFragmentManager(), datePicker.toString());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnSearch){
            searchRoom();
        }
        if(id == R.id.btnBack){
            finish();
        }
        if(id == R.id.departureDateButton){
            showDatePicker(departureDateButton);
        }
        if(id == R.id.arrivalDateButton){
            showDatePicker(arrivalDateButton);
        }
    }

    private void searchRoom() {
        String arrivalDateString = arrivalDateButton.getText().toString();
        String departureDateString = departureDateButton.getText().toString();
        int selectedPersons = optionsSeekBar.getProgress() + 1;

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        Date currentDate = new Date();

        try {
            Date arrivalDate = sdf.parse(arrivalDateString);
            Date departureDate = sdf.parse(departureDateString);

            if (arrivalDate.before(currentDate)) {
                Toast.makeText(this, "Arrival date cannot be before the current date.", Toast.LENGTH_LONG).show();
            } else if (arrivalDate.after(departureDate) || arrivalDate.equals(departureDate)) {
                Toast.makeText(this, "Arrival date cannot be after or the same as departure date.", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(this, SearchResults.class);
                intent.putExtra("RoomTypeTitle", roomType);
                intent.putExtra("ArrivalDate", arrivalDateString);
                intent.putExtra("DepartureDate", departureDateString);
                intent.putExtra("NumberOfPersons", selectedPersons);
                intent.putExtra("RoomType", imageResource);

                startActivity(intent);
            }
        } catch (ParseException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

}
