package com.vahanhar.pcplanner.MainMenu.Admin;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.vahanhar.pcplanner.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AmdRadeon2Admin extends AppCompatActivity {


    TextInputEditText brandEditText, modelEditText, characteristicsEditText, itemNameEditText, LDateEditText,NCoresEditText,NThreadsEditText
            ,BFreqEditText,MFreqEditText,MPowerEditText,NRamEditText,RTypeEditText,SocketEditText,MPriceEditText,BenchEditText;

    Button modifyItemButton;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amdradeon2);

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

        ///

        itemNameEditText = findViewById(R.id.editText_itemName);
        itemNameEditText.setSingleLine(true);
        itemNameEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        LDateEditText = findViewById(R.id.editText_date);
        LDateEditText.setSingleLine(true);
        LDateEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        NCoresEditText = findViewById(R.id.editText_core);
        NCoresEditText.setSingleLine(true);
        NCoresEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        NThreadsEditText = findViewById(R.id.editText_threads);
        NThreadsEditText.setSingleLine(true);
        NThreadsEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        BFreqEditText = findViewById(R.id.editText_bfreq);
        BFreqEditText.setSingleLine(true);
        BFreqEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        MFreqEditText = findViewById(R.id.editText_nfreq);
        MFreqEditText.setSingleLine(true);
        MFreqEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        MPowerEditText = findViewById(R.id.editText_mpower);
        MPowerEditText.setSingleLine(true);
        MPowerEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        NRamEditText = findViewById(R.id.editText_nram);
        NRamEditText.setSingleLine(true);
        NRamEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        RTypeEditText = findViewById(R.id.editText_rtype);
        RTypeEditText.setSingleLine(true);
        RTypeEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        SocketEditText = findViewById(R.id.editText_socket);
        SocketEditText.setSingleLine(true);
        SocketEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        MPriceEditText = findViewById(R.id.editText_mprice);
        MPriceEditText.setSingleLine(true);
        MPriceEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);

        BenchEditText = findViewById(R.id.editText_bench);
        BenchEditText.setSingleLine(true);
        BenchEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);

        ///
        modifyItemButton = findViewById(R.id.button_modfyItem);

        modifyItemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String brand = brandEditText.getText().toString().trim();
                if (!brand.isEmpty()) {
                    brand = brand.substring(0, 1).toUpperCase() + brand.substring(1).toLowerCase();
                }

                String generation = modelEditText.getText().toString().trim();

                String characteristics = characteristicsEditText.getText().toString().trim();
                if (!characteristics.isEmpty()) {
                    characteristics = characteristics.substring(0, 1).toUpperCase() + characteristics.substring(1).toLowerCase();
                }

                String itemName = itemNameEditText.getText().toString().trim();

                String LDate = LDateEditText.getText().toString().trim();
                String NCores = NCoresEditText.getText().toString().trim();
                String NThreads = NThreadsEditText.getText().toString().trim();
                String BFreq = BFreqEditText.getText().toString().trim();
                String MFreq = MFreqEditText.getText().toString().trim();
                String Mpower = MPowerEditText.getText().toString().trim();
                String Nram = NRamEditText.getText().toString().trim();
                String Rtype = RTypeEditText.getText().toString().trim();
                String Socket = SocketEditText.getText().toString().trim();
                String MPrice = MPriceEditText.getText().toString().trim();

                String benchM = BenchEditText.getText().toString().trim();
                int bench = 0;

                if (!benchM.isEmpty()) {
                    bench = Integer.parseInt(benchM);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter a BenchMark value", Toast.LENGTH_SHORT).show();
                }



                if (brand.isEmpty() || generation.isEmpty() || characteristics.isEmpty() || itemName.isEmpty() || LDate.isEmpty()
                        || NCores.isEmpty()|| NThreads.isEmpty()|| BFreq.isEmpty()|| MFreq.isEmpty()|| Mpower.isEmpty()|| Nram.isEmpty()
                        || Rtype.isEmpty()|| Socket.isEmpty()|| MPrice.isEmpty()
                ) {
                    Toast.makeText(getApplicationContext(), "Please fill out all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Map<String, Object> item = new HashMap<>();
                    item.put("1.Processor Number", itemName);
                    item.put("2.Launch Date", LDate);
                    item.put("3.Number of Cores", NCores);
                    item.put("4.DirectX", NThreads);
                    item.put("5.Base Frequency", BFreq);
                    item.put("6.Maximum Frequency", MFreq);
                    item.put("7.Maximum Power usage", Mpower);
                    item.put("8.Memory", Nram);
                    item.put("9.Memory Type", Rtype);
                    item.put("a.Memory Bus", Socket);
                    item.put("b.Maximum Price", MPrice);
                    item.put("c.BenchMark", bench);





                    CollectionReference parentCollectionRef = firestore.collection("PC Components");
                    DocumentReference cpuDocRef = parentCollectionRef.document("GPU");
                    CollectionReference intelCollectionRef = cpuDocRef.collection("AMD");
                    DocumentReference brandDocRef = intelCollectionRef.document(brand);
                    CollectionReference subsubCollectionRef = brandDocRef.collection("sub");
                    DocumentReference modelCollectionRef = subsubCollectionRef.document(generation);
                    CollectionReference detailscoleRef = modelCollectionRef.collection("Characteristics");
                    DocumentReference characteristicsDocRef = detailscoleRef.document(characteristics) ;




                    characteristicsDocRef.set(item)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(), "Item added successfully", Toast.LENGTH_SHORT).show();
                                    brandEditText.setText("");
                                    modelEditText.setText("");
                                    characteristicsEditText.setText("");

                                    itemNameEditText.setText("");
                                    LDateEditText.setText("");
                                    NCoresEditText.setText("");
                                    NThreadsEditText.setText("");
                                    BFreqEditText.setText("");
                                    MFreqEditText.setText("");
                                    MPowerEditText.setText("");
                                    NRamEditText.setText("");
                                    RTypeEditText.setText("");
                                    SocketEditText.setText("");
                                    MPriceEditText.setText("");
                                    BenchEditText.setText("");
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
