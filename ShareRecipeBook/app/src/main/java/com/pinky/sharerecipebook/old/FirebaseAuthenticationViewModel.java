/*
package com.pinky.sharerecipebook.viewmodels;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class FirebaseAuthenticationViewModel {

    // authentication
    //protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();


 */
/*   public FirebaseAuthenticationViewModel(FirebaseAuth firebaseAuth) {
        this.mFirebaseAuth = firebaseAuth;
    }*//*


    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public void signIn(String email, String password) {
        emailPasswordSignIn(email, password);
    }

    private void emailPasswordSignIn(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        user.setValue(task.getResult().getUser());

                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
            }
        });
    }

    public void signUp(String email, String password) {

        mFirebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean isExists = !task.getResult().getSignInMethods().isEmpty();
                if (isExists) {
                    Log.d("signUp", "onComplete: user already exists");
                } else {
                    SignUpWithEmailAndPassword(email, password);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("signUp", "onFailure: failed  " + e.getMessage());
            }
        });

    }

    private void SignUpWithEmailAndPassword(String email, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        user.setValue(task.getResult().getUser());
                    }
                }
            }
        });
    }


    */
/*//*
/
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
    //*//*


}
*/


package com.pinky.sharerecipebook.old;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.SignInMethodQueryResult;

public class FirebaseAuthenticationViewModel {

    // authentication
    //protected FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();


 /*   public FirebaseAuthenticationViewModel(FirebaseAuth firebaseAuth) {
        this.mFirebaseAuth = firebaseAuth;
    }*/

    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public void signIn(String email, String password) {
        emailPasswordSignIn(email, password);
    }

    private void emailPasswordSignIn(String email, String password) {
        mFirebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        user.setValue(task.getResult().getUser());

                    }
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
            }
        });
    }

    public void signUp(String email, String password) {

        mFirebaseAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean isExists = !task.getResult().getSignInMethods().isEmpty();
                if (isExists) {
                    Log.d("signUp", "onComplete: user already exists");
                } else {
                    SignUpWithEmailAndPassword(email, password);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("signUp", "onFailure: failed  " + e.getMessage());
            }
        });

    }

    private void SignUpWithEmailAndPassword(String email, String password) {
        mFirebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    if (task.getResult() != null) {
                        user.setValue(task.getResult().getUser());
                    }
                }
            }
        });
    }


    /*//
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
    //*/

}


// https://www.youtube.com/watch?v=xoP2GsPcr1o
