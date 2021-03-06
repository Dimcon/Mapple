package com.opsc7311.mapple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.geojson.Feature;
import com.opsc7311.mapple.auth.data.LoginRepository;
import com.opsc7311.mapple.auth.data.model.LoggedInUser;
import com.opsc7311.mapple.main.NavigationActivity;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;


public class PointOfInterest extends AppCompatActivity {
    //variables

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_interest);

        Bundle configs = getIntent().getExtras();

        Feature feature = Feature.fromJson((String)configs.get("feature"));

        double currentLong = configs.getDouble("currentLong");
        double currentLatt = configs.getDouble("currentLatt");
        double destLong = configs.getDouble("destLong");
        double destLatt = configs.getDouble("destLatt");

        TextView poi_heading = findViewById(R.id.poi_heading) ;
        TextView poi_body = findViewById(R.id.poi_description) ;
        Switch swi_favourite = findViewById(R.id.swi_favourite);
        TextView txtComment = findViewById(R.id.txt_comment) ;
        Button navBtn = findViewById(R.id.navigate_button);

        navBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNavigation  = new Intent(
                        PointOfInterest.this,
                        NavigationActivity.class
                );
                startNavigation.putExtra("currentLong", currentLong);
                startNavigation.putExtra("currentLatt", currentLatt);
                startNavigation.putExtra("destLong", destLong);
                startNavigation.putExtra("destLatt", destLatt);
                startActivity(startNavigation);
            }
        });

        poi_heading.setText(feature.getStringProperty("name"));
        poi_body.setText(feature.getStringProperty("category"));

        LoginRepository.getInstance().isLoggedIn(user -> {
            swi_favourite.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    if (!user.favourites.contains((feature.getStringProperty("name")))) {
                        user.favourites.add(feature.getStringProperty("name"));
                        user.save();
                    }
                } else {
                    user.favourites.remove(feature.getStringProperty("name"));
                    user.save();
                }
            });
            if (user.favourites.contains((feature.getStringProperty("name")))) {
                swi_favourite.setChecked(true);
            }
        });






    }
}