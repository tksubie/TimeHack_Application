package com.example.timehack.ui.notes.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;

import com.example.timehack.ui.notes.NotesActivity;

@Database(entities = NotesActivity.class, version = 1, exportSchema = false)
public abstract class RoomDatabase extends androidx.room.RoomDatabase {

    private static RoomDatabase roomDatabase;
    //Declare database name
    private static String DATABASE_NAME = "TimeHackNotes";

    public synchronized static RoomDatabase getInstance(Context context){
        //check if null or not
        if(roomDatabase == null){
            roomDatabase = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDatabase.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        }
        return roomDatabase;
    }

    //create instance of the NotesActivity
    public abstract NotesDataInterface notesDataInterface();

}
