package com.try64.java.android.archetecturecomponents.M;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.try64.java.android.archetecturecomponents.M.Entity.Note;
import com.try64.java.android.archetecturecomponents.R;

import java.util.ArrayList;
import java.util.List;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteHolder> {
    private List<Note> allNotes = new ArrayList<>();
    private onItemClickListener listener;


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.note_item, parent, false);

        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Note currentNote = allNotes.get(position);
        holder.titleTV.setText(currentNote.getTitle());
        holder.descriptionTV.setText(currentNote.getDescription());
        holder.priorityTV.setText(String.valueOf(currentNote.getPriority()));
    }

    @Override
    public int getItemCount() {
        return allNotes.size();
    }

    public void setAllNotes(List<Note> notes) {
        this.allNotes = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position) {
        return allNotes.get(position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    public interface onItemClickListener {
        void onItemClick(Note note);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        private TextView titleTV;
        private TextView descriptionTV;
        private TextView priorityTV;

        public NoteHolder(@NonNull View itemView) {
            super(itemView);
            titleTV = itemView.findViewById(R.id.text_view_title);
            descriptionTV = itemView.findViewById(R.id.text_view_description);
            priorityTV = itemView.findViewById(R.id.text_view_priority);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemClick(allNotes.get(position));
                    }
                }
            });
        }
    }

}
