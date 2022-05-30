package com.example.shiftmanagerhit.ui.EmployeeSelectsShifts;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanagerhit.DatabaseCommunication.FirebaseManager;
import com.example.shiftmanagerhit.Date.UserDate;
import com.example.shiftmanagerhit.NavigationDrawerActivity;
import com.example.shiftmanagerhit.R;
import com.example.shiftmanagerhit.databinding.FragmentHomeBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Objects;

public class EmployeeSelectsShiftsFragment extends Fragment {

    // key for shared preferences
    public static final String myPreference = "my_pref";
    //key for ids list
    public static final String List = "listKey";
    EmployeeSelectsShiftsViewModel employeeSelectsShiftsViewModel;
    Button saveButton;
    Button sandToManagerButton;
    //
    private SharedPreferences sharedpreferences;
    private UserDate user;
    private FirebaseManager FBM;
    //Fragment element's
    private ChipGroup chipGroup;
    private FragmentHomeBinding binding;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        employeeSelectsShiftsViewModel =
                new ViewModelProvider(this).get(EmployeeSelectsShiftsViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

//        final TextView textView = binding.textHome;
//        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        NavigationDrawerActivity temp = (NavigationDrawerActivity) getActivity();
        FBM = Objects.requireNonNull(temp).FBM;
        user = temp.signInUser;

        Doc(root);

        return root;
    }

    @Override
    public void onDestroyView() {
        saveChipsOnDevice();
        super.onDestroyView();
        binding = null;
    }

    public void load_chip_settings() {
        sharedpreferences = requireActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);
//        sharedpreferences = getActivity().getSharedPreferences(myPreference, Context.MODE_PRIVATE);


        if (sharedpreferences.contains(List)) {
            String string_of_ids_chip = sharedpreferences.getString(List, "");

            // removing "[ ]"
            string_of_ids_chip = string_of_ids_chip.substring(1, string_of_ids_chip.length() - 1);
            // removing  whitespace
            string_of_ids_chip = string_of_ids_chip.trim();
            //convert to arrayList
            ArrayList<Integer> Array_list_of_ids = new ArrayList<>();

            for (String token : string_of_ids_chip.split(", ")) {
                if (token.length() > 2)
                    Array_list_of_ids.add(Integer.parseInt(token));
            }
//            Log.d("this is ArrayList", "" + Array_list_of_ids);

            for (int id : Array_list_of_ids) {

                Chip chip = chipGroup.findViewById(id);
//                Log.d("chipGroup", "" + chip.getTag());
//                Log.d("chipGroup", "" + chip.getText());
                chip.setChecked(true);
            }

//            user.setShift(string_of_ids_chip);

        }//no shared-preferences

    }

    private void Doc(View root) {
        chipGroup = root.findViewById(R.id.chip_group);
        saveButton = root.findViewById(R.id.save_button);
        sandToManagerButton = root.findViewById(R.id.sand_button);

        // fun load all id's chips using SharedPreferences
        load_chip_settings();

        // save all id's chips using SharedPreferences  -----
        saveButton.setOnClickListener(v -> saveChipsOnDevice());

        //send Date to DB
        sandToManagerButton.setOnClickListener(v -> {

            java.util.List<Integer> ids = chipGroup.getCheckedChipIds();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(List, ids.toString());
            editor.apply();

            Log.d("sand to manager", "" + ids.toString());
            //save to userDate class
            user.setShiftWants(ids.toString());

//            FBM.sendDataFirebase(user,"shiftGet");
            FBM.sendDataFirebase(user, "shiftWants");

        });
    }

    private void saveChipsOnDevice() {

        //list of chips ids
        java.util.List<Integer> ids = chipGroup.getCheckedChipIds();

        SharedPreferences.Editor editor = sharedpreferences.edit();
        Log.d("save on device", "" + ids.toString());
        editor.putString(List, ids.toString());
        editor.apply();

            /*
            //save to userDate class
            user.setShift(ids.toString());
            */
    }
}