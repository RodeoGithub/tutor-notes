package com.maey.tutornotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maey.tutornotes.model.Note;

public class AddNoteActivity extends AppCompatActivity {

    private TextInputLayout noteInputText;
    private FloatingActionButton fabPostNote;
    private ProgressBar addNotePrgBar;
    private ImageButton cancelNoteButton;
    private String mNoteText;
    private String mCourseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        // Setting up ActionBar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("@string/new_note_actionbar_name");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //widgets
        noteInputText = findViewById(R.id.noteInputText);
        fabPostNote = findViewById(R.id.fabPostNote);
        addNotePrgBar = findViewById(R.id.addNotePrgBar);
        addNotePrgBar.setVisibility(View.GONE);
        cancelNoteButton = findViewById(R.id.cancelNoteButton);

        //Getting courseCode from extra
        /*
        if (savedInstanceState == null){
            Bundle bundle = getIntent().getExtras();
            if (bundle == null){
                mCourseCode = null;
            }
            else{
                mCourseCode = bundle.getString("courseCode");
            }
        }
        else {
            mCourseCode = (String) savedInstanceState.getSerializable("courseCode");
        }
        */

        //Test CourseCode
        mCourseCode = "XZBKIM";

        //Adding Cancel Button on click
        cancelNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        //adding Floating Action Button on click
        fabPostNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteInputText.setVisibility(View.INVISIBLE);
                addNotePrgBar.setVisibility(View.VISIBLE);

                //if the note is not empty

                if(!noteInputText.getEditText().getText().toString().isEmpty()){
                    mNoteText = noteInputText.getEditText().getText().toString();
                    //Creating note
                    Note note = new Note(mNoteText, mCourseCode);
                    addNote(note);
                }
                else{
                    Toast.makeText(AddNoteActivity.this,"Escriba la nota que desea publicar",
                            Toast.LENGTH_SHORT).show();
                    noteInputText.requestFocus();
                }
            }
        });
    }

    private void addNote(Note note) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        //get reference and create a new note
        DatabaseReference dbRef = db.getReference("notes").child(mCourseCode).push();

        //get note unique ID and update noteKey

        String key = dbRef.getKey();
        note.setNoteKey(key);

        //add post to firebase

        dbRef.setValue(note).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Nota publicada con éxito");
                //finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Algo salió mal");
            }
        });
    }
    private void showMessage (String message){
        Toast.makeText(AddNoteActivity.this, message, Toast.LENGTH_SHORT);
    }
}