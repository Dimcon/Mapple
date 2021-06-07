package com.opsc7311.mapple;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

//        Settings = (TextView) findViewById(R.id.settings);
//        Measurements = (TextView) findViewById(R.id.measurements);
//        Plt = (TextView) findViewById(R.id.plt);
//        Location = (Switch) findViewById(R.id.location);
//        Save = (Button) findViewById(R.id.savesets);


//        Measurement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            }
//
//            public void onClick(View View) {
//                reference.setValue("");
//            }
//        });
//
//        Save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View View) {
//                reference.setValue("");
//            }
//        });


    }
}