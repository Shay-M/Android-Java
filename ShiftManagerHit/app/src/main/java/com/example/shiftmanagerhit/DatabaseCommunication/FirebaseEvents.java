package com.example.shiftmanagerhit.DatabaseCommunication;

import com.example.shiftmanagerhit.Date.UserDate;

import java.util.List;

public class FirebaseEvents {


    public interface OnDataChangeGetAllEmployees {
        void SucceededGetAllEmployees(List<UserDate> AllEmployees);

        void FailedGetAllEmployees();
    }


    public interface OnSignIn {
        void SucceededSignIn(UserDate UserSignIn);

        void FailedSignIn();
    }

    public interface OnGetData {
        void SucceededGetData(UserDate UserSignIn);

        void FailedGetData(UserDate UserSignIn);

    }

    public interface OnCreateUser {
        void SucceededCreateUser();

        void FailedCreateUser();
    }

    public interface OnResatPassword {
        void SucceededResatPassword();

        void FailedResatPassword();
    }

}







/*import java.util.Calendar;
import java.util.Date;
//
import java.time.LocalDate;
import static java.time.DayOfWeek.SUNDAY;
import static java.time.temporal.TemporalAdjusters.next;
import static java.time.temporal.TemporalAdjusters.previous;


public class doc {

    public static void main(String[] args) {

        Date currentTime = Calendar.getInstance().getTime();

        System.out.println("currentTime: " + currentTime);

        System.out.println("next SUNDAY: " +  LocalDate.now().with( next( SUNDAY ) ));
//11 + (3-1 )
    }
}
*/
