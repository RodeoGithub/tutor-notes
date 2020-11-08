package com.maey.tutornotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TypeActivity extends AppCompatActivity {

    private static final int USER_STUDENT = 0;
    private static final int USER_PRECEPTOR = 1;

    public int mUserType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);

        Button studentButton = (Button) findViewById(R.id.studentButton);
        studentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserType = USER_STUDENT;
                startActivity(new Intent(TypeActivity.this,LoginActivity.class));
            }
        });

        Button preceptorButton = (Button) findViewById(R.id.preceptorButton);

        preceptorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUserType = USER_PRECEPTOR;
                startActivity(new Intent(TypeActivity.this,LoginActivity.class));
            }
        });
    }
}