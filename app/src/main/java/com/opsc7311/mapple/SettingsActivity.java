package com.opsc7311.mapple;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.opsc7311.mapple.auth.data.LoginRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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

        LoginRepository.getInstance().isLoggedIn(user -> {
            CheckBox imperial = findViewById(R.id.cbxImperial);
            CheckBox metric = findViewById(R.id.cbxMetric);

            imperial.setChecked(!user.isMetric());
            metric.setChecked(user.isMetric());

            imperial.setOnClickListener(v -> {
                metric.setChecked(false);
                user.isMetric = false;
                user.save();
            });

            metric.setOnClickListener(v -> {
                imperial.setChecked(false);
                user.isMetric = true;
                user.save();
            });

            final ListView listview = (ListView) findViewById(R.id.listview);

            final ArrayList<String> list = new ArrayList<String>(user.favourites);
            final FavouritesArrayAdapter adapter = new FavouritesArrayAdapter(this,
                    android.R.layout.simple_list_item_1, list);
            listview.setAdapter(adapter);

            listview.setOnItemClickListener((parent, view, position, id) -> {
                final String item = (String) parent.getItemAtPosition(position);
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(() -> {
                            list.remove(item);
                            adapter.notifyDataSetChanged();
                            view.setAlpha(1);
                        });
            });
        });



    }

    private class FavouritesArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public FavouritesArrayAdapter(Context context, int textViewResourceId,
                                      List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            Integer integer = mIdMap.get(item);
            return integer;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}