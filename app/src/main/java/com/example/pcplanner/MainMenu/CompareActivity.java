package com.example.pcplanner.MainMenu;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pcplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CompareActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String collectionPath;
    private String documentId;

    private String collectionpath;

    private String subdocumentId1;
    private String subdocumentId2;

    private String documentId1;
    private String documentId2;

    private RecyclerView fieldListRecyclerView1;
    private RecyclerView fieldListRecyclerView2;
    private List<Map<String, Object>> subdocuments1;
    private List<Map<String, Object>> subdocuments2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        firestore = FirebaseFirestore.getInstance();
        fieldListRecyclerView1 = findViewById(R.id.field_list_recyclerview_1);
        fieldListRecyclerView2 = findViewById(R.id.field_list_recyclerview_2);

        collectionpath = getIntent().getStringExtra("collectionpath");
        subdocumentId1 = getIntent().getStringExtra("subdocumentId1");
        subdocumentId2 = getIntent().getStringExtra("subdocumentId2");

        documentId1 = getIntent().getStringExtra("documentId1");
        documentId2 = getIntent().getStringExtra("documentId2");

        documentId = getIntent().getStringExtra("documentId");
        collectionPath = getIntent().getStringExtra("collectionPath");

        firestore.collection("PC Components").document("CPU").collection("INTEL").document(documentId).collection("sub");

        CollectionReference documentRef1 = firestore.collection(collectionPath).document(documentId1).collection("sub").document(subdocumentId1).collection("Characteristics");

        CollectionReference documentRef2 = firestore.collection(collectionPath).document(documentId2).collection("sub").document(subdocumentId2).collection("Characteristics");

        documentRef1.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                subdocuments1 = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    Map<String, Object> fields = document.getData();
                    // Sort the fields by name in ascending order
                    List<Map.Entry<String, Object>> sortedFields = new ArrayList<>(fields.entrySet());
                    Collections.sort(sortedFields, new Comparator<Map.Entry<String, Object>>() {
                        @Override
                        public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                            return o1.getKey().compareTo(o2.getKey());
                        }
                    });

                    // Convert the sorted List of Map.Entry objects back to a Map object
                    LinkedHashMap<String, Object> sortedFieldsMap = new LinkedHashMap<>();
                    for (Map.Entry<String, Object> entry : sortedFields) {
                        String fieldName = entry.getKey();
                        Object fieldValue = entry.getValue();
                        sortedFieldsMap.put(fieldName.substring(2), fieldValue);
                    }

                    subdocuments1.add(sortedFieldsMap);
                }
