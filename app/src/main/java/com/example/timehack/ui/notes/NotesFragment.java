package com.example.timehack.ui.notes;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.timehack.R;
import com.example.timehack.ui.notes.Database.RoomDatabase;

import java.util.ArrayList;
import java.util.List;

//Class to take care of the notes fragment in the application.

public class NotesFragment extends Fragment {

    RecyclerView recyclerView;
    NotesAdapter notesAdapter;
    List<NotesActivity> notes = new ArrayList<>();
    RoomDatabase notesDatabase;

    SearchView searchNotes;

    NotesActivity selectedNote;

    View v;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        //show action bar
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();


        //for the info fragment view when it starts up
        v = inflater.inflate(R.layout.fragment_notes, container, false);


        //initialize recycler view and search view
        recyclerView = v.findViewById(R.id.recycler_notes);
        searchNotes = v.findViewById(R.id.searchNotes);

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
            }

            @SuppressWarnings("deprecation")
            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.addN){
                    Intent intent = new Intent(getContext(), NotesTaker.class);
                    startActivityForResult (intent, 101);
                    notesAdapter.notifyDataSetChanged();
                    refreshFragment();
                    return true;
                }
                else return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);

        //for the search view to have a bigger touch area
        searchNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchNotes.setIconified(false);
            }
        });
        //for the search view searching notes and filtering when the text changes by the user
        searchNotes.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });



        return v;
    }

//filter the list
    private void filter(String newText) {
        List<NotesActivity> filteredList = new ArrayList<>();
        for(NotesActivity singleNote : notes){
            //looking inside the titles for the notes saved and also the body of all the notes
            if(singleNote.getTitle().toLowerCase().contains(newText.toLowerCase())
                    || singleNote.getBody().toLowerCase().contains((newText.toLowerCase()))){
                //create filtered list with results
                filteredList.add(singleNote);
            }

        }
        notesAdapter.filterList(filteredList);
    }

    //101 is for adding a note into the database and will return an error if wrong
    //102 is for updating a previous not in teh database
    @SuppressWarnings("deprecation")
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
                refreshFragment();
                Toast.makeText(getContext(), "Added note - " + new_notes.getTitle(), Toast.LENGTH_SHORT).show();

            }
        }
        else if (requestCode == 102){
            if(resultCode == Activity.RESULT_OK){
                NotesActivity new_notes = (NotesActivity) data.getSerializableExtra("note");
                notesDatabase.notesDataInterface().update(new_notes.getID(), new_notes.getTitle(), new_notes.getBody(), new_notes.getDate(),new_notes.getLastModified());

                //clear notes list
                notes.clear();
                notes.addAll(notesDatabase.notesDataInterface().getAll());
                notesAdapter.notifyDataSetChanged();
                refreshFragment();
                Toast.makeText(getContext(), "Updated note - " + new_notes.getTitle(), Toast.LENGTH_SHORT).show();

            }
        }

    }

    //intents for the onclick and long click looking for errors
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
            selectedNote = new NotesActivity();
            selectedNote = notes;
            showPopup(cardview);

        }
    };

    //update the recycler view when it needs to be updated after functions
    public void updateRecycler(List<NotesActivity> notes) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1,LinearLayoutManager.VERTICAL));
        notesAdapter = new NotesAdapter(getContext(), notes, notesClickListener);
        recyclerView.setAdapter(notesAdapter);
    }

    //show the popup menu when a long click happens
    private void showPopup(CardView cardview) {
        //create menu options and click on options
        PopupMenu inNotePopUp = new PopupMenu(getContext(), cardview);

        inNotePopUp.inflate(R.menu.in_note_actionbar);


        inNotePopUp.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                if (item.getItemId() == R.id.deleteN){
                    notesDatabase.notesDataInterface().delete(selectedNote);
                    notes.remove(selectedNote);
                    notesAdapter.notifyDataSetChanged();
                    refreshFragment();
                    Toast.makeText(getContext(), "Deleted note - " + selectedNote.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }
                else if (item.getItemId() == R.id.shareN){
                    String title = selectedNote.getTitle();
                    String body = selectedNote.getBody();

                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "* Note from TimeHack Application * \nTitle: " + title + "\n" + body);
                    sendIntent.setType("text/plain");

                    Intent shareIntent = Intent.createChooser(sendIntent, null);
                    startActivity(shareIntent);
                }
                return false;
            }
        });
        //set popout to right side of card
        inNotePopUp.setGravity(Gravity.END);
        inNotePopUp.show();
    }

    //refresh the fragment of the dataset changing so that the view is changed when their is updates.
    private void refreshFragment(){
        // This method refreshes the fragment
        NavHostFragment.findNavController(NotesFragment.this)
                .navigate(R.id.navigation_notes);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


}