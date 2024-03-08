package com.example.timehack.ui.notes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.timehack.R;
import com.example.timehack.databinding.FragmentNotesBinding;
import com.example.timehack.ui.notes.Database.RoomDatabase;

import java.util.ArrayList;
import java.util.List;

//Class to take care of the notes fragment in the application.

public class NotesFragment extends Fragment {

    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    List<NotesActivity> notes = new ArrayList<>();
    RoomDatabase notesDatabase;


    private FragmentNotesBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        NotesViewModel notesViewModel =
                new ViewModelProvider(this).get(NotesViewModel.class);

        binding = FragmentNotesBinding.inflate(inflater, container, false);

        //show action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();

        //for the info fragment view when it starts up
        View v = inflater.inflate(R.layout.fragment_notes, container, false);

        //initialize recycler view
        recyclerView = v.findViewById(R.id.recycler_notes);

        //initialize database
        notesDatabase = RoomDatabase.getInstance(getContext());
        notes = notesDatabase.notesDataInterface().getAll();

        //update method
        updateRecycler(notes);




        //create menu options and click on options

        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.actionbar, menu);

                // Add option Menu Here

            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.addN){
                    Intent intent = new Intent(getContext(), NotesTaker.class);
                    startActivityForResult (intent, 101);
                    return true;
                }
                else return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        return v;
    }

    @SuppressWarnings("deprecation")
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==101){
            if(resultCode == Activity.RESULT_OK){
                NotesActivity new_notes = (NotesActivity) data.getSerializableExtra("note");
                notesDatabase.notesDataInterface().insert(new_notes);

                //clear notes list
                notes.clear();
                notes.addAll(notesDatabase.notesDataInterface().getAll());

                notesAdapter.notifyDataSetChanged();
            }
        }
        else if (requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                NotesActivity new_notes = (NotesActivity) data.getSerializableExtra("note");
                notesDatabase.notesDataInterface().update(new_notes.getID(), new_notes.getTitle(), new_notes.getBody(), new_notes.getDate());

                //clear notes list
                notes.clear();
                notes.addAll(notesDatabase.notesDataInterface().getAll());

                notesAdapter.notifyDataSetChanged();
            }
        }

    }

    private void updateRecycler(List<NotesActivity> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,LinearLayoutManager.VERTICAL));
        notesAdapter = new NotesAdapter(getContext(), notes, notesClickListener);
        recyclerView.setAdapter(notesAdapter);
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @SuppressWarnings("deprecation")
        @Override
        public void onClick(NotesActivity notes) {
            Intent intent = new Intent(getContext(), NotesTaker.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(NotesActivity notes, CardView cardview) {

        }
    };

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //hide action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        binding = null;
    }


}