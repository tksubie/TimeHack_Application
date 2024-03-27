package com.example.timehack.ui.notes;

import androidx.cardview.widget.CardView;

//listener for the single click and long click on the note card in the card-view
public interface NotesClickListener {
    void onClick(NotesActivity notes);

    void onLongClick(NotesActivity notes, CardView cardview);
}
