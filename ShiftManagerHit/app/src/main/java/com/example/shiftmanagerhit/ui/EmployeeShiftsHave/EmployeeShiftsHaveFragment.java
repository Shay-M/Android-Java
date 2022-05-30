package com.example.shiftmanagerhit.ui.EmployeeShiftsHave;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.shiftmanagerhit.DatabaseCommunication.FirebaseEvents;
import com.example.shiftmanagerhit.DatabaseCommunication.FirebaseManager;
import com.example.shiftmanagerhit.Date.UserDate;
import com.example.shiftmanagerhit.NavigationDrawerActivity;
import com.example.shiftmanagerhit.R;
import com.example.shiftmanagerhit.databinding.FragmentGalleryBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;

public class EmployeeShiftsHaveFragment extends Fragment {

    //Fragment element's
    public ChipGroup chipGroup;
    public Button calculateSalaryButton;
    public Button SyncButton;

    UserDate user;
    FirebaseManager FBM;
    EmployeeShiftsHaveViewModel employeeShiftsHaveViewModel;
    ArrayList<Integer> Array_list_of_ids;

    private int day;
    private int shift;


    private FragmentGalleryBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        employeeShiftsHaveViewModel =
                new ViewModelProvider(this).get(EmployeeShiftsHaveViewModel.class);

        binding = FragmentGalleryBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        NavigationDrawerActivity temp = (NavigationDrawerActivity) getActivity();
        FBM = Objects.requireNonNull(temp).FBM;
        user = temp.signInUser;

        Doc(root);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void load_chip_from_DB() {

        FBM.getDataFirebase(user.getId(), new FirebaseEvents.OnGetData() {
            @Override
            public void SucceededGetData(UserDate UserSignIn) {
                user = UserSignIn;

                String string_of_ids_chip = user.getShiftGet();

                if (!string_of_ids_chip.trim().isEmpty()) {

                    // removing "[ ]"
                    string_of_ids_chip = string_of_ids_chip.substring(1, string_of_ids_chip.length() - 1);
                    // removing  whitespace
                    string_of_ids_chip = string_of_ids_chip.trim();
                    //convert to arrayList

                    Array_list_of_ids = new ArrayList<>();

                    for (String token : string_of_ids_chip.split(", ")) {
                        if (token.length() > 2)
                            Array_list_of_ids.add(Integer.parseInt(token));

                    }
                    //resat chips
                    resat_chips();

                    // set on chips from the list
                    for (int id : Array_list_of_ids) {
                        Chip chip = chipGroup.findViewById(id);
                        chip.setChecked(true);
                    }

                }
            }

            @Override
            public void FailedGetData(UserDate UserSignIn) {
                resat_chips();
                calculateSalaryButton.setOnClickListener(null);
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void Doc(View root) {
        chipGroup = root.findViewById(R.id.chip_group);
        calculateSalaryButton = root.findViewById(R.id.calculate_salary);
        SyncButton = root.findViewById(R.id.synchronize_with_calendar);

        // fun load all id's chips from DB
        load_chip_from_DB();

        calculateSalaryButton.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
            View dialogView = getLayoutInflater().inflate(R.layout.sign_dialog, null);

            TextView salaryText = dialogView.findViewById(R.id.salary);

            salaryText.setText("Hello " + user.getName() + ", you will work " + chipGroup.getCheckedChipIds().size() + " shifts and the expected salary is: " + chipGroup.getCheckedChipIds().size() * 30 + " ₪.");
            builder.setView(dialogView).setPositiveButton("Ok", (dialog, which) -> {
            }).show();
        });

        SyncButton.setOnClickListener(v -> {

            Date currentTime = java.util.Calendar.getInstance().getTime();

//            Log.d("SyncButton", "currentTime: " + currentTime);

            for (int id : chipGroup.getCheckedChipIds()) {
                Chip chip = chipGroup.findViewById(id);

                int intInTag = Integer.parseInt(chip.getTag().toString());

                day = (int) Math.ceil(intInTag / 3.0);
                shift = intInTag % 3;


            }
            LocalDate.now().with(next(SUNDAY));

            Intent intent = new Intent(Intent.ACTION_INSERT);
            intent.setData(CalendarContract.Events.CONTENT_URI);
            intent.putExtra(CalendarContract.Events.STATUS, CalendarContract.Events.STATUS_CONFIRMED);
            Calendar fixTime = Calendar.getInstance();


            fixTime.set(currentTime.getYear(), currentTime.getMonth(), currentTime.getDay(), 0, 0, 0);
            fixTime.add(Calendar.DATE, day);
            //fixTime.add(Calendar.YEAR, currentTime.getYear());
//            fixTime.add(Calendar.YEAR, currentTime.getYear());
            Log.d("TAG", "fixTime: "+fixTime);
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, fixTime.getTimeInMillis());
            startActivity(intent);
        });
    }

    private void resat_chips() {
        for (int i = 0; i < chipGroup.getChildCount(); i++)
            if (chipGroup.getChildAt(i) instanceof com.google.android.material.chip.Chip)
                ((Chip) chipGroup.getChildAt(i)).setChecked(false);
    }
}