package com.maey.tutornotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maey.tutornotes.model.Courses;
import com.maey.tutornotes.model.RandomString;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class AddCourseActivity extends AppCompatActivity {

    private TextInputLayout titleInputText;
    private TextInputLayout shiftInputText;

    private FloatingActionButton fabPostCourse;
    private ProgressBar addCoursePrgBar;
    private String mCourseTitle;
    private String mCourseShift;
    private String mCourseCode;

    private TextView newCourseCode;
    private Button backToListButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);

        // Setting up ActionBar

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Add a new Course");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //widgets
        addCoursePrgBar = findViewById(R.id.addCoursePrgBar);
        titleInputText = findViewById(R.id.newCourseTitle);
        shiftInputText = findViewById(R.id.newCourseShift);
        addCoursePrgBar.setVisibility(View.INVISIBLE);
        fabPostCourse = findViewById(R.id.fabPostCourse);
        newCourseCode = findViewById(R.id.newCourseCode);
        backToListButton = findViewById(R.id.backToListButton);
        backToListButton.setVisibility(View.INVISIBLE);
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
        //mCourseCode = "XZBKIM";

        //TODO: Adding Cancel Button on click

        backToListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //adding Floating Action Button on click
        fabPostCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                titleInputText.setVisibility(View.INVISIBLE);
                shiftInputText.setVisibility(View.INVISIBLE);
                addCoursePrgBar.setVisibility(View.VISIBLE);

                //if the note is not empty

                if(!titleInputText.getEditText().getText().toString().isEmpty() &&
                    !shiftInputText.getEditText().getText().toString().isEmpty()){
                    mCourseTitle = titleInputText.getEditText().getText().toString();
                    mCourseShift = shiftInputText.getEditText().getText().toString();
                    RandomString code = new RandomString(5);
                    mCourseCode = code.nextString();
                    code = null;
                    //Creating course
                    Courses course = new Courses(mCourseTitle,mCourseShift,mCourseCode);
                    addCourse(course);
                }
                else{
                    showMessage("Revise los campos necesarios");
                    titleInputText.requestFocus();
                }
            }
        });
    }

    private void addCourse(Courses course) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();

        //get reference and create a new note
        DatabaseReference dbRef = db.getReference("courses").child(mCourseCode);

        //add post to firebase

        dbRef.setValue(course).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                showMessage("Se añadio el curso");

                fabPostCourse.setVisibility(View.INVISIBLE);
                shiftInputText.setVisibility(View.INVISIBLE);
                titleInputText.setVisibility(View.INVISIBLE);
                addCoursePrgBar.setVisibility(View.INVISIBLE);
                newCourseCode.setText("El codigo de tu nuevo curso es: " + mCourseCode);
                newCourseCode.setVisibility(View.VISIBLE);
                backToListButton.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showMessage("Algo salió mal");
            }
        });
    }
    private void showMessage (String message){
        Toast.makeText(AddCourseActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}