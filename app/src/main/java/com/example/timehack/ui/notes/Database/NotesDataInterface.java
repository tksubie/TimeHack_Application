package com.example.timehack.ui.notes.Database;


import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.timehack.ui.notes.NotesActivity;

import java.util.List;

@Dao
public interface NotesDataInterface {

    //Insert method for creating a note
    @Insert(onConflict = REPLACE)
    void insert(NotesActivity notes);

    //get all items from the notes table ordered in descending order
    @Query("SELECT * FROM Notes ORDER BY id DESC")
    List<NotesActivity> getAll();

    //update the database with the update query
    @Query("UPDATE Notes SET title = :title, body = :body, date = :date WHERE ID = :id")
    void update(int id, String title, String body, String date);

    //method to delete a note from the table
    @Delete
    void delete(NotesActivity notes);

}
