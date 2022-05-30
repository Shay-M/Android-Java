package com.pinky.sharerecipebook.view.fragments;
// https://android-arsenal.com/details/1/7136

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.astritveliu.boom.Boom;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.pinky.sharerecipebook.R;
import com.pinky.sharerecipebook.models.User;
import com.pinky.sharerecipebook.repositories.AuthRepository;
import com.pinky.sharerecipebook.repositories.FirebaseDatabaseRepository;
import com.pinky.sharerecipebook.viewmodels.LoginViewModel;

public class LoginPageFragment extends Fragment {
    // private EditText emailEditText;
    // private EditText passwordEditText;
    private TextInputLayout passwordEditText;
    private TextInputLayout emailEditText;
    private Button loginButton;
    private Button registerButton;
    private String email = "";
    private TextView ResatPasswordTextView;
    private ProgressBar progressBarLogin;
    private View rootView;

    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();

        boolean cameFromAddRecipeBtn = (boolean) requireArguments().getSerializable("cameFromAddRecipeButton");
        //boolean cameFromAddRecipeBtn = (boolean) requireArguments().getBoolean("cameFromAddRecipeButton");

        if (actionBar != null) {
            actionBar.hide();
        }

        super.onCreate(savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);


        // if login go to addNewRecipeFragment or homepage, depending on where the user came from
        loginViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) { //skype login fragment
                    //UserLoginHelper.getInstance().setUser(firebaseUser.getEmail());
                    String userID = firebaseUser.getUid();
                    User user = FirebaseDatabaseRepository.getInstance().getUserByIdFromFirebase(userID).getValue();
                    progressBarLogin.setVisibility(View.GONE);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("expandLoginUser", user);
                    if (cameFromAddRecipeBtn) {
                        Navigation.findNavController(getView()).navigate(R.id.action_loginPageFragment_to_addNewRecipeFragment, bundle);
                    } else {
                        Navigation.findNavController(getView()).navigate(R.id.action_loginPageFragment_to_homepageFragment, bundle);
                    }
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login_page, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Login");

        emailEditText = rootView.findViewById(R.id.fragment_login_email_textinput);
        passwordEditText = rootView.findViewById(R.id.fragment_login_password_textinput);
        loginButton = rootView.findViewById(R.id.fragment_login_login_button);
        registerButton = rootView.findViewById(R.id.fragment_login_register_button);
        ResatPasswordTextView = rootView.findViewById(R.id.fragment_login_resat_password);
        progressBarLogin = rootView.findViewById(R.id.fragment_login_progressBar);

        progressBarLogin.setVisibility(View.GONE);

        new Boom(ResatPasswordTextView);


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //user click button login
        loginButton.setOnClickListener(view1 -> {

            //String email ??need like that?
            email = emailEditText.getEditText().getText().toString();
            String password = passwordEditText.getEditText().getText().toString();

            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailEditText.setError("Enter Email");
                emailEditText.requestFocus();
                Log.d("onClick", "email.isEmpty()");

            } else if (password.isEmpty() || password.length() < 6) {
//                Toast.makeText(getContext(), "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                passwordEditText.setError("Password length must be more than 6");
                passwordEditText.requestFocus();
                Log.d("onClick", "password.isEmpty() || password.length()");

            } else {
                //HidesKeyboard.hideKeyboard(getActivity());// hmm

                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(rootView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                progressBarLogin.setVisibility(View.VISIBLE);

                loginViewModel.login(email, password, new AuthRepository.OnTaskLoginAuth() {
                    @Override
                    public void onSuccessLogin() {
                        progressBarLogin.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailureLogin(String s) {
                        progressBarLogin.setVisibility(View.GONE);

                        Snackbar.make(rootView, s, BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                });

            }
        });

        registerButton.setOnClickListener(view2 -> {
            Navigation.findNavController(getView()).navigate(R.id.action_loginPageFragment_to_registerFragment);
        });

        // resat
        ResatPasswordTextView.setOnClickListener(v -> {
            email = emailEditText.getEditText().getText().toString();
            if (!email.isEmpty()) {
                loginViewModel.resatPassword(email);
                Log.d("TAG", "onViewCreated: ");
                Snackbar.make(rootView, "We'll send you a password reset link to " + email, BaseTransientBottomBar.LENGTH_SHORT).show();
            } else
                Snackbar.make(rootView, "An email must be filled in to perform a password reset", BaseTransientBottomBar.LENGTH_SHORT).show();

        });
    }
}

// https://material.io/components/text-fields/android#filled-text-field