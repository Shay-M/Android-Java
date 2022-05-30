package com.example.shiftmanagerhit.ui.addEmployee;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanagerhit.DatabaseCommunication.FirebaseEvents;
import com.example.shiftmanagerhit.DatabaseCommunication.FirebaseManager;
import com.example.shiftmanagerhit.Date.UserDate;
import com.example.shiftmanagerhit.NavigationDrawerActivity;
import com.example.shiftmanagerhit.R;
import com.example.shiftmanagerhit.Utility.HidesKeyboard;
import com.example.shiftmanagerhit.databinding.FragmentSlideshowBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class AddEmployeeFragment extends Fragment implements View.OnClickListener {

    //
    AddEmployeeViewModel addEmployeeViewModel;
    EditText mEmail, mPassword, mName;
    Button addEmployeeBtn;
    CheckBox isManger;
    private FirebaseManager FBM;
    private FragmentSlideshowBinding binding;
    //
    private AlertDialog progressDialog;

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addEmployeeViewModel =
                new ViewModelProvider(this).get(AddEmployeeViewModel.class);

        binding = FragmentSlideshowBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

       /* final TextView textView = binding.textSlideshow;//<<<<
        slideshowViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        addEmployeeBtn = root.findViewById(R.id.newEmployeeBtn);
        addEmployeeBtn.setOnClickListener(this);

        isManger = root.findViewById(R.id.isManger);
        mEmail = root.findViewById(R.id.eT_PersonEmail);
        mPassword = root.findViewById(R.id.eT_PersonPassword);
        mName = root.findViewById(R.id.eT_PersonName);
//        mSalary = root.findViewById(R.id.eT_PersonSalary);

        NavigationDrawerActivity temp = (NavigationDrawerActivity) getActivity();
        FBM = Objects.requireNonNull(temp, "AddEmployeeFragment FBM is null!").FBM;

        progressDialog = new AlertDialog.Builder(this.getContext()).create();


        return root;
    }

    @Override
    public void onDestroyView() {
        HidesKeyboard.hideKeyboard(getActivity());
        super.onDestroyView();
        binding = null;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {


        if (v.getId() == R.id.newEmployeeBtn) {
            if (mEmail.getText().toString().isEmpty()) {
                mEmail.setError("Full name is required!");
                mEmail.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(mEmail.getText().toString()).matches()) {
                mEmail.setError("Email is not good ");
                mEmail.requestFocus();
                return;
            }

            if (mPassword.getText().toString().isEmpty()) {
                mPassword.setError("Password is required!");
                mPassword.requestFocus();
                return;
            }

            if (mPassword.getText().toString().length() < 6) {
                mPassword.setError("Password is 6 or more");
                mPassword.requestFocus();
                return;
            }


            //HidesKeyboard.hideKeyboard(Snackbar());
            progressDialog.setMessage("Adding this user, Please wait ...");
            progressDialog.show();

            UserDate newUser = new UserDate(mName.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString(), isManger.isChecked());

            FBM.createUser(newUser, new FirebaseEvents.OnCreateUser() {
                @Override
                public void SucceededCreateUser() {
                    //hide keyboard


                    //resat all inputs
                    isManger.setChecked(false);
                    mEmail.getText().clear();
                    mPassword.getText().clear();
                    mName.getText().clear();

                    //hide progressDialog
                    progressDialog.dismiss();
                }

                @Override
                public void FailedCreateUser() {
                    //hide progressDialog
                    progressDialog.dismiss();

                }
            });
        }

    }
}