package com.pinky.sharerecipebook.repositories;/* Created by Shay Mualem 17/09/2021 */

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.pinky.sharerecipebook.models.Comment;
import com.pinky.sharerecipebook.models.Recipe;
import com.pinky.sharerecipebook.models.User;

import java.util.ArrayList;

public class FirebaseDatabaseRepository {

/*

public abstract class FirebaseDatabaseRepository<Model> {

    protected DatabaseReference databaseReference;
    protected FirebaseDatabaseRepositoryCallback<Model> firebaseCallback;
    private Application application;
    private ValueEventListener listener;

    public FirebaseDatabaseRepository(Application application) {
        databaseReference = FirebaseDatabase.getInstance().getReference(getRootNode());
        this.application = application;
    }

    public static FirebaseDatabaseRepository getInstance() {
        return FirebaseDatabaseRepository.getInstance();
    }

    protected abstract String getRootNode();

//    public void addListener(FirebaseDatabaseRepositoryCallback<Model> firebaseCallback) {
    public void addListener(FirebaseDatabaseRepositoryCallback firebaseCallback) {
        this.firebaseCallback = firebaseCallback;
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("onDataChange", "snapshot: " + snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("onCancelled", "snapshot: " + error);

            }
        };
        databaseReference.addValueEventListener(listener);
    }

    public void removeListener() {
        databaseReference.removeEventListener(listener);
    }

    public interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> result);

        void onError(Exception e);
    }

*/

    static FirebaseDatabaseRepository INSTANCE;

    private ArrayList<Recipe> recipeList = new ArrayList<>();
    private User user = new User();

    private MutableLiveData<ArrayList<Recipe>> arrayListMutableLiveData;
    private MutableLiveData<User> userMutableLiveData;
    private LiveData<User> userLiveData;


    public static FirebaseDatabaseRepository getInstance() {
        if (INSTANCE == null)
            INSTANCE = new FirebaseDatabaseRepository();

        return INSTANCE;
    }

    // new FirebaseDatabaseRepository.FirebaseDatabaseRepositoryCallback<Recipe>()

    public MutableLiveData<ArrayList<Recipe>> getRecipes() {
        LoadRecipeData();
        arrayListMutableLiveData = new MutableLiveData<>();
        arrayListMutableLiveData.setValue(recipeList);
        return arrayListMutableLiveData;
    }


    //    private void LoadRecipeData(FirebaseDatabaseRepositoryCallback<Recipe> firebaseCallback) {
    private void LoadRecipeData() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("recipe");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recipeList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Recipe obj = snapshot.getValue(Recipe.class);
//                    obj.setFirebaseUserIdMade(snapshot.getKey()); // todo need?
                    recipeList.add(obj);
                    arrayListMutableLiveData.setValue(recipeList);
                }
