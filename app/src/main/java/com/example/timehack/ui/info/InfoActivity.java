package com.example.timehack.ui.info;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timehack.R;

//activity for the info page setting the on create showing the fragment_info view
public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_info);
    }
}