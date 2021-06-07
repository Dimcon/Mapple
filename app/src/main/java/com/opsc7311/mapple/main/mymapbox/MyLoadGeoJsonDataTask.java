package com.opsc7311.mapple.main.mymapbox;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.tasks.OnCompleteListener;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MyLoadGeoJsonDataTask {
    /**
     * AsyncTask to load data from the assets folder.
     */
    public static class LoadGeoJsonDataTask extends AsyncTask<Void, Void, FeatureCollection> {

        private final WeakReference<MainActivityMapFeatures> activityRef;

        LoadGeoJsonDataTask(MainActivityMapFeatures activity) {
            this.activityRef = new WeakReference<MainActivityMapFeatures>(activity);
        }

        @Override
        protected FeatureCollection doInBackground(Void... params) {
            MainActivityMapFeatures activity = activityRef.get();

            if (activity == null) {
                return null;
            }

//            String geoJson = LoadGeoJsonDataTask.loadGeoJsonFromAsset(activity.mainActivity, "johannesburg-geo.json");
            String geoJson = "{}";
            List<Feature> featureList = new ArrayList<Feature>();
            if (activity.mainActivity.lastLocation != null) {
                String request = "https://api.mapbox.com/v4/mapbox.mapbox-streets-v8/tilequery/" +
                                String.valueOf(activity.mainActivity.lastLocation.getLatitude()) +
                                "," + String.valueOf(activity.mainActivity.lastLocation.getLongitude()) +
                                ".json?" +
                                "radius=100&limit=50&dedupe&geometry=point" +
                                "&layers=poi_label" +
                                "&access_token=sk.eyJ1IjoiZGFpbW9uc2V3ZWxsIiwiYSI6ImNrcGxvMXd5ZjI3OXUycHJpZmxzMnM2azUifQ.4IwU_a5LsBQbR0uwU0m9EA";
                geoJson = httpCall(request);
                FeatureCollection features = FeatureCollection.fromJson(geoJson);
                for (Feature singleFeature : features.features()) {
                    if (singleFeature.hasProperty("name")) {
                        featureList.add(singleFeature);
                    }
                }
            }
            FeatureCollection finalFeats = FeatureCollection.fromFeatures(featureList);
            return finalFeats;
        }

        public void getFromMapbox() {

        }


        public String httpCall(String url) {
            OkHttpClient client = new OkHttpClient();
            Request request = new okhttp3.Request.Builder().url(url).build();
            try {
                Response response = client.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                return "{}";
            }
        }

        @Override
        protected void onPostExecute(FeatureCollection featureCollection) {
            super.onPostExecute(featureCollection);
            MainActivityMapFeatures activity = activityRef.get();
            if (featureCollection == null || activity == null) {
                return;
            }

            // This example runs on the premise that each GeoJSON Feature has a "selected" property,
            // with a boolean value. If your data's Features don't have this boolean property,
            // add it to the FeatureCollection 's features with the following code:
            for (Feature singleFeature : featureCollection.features()) {
                singleFeature.addBooleanProperty(MainActivityMapFeatures.PROPERTY_SELECTED, false);
            }

            activity.setUpData(featureCollection);
            new MyGenerateViewIconTask.GenerateViewIconTask(activity, true).execute(featureCollection);
        }

        static String loadGeoJsonFromAsset(Context context, String filename) {
            try {
                // Load GeoJSON file from local asset folder
                InputStream is = context.getAssets().open(filename);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                return new String(buffer, Charset.forName("UTF-8"));
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }
    }
}