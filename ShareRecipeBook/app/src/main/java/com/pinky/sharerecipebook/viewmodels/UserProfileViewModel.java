package com.pinky.sharerecipebook.viewmodels;/* Created by Shay Mualem 09/10/2021 */

import android.net.Uri;
import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.pinky.sharerecipebook.repositories.AuthRepository;
import com.pinky.sharerecipebook.repositories.FirebaseDatabaseRepository;
import com.pinky.sharerecipebook.repositories.FirebaseStorgeRepository;

public class UserProfileViewModel extends ViewModel {
    private AuthRepository authRepository;

    public void saveUserProfileChanges(Uri photoURI, String newUserName, String IdTofind, UserProfileViewModel.saveUserListener saveUserListener) {

        if (photoURI != null)
            FirebaseStorgeRepository.getInstance().UploadFile(photoURI, "users", new FirebaseStorgeRepository.OnTaskDownloadUri() {

                @Override
                public void onSuccess(Uri downloadUri) {
                    //save uri in db
                    String folder = "users";
                    String fildeToChange = "userImagePath";

                    // change like in recipe
                    FirebaseDatabaseRepository.getInstance().changeDataFirebase(folder, IdTofind, fildeToChange, downloadUri.toString(), 1);

                    saveUserListener.onSucceededSaveUri();

                }

                @Override
                public void onFailure() {
                    saveUserListener.onFailedSaveUri();
                    Log.d("onFailure", "onFailedSaveUri");

                }

                @Override
                public void onProgress(double progressNum) {

                }
            });

        //save new name if needed
        if (newUserName != null) {
            String folder = "users";
            String fildeToChange = "name";

            // change like in recipe
            FirebaseDatabaseRepository.getInstance().changeDataFirebase(folder, IdTofind, fildeToChange, newUserName, 1);

        }
    }

    public interface saveUserListener {


        void onSucceededSaveUri();

        void onSucceededSaveData();

        void onFailedSaveUri();

        void onFailedSaveData();

    }

    public void resatPassword(String email) {
        authRepository.resatPassword(email);

    }


}
