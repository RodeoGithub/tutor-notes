package com.maey.tutornotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    private static final int USER_STUDENT = 0;
    private int mUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button studentButton = findViewById(R.id.studentButton);
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserType = USER_STUDENT;
                startActivity(new Intent(StartActivity.this,LoginActivity.class));
            }
        });
    }
}