package com.opsc7311.mapple.main.mymapbox;

import android.content.Context;
import android.os.AsyncTask;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.Point;
import com.mapbox.turf.TurfMeasurement;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoadGeoJsonDataTask extends AsyncTask<Void, Void, FeatureCollection> {
    /**
     * AsyncTask to load data from the assets folder.
     */
        private final WeakReference<MapFeatures> activityRef;
        FeatureCollection ourFeatures;

        LoadGeoJsonDataTask(MapFeatures activity) {
            this.activityRef = new WeakReference<MapFeatures>(activity);
        }

        @Override
        protected FeatureCollection doInBackground(Void... params) {
            MapFeatures activity = activityRef.get();

            if (activity == null) {
                return null;
            }

            String geoJson = LoadGeoJsonDataTask.loadGeoJsonFromAsset(activity.mainActivity, "johannesburg-geo.json");
//            String geoJson = "{}";
            List<Feature> featureList = new ArrayList<Feature>();
//            if (activity.mainActivity.lastLocation != null) {
//                String request = "https://api.mapbox.com/v4/mapbox.mapbox-streets-v8/tilequery/" +
//                                String.valueOf(activity.mainActivity.lastLocation.getLatitude()) +
//                                "," + String.valueOf(activity.mainActivity.lastLocation.getLongitude()) +
//                                ".json?" +
//                                "radius=100&limit=50&dedupe&geometry=point" +
//                                "&layers=poi_label" +
//                                "&access_token=sk.eyJ1IjoiZGFpbW9uc2V3ZWxsIiwiYSI6ImNrcGxvMXd5ZjI3OXUycHJpZmxzMnM2azUifQ.4IwU_a5LsBQbR0uwU0m9EA";
//                geoJson = httpCall(request);
//            }

            if (activity.mainActivity.lastLocation != null) {
                FeatureCollection features = FeatureCollection.fromJson(geoJson);
                for (Feature singleFeature : features.features()) {
                    if (singleFeature.hasProperty("name")) {
                        double dist = TurfMeasurement.distance(
                                (Point) singleFeature.geometry(),
                                Point.fromLngLat(
                                        activity.mainActivity.lastLocation.getLongitude(),
                                        activity.mainActivity.lastLocation.getLatitude()
                                )
                        );
                        if (dist < 20 ) {
                            featureList.add(singleFeature);
                        }
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
            MapFeatures activity = activityRef.get();
            if (featureCollection == null || activity == null) {
                return;
            }
            boolean shouldFilter = false;
            List<Feature> newFeatures = new ArrayList<>();
            FeatureCollection newFeatureCollection;
            if (ourFeatures == null) {
                ourFeatures = featureCollection;
                newFeatureCollection = featureCollection;
                for (Feature singleFeature : newFeatureCollection.features()) {
                    singleFeature.addBooleanProperty(MapFeatures.PROPERTY_SELECTED, false);
                }
            } else {
                for (Feature singleFeature : featureCollection.features()) {
                    boolean found = false;
                    for (Feature ourFeature : ourFeatures.features()) {
                        if (ourFeature.getStringProperty("name")
                                .equals(singleFeature.getStringProperty("name"))) {
                            found = true;
                        }
                    }
                    if (!found) {
                        singleFeature.addBooleanProperty(MapFeatures.PROPERTY_SELECTED, false);
                        newFeatures.add(singleFeature);
                    }
                }
                newFeatureCollection = FeatureCollection.fromFeatures(newFeatures);
            }
            // This example runs on the premise that each GeoJSON Feature has a "selected" property,
            // with a boolean value. If your data's Features don't have this boolean property,
            // add it to the FeatureCollection 's features with the following code:
//            for (Feature singleFeature : featureCollection.features()) {
//            }

            if (newFeatureCollection.features().size() == 0) {
                return;
            }
            activity.setUpData(newFeatureCollection);
            new GenerateViewIconTask(activity, true).execute(newFeatureCollection);
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