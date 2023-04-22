package com.example.pcplanner.MainMenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;


public class AdminActivity extends AppCompatActivity {

    TextInputEditText itemNameEditText, itemPriceEditText;
    Button addItemButton;
    Button logoutButton;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        itemNameEditText = findViewById(R.id.editText_itemName);
        itemPriceEditText = findViewById(R.id.editText_itemPrice);
        addItemButton = findViewById(R.id.button_addItem);
        firestore = FirebaseFirestore.getInstance();
        logoutButton = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);







        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if(user == null){
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        }
        else{
            textView.setText(user.getEmail());
        }

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = itemNameEditText.getText().toString();


                if (itemName.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter item name", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> Atom = new HashMap<>();
                    Atom.put("name", itemName);



                    CollectionReference parentCollectionRef = firestore.collection("PC Components");


                    DocumentReference documentRef = parentCollectionRef.document("CPU");


                    CollectionReference subCollectionRef = documentRef.collection("INTEL");

                    DocumentReference subDocumentRef = subCollectionRef.document("Core i7");

                    subDocumentRef.set(Atom)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                                    itemNameEditText.setText("");
                                    itemPriceEditText.setText("");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Error adding item", Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }
        });

    }
}
