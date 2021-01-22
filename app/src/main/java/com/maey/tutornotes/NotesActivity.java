package com.maey.tutornotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.maey.tutornotes.controller.NotesRecyclerAdapter;
import com.maey.tutornotes.model.Courses;
import com.maey.tutornotes.model.Note;

import java.util.ArrayList;

public class NotesActivity extends AppCompatActivity {

    private int ADD_NOTE = 1;
    private ProgressBar notesPrgBar;
    private RecyclerView notesRecycler;
    private ArrayList<Note> mNotesList;
    private NotesRecyclerAdapter mNotesRecyclerAdapter;
    private Context mContext;
    private FloatingActionButton fabAddNote;

    private String mUID;
    private int mUserType;
    private String mCourseCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);
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

        //widgets
        notesPrgBar = findViewById(R.id.prgBar_notes);
        notesRecycler = findViewById(R.id.notesRecycler);
        fabAddNote = findViewById(R.id.fabAddNote);

        //AuthRef
        mUID = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();

        //DatabaseRef
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference dbUser = db.getReference("users/" + mUID);

        getUserType();

        //setting UI
        notesRecycler.setVisibility(View.GONE);
        notesPrgBar.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        notesRecycler.setLayoutManager(layoutManager);

        mNotesList = new ArrayList<>();
        ClearList();

        getNotesFromFirebase();

        if (mUserType!=0){
            fabAddNote.setVisibility(View.GONE);
        }
        else {
            fabAddNote.setVisibility(View.VISIBLE);
        }

        fabAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(NotesActivity.this, AddNoteActivity.class);
                i.putExtra("courseCode", mCourseCode);
                startActivityForResult(i, ADD_NOTE);
            }
        });

        //Setting Recycler View Visible
        notesRecycler.setVisibility(View.VISIBLE);
        notesPrgBar.setVisibility(View.GONE);
    }

    private void getNotesFromFirebase() {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference mDbNotes = db.getReference("notes");

        Query query = mDbNotes.child(mCourseCode);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearList();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Note note = new Note();
                    note.setCourse(mCourseCode);
                    note.setNoteKey(snapshot.getKey());
                    note.setTimeStamp(snapshot.child("timeStamp"));
                    note.setText(snapshot.child("text").getValue().toString());
                    mNotesList.add(note);
                }
                mNotesRecyclerAdapter = new NotesRecyclerAdapter(getApplicationContext(),mNotesList);
                notesRecycler.setAdapter(mNotesRecyclerAdapter);
                mNotesRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void ClearList() {
        if (mNotesList != null){
            mNotesList.clear();
            if(mNotesRecyclerAdapter != null){
                mNotesRecyclerAdapter.notifyDataSetChanged();
            }
        }
        mNotesList = new ArrayList<>();
    }

    private void getUserType() {
        Query query = FirebaseDatabase.getInstance().getReference("users/" + mUID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUserType = (int) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}