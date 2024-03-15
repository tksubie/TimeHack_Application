package com.example.timehack.ui.notes.Database;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.timehack.ui.notes.NotesActivity;

import java.util.Date;
import java.util.List;

//data interface for the database that has stored queries for certain functions
@Dao
public interface NotesDataInterface {

    //Insert method for creating a note
    @Insert(onConflict = REPLACE)
    void insert(NotesActivity notes);

    //get all items from the notes table ordered in descending order by lastmodified date
    @Query("SELECT * FROM Notes ORDER BY lastModified DESC")
    List<NotesActivity> getAll();

    //update the database with the update query
    @Query("UPDATE Notes SET title = :title, body = :body, date = :date, lastModified = :lastModified WHERE ID = :id")
    void update(int id, String title, String body, String date, Date lastModified);

    //method to delete a note from the table
    @Delete
    void delete(NotesActivity notes);

}
