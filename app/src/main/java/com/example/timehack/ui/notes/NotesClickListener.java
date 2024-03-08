package com.example.timehack.ui.notes;

import androidx.cardview.widget.CardView;

public interface NotesClickListener {
    void onClick(NotesActivity notes);
    void onLongClick(NotesActivity notes, CardView cardview);
}
