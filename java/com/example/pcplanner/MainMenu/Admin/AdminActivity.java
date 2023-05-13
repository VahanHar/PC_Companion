package com.example.pcplanner.MainMenu.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.example.pcplanner.MainMenu.Login;
import com.example.pcplanner.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;


public class AdminActivity extends AppCompatActivity {


    Button logoutButton;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    TextView textView;
    FirebaseUser user;
    private Button Add;
    private Button Modify;
    private Button link;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);


        firestore = FirebaseFirestore.getInstance();
        logoutButton = findViewById(R.id.logout);
        textView = findViewById(R.id.user_details);
        Add = findViewById(R.id.add_button);
        link = findViewById(R.id.link_button);
        Modify = findViewById(R.id.modify_button);




        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, AddActivity.class));
            }
        });

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, LinkActivity.class));
            }
        });

        Modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminActivity.this, ModifyActivity.class));
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

    }
}




//    TextInputEditText itemNameEditText, itemPriceEditText;
//    Button addItemButton;


//        itemNameEditText = findViewById(R.id.editText_itemName);
//        itemPriceEditText = findViewById(R.id.editText_itemPrice);
//        addItemButton = findViewById(R.id.button_addItem);



//        addItemButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String itemName = itemNameEditText.getText().toString();
//
//
//                if (itemName.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "Please enter item name", Toast.LENGTH_SHORT).show();
//                } else {
//                    Map<String, Object> Atom = new HashMap<>();
//                    Atom.put("name", itemName);
//
//
//
//                    CollectionReference parentCollectionRef = firestore.collection("PC Components");
//
//
//                    DocumentReference documentRef = parentCollectionRef.document("CPU");
//
//
//                    CollectionReference subCollectionRef = documentRef.collection("INTEL");
//
//                    DocumentReference subDocumentRef = subCollectionRef.document("Core i7");
//
//
//                    subDocumentRef.set(Atom)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
//                                    itemNameEditText.setText("");
//                                    itemPriceEditText.setText("");
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    Toast.makeText(getApplicationContext(), "Error adding item", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                }
//            }
//        });