//                //COMPARE FIELDS HERE
//                if (subdocuments1.size() > 0 && subdocuments2.size() > 0) {
//                    LinkedHashMap<String, Object> fields1 = (LinkedHashMap<String, Object>) subdocuments1.get(0);
//                    LinkedHashMap<String, Object> fields2 = (LinkedHashMap<String, Object>) subdocuments2.get(0);
//                    for (String fieldName : fields1.keySet()) {
//                        Object fieldValue1 = fields1.get(fieldName);
//                        Object fieldValue2 = fields2.get(fieldName);
//
//                        if (fieldValue1 instanceof String && fieldValue2 instanceof String) {
//                            // Retrieve numerical part of the field values
//                            String numericalPart1 = ((String) fieldValue1).replaceAll("[^\\d.]", "");
//                            String numericalPart2 = ((String) fieldValue2).replaceAll("[^\\d.]", "");
//
//                            if (!numericalPart1.isEmpty() && !numericalPart2.isEmpty()) {
//                                // Convert the numerical parts to float for comparison
//                                float floatFieldValue1 = Float.parseFloat(numericalPart1);
//
//                                float floatFieldValue2 = Float.parseFloat(numericalPart2);
//
//                                if (floatFieldValue1 < floatFieldValue2) {
//                                    //// Do something if fieldValue1 is less than fieldValue2 <<<<>
//
//
//                                } else if (floatFieldValue1 > floatFieldValue2) {
//                                    // Do something if fieldValue1 is greater than fieldValue2 >>>>>
//
//                                } else {
//                                    // Do something if fieldValue1 is equal to fieldValue2  =========
//
//                                }
//                            } else {
//                                // Do something if the field values cannot be compared XXXXXXXX
//
//                            }
//                        } else {
//                            // Do something if the field values cannot be compared XXXXXXXX
//
//                        }
//                    }
//                }


                FieldListAdapter adapter = new FieldListAdapter(subdocuments1);
                fieldListRecyclerView1.setAdapter(adapter);
                fieldListRecyclerView1.setLayoutManager(new LinearLayoutManager(CompareActivity.this));
            } else {
                // Handle error
            }
        });



        documentRef2.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                subdocuments2 = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    Map<String, Object> fields = document.getData();
                    // Sort the fields by name in ascending order
                    List<Map.Entry<String, Object>> sortedFields = new ArrayList<>(fields.entrySet());
                    Collections.sort(sortedFields, new Comparator<Map.Entry<String, Object>>() {
                        @Override
                        public int compare(Map.Entry<String, Object> o1, Map.Entry<String, Object> o2) {
                            return o1.getKey().compareTo(o2.getKey());
                        }
                    });

                    // Convert the sorted List of Map.Entry objects back to a Map object
                    LinkedHashMap<String, Object> sortedFieldsMap = new LinkedHashMap<>();
                    for (Map.Entry<String, Object> entry : sortedFields) {
                        String fieldName = entry.getKey();
                        Object fieldValue = entry.getValue();
                        sortedFieldsMap.put(fieldName.substring(2), fieldValue);
                    }

                    subdocuments2.add(sortedFieldsMap);
                }
                FieldListAdapter adapter = new FieldListAdapter(subdocuments2);
                fieldListRecyclerView2.setAdapter(adapter);
                fieldListRecyclerView2.setLayoutManager(new LinearLayoutManager(CompareActivity.this));
            } else {
                // Handle error
            }
        });
    }

    private static class FieldListAdapter extends RecyclerView.Adapter<FieldViewHolder> {
        private final List<Map<String, Object>> subdocuments;

        public FieldListAdapter(List<Map<String, Object>> subdocuments) {
            this.subdocuments = subdocuments;
        }

        @NonNull
        @Override
        public FieldViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.field_it_layout_comparison, parent, false);
            return new FieldViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull FieldViewHolder holder, int position) {
            Map<String, Object> fields = subdocuments.get(position);
            holder.bindFields(fields);
        }

        @Override
        public int getItemCount() {
            return subdocuments.size();
        }
    }

    private static class FieldViewHolder extends RecyclerView.ViewHolder {
        private final TextView[] nameTextViews;
        private final TextView[] valueTextViews;




        public FieldViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextViews = new TextView[]{
                    itemView.findViewById(R.id.field_name1_textview),
                    itemView.findViewById(R.id.field_name2_textview),
                    itemView.findViewById(R.id.field_name3_textview),
                    itemView.findViewById(R.id.field_name4_textview),
                    itemView.findViewById(R.id.field_name5_textview),
                    itemView.findViewById(R.id.field_name6_textview),
                    itemView.findViewById(R.id.field_name7_textview),
                    itemView.findViewById(R.id.field_name8_textview),
                    itemView.findViewById(R.id.field_name9_textview)
            };
            valueTextViews = new TextView[]{
                    itemView.findViewById(R.id.field_value1_textview),
                    itemView.findViewById(R.id.field_value2_textview),
                    itemView.findViewById(R.id.field_value3_textview),
                    itemView.findViewById(R.id.field_value4_textview),
                    itemView.findViewById(R.id.field_value5_textview),
                    itemView.findViewById(R.id.field_value6_textview),
                    itemView.findViewById(R.id.field_value7_textview),
                    itemView.findViewById(R.id.field_value8_textview),
                    itemView.findViewById(R.id.field_value9_textview)
            };
        }

        public void bindFields(Map<String, Object> fields) {
            int i = 0;
            for (Map.Entry<String, Object> entry : fields.entrySet()) {
                if (i >= 9) {
                    break;
                }
                String name = entry.getKey();
                Object value = entry.getValue();
                nameTextViews[i].setText(name);
                valueTextViews[i].setText(String.valueOf(value));

                i++;
            }
            for (; i < 9; i++) {
                nameTextViews[i].setVisibility(View.GONE);
                valueTextViews[i].setVisibility(View.GONE);
            }
        }
    }
}