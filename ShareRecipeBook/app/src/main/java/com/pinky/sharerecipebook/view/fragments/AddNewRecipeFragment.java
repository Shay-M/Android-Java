/*
package com.pinky.sharerecipebook.view.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pinky.sharerecipebook.R;
import com.pinky.sharerecipebook.models.AuthAppRepository;
import com.pinky.sharerecipebook.models.Recipe;
import com.pinky.sharerecipebook.repositories.FirebaseStorgeRepository;
import com.pinky.sharerecipebook.utils.CameraManagerUrl;
import com.pinky.sharerecipebook.viewmodels.AddRecipeViewModel;


public class AddNewRecipeFragment extends Fragment {
    final int GALLERY_REQUEST_CODE = 3;
    private EditText TitleText;
    private EditText IngredientsText;
    private EditText preparationText;
    private ImageView picContentView;
    private Uri imgUri = null;

    private EditText linkText;
    private EditText nameText;

    private FloatingActionButton floating_attach_recipe;

    private CameraManagerUrl cameraManagerUrl;

    private AddRecipeViewModel addRecipeViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addRecipeViewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);
        //addRecipeViewModel.init();

        //recipeArrayList = loadRecipeViewModel.getRecipeLiveData().getValue();

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_recipe, container, false);

        cameraManagerUrl = CameraManagerUrl.getInstance();

        TitleText = view.findViewById(R.id.fragment_add_recipe_title);
        IngredientsText = view.findViewById(R.id.fragment_add_recipe_ingredients);
        preparationText = view.findViewById(R.id.fragment_add_recipe_preparation);
        picContentView = view.findViewById(R.id.fragment_add_recipe_image);
        floating_attach_recipe = view.findViewById(R.id.fragment_add_floating_attach_new_recipe);

        new Boom(floating_attach_recipe);


        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageView takeApicBtn = view.findViewById(R.id.take_a_pic);
        ImageView galleriaPicBtn = view.findViewById(R.id.add_a_pic);

        //From Camera Button
        new Boom(takeApicBtn);
        takeApicBtn.setOnClickListener(v -> takeApicFromCamera());

        //From Galleria Button
        new Boom(galleriaPicBtn);
        galleriaPicBtn.setOnClickListener(v -> picFromGalleria());

        floating_attach_recipe.setOnClickListener(v -> {
            Log.d("onViewCreated", "floating_attach_recipe: " + AuthAppRepository.getInstance().getCurrentUser());
            //    public Recipe(String firebaseUserMadeId, String title, String preparation, String ingredients, User owner, String imagePath) {

            if (TitleText.toString().isEmpty()) {
                TitleText.requestFocus();
                TitleText.setError("hii");
            }

            Recipe tempRecipe = new Recipe(
                    AuthAppRepository.getInstance().getCurrentUser().getUid(),
                    TitleText.getText().toString(),
                    preparationText.getText().toString(),
                    IngredientsText.getText().toString(),
                    imgUri.toString()

            );
            Log.d("tempRecipe", "onViewCreated: " + tempRecipe);
            addRecipeViewModel.AttachNewRecipe(tempRecipe);

            Navigation.findNavController(v).navigate(R.id.action_addNewRecipeFragment_to_homepageFragment);

        });


    }

    private void picFromGalleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_REQUEST_CODE);
    }

    //Galleria Result
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            try {
                imgUri = data.getData();
                Log.d("onActivityResult", "imgUri: " + imgUri);

                Glide.with(this).load(imgUri).centerCrop().thumbnail(0.10f).into(picContentView);
                //
                FirebaseStorgeRepository.getInstance().UploadFile(imgUri);
            } catch (Exception e) {
                Log.d("picFromGalleria", "onActivityResult: " + e.getMessage());
            }

        }

    }

    private void takeApicFromCamera() {
        //imgUri = cameraManagerUrl.dispatchTakePictureIntent();
        Log.d("takeApicFromCamera", "imgUri: " + imgUri);

        Glide.with(this).load(imgUri).centerCrop().thumbnail(0.10f).into(picContentView);
        FirebaseStorgeRepository.getInstance().UploadFile(imgUri);
    }


}*/

package com.pinky.sharerecipebook.view.fragments;
// https://www.delish.com/cooking/recipe-ideas/a22745193/easy-banana-cake-recipe/

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.astritveliu.boom.Boom;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.pinky.sharerecipebook.R;
import com.pinky.sharerecipebook.models.User;
import com.pinky.sharerecipebook.repositories.AuthRepository;
import com.pinky.sharerecipebook.utils.CameraManagerUrl;
import com.pinky.sharerecipebook.viewmodels.AddRecipeViewModel;


