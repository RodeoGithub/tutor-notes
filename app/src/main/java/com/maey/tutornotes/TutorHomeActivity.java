package com.maey.tutornotes;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.maey.tutornotes.controller.ItemClickSupport;
import com.maey.tutornotes.controller.RecyclerViewAdapter;
import com.maey.tutornotes.model.Courses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.maey.tutornotes.model.Courses.*;

public class TutorHomeActivity extends AppCompatActivity {

    //Database
    FirebaseDatabase mDb;
    FirebaseAuth mAuth;
    FirebaseUser user;
    private DatabaseReference mDbRef;

    TextView mTextView2;

    TextInputLayout inputCodeText;
    ProgressBar prgBar;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    private final int mUserType = 11;

    //preceptor widgets
    private Button coursesButton;
    private FloatingActionButton fabAddCourse;
    private RecyclerView mRecyclerView;
    private ArrayList<Courses> mCoursesList;
    private RecyclerViewAdapter mRecyclerViewAdapter;


    private Context mContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_tutor);

        //Firebase
        mDb = FirebaseDatabase.getInstance();
        mDbRef = mDb.getReference();
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        prgBar = findViewById(R.id.prgBar_home);
        prgBar.setVisibility(View.VISIBLE);

        setUpTutorUI();

        prgBar.setVisibility(View.GONE);
    }

    private void setUpTutorUI() {
        prgBar = findViewById(R.id.prgBar_home);
        prgBar.setVisibility(View.VISIBLE);
        fabAddCourse = findViewById(R.id.fabAddCourse);
        //RecyclerView
        mRecyclerView = findViewById(R.id.recycler_courses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        mCoursesList = new ArrayList<>();
        ClearList();
        mRecyclerView.setVisibility(View.GONE);
        prgBar.setVisibility(View.VISIBLE);
        getCoursesListFromFirebase();
        ItemClickSupport.addTo(mRecyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        Intent i = new Intent (TutorHomeActivity.this, NotesActivity.class);
                        String courseCode = mCoursesList.get(position).getCode();
                        i.putExtra("courseCode", courseCode);
                        startActivity(i);
                    }
                });

        fabAddCourse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity( new Intent ( TutorHomeActivity.this, AddCourseActivity.class));
            }
        });
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    private void getCoursesListFromFirebase() {
        Query query = mDbRef.child("courses");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearList();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Courses course = new Courses();
                    course.setCode(snapshot.getKey());
                    course.setName(snapshot.child("name").getValue().toString());
                    course.setShift(snapshot.child("shift").getValue().toString());
                    mCoursesList.add(course);
                }
                mRecyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), mCoursesList);
                mRecyclerView.setAdapter(mRecyclerViewAdapter);
                mRecyclerViewAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        prgBar.setVisibility(View.GONE);
    }

    private void ClearList(){
        if(mCoursesList!= null){
            mCoursesList.clear();
            if(mRecyclerViewAdapter != null){
                mRecyclerViewAdapter.notifyDataSetChanged();
            }
        }
        mCoursesList = new ArrayList<>();
    }

    private Context getActivity() { return TutorHomeActivity.this; }

    private void testFirebase() {
        //Writing a map on database
        Map<String,Object> courseInfo = new HashMap<String, Object>();
        String code = generateCourseCode();
        courseInfo.put("code", code);
        courseInfo.put("name", "Testing");
        courseInfo.put("shift", "Tarde");
        //
        DatabaseReference mDbRef = mDb.getReference();
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