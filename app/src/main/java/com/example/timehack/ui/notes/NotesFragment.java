package com.example.timehack.ui.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.timehack.databinding.FragmentNotesBinding;

//Class to take care of the notes fragment in the application.

public class NotesFragment extends Fragment {

    private FragmentNotesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotesViewModel notesViewModel =
                new ViewModelProvider(this).get(NotesViewModel.class);

        //show action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        binding = FragmentNotesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotes;
        notesViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //hide action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        binding = null;
    }


}