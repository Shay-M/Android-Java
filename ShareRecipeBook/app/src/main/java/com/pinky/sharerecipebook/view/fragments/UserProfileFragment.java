package com.pinky.sharerecipebook.view.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.pinky.sharerecipebook.R;
import com.pinky.sharerecipebook.models.User;
import com.pinky.sharerecipebook.utils.MyShimmer;
import com.pinky.sharerecipebook.view.LoadingDialog;
import com.pinky.sharerecipebook.viewmodels.UserProfileViewModel;

public class UserProfileFragment extends Fragment {
    //((AppCompatActivity) getActivity()).getSupportActionBar();

    //    ActivityResultLauncher<Uri> cameraFullSizeResultLauncher; //big img
    ActivityResultLauncher<String> pickContentResultLauncher;
    Uri photoURI;
    private TextInputLayout userNameTInput;
    private TextView userEmailTV;
    //private TextView ResatPasswordTextView;
    private Button saveButton;

    private ImageView userImage;
    private User LoginUserGet;

    private LoadingDialog loadingDialog;

    private UserProfileViewModel userProfileViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userProfileViewModel = new ViewModelProvider(this).get(UserProfileViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");

        userImage = view.findViewById(R.id.fragment_user_profile_user_image);
        userNameTInput = view.findViewById(R.id.fragment_user_profile_name_input);
        userEmailTV = view.findViewById(R.id.fragment_user_profile_email_textView);
        //ResatPasswordTextView = view.findViewById(R.id.fragment_user_profile_resat_password);

        saveButton = view.findViewById(R.id.fragment_user_profile_save_button);

        loadingDialog = new LoadingDialog(this);

        new Boom(userImage);
        new Boom(saveButton);

        initLaunchers();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        LoginUserGet = (User) requireArguments().getSerializable("expandLoginUser");
        Log.d("TAG", "LoginUserGet.getFirebaseUserId(): " + LoginUserGet.getFirebaseUserId());
        if (LoginUserGet.getName().equals("Guest")) {
            saveButton.setVisibility(View.GONE);
            userNameTInput.setEnabled(false);

        } else {
            userImage.setOnClickListener(v -> {
                picFromGalleria();
            });
        }
        userNameTInput.getEditText().setText(LoginUserGet.getName());
        userEmailTV.setText(LoginUserGet.getEmailAddress());

        Glide.with(this)
                .load(LoginUserGet.getUserImagePath())
                .circleCrop()
                .placeholder(MyShimmer.getShimmer())
                .error(R.drawable.ic_twotone_person_outline_24)
                .into(userImage);

        // take a pic


        // save user profile
        saveButton.setOnClickListener(v -> {

            String userName = userNameTInput.getEditText().getText().toString();

            if (userName.isEmpty()) {
                userNameTInput.setError(getString(R.string.error_fill_name));
                userNameTInput.requestFocus();
                if (userName.equals(LoginUserGet.getName()))
                    userName = null;

            } else if (photoURI == null) {
                Log.d("floating_attach_recipe", "photoURI is Empty!");
            }

            loadingDialog.startLoadingDialog();

            userProfileViewModel.saveUserProfileChanges(photoURI, userName, LoginUserGet.getFirebaseUserId(), new UserProfileViewModel.saveUserListener() {
                @Override
                public void onSucceededSaveUri() {
                    loadingDialog.dismissLoadingDialog();
                }

                @Override
                public void onSucceededSaveData() {
                    loadingDialog.dismissLoadingDialog();
                }

                @Override
                public void onFailedSaveUri() {
                    photoURI = null;
                    loadingDialog.dismissLoadingDialog();
                }

                @Override
                public void onFailedSaveData() {
                    loadingDialog.dismissLoadingDialog();
                }
            });


        });

        /*ResatPasswordTextView.setOnClickListener(v -> {
            userProfileViewModel.resatPassword();
            Snackbar.make(this.getView(), "We'll send you a password reset email", BaseTransientBottomBar.LENGTH_SHORT).show();

        });*/


    }

    private void initLaunchers() {
        // gallery
        pickContentResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
                        photoURI = result;
                        Glide.with(this)
                                .load(photoURI)
                                .circleCrop()
                                .placeholder(MyShimmer.getShimmer())
                                .error(R.drawable.ic_twotone_person_outline_24)
                                .into(userImage);

                    }
                });
    }

    private void picFromGalleria() {
        pickContentResultLauncher.launch("image/*");


    }


}