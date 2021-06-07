package com.opsc7311.mapple.main.mymapbox;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.annotations.BubbleLayout;
import com.mapbox.navigation.ui.internal.NavigationContract;
import com.opsc7311.mapple.PointOfInterest;
import com.opsc7311.mapple.R;
import com.opsc7311.mapple.main.MainActivity;

import java.lang.ref.WeakReference;
import java.util.HashMap;

import static androidx.core.content.ContextCompat.startActivity;

public class MyGenerateViewIconTask {
    /**
     * AsyncTask to generate Bitmap from Views to be used as iconImage in a SymbolLayer.
     * <p>
     * Call be optionally be called to update the underlying data source after execution.
     * </p>
     * <p>
     * Generating Views on background thread since we are not going to be adding them to the view hierarchy.
     * </p>
     */
    public static class GenerateViewIconTask extends
            AsyncTask<FeatureCollection, Void, HashMap<String, Bitmap>> {

        private final HashMap<String, View> viewMap = new HashMap<String, View>();
        private final WeakReference<MainActivityMapFeatures> activityRef;
        private final boolean refreshSource;

         GenerateViewIconTask(MainActivityMapFeatures activity, boolean refreshSource) {
            this.activityRef = new WeakReference<MainActivityMapFeatures>(activity);
            this.refreshSource = refreshSource;
        }

        @SuppressWarnings("WrongThread")
        @Override
        protected HashMap<String, Bitmap> doInBackground(FeatureCollection... params) {
            MainActivityMapFeatures activity = activityRef.get();
            if (activity != null) {
                HashMap<String, Bitmap> imagesMap = new HashMap<String, Bitmap>();
                LayoutInflater inflater = LayoutInflater.from(activity.mainActivity);

                FeatureCollection featureCollection = params[0];

                for (Feature feature: featureCollection.features()) {
                    BubbleLayout bubbleLayout = (BubbleLayout)
                            inflater.inflate(R.layout.symbol_layer_info_window_layout_callout, null);

                    String name = feature.getStringProperty(MainActivityMapFeatures.PROPERTY_NAME);
                    TextView titleTextView = bubbleLayout.findViewById(R.id.info_window_title);
                    Button btnNavigate = bubbleLayout.findViewById(R.id.navigate_button);

                    btnNavigate.setOnClickListener(v -> {
                        Intent switchToMainActivity = new Intent(activity.mainActivity, PointOfInterest.class);
                        activity.mainActivity.startActivity(switchToMainActivity);
                    });

                    titleTextView.setText(name);

                    String style = feature.getStringProperty(MainActivityMapFeatures.PROPERTY_CAPITAL);
                    TextView descriptionTextView = bubbleLayout.findViewById(R.id.info_window_description);
                    descriptionTextView.setText(
                            String.format(activity.mainActivity.getString(R.string.capital), style));

                    int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    bubbleLayout.measure(measureSpec, measureSpec);

                    float measuredWidth = bubbleLayout.getMeasuredWidth();

                    bubbleLayout.setArrowPosition(measuredWidth / 2 - 5);

                    Bitmap bitmap = MyGenerateViewIconTask.SymbolGenerator.generate(bubbleLayout);
                    imagesMap.put(name, bitmap);
                    viewMap.put(name, bubbleLayout);
                }

                return imagesMap;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(HashMap<String, Bitmap> bitmapHashMap) {
            super.onPostExecute(bitmapHashMap);
            MainActivityMapFeatures activity = activityRef.get();
            if (activity != null && bitmapHashMap != null) {
                activity.setImageGenResults(bitmapHashMap);
                if (refreshSource) {
                    activity.refreshSource();
                }
            }
            Toast.makeText(activity.mainActivity, R.string.tap_on_marker_instruction, Toast.LENGTH_SHORT).show();
        }
    }

    private static class SymbolGenerator {

        /**
         * Generate a Bitmap from an Android SDK View.
         *
         * @param view the View to be drawn to a Bitmap
         * @return the generated bitmap
         */
        static Bitmap generate(@NonNull View view) {
            int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            view.measure(measureSpec, measureSpec);

            int measuredWidth = view.getMeasuredWidth();
            int measuredHeight = view.getMeasuredHeight();

            view.layout(0, 0, measuredWidth, measuredHeight);
            Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
            bitmap.eraseColor(Color.TRANSPARENT);
            Canvas canvas = new Canvas(bitmap);
            view.draw(canvas);
            return bitmap;
        }
    }
}