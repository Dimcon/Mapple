package com.opsc7311.mapple;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opsc7311.mapple.auth.data.LoginRepository;
import com.opsc7311.mapple.auth.data.model.LoggedInUser;

public class SettingsActivity extends AppCompatActivity {
    private TextView Settings;
    private TextView Measurements;
    private ListView Measurement;
    private TextView Plt;
    private ListView Pltlist;
    private Switch Location;
    private Button Save;

    ListView simpleList;
    String countryList1[] = {"Kilometers", "Miles"};
    String countryList2[] = {"Entertainment", "Nature", "Food", "Historical"};

    FirebaseDatabase rootNode;
    DatabaseReference reference;


    LoggedInUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        user = LoginRepository.getInstance().isLoggedIn();

        CheckBox imperial = findViewById(R.id.cbxImperial);
        CheckBox metric = findViewById(R.id.cbxMetric);

        imperial.setChecked(!user.isMetric());
        metric.setChecked(user.isMetric());

        imperial.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                metric.setChecked(!imperial.isChecked());
                user.isMetric = !imperial.isChecked();
                user.save();
            }
        });

        metric.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                imperial.setChecked(!metric.isChecked());
                user.isMetric = !metric.isChecked();
                user.save();
            }
        });

    }
}