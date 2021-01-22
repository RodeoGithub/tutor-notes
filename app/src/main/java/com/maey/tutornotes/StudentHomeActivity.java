package com.maey.tutornotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Context;

public class StudentHomeActivity extends AppCompatActivity {

    //Database
    private FirebaseDatabase mDb;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference mDbRef;

    private TextInputLayout inputCodeText;
    private final int mUserType = 11;
    private String mStudentCourseCode;

    //Student widgets
    private TextView welcomeStudentText;
    private CardView goToAllNotes;
    private TextView lastNoteText;
    private Button enterCourseButton;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_student);
        /*
        inputCodeText = findViewById(R.id.inputCourseCode);
        //inputCodeText.setVisibility(View.GONE);
        //prgBar = findViewById(R.id.prgBar_home);
        //prgBar.setVisibility(View.VISIBLE);

        setUpStudentUI();
        */
    }


    private void setUpStudentUI() {
        mDbRef = mDb.getReference("users").child(user.getUid());
        goToAllNotes = findViewById(R.id.goToAllNotes);
        lastNoteText = findViewById(R.id.lastNoteText);

        //prgBar = findViewById(R.id.prgBar_home);
        //prgBar.setVisibility(View.VISIBLE);
        welcomeStudentText = findViewById(R.id.welcomeTutor);
        enterCourseButton = findViewById(R.id.enterCourseButton);
        enterCourseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String courseCode = inputCodeText.getEditText().getText().toString();
                mDbRef.child("code").setValue(courseCode);
                mStudentCourseCode = courseCode;
                enterCourseButton.setVisibility(View.GONE);
                inputCodeText.setVisibility(View.GONE);
            }
        });
        //check if students have course

        //checking user
        mDbRef.child("code").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //If the user doesn't have a course
                    welcomeStudentText.setVisibility(View.INVISIBLE);
                    //lastNote.setVisibility(View.INVISIBLE);
                    goToAllNotes.setVisibility(View.INVISIBLE);
                    inputCodeText.setVisibility(View.VISIBLE);
                    enterCourseButton.setVisibility(View.VISIBLE);
                }
                else{
                    mStudentCourseCode = dataSnapshot.getValue().toString();
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError) {

            }
        });
        /*
        showLastNote(mStudentCourseCode);
        goToAllNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
         */

    }

    private void showLastNote(String courseCode) {
        // TODO: refactor to handle empty collection and dynamic code
        /*
        Query query = mDbRef.child("notes").child("XZBKIM").limitToFirst(1);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String ln = dataSnapshot.child("text").getValue().toString();
                lastNoteText.setText(ln);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        */
    }
}