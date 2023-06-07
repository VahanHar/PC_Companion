package com.vahanhar.pcplanner.MainMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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

import com.vahanhar.pcplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {
    TextInputEditText editTextEmail, editTextPassword1, editTextPassword2;
    Button buttonReg;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    private boolean isValidEmail(CharSequence email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword1 = findViewById(R.id.password1);
        editTextPassword2 = findViewById(R.id.password2);
        buttonReg = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);
        textView = findViewById(R.id.loginNow);

        editTextPassword1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editTextPassword2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        TextInputEditText editTextPassword2 = findViewById(R.id.password2);
        editTextPassword2.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                new Handler().postDelayed(() -> {
                    Button buttonReg = findViewById(R.id.btn_register);
                    buttonReg.performClick();
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


        buttonReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email,password1,password2;
                email = String.valueOf(editTextEmail.getText());
                password1 = String.valueOf(editTextPassword1.getText());
                password2 = String.valueOf(editTextPassword2.getText());


                // Check if email is valid
                if(!isValidEmail(email)){
                    Toast.makeText(Register.this, "Invalid email address",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    // Hide keyboard after login button is clicked
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return;
                }


                if(TextUtils.isEmpty(email)){
                    Toast.makeText(Register.this, "Enter email",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    // Hide keyboard after login button is clicked
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return;
                }



                if(TextUtils.isEmpty(password1)){
                    Toast.makeText(Register.this, "Enter password",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    // Hide keyboard after login button is clicked
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return;
                }
                if(TextUtils.isEmpty(password2)){
                    Toast.makeText(Register.this, "Repeat password",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    // Hide keyboard after login button is clicked
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    return;
                }
                String password3 = null;
                if(password1.equals(password2)){
                    password3 = password1;
                }
                else{
                    Toast.makeText(Register.this, "Your passwords do not match",Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }


                if(password3!=null) {
                    mAuth.createUserWithEmailAndPassword(email, password3)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    progressBar.setVisibility(View.GONE);

                                    if (task.isSuccessful()) {

                                        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if(task.isSuccessful()){
                                                    Toast.makeText(Register.this, "Please verify your email address",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                                else{
                                                    Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else {

                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(Register.this, "Registration failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    // Hide keyboard after login button is clicked
                                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                                    if (imm != null) {
                                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                                    }
                                }
                            });
                }

            }
        });


    }
}