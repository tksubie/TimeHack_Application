package com.example.timehack.ui.notes;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


//This is a class to display the view of what is in the Notes page.


public class NotesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public NotesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is the notes fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}