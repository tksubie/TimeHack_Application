package com.example.timehack.ui.clock;

import static com.example.timehack.R.id;

import android.os.Bundle;
import android.widget.TextClock;

import androidx.appcompat.app.AppCompatActivity;

//class to implement the text clocks for the clock fragment

public class ClockActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //USE TEXT CLOCK WIDGET FOR TIME
        // create variable for ZuluTime for TextClock
        TextClock clock = findViewById(id.zulu_time);

        //current time variable
        TextClock current = findViewById(id.cdateTime);

        // on below line we are setting the format
        // format for clocks
        clock.getFormat24Hour();
        current.getFormat24Hour();

    }

}

