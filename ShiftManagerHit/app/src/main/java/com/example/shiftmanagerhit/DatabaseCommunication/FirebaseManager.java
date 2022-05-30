package com.example.shiftmanagerhit.DatabaseCommunication;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.shiftmanagerhit.Date.UserDate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class FirebaseManager {
    private final Activity _activity;
    public Boolean isSuccess = false;
    // authentication
    protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    //DB
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("Users");
    //    FirebaseAuth.AuthStateListener authStateListener;
    private UserDate User_sign_In;
    private UserDate NewUser;

//    private AllEmployees

    /*public FirebaseManager() {
    }*/

    public FirebaseManager(Activity activity) {
        super();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUI(currentUser);
        //Log.d("FirebaseManager", "user is signed in (non-null) : " + currentUser.getUid());

        Log.d("FirebaseManager", "db create from: " + activity);
        this._activity = activity;
    }

    private void updateUI(FirebaseUser currentUser) {

        if (currentUser != null) {
//                startActivity(new Intent(this,AnotherActivity.class));
            Toast.makeText(_activity, "signInWithEmailAndPassword: success. ", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(_activity, "signInWithEmailAndPassword: failure", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * @param User         get User class
     * @param onCreateUser interface for call back
     */
    public void createUser(UserDate User, FirebaseEvents.OnCreateUser onCreateUser) {

        NewUser = new UserDate();
        NewUser = User;

        mAuth.createUserWithEmailAndPassword(NewUser.getEmail(), NewUser.getPassword())
                .addOnCompleteListener(_activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            NewUser.setId(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(NewUser).addOnCompleteListener(_activity, new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("createUser", "signInWithEmail: success");
                                        onCreateUser.SucceededCreateUser();
                                        Toast.makeText(_activity, "signInWithEmail: success", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Log.d("createUser", "signInWithEmail: failure");
                                        Toast.makeText(_activity, "signInWithEmail: failure ", Toast.LENGTH_SHORT).show();
                                        onCreateUser.FailedCreateUser();
                                    }
                                }
                            });
                        } else {
                            Log.d("createUser", "Authentication failed.");
                            Toast.makeText(_activity, "Authentication failed. ", Toast.LENGTH_SHORT).show();
                            onCreateUser.FailedCreateUser();
                        }

                    }
                });

    }

    public void sendDataFirebase(UserDate currentUserObj, String shiftGet_shiftWants) {

        User_sign_In = currentUserObj;
        // Write a message to the database
        try {
            database = FirebaseDatabase.getInstance();
            myRef = database.getReference("Users")
//                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    .child(currentUserObj.getId());

            myRef.child(shiftGet_shiftWants).setValue(currentUserObj.getShiftWants());
            myRef = database.getReference("Users"); //resat myRef val
        } catch (Exception e) {
            Log.d("send Data Exception", "sendDataFirebase : " + e);
        }

    }


    /**
     * @param callBackGetAllEmployees interface for call back
     */
    public void getAllEmployeesFirebase(FirebaseEvents.OnDataChangeGetAllEmployees callBackGetAllEmployees) {
        List<UserDate> EmployeesIdList = new ArrayList<>();

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("getAllEmployeesFirebase 1", "dataSnapshot: " + dataSnapshot);

                //if (dataSnapshot != null)//todo
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    Log.d("getAllEmployeesFirebase 2", "onDataChange: " + ds);
//                        if (ds.getKey() != "email")
                    if (!Objects.requireNonNull(ds.child("is_Manger").getValue(Boolean.class))) {

//                        String name = ds.child("name").getValue(String.class);
//                        String name = ds.getKey();

                        UserDate tempUser = new UserDate();
                        tempUser.setId(ds.getKey());
                        Log.d("ds.getKey()", "onDataChange: " + ds.getKey());
                        tempUser = ds.getValue(UserDate.class);
                        EmployeesIdList.add(tempUser);

                    }
                }

                callBackGetAllEmployees.SucceededGetAllEmployees(EmployeesIdList); // return Employees;
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
                callBackGetAllEmployees.FailedGetAllEmployees();
            }
        };
        myRef.addListenerForSingleValueEvent(eventListener);

    }

    /**
     * @param onSignIn interface for call back
     * @param email    try with this login
     * @param pas      try with this login
     */
    public void signIn(FirebaseEvents.OnSignIn onSignIn, String email, String pas) {

        mAuth.signInWithEmailAndPassword(email, pas)
                .addOnCompleteListener(_activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information

                            FirebaseUser user = mAuth.getCurrentUser();

                            User_sign_In = new UserDate();
//
                            //User_sign_In.setEmail(email);
                            User_sign_In.setId(Objects.requireNonNull(user).getUid());

//                            onSignIn.SucceededSignIn(UserSignIn);
                            //
                            getDataFirebase(user.getUid(), new FirebaseEvents.OnGetData() {
                                @Override
                                public void SucceededGetData(UserDate UserSignIn) {
                                    if (!isSuccess) {
                                        User_sign_In = UserSignIn;
                                        isSuccess = true;
                                        updateUI(user);

                                        onSignIn.SucceededSignIn(UserSignIn);

                                    }

                                }

                                @Override
                                public void FailedGetData(UserDate UserSignIn) {
                                    Log.w("signIn", "FailedGetData: failure");

                                    onSignIn.FailedSignIn();
                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("signIn", "signInWithEmailAndPassword: failure", task.getException());
//                            isSuccess = false;
                            updateUI(null);
                            onSignIn.FailedSignIn();

                        }
                    }
                });

    }

    /**
     *
     */
    public void signOut() {
        mAuth.signOut();
    }


    /**
     * @param email           for reseating password
     * @param onResatPassword interface for call back
     */
    public void resatPassword(String email, FirebaseEvents.OnResatPassword onResatPassword) {
        Log.d("resatPassword", "for: " + email);

        mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.w("FirebaseManager", "resatPassword: " + User_sign_In);
                    onResatPassword.SucceededResatPassword();

                } else onResatPassword.FailedResatPassword();
            }
        });

    }


    /**
     * @param UserId    id of a user to get info from db
     * @param onGetData interface for call back
     */
    public void getDataFirebase(String UserId, FirebaseEvents.OnGetData onGetData) {

//        if (User_sign_In.getId() == null)
//            UserId = User_sign_In.getId();


        myRef.child(UserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                //Get UserDate class from DB
                User_sign_In = dataSnapshot.getValue(UserDate.class);
                Log.w("getDataFirebase", "onDataChange." + User_sign_In);

                onGetData.SucceededGetData(User_sign_In);
            }

            @Override
            public void onCancelled(@NotNull DatabaseError error) {
                // Failed to read value
                Log.w("getDataFirebase", "Failed to read value.", error.toException());
                onGetData.FailedGetData(User_sign_In);
            }
        });

    }

}