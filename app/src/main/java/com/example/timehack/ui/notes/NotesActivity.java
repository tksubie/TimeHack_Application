package com.example.timehack.ui.notes;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "Notes")
public class NotesActivity implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int ID = 0;

    @ColumnInfo(name = "title")
    String title = "";

    @ColumnInfo(name = "date")
    String date = "";

    @ColumnInfo(name = "body")
    String body = "";

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}





