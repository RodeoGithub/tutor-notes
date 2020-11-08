package com.maey.tutornotes;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.maey.tutornotes.model.Courses;
import com.maey.tutornotes.model.Courses.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static com.maey.tutornotes.model.Courses.*;

public class HomeActivity extends AppCompatActivity {

    private FirebaseDatabase mDb;
    private DatabaseReference mDbRef;
    TextView mTextView2;
    Button coursesButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getIntent();
        mDb = FirebaseDatabase.getInstance();
        mDbRef = mDb.getReference();
        testFirebase();
        mTextView2 = findViewById(R.id.textView2);
        coursesButton = findViewById(R.id.courseListButton);
        coursesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, CoursesListActivity.class));
            }
        });
        // Write a message to the database
    }

    private void testFirebase() {
        //Writing a map on database
        Map<String,Object> courseInfo = new HashMap<String, Object>();
        String code = addNewCourse();
        courseInfo.put("code", code);
        courseInfo.put("name", "Testing");
        courseInfo.put("shift", "Tarde");
        //
        mDbRef.child("users").child("randomUID").setValue(courseInfo);

        //Reading from database
        mDbRef.child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String m = (String) dataSnapshot.getValue();
                mTextView2.setText(m);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}