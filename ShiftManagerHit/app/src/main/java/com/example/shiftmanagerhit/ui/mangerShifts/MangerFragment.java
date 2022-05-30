package com.example.shiftmanagerhit.ui.mangerShifts;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanagerhit.DatabaseCommunication.FirebaseEvents;
import com.example.shiftmanagerhit.DatabaseCommunication.FirebaseManager;
import com.example.shiftmanagerhit.Date.UserDate;
import com.example.shiftmanagerhit.NavigationDrawerActivity;
import com.example.shiftmanagerhit.R;
import com.example.shiftmanagerhit.databinding.FragmentManagerBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MangerFragment extends Fragment {

    public ChipGroup chipGroup;
    public Button sandToEmployeeButton;

    MangerViewModel mangerViewModel;
    private FragmentManagerBinding binding;
    //    private UserDate user;
    private UserDate userLoad = new UserDate();
    private FirebaseManager FBM;
    //
    private Spinner spinnerAllEmployees;
    private List<String> listAllNamesOfEmployees;


    @SuppressLint("ResourceAsColor")
    public void load_chip_from_DB() {

        FBM.getDataFirebase(userLoad.getId(), new FirebaseEvents.OnGetData() {
            @Override
            public void SucceededGetData(UserDate UserSignIn) {
                userLoad = UserSignIn;

                String string_of_ids_chip = userLoad.getShiftWants();


//                string_of_ids_chip.trim()
                if (!string_of_ids_chip.trim().isEmpty()) {
                    // removing "[ ]"
                    string_of_ids_chip = string_of_ids_chip.substring(1, string_of_ids_chip.length() - 1);
                    // removing  whitespace
                    string_of_ids_chip = string_of_ids_chip.trim();
                    //convert to arrayList

                    //
                    ArrayList<Integer> array_list_of_ids = new ArrayList<>();

                    for (String token : string_of_ids_chip.split(", "))
                        if (token.length() > 2)
                            array_list_of_ids.add(Integer.parseInt(token));

                    //resat chips
                    resat_chips();

                    for (int id : array_list_of_ids) {
                        Chip chip = chipGroup.findViewById(id);
                        if (chip != null)
                            chip.setChipBackgroundColorResource(R.color.select_chip);

                    }

                } else resat_chips();

            }

            @Override
            public void FailedGetData(UserDate UserSignIn) {
                resat_chips();
                sandToEmployeeButton.setOnClickListener(null);
            }
        });


    }

    /**
     * resat chips in chipGroup , unChecked and color rad
     */
    private void resat_chips() {

        for (int i = 0; i < chipGroup.getChildCount(); i++)
            if (chipGroup.getChildAt(i) instanceof com.google.android.material.chip.Chip) {
                ((Chip) chipGroup.getChildAt(i)).setChipBackgroundColorResource(R.color.c5);
                ((Chip) chipGroup.getChildAt(i)).setChecked(false);
            }
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mangerViewModel =
                new ViewModelProvider(this).get(MangerViewModel.class);

        binding = FragmentManagerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        NavigationDrawerActivity temp = (NavigationDrawerActivity) getActivity();
        FBM = Objects.requireNonNull(temp).FBM;
//        user = temp.signInUser;

        chipGroup = root.findViewById(R.id.chip_group);
        sandToEmployeeButton = root.findViewById(R.id.sand_button);

        listAllNamesOfEmployees = new ArrayList<>();
        FBM.getAllEmployeesFirebase(new FirebaseEvents.OnDataChangeGetAllEmployees() {
            @Override
            public void SucceededGetAllEmployees(List<UserDate> AllEmployees) {

                //get names of all from Employees list
                for (UserDate employee : AllEmployees)
                    listAllNamesOfEmployees.add("Shifts " + employee.getName().toUpperCase() + " asked for");


                spinnerAllEmployees = root.findViewById(R.id.spinner);

                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_spinner_item, listAllNamesOfEmployees);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinnerAllEmployees.setAdapter(spinnerArrayAdapter);

                spinnerAllEmployees.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        String selectedItemText = (String) parent.getItemAtPosition(position);

                        userLoad = AllEmployees.get(position);

                        // load user selected
                        load_chip_from_DB();

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }

            @Override
            public void FailedGetAllEmployees() {
                listAllNamesOfEmployees = null;
            }
        });

        sandToEmployeeButton.setOnClickListener(v -> {
            java.util.List<Integer> ids = chipGroup.getCheckedChipIds();
            userLoad.setShiftWants(ids.toString());
            //FBM.signIn(userLoad.getEmail(), userLoad.getPassword());
            FBM.sendDataFirebase(userLoad, "shiftGet");
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
