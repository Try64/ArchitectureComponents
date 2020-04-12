package com.try64.java.android.archetecturecomponents.V.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.try64.java.android.archetecturecomponents.R;

public class AddNoteActivity extends AppCompatActivity {

    private EditText editTextAddTitle, editTextAddDescription;
    private NumberPicker numberPicker;
    private String strTitle, strDescription, strRequestType;
    private int priority, tempId;
    private boolean shouldSetForEditRequest = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        strRequestType = getIntent().getStringExtra("strRequestType");
        if (strRequestType != null) {
            if (strRequestType.equals("Edit")) {
                strTitle = getIntent().getStringExtra("Title");
                strDescription = getIntent().getStringExtra("Description");
                priority = getIntent().getIntExtra("Priority", 0);
                tempId = getIntent().getIntExtra("id", -1);
                shouldSetForEditRequest = true;
            }
        }

        editTextAddTitle = findViewById(R.id.editText_Title_addNoteActivity);
        editTextAddDescription = findViewById(R.id.editText_Description_addNoteActivity);
        numberPicker = findViewById(R.id.numberPicker_addNoteActivity);

        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(10);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);
        if (shouldSetForEditRequest) {
            setTitle("Edit Note");
            editTextAddTitle.setText(strTitle);
            editTextAddDescription.setText(strDescription);
            numberPicker.setValue(priority);
        } else {
            setTitle("Add Note");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_addNoteMenu:
                onSaveCommandRequested();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onSaveCommandRequested() {
        strTitle = editTextAddTitle.getText().toString().trim();
        strDescription = editTextAddDescription.getText().toString().trim();
        priority = numberPicker.getValue();

        if (strTitle.isEmpty() || strDescription.isEmpty()) {
            if (strTitle.isEmpty()) {
                editTextAddTitle.setText("");
                editTextAddTitle.setError("Can not be Empty");
                return;
            } else {
                editTextAddDescription.setText("");
                editTextAddDescription.setError("Can not be Empty");
                return;
            }
        }
        if (strRequestType.equals("Add")) {
            Intent intent = new Intent();
            intent.putExtra("Add", true);
            intent.putExtra("Title", strTitle);
            intent.putExtra("Description", strDescription);
            intent.putExtra("Priority", priority);
            setResult(RESULT_OK, intent);
            finish();
        } else if (strRequestType.equals("Edit")) {
            Intent intent = new Intent();
            intent.putExtra("id", tempId);
            intent.putExtra("Edit", true);
            intent.putExtra("Title", strTitle);
            intent.putExtra("Description", strDescription);
            intent.putExtra("Priority", priority);
            setResult(RESULT_OK, intent);
            finish();
        }


    }
}
