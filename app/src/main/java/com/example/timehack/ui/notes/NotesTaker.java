package com.example.timehack.ui.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timehack.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NotesTaker extends AppCompatActivity {

    EditText editTitle, editBody;
    ImageView saveImage, shareImage, backButton;
    NotesActivity notes;


    //for notes in two instances
    boolean alreadyNotes = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);


        saveImage = findViewById(R.id.saveImage);
        editTitle = findViewById(R.id.editTitle);
        editBody = findViewById(R.id.editBody);
        shareImage = findViewById(R.id.shareImage);
        backButton = findViewById(R.id.backImage);


        notes = new NotesActivity();
        try {
            //find the old previous made note
            notes = (NotesActivity) getIntent().getSerializableExtra("old_note");
            //get values from old note
            editTitle.setText(notes.getTitle());
            editBody.setText(notes.getBody());

            alreadyNotes = true;
        } catch (Exception e) {
            e.printStackTrace();
        }


        //when adding note make sure the body and title fields are filled in when clicking on save
        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = editTitle.getText().toString();
                String body = editBody.getText().toString();

                if (title.isEmpty()) {
                    Toast.makeText(NotesTaker.this, "Enter Title before finishing.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (body.isEmpty()) {
                    Toast.makeText(NotesTaker.this, "Enter notes before finishing.", Toast.LENGTH_SHORT).show();
                    return;
                }
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yy HH:mm");
                @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter_created = new SimpleDateFormat("MM/dd/yy HH:mm");
                Date date = new Date();

                if (!alreadyNotes) {
                    //initialize notes
                    notes = new NotesActivity();
                }


                notes.setTitle(title);
                notes.setBody(body);
                notes.setLastModified(date);
                notes.setCreation_date("Created: " + formatter_created.format(date));
                notes.setDate("Last Modified: " + formatter.format(date));


                Intent intent = new Intent();
                intent.putExtra("note", notes);
                setResult(Activity.RESULT_OK, intent);

                //finish activity
                finish();
            }
        });

        //on click for the share button, pulling up an intent and passing the title and body into the intent
        shareImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String title = editTitle.getText().toString();
                String body = editBody.getText().toString();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "* Note from TimeHack Application * \nTitle: " + title + "\n" + body);
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);

            }
        });

        //set back button to work in note editor/viewer screen
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


    }
}