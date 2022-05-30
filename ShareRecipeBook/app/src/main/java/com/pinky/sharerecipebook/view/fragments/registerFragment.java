package com.pinky.sharerecipebook.view.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;
import com.pinky.sharerecipebook.R;
import com.pinky.sharerecipebook.viewmodels.LoginViewModel;


public class registerFragment extends Fragment {

    private TextInputLayout passwordEditText;
    private TextInputLayout emailEditText;
    private TextInputLayout nameEditText;
    private Button registerButton;
//    private String email = "";

    private LoginViewModel loginViewModel;


    public registerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        loginViewModel.getUserLiveData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) { // if login go to addNewRecipeFragment
                    Navigation.findNavController(getView()).navigate(R.id.action_registerFragment_to_homepageFragment);
                }
            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Register");

        emailEditText = view.findViewById(R.id.fragment_register_email_textinput);
        nameEditText = view.findViewById(R.id.fragment_register_name_textinput);
        passwordEditText = view.findViewById(R.id.fragment_register_password_textinput);
        registerButton = view.findViewById(R.id.fragment_register_register_button);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        registerButton.setOnClickListener(view12 -> {
            String name = nameEditText.getEditText().getText().toString();
            String email = emailEditText.getEditText().getText().toString();
            String password = passwordEditText.getEditText().getText().toString();

            // name
            if (name.trim().isEmpty()) {
                nameEditText.setError(getString(R.string.error_fill_name));
                nameEditText.requestFocus();
                Log.d("onClick", "name.isEmpty()");
                // EMAIL
            } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                //Toast.makeText(getContext(), "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                emailEditText.setError(getString(R.string.error_fill_email));
                emailEditText.requestFocus();
                Log.d("onClick", "Email");
                // password
            } else if (password.isEmpty() || password.length() < 6) {
                passwordEditText.setError(getString(R.string.error_fill_password));
                passwordEditText.requestFocus();
                Log.d("onClick", "password");
            } else {
                Snackbar.make(this.getView(), getString(R.string.login_wait), BaseTransientBottomBar.LENGTH_SHORT).show();

                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        loginViewModel.register(email, password, name, task.getResult());
                    }
                });
            }
        });
    }
}