package com.vahanhar.pcplanner.MainMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.vahanhar.pcplanner.R;

public class Login extends AppCompatActivity {

    TextInputEditText editTextEmail, editTextPassword;

    Button buttonLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;


        Handler handler = new Handler();
        private boolean isNetworkConnected() {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
        }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null) {
            if(!mAuth.getCurrentUser().isEmailVerified() && !currentUser.getEmail().equals("admin@pcplanner.com")){
                mAuth.signOut();
            }

            progressBar.setVisibility(View.VISIBLE);
            if (currentUser.getEmail().equals("admin@pcplanner.com")) {
                Intent intent = new Intent(getApplicationContext(), SplashAdmin.class);
                startActivity(intent);
            } else if(currentUser.isEmailVerified()){
                    Intent intent = new Intent(getApplicationContext(), Splash2Activity.class);
                    startActivity(intent);
                    finish();
            }
            else{
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (!isNetworkConnected()) {
                Toast.makeText(this, "No internet connection. Running in offline mode.", Toast.LENGTH_SHORT).show();
            }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword = findViewById(R.id.password);

        editTextPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        buttonLogin = findViewById(R.id.btn_login);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.registerNow);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });


        TextInputEditText editTextPassword = findViewById(R.id.password);
        editTextPassword.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                new Handler().postDelayed(() -> {
                    Button buttonLogin = findViewById(R.id.btn_login);
                    buttonLogin.performClick();
                }, 100);
                // Hide keyboard after login button is clicked
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                return true;
            }
            return false;
        });



        TextView forgotPasswordTextView = findViewById(R.id.forgot_password);
        forgotPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(editTextEmail.getText());

                // Check if email is valid

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Password reset email sent.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Login.this, "Failed to send password reset email.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


        Button buttonGuest = findViewById(R.id.btn_guest);
               buttonGuest.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);

                    if (!isNetworkConnected()) {
                        // No internet connection, go to MainActivity directly
                        Intent intent = new Intent(Login.this, Splash2Activity.class);
                        startActivity(intent);
                        finish();

                    } else {
                        mAuth.signInAnonymously().addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                // Hide keyboard after login button is clicked
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        Intent intent = new Intent(getApplicationContext(), Splash2Activity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                    Toast.makeText(getApplicationContext(),"Login as guest Successful",Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                                progressBar.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            });

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter email", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    // Hide keyboard after login button is clicked
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    // Hide keyboard after login button is clicked
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                // Hide keyboard after login button is clicked
                                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                if (imm != null) {
                                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                                if (task.isSuccessful()) {


                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null) {
                                        if (user.getEmail().equals("admin@pcplanner.com")) {
                                            // Redirect the user to the admin activity
                                            // Hide keyboard after login button is clicked
                                            InputMethodManager imm2 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                            if (imm2 != null) {
                                                imm2.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                            }
                                            Intent intent = new Intent(getApplicationContext(), SplashAdmin.class);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            //TO MAIN ACTIVITY
                                            if(mAuth.getCurrentUser().isEmailVerified()){
                                                Intent intent = new Intent(getApplicationContext(), Splash2Activity.class);
                                                startActivity(intent);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(), "Please confirm your email address", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }

                                } else {
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }

                                // Hide keyboard after login button is clicked
                                InputMethodManager imm1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                if (imm1 != null) {
                                    imm1.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                }
                            }
                        });
            }
        });

    }
}




