package com.example.shiftmanagerhit;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.shiftmanagerhit.DatabaseCommunication.FirebaseManager;
import com.example.shiftmanagerhit.Date.UserDate;
import com.example.shiftmanagerhit.databinding.ActivityNavigationDrawerBinding;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerActivity extends AppCompatActivity {

    public FirebaseManager FBM;
    public UserDate signInUser;
    public ActivityNavigationDrawerBinding binding;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FBM = new FirebaseManager(this);

        Bundle data = getIntent().getExtras();

        signInUser = data.getParcelable("User_sign_In");
        Log.d("NavigationDrawerActivity", "the user send from login act: " + signInUser.getName());
        Log.d("NavigationDrawerActivity", "is Manger? " + signInUser.getIs_Manger());

        binding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarNavigationDrawer.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_employee_selects_shifts, R.id.nav_my_shifts, R.id.nav_add_employee, R.id.nav_manager)
//                .setDrawerLayout(drawer) // deprecated
                .setOpenableLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        ///////////////////////////////////////////

        // https://stackoverflow.com/questions/35454644/colored-icons-in-navigationview // colorful icons
        navigationView.setItemIconTintList(null);

        // texts name and email fields from header
        TextView userNameTexNav = navigationView.getHeaderView(0).findViewById(R.id.userNameNav);
        TextView userEmailTexNav = navigationView.getHeaderView(0).findViewById(R.id.userEmailNav);
        // set texts name and email from the login user activity
        userNameTexNav.setText(signInUser.getName());
        userEmailTexNav.setText(signInUser.getEmail());

        //if (true) {
        if (!signInUser.getIs_Manger()) {
            // user is a manger so remove 'nav_add_employee' and 'nav_manager'
            navigationView.getMenu().findItem(R.id.nav_add_employee).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_manager).setVisible(false);


        } else {
            // user is a manger so remove 'nav_employee_selects_shifts' and 'nav_my_shifts'
            navigationView.getMenu().findItem(R.id.nav_employee_selects_shifts).setVisible(false);
            navigationView.getMenu().findItem(R.id.nav_my_shifts).setVisible(false);
            //set 'nav_manager' as Home
            navController.navigate(R.id.nav_manager);//home

        }
        //}
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            Log.d("NavigationDrawerActivity", "onOptionsItemSelected: action_logout");
            FBM.signOut();

            Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);// New activity
            startActivity(intent);
            finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_drawer);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}