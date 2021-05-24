package com.opsc7311.mapple;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Testing");
        System.out.println("Testing3");
        System.out.println("Testing4");
        setContentView(R.layout.activity_main);
    }
}