package com.maey.tutornotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.maey.tutornotes.model.User;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;
    private static final int STATE_VERIFY_FAILED = 3;
    private static final int STATE_VERIFY_SUCCESS = 4;
    private static final int STATE_SIGNIN_FAILED = 5;
    private static final int STATE_SIGNIN_SUCCESS = 6;

    private static final int TYPE_STUDENT = 10;
    private static final int TYPE_TUTOR = 11;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDb;

    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private EditText mUserPhone;
    private EditText mVerificationCode;
    private String mPhoneNumber;
    private Button mSignInButton;
    private Button mVerifyCodeButton;
    private int mUserType;
    private String mName;
    private User mUser;
    private DatabaseReference mRefUsers;
    private String mUserId;

    @Override
    public void onStart() {
        super.onStart();
        // Check auth on Activity start
        if (mAuth.getCurrentUser() != null) {
            onAuthSuccess(mAuth.getCurrentUser());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mDb = FirebaseDatabase.getInstance();

        //Binding Buttons and text fields
        mUserPhone = (EditText) findViewById(R.id.userPhone);
        mVerificationCode = (EditText) findViewById(R.id.verificationCode);
        mVerifyCodeButton = (Button) findViewById(R.id.verifyCodeButton);

        mSignInButton = (Button) findViewById(R.id.signInButton);
        mSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkMobilePhone()){
                    sendVerificationCode(mPhoneNumber);
                    mUserPhone.setVisibility(View.GONE);
                    mVerificationCode.setVisibility(View.VISIBLE);
                    mSignInButton.setVisibility(View.GONE);
                    mSignInButton.setVisibility(View.GONE);
                    mVerifyCodeButton.setVisibility(View.VISIBLE);
                }
            }
        });

        mVerifyCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verifyVerificationCode(mVerificationCode.getText().toString());
            }
        });
    }

    private void sendVerificationCode(String phoneNumber) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                //TODO: change static value to spinner
                "+54" + phoneNumber,
                60,
                TimeUnit.SECONDS,
                this,
                mCallbacks);
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String code = phoneAuthCredential.getSmsCode();
                    if (code != null) {
                        mVerificationCode.setText(code);
                        //verifying the code
                        verifyVerificationCode(code);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    mVerificationId = s;
                    mResendToken = forceResendingToken;
                }
            };

    private void verifyVerificationCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(LoginActivity.this,
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            onAuthSuccess(task.getResult().getUser());
                        }
                        else {
                             //verification unsuccessful, display an error message
                            String message = "Something is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }

    private void onAuthSuccess(FirebaseUser user) {
        mUserId = user.getUid().toString();
        System.out.println(mUserId);
        checkNewUser();
        Class destination;
        if (mUserType == TYPE_TUTOR){
            destination = TutorHomeActivity.class;
        }
        else {
            destination = StudentHomeActivity.class;
        }
        Intent i = new Intent(LoginActivity.this, destination);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
        finish();
    }

    private void checkNewUser() {
        mRefUsers = mDb.getReference("users");
        //checking user
        mRefUsers.child(mUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    //If the user doesn't exists
                    writeNewUser();
                }
                else{
                    User user = dataSnapshot.getValue(User.class);
                    mName = user.getName();
                    mUserType = user.getUserType();
                }
            }
            @Override
            public void onCancelled (@NonNull DatabaseError databaseError) {

                //TODO: AlertDialog to notify user.

                System.out.println(databaseError.getCode() +" "+
                        databaseError.getMessage() +" "+
                        databaseError.getDetails());
            }
        });
    }

    private void writeNewUser() {
            // TODO: FORM TO CHOOSE USER TYPE
            mName = "Rodrigo";
            mUserType = TYPE_TUTOR;
            mUser = new User(mName, mUserType);

            mRefUsers.child(mUserId).setValue(mUser);
    }

    private void setUserType(int index) {
        ((TutorNotes) this.getApplication()).setUserType(index);
    }

    private Context getActivity() {
        return LoginActivity.this;
    }

    private boolean checkMobilePhone() {
        mPhoneNumber = mUserPhone.getText().toString().trim();

        if(mPhoneNumber.isEmpty() || mPhoneNumber.length() < 10){
            Toast.makeText(LoginActivity.this, "Ingrese un número de teléfono válido", Toast.LENGTH_SHORT).show();
            mUserPhone.requestFocus();
            return false;
        }
        return true;
    }
        
}