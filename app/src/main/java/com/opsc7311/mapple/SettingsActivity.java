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

        Settings = (TextView) findViewById(R.id.settings);
        Measurements = (TextView) findViewById(R.id.measurements);
        Measurement = (ListView) findViewById(R.id.measurement);
        Plt = (TextView) findViewById(R.id.plt);
        Pltlist = (ListView) findViewById(R.id.pltlist);
        Location = (Switch) findViewById(R.id.location);
        Save = (Button) findViewById(R.id.savesets);

        simpleList = (ListView)findViewById(R.id.measurement);
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, R.layout.activity_settings, R.id.measurements, countryList2);
        simpleList.setAdapter(arrayAdapter1);


        simpleList = (ListView)findViewById(R.id.pltlist);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this, R.layout.activity_settings, R.id.plt, countryList2);
        simpleList.setAdapter(arrayAdapter2);

        Measurement.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }

            public void onClick(View View) {
                reference.setValue("");
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {
                reference.setValue("");
            }
        });


    }
}