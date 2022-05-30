package com.example.shiftmanagerhit;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.shiftmanagerhit.DatabaseCommunication.FirebaseEvents;
import com.example.shiftmanagerhit.DatabaseCommunication.FirebaseManager;
import com.example.shiftmanagerhit.Date.UserDate;
import com.example.shiftmanagerhit.Utility.HidesKeyboard;

import java.io.File;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private UserDate User_sign_In;
    private EditText mEmail, mPassword;
    private Button mLoginBtn;
    private TextView mForgot;
    private ProgressBar progressBar;
    private FirebaseManager FBM;
    private String email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        File file = new File("");

        Log.d("onCreate", "LoginActivity:");


        FBM = new FirebaseManager(this);


        if (getSupportActionBar() != null)
            getSupportActionBar().hide();


        mEmail = findViewById(R.id.email_address_login);
        mPassword = findViewById(R.id.password_in_login);
        mLoginBtn = findViewById(R.id.loginBtn);
        progressBar = findViewById(R.id.progressBar);
        mForgot = findViewById(R.id.forgot_Password_in_login);
        
        mForgot.setOnClickListener(this);
        mLoginBtn.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.loginBtn:

                email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (email.isEmpty()) {
                    mEmail.setError("Full name is required!");
                    mEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Email is not good ");
                    mEmail.requestFocus();
                    return;
                }

                if (password.isEmpty()) {
                    mPassword.setError("Password is required!");
                    mPassword.requestFocus();
                    return;
                }

                if (mPassword.length() < 6) {
                    mPassword.setError("Password is last then 6");
                    mPassword.requestFocus();
                    return;
                }

                HidesKeyboard.hideKeyboard(this);

                //password and email are ok
                mLoginBtn.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                mEmail.setVisibility(View.GONE);
                mForgot.setVisibility(View.GONE);
                mPassword.setVisibility(View.GONE);

                User_sign_In = new UserDate();

                FBM.signIn(new FirebaseEvents.OnSignIn() {
                    @Override
                    public void SucceededSignIn(UserDate UserSignIn) {
                        User_sign_In = UserSignIn;

                        if (User_sign_In != null) {

//                            FBM.getDataFirebase(User_sign_In.getId(), new FirebaseEvents.OnGetData() {
//                                @Override
//                                public void SucceededGetData(UserDate UserSignIn) {
//                                    Log.w("signIn3", "" + UserSignIn.getName());
                            progressBar.setVisibility(View.GONE);

                            Intent intent = new Intent(LoginActivity.this, NavigationDrawerActivity.class);// New activity

                            Log.d("LoginActivity", "SucceededSignIn: " + User_sign_In.getIs_Manger());

                            intent.putExtra("User_sign_In", (Parcelable) User_sign_In);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();

//                                }
//                            });
                        }

                    }

                    @Override
                    public void FailedSignIn() {
                        //not login
                        mLoginBtn.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                        mEmail.setVisibility(View.VISIBLE);
                        mForgot.setVisibility(View.VISIBLE);
                        mPassword.setVisibility(View.VISIBLE);
                    }
//                }, "manager@gmail.com", "123456");
//                }, "Employee@gmail.com", "123456");
                }, email, password);

                break;

            case R.id.forgot_Password_in_login:
/*
//                UserDate tempAddUser = new UserDate("manager", "0000", "[]", "[]", "manager@gmail.com", "123456", true);
                UserDate tempAddUser = new UserDate("Employee", "0000", "[]", "[]", "Employee@gmail.com", "123456", false);


                FBM.createUser(tempAddUser, () -> {
//                    progressDialog.dismiss();
                });*/

                email = mEmail.getText().toString();

                if (email.isEmpty()) {
                    mEmail.setError("Email is required to Resat Password!");
                    mEmail.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mEmail.setError("Email is not good ");
                    mEmail.requestFocus();
                    return;
                }

                FBM.resatPassword(email, new FirebaseEvents.OnResatPassword() {

                    @Override
                    public void SucceededResatPassword() {
                        Log.d("LoginActivity", "Succeeded Resat Password: " + email);

                    }

                    @Override
                    public void FailedResatPassword() {
                        Log.d("LoginActivity", "Fails Resat Password: " + email);
                    }
                });

        }

    }

}

/*create repository in git (empty not read me file)
go to AS > VCS> Enable Version Control.. > select git > ok
change to 'project view' select the project folder> git > add
select the project folder> git > commit
https://www.youtube.com/watch?v=KB2BIm_m1Os
*/
