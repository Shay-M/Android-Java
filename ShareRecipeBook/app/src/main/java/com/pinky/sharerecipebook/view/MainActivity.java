package com.pinky.sharerecipebook.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;

import com.pinky.sharerecipebook.R;
import com.pinky.sharerecipebook.utils.CameraManagerUrl;
import com.pinky.sharerecipebook.utils.MyContextWrapper;

public class MainActivity extends AppCompatActivity {

    /*    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }*/
    private SharedPreferences sharedPreferences;
//    private String language;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.MysplashScreen);
        getSupportActionBar().setTitle("");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // dark Mode
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        //language = sharedPreferences.getString("language", "en");


        boolean darkMode = sharedPreferences.getBoolean("dark", false);
        Log.d("darkMode", " " + darkMode);
        if (darkMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // init camera
        CameraManagerUrl.init(this);


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(newBase);
        String language = sharedPreferences.getString("language", "en");
        super.attachBaseContext(MyContextWrapper.wrap(newBase, language));
    }
}

 /* ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F9D58"));
        ActionBar actionBar;
        actionBar = getSupportActionBar();
        // Set BackgroundDrawable
        actionBar.setBackgroundDrawable(colorDrawable);*/


 /* Locale locale = new Locale("en");
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());*/

///
        /*Context context = MyContextWrapper.wrap(this, "en");
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(context.getResources().getLayout(R.layout.activity_main), null);
        this.setContentView(view);*/