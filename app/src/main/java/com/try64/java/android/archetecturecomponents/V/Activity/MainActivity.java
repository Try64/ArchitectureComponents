package com.try64.java.android.archetecturecomponents.V.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.try64.java.android.archetecturecomponents.M.Entity.Note;
import com.try64.java.android.archetecturecomponents.M.NoteAdapter;
import com.try64.java.android.archetecturecomponents.R;
import com.try64.java.android.archetecturecomponents.VM.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private Note editableNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.button_add_note);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRequestNoteManupulation("Add");
            }
        });

        recyclerView = findViewById(R.id.recyclerView_mainActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final NoteAdapter noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);

        noteViewModel = new ViewModelProvider.AndroidViewModelFactory(getApplication()).create(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                //Toast.makeText(MainActivity.this,"OnChanged",Toast.LENGTH_SHORT).show();
                noteAdapter.setAllNotes(notes);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(noteAdapter.getNoteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Note deleted Sucessfully", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                editableNote = note;
                onRequestNoteManupulation("Edit");

            }
        });

    }

    private void onRequestNoteManupulation(@NonNull String strRequestType) {
        Intent intent = new Intent(MainActivity.this, AddNoteActivity.class);
        if (strRequestType.equals("Add")) {
            intent.putExtra("strRequestType", "Add");
            startActivityForResult(intent, 1000);
        } else {
            intent.putExtra("id", editableNote.getId());
            intent.putExtra("strRequestType", "Edit");
            intent.putExtra("Title", editableNote.getTitle());
            intent.putExtra("Description", editableNote.getDescription());
            intent.putExtra("Priority", editableNote.getPriority());
            startActivityForResult(intent, 1001);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1000 && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("Add", false)) {
                Note note = new Note(data.getStringExtra("Title"), data.getStringExtra("Description"), data.getIntExtra("Priority", -1));
                noteViewModel.insert(note);
                Toast.makeText(MainActivity.this, "Note Inserted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Something is Wrong Receiving Data!!", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == 1001 && resultCode == RESULT_OK) {
            if (data != null && data.getBooleanExtra("Edit", false)) {
                Note note = new Note(data.getStringExtra("Title"), data.getStringExtra("Description"), data.getIntExtra("Priority", 0));
                note.setId(data.getIntExtra("id", -1));
                noteViewModel.update(note);
                Toast.makeText(MainActivity.this, "Note Updated", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(MainActivity.this, "Operation Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteAllNotes_mainActivityMenu) {
            noteViewModel.deleteAllNotes();
            Toast.makeText(MainActivity.this, "All notes Deleted", Toast.LENGTH_SHORT).show();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }
}
