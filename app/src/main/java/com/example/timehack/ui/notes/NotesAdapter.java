package com.example.timehack.ui.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timehack.R;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesViewHolder>{

    Context context;
    List<NotesActivity> list;
    NotesClickListener listener;

    public NotesAdapter(Context context, List<NotesActivity> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        //bind all data in view
        holder.noteTitle.setText(list.get(position).getTitle());
        holder.noteTitle.setSelected(true);

        holder.notesBody.setText(list.get(position).getBody());

        holder.notesDate.setText(list.get(position).getDate());
        holder.notesDate.setSelected(true);

        holder.notes_list_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onClick(list.get(holder.getAdapterPosition()));
            }
        });

        holder.notes_list_container.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_list_container);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    //create items from custom list made in layout
    CardView notes_list_container;
    ImageView noteOption;
    TextView noteTitle, notesBody, notesDate;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);
        notes_list_container = itemView.findViewById(R.id.notes_list_container);
        noteOption = itemView.findViewById(R.id.noteOption);
        noteTitle = itemView.findViewById(R.id.noteTitle);
        notesBody = itemView.findViewById(R.id.notesBody);
        notesDate = itemView.findViewById(R.id.notesDate);
    }
}