//                arrayListMutableLiveData.setValue(recipeList);
//                arrayListMutableLiveData.postValue(recipeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("LoadRecipeData", databaseError.toString());
            }
        });
    }

    public MutableLiveData<User> getUserByIdFromFirebase(String userIdTofind) {
        //if (userIdTofind != null) {
        //user = null;
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    if (!Objects.requireNonNull(ds.child("is_Manger").getValue(Boolean.class))) {
                    if (snapshot.getKey().equals(userIdTofind)) {
                        user = snapshot.getValue(User.class);
//                        return;
                        // userMutableLiveData.setValue(obj);
//                        Log.d("getUserByIdFromFirebase", "obj.getName() : " + user.getName());
                    } else {
                        //user = null;
                        Log.d("getUserByIdFromFirebase", "not found user by this id: " + userIdTofind);
                    }
                }
                userMutableLiveData.postValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("getUserByIdFromFirebase", databaseError.toString());
            }
        });
        // }
        userMutableLiveData = new MutableLiveData<>();
        userMutableLiveData.setValue(user);
        return userMutableLiveData;
    }

    public void changeDataFirebase(String folder, String IdTofind, String fildeToChange, String newValue, int kindOfValue) {

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference myRef = database
                    .getReference(folder)
                    .child(IdTofind)
                    .child(fildeToChange);
            if (kindOfValue == 0)  // int
                myRef.setValue(Integer.parseInt(newValue));
             /*else if (kindOfValue == 1) {  // list

               Map<String, Object> map = new HashMap<>();
                map.put("5", "comment55");
                rootRef.child("list").child(list_id).updateChildren(map);*/
               /* ArrayList arrayList = new ArrayList<String>();
                arrayList.add("hii");
                Log.d("TAG", " myRef.getKey(): "+ myRef.getKey());*/

//                String key = myRef.push().getKey();
//                myRef.child(key).setValue(newValue);
                //myRef.push().setValue(newValue);
                // }
            else myRef.setValue(newValue);

        } catch (Exception e) {
            Log.d("send Data Exception", "sendDataFirebase : " + e);
        }

        /*String key = myRecipeDBRef.push().getKey();
        newRecipe.setRecipeId(key);
        myRecipeDBRef.child(key).setValue(newRecipe);*/


    }

    public void changeDataFirebaseArrayList(String folder, String idTofind, String fildeToChange, ArrayList<String> newValue) {

        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference myRef = database
                    .getReference(folder)
                    .child(idTofind)
                    .child(fildeToChange);

            myRef.setValue(newValue);

        } catch (Exception e) {
            Log.d("send Data Exception", "sendDataFirebase : " + e);
        }

    }

    //todo !! remove and create one


    public void changeDataFirebase(String folder, String idTofind, String fildeToChange, Comment comment) {
        try {
            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference myRef = database
                    .getReference(folder)
                    .child(idTofind)
                    .child(fildeToChange);

            myRef.push().setValue(comment);

        } catch (Exception e) {
            Log.d("send Data Exception", "sendDataFirebase : " + e);
        }

    }



/*    public MutableLiveData<Boolean> userLikedThisRecipe(String recipeId) {
    getUserByIdFromFirebase(FirebaseAuth.getInstance().getCurrentUser().getUid());
        MutableLiveData<User>

    }*/


   /* public LiveData<User> getUserByIdFromFirebaseLiveData(String userIdTofind) {
        user = new User();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(userIdTofind)) {
                        user = snapshot.getValue(User.class);
                    } else {
                        //user = null;
                        Log.d("getUserByIdFromFirebase", "not found user by this id: " + userIdTofind);
                    }
                }
                //userMutableLiveData.postValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("getUserByIdFromFirebase", databaseError.toString());
            }
        });
        // }
       *//* userMutableLiveData = new MutableLiveData<>();
        userMutableLiveData.setValue(user);*//*
        return userLiveData;
    }*/


    //
/*    private void LoadUserData(String userIdTofind) {
        recipeList.clear();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if (snapshot.getKey().equals(userIdTofind)) {
                        User obj = snapshot.getValue(User.class);
                        userMutableLiveData.setValue(null);
//                        recipeList.add(obj);
                       // articles.setValue(obj);
                        Log.d("getUserByIdFromFirebase", "obj.getName() : " + obj.getName());
                    } else {
                        //userMutableLiveData.setValue(null);
                        Log.d("getUserByIdFromFirebase", "not found user by this id: " + userIdTofind);

                    }
                }
                arrayListMutableLiveData.postValue(recipeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("LoadRecipeData", databaseError.toString());
            }
        });
    }*/

    //


    /*public void removeListener() {
        myRef.removeEventListener(listener); // not work ,see if need
    }*/

/*    public interface FirebaseDatabaseRepositoryCallback<T> {
        void onSuccess(List<T> AllRecipes);

        void onError(Exception e);
    }*/


}
// https://www.youtube.com/watch?v=0W1QoPxfcS8
//https://www.youtube.com/watch?v=PAi9m69KYWs
// https://medium.com/globallogic-latinoamerica-mobile/viewmodel-firebase-database-3cc708044b5d