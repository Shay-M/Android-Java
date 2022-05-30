package com.pinky.sharerecipebook.view.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.astritveliu.boom.Boom;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.pinky.sharerecipebook.R;
import com.pinky.sharerecipebook.models.Recipe;
import com.pinky.sharerecipebook.models.User;
import com.pinky.sharerecipebook.repositories.AuthRepository;
import com.pinky.sharerecipebook.repositories.FirebaseStorgeRepository;
import com.pinky.sharerecipebook.utils.HidesKeyboard;
import com.pinky.sharerecipebook.view.LoadingDialog;
import com.pinky.sharerecipebook.viewmodels.AddRecipeViewModel;


public class AddNewRecipeFragment2 extends Fragment {
    private TextInputLayout IngredientsText;
    private TextInputLayout preparationText;
    private FloatingActionButton floating_attach_recipe;
    private LoadingDialog loadingDialog;

    private AddRecipeViewModel addRecipeViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addRecipeViewModel = new ViewModelProvider(this).get(AddRecipeViewModel.class);
        //addRecipeViewModel.init();

        //recipeArrayList = loadRecipeViewModel.getRecipeLiveData().getValue();
        loadingDialog = new LoadingDialog(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_new_recipe2, container, false);

        IngredientsText = view.findViewById(R.id.fragment_add2_recipe_ingredients);
        preparationText = view.findViewById(R.id.fragment_add2_recipe_preparation);

        floating_attach_recipe = view.findViewById(R.id.fragment_add_floating2_attach_new_recipe);

        new Boom(floating_attach_recipe);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String mTitleText = requireArguments().getString("expandRecipeName");
        String mPhotoUriString = requireArguments().getString("expandPhotoUriString");
        User mLoginUserGet = (User) requireArguments().getSerializable("expandLoginUser");

        /*String mmmTitleText = addRecipeViewModel.getmRecipeName();
        String photoURIString = addRecipeViewModel.getmPhotoURI();
        User LoginUserGet = addRecipeViewModel.getmLoginUserGet();*/

        Uri photoURI = Uri.parse(mPhotoUriString); // need?

        floating_attach_recipe.setOnClickListener(v -> {
            Log.d("onViewCreated", "floating_attach_recipe: " + AuthRepository.getInstance().getCurrentUser());

            if (IngredientsText.getEditText().getText().toString().isEmpty()) {
                IngredientsText.requestFocus();
                IngredientsText.setError("what the Ingredients");
            } else if (preparationText.getEditText().getText().toString().isEmpty()) {
                preparationText.requestFocus();
                preparationText.setError("preparation is Empty");
            } else {
                loadingDialog.startLoadingDialog();

                HidesKeyboard();
                // upload img to Firebase Storge
                FirebaseStorgeRepository.getInstance().UploadFile(photoURI, "recipe", new FirebaseStorgeRepository.OnTaskDownloadUri() {
                    @Override
                    public void onSuccess(Uri downloadUri) {

                        Recipe tempRecipe = new Recipe(
                                AuthRepository.getInstance().getCurrentUser().getUid(), // same? AuthRepository.getInstance().getUid()
                                (mLoginUserGet).getDeviceTokenId(),
                                mTitleText,
                                preparationText.getEditText().getText().toString(),
                                IngredientsText.getEditText().getText().toString(),
                                downloadUri.toString(),
                                ""
                        );

                        addRecipeViewModel.AttachNewRecipe(tempRecipe); // add to db
                        HidesKeyboard();
                        loadingDialog.dismissLoadingDialog();


                        Navigation.findNavController(v).navigate(R.id.action_addNewRecipeFragment2_to_homepageFragment);
                    }

                    @Override
                    public void onFailure() {
                        //photoURI = null;
                        HidesKeyboard();
                        loadingDialog.dismissLoadingDialog();
                        Log.d("onFailure", "onFailure");

                    }

                    @Override
                    public void onProgress(double progressNum) { // todo remove?
                        Log.d("onProgress", "progressNum: " + progressNum + " %");
                    }
                });
            }
        });


    }

    public void HidesKeyboard() {
        HidesKeyboard.hideKeyboard(this.getActivity());

    }

}