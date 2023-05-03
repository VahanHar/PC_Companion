package com.example.pcplanner.MainMenu.Admin;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Intel2Admin extends AppCompatActivity {


    TextInputEditText brandEditText, modelEditText, characteristicsEditText, itemNameEditText, itemPriceEditText;
    Button modifyItemButton;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intel2);

        firestore = FirebaseFirestore.getInstance();

        brandEditText = findViewById(R.id.textInput_collectionName);
        brandEditText.setSingleLine(true);
        brandEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        modelEditText = findViewById(R.id.textInput_subCollectionName);
        modelEditText.setSingleLine(true);
        modelEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        characteristicsEditText = findViewById(R.id.textInput_subDocumentName);
        characteristicsEditText.setSingleLine(true);
        characteristicsEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        itemNameEditText = findViewById(R.id.editText_itemName);
        itemNameEditText.setSingleLine(true);
        itemNameEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        itemPriceEditText = findViewById(R.id.editText_itemPrice);
        itemPriceEditText.setSingleLine(true);
        itemPriceEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        modifyItemButton = findViewById(R.id.button_modfyItem);

        modifyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String brand = brandEditText.getText().toString().trim();
                if (!brand.isEmpty()) {
                    brand = brand.substring(0, 1).toUpperCase() + brand.substring(1).toLowerCase();
                }

                String model = modelEditText.getText().toString().trim();
                if (!model.isEmpty()) {
                    model = model.substring(0, 1).toUpperCase() + model.substring(1).toLowerCase();
                }

                String characteristics = characteristicsEditText.getText().toString().trim();
                if (!characteristics.isEmpty()) {
                    characteristics = characteristics.substring(0, 1).toUpperCase() + characteristics.substring(1).toLowerCase();
                }

                String itemName = itemNameEditText.getText().toString().trim();
                if (!itemName.isEmpty()) {
                    itemName = itemName.substring(0, 1).toUpperCase() + itemName.substring(1).toLowerCase();
                }

                String priceStr = itemPriceEditText.getText().toString().trim();
                int itemPrice = 0;

                if (!priceStr.isEmpty()) {
                    itemPrice = Integer.parseInt(priceStr);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a price", Toast.LENGTH_SHORT).show();
                }



                if (brand.isEmpty() || model.isEmpty() || characteristics.isEmpty() || itemName.isEmpty() || priceStr.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();

                } else {
                    Map<String, Object> item = new HashMap<>();
                    item.put("name", itemName);
                    item.put("price", itemPrice);


                    CollectionReference parentCollectionRef = firestore.collection("PC Components");
                    DocumentReference cpuDocRef = parentCollectionRef.document("CPU");
                    CollectionReference amdCollectionRef = cpuDocRef.collection("INTEL");
                    DocumentReference brandDocRef = amdCollectionRef.document(brand);
                    CollectionReference modelCollectionRef = brandDocRef.collection(model);
                    DocumentReference characteristicsDocRef = modelCollectionRef.document(characteristics);


                    characteristicsDocRef.set(item)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                                    brandEditText.setText("");
                                    modelEditText.setText("");
                                    characteristicsEditText.setText("");
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