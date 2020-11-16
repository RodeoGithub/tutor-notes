package com.maey.tutornotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.maey.tutornotes.controller.ItemClickSupport;
import com.maey.tutornotes.controller.RecyclerViewAdapter;
import com.maey.tutornotes.model.Courses;

import java.util.ArrayList;

import static android.app.DownloadManager.*;

public class CoursesListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<Courses> mCoursesList;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private DatabaseReference mDbRef;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_courses);
        mDbRef = FirebaseDatabase.getInstance().getReference();
        mRecyclerView = findViewById(R.id.recycler_courses);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        ItemClickSupport.addTo(mRecyclerView)
                .setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                       /*
                        Intent i = new Intent (CoursesListActivity.this, NotesActivity.class);
                        startActivity(new Intent ());
                        */
                        Toast.makeText(getApplicationContext(),"Item Clicked at position: " + position, Toast.LENGTH_LONG).show();
                    }
                });
        mCoursesList = new ArrayList<>();
        ClearList();
        getCoursesListFromFirebase();

    }

    private void getCoursesListFromFirebase() {
        Query query = mDbRef.child("courses").child("randomUID");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ClearList();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Courses course = new Courses();
                    course.setCode(snapshot.getKey());
                    course.setTitle(snapshot.child("name").getValue().toString());
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
}