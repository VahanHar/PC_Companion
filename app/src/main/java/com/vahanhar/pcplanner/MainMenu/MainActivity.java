package com.vahanhar.pcplanner.MainMenu;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.vahanhar.pcplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isTablet()) {
            setContentView(R.layout.activity_main_tablet);
        } else {
            setContentView(R.layout.activity_main);
        }

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        user = auth.getCurrentUser();

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo == null || !networkInfo.isConnected()) {
            textView.setText("(OFFLINE MODE)");
        }
        if(user!=null){
            textView.setText(user.getEmail());
        }



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();

            }
        });

    }

    public void openComparisonActivity(View view) {
        Intent intent = new Intent(this, SuggestPc.class);
        startActivity(intent);
    }

    public void openConfigurationActivity(View view) {
        Intent intent = new Intent(this, ConfigurationActivity.class);
        startActivity(intent);
    }
    public boolean isTablet() {
        // Get the current display metrics
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        // Calculate the screen size in inches
        float screenWidthInches = displayMetrics.widthPixels / displayMetrics.xdpi;
        float screenHeightInches = displayMetrics.heightPixels / displayMetrics.ydpi;
        double screenSizeInches = Math.sqrt(Math.pow(screenWidthInches, 2) + Math.pow(screenHeightInches, 2));

        // Determine if the screen size indicates a tablet
        double tabletScreenSizeThreshold = 7.0; // Adjust this threshold as needed
        return screenSizeInches >= tabletScreenSizeThreshold;
    }
}
