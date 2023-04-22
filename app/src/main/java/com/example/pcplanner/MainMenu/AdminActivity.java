package com.example.pcplanner.MainMenu;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {

    TextInputEditText itemNameEditText, itemPriceEditText;
    Button addItemButton;

    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        itemNameEditText = findViewById(R.id.editText_itemName);
        itemPriceEditText = findViewById(R.id.editText_itemPrice);
        addItemButton = findViewById(R.id.button_addItem);
        firestore = FirebaseFirestore.getInstance();

        addItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemName = itemNameEditText.getText().toString();
                String itemPrice = itemPriceEditText.getText().toString();

                if (itemName.isEmpty() || itemPrice.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter item name and price", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", itemName);
                    item.put("price", itemPrice);

                    CollectionReference itemsRef = firestore.collection("items");

                    itemsRef.add(item)
                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                @Override
                                public void onSuccess(DocumentReference documentReference) {
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