public class AddNewRecipeFragment extends Fragment {
    //
    private Uri photoURI;
    private ActivityResultLauncher<Uri> cameraFullSizeResultLauncher; //big img
    private ActivityResultLauncher<String> pickContentResultLauncher;
    private TextInputLayout TitleText;
    private ImageView picContentView;
    private FloatingActionButton floating_add;
    private CameraManagerUrl cameraManagerUrl;

    private AddRecipeViewModel addRecipeViewModel;


    //

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addRecipeViewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        //recipeArrayList = loadRecipeViewModel.getRecipeLiveData().getValue();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_recipe, container, false);

        cameraManagerUrl = CameraManagerUrl.getInstance();

        TitleText = view.findViewById(R.id.fragment_add_recipe_title);
        picContentView = view.findViewById(R.id.fragment_add_recipe_image);
        floating_add = view.findViewById(R.id.fragment_add_floating_next);

        new Boom(floating_add);
        new Boom(TitleText);

        initLaunchers();

        return view;
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        User LoginUserGet = (User) requireArguments().getSerializable("expandLoginUser");

        TitleText.getEditText().setText(addRecipeViewModel.getmRecipeName());
        if (addRecipeViewModel.getmPhotoURI() != null) {
            Glide.with(this)
                    .load(addRecipeViewModel.getmPhotoURI())
                    .centerCrop()
                    .error(android.R.drawable.ic_dialog_info)
                    .into(picContentView);
        }

        //ImageView takeApicBtn = view.findViewById(R.id.take_a_pic);
        ImageView galleriaPicBtn = view.findViewById(R.id.add_a_pic);

        //From Camera Button
        new Boom(picContentView);
        picContentView.setOnClickListener(v -> takeApicFromCamera());

        //From Galleria Button
        new Boom(galleriaPicBtn);
        galleriaPicBtn.setOnClickListener(v -> picFromGalleria());


        floating_add.setOnClickListener(v -> {
            Log.d("onViewCreated", "floating_attach_recipe: " + AuthRepository.getInstance().getCurrentUser());

            String recipeName = TitleText.getEditText().getText().toString();


            if (photoURI == null) {
                Log.d("floating_attach_recipe", "photoURI is Empty!");
                takeApicFromCamera();
            } else if (recipeName.isEmpty()) {
                TitleText.setError("Give a name");
                TitleText.requestFocus();
            } else {

                addRecipeViewModel.setRecipeName(recipeName);
                addRecipeViewModel.setRecipephotoURI(photoURI.toString());
                addRecipeViewModel.setLoginUserGet(LoginUserGet);

                Bundle bundle = new Bundle();
                bundle.putString("expandRecipeName", recipeName);
                bundle.putString("expandPhotoUriString", photoURI.toString());
                bundle.putSerializable("expandLoginUser", LoginUserGet);

                Navigation.findNavController(v).navigate(R.id.action_addNewRecipeFragment_to_addNewRecipeFragment2, bundle);
            }

        });
    }

    private void initLaunchers() {
        // camera
        cameraFullSizeResultLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                result -> {
                    if (result) { //true if the image saved to the uri given in the launch function
                        Glide.with(this)
                                .load(photoURI)
                                .centerCrop()
                                //.placeholder(R.drawable.common_google_signin_btn_icon_dark) // todo change img or not need?
                                .error(android.R.drawable.ic_dialog_info)
                                .into(picContentView);
                    }

                });

        // gallery
        pickContentResultLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                result -> {
                    if (result != null) {
//                        picContentView.setCropToPadding(false);
                        photoURI = result;
                        Glide.with(this)
                                .load(photoURI)
                                .centerCrop()
                                //.placeholder(R.drawable.common_google_signin_btn_icon_dark) // todo change img or not need?
                                .error(android.R.drawable.ic_dialog_info)
                                .into(picContentView); // todo add try?

                    }
                });
    }

    private void picFromGalleria() {
        pickContentResultLauncher.launch("image/*");
    }


    private void takeApicFromCamera() {
        photoURI = CameraManagerUrl.getInstance().dispatchTakePictureIntent();
        cameraFullSizeResultLauncher.launch(photoURI);
    }

}
