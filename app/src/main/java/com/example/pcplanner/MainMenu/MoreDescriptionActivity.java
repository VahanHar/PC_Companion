package com.example.pcplanner.MainMenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import java.util.List;
import java.util.Map;

public class MoreDescriptionActivity extends AppCompatActivity {

    private FirebaseFirestore firestore;
    private String collectionPath;
    private String subdocumentId;
    private RecyclerView fieldListRecyclerView;
    private List<Map<String, Object>> subdocuments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moredescription);

        firestore = FirebaseFirestore.getInstance();
        fieldListRecyclerView = findViewById(R.id.field_list_recyclerview);

        collectionPath = getIntent().getStringExtra("collectionpath");
        subdocumentId = getIntent().getStringExtra("subdocumentId");

        CollectionReference documentRef = firestore.collection(collectionPath).document(subdocumentId).collection("Characteristics");

        documentRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                subdocuments = new ArrayList<>();
                for (DocumentSnapshot document : task.getResult().getDocuments()) {
                    Map<String, Object> fields = document.getData();
                    subdocuments.add(fields);
                }
                FieldListAdapter adapter = new FieldListAdapter(subdocuments);
                fieldListRecyclerView.setAdapter(adapter);
                fieldListRecyclerView.setLayoutManager(new LinearLayoutManager(MoreDescriptionActivity.this));
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
                    .inflate(R.layout.field_item_layout, parent, false);
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
