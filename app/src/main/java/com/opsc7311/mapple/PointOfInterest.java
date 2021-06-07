package com.opsc7311.mapple;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class PointOfInterest extends AppCompatActivity {
    //variables
    private TextView POI;
    private SearchView Navigate;
    private Switch Favourites;
    private EditText Comments;
    private Button Save;


    FirebaseDatabase rootNode;
    DatabaseReference reference;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_point_of_interest);

        // assigning the variables to the ids
        POI = (TextView) findViewById(R.id.poi);
        Navigate = (SearchView) findViewById(R.id.navigate);
        Favourites = (Switch) findViewById(R.id.favourites);
        Comments = (EditText) findViewById(R.id.comments);
        Save = (Button) findViewById(R.id.savepoi);


        Favourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {
                rootNode = FirebaseDatabase.getInstance("Favourites");
                reference = rootNode.getReference();

                reference.setValue("");
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View View) {
                reference.setValue("");
            }
        });


        //Code from https://developer.android.com/guide/topics/search/search-dialog#java
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            doMySearch(query);
        }

    }

    private void doMySearch(String query) {
    }
}