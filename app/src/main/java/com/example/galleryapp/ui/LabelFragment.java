package com.example.galleryapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galleryapp.R;
import com.example.galleryapp.model.LabelAdapter;
import com.example.galleryapp.model.LabelModel;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class LabelFragment extends Fragment {

        EditText etLabel, etDesc;
        Button btnAdd;
        RecyclerView recyclerView;
        CollectionReference labelsColletion;
        List<LabelModel> labelList;
        LabelAdapter labelAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_label,container,false);

        etLabel = root.findViewById(R.id.etLabel);
        etDesc = root.findViewById(R.id.etDesc);
        btnAdd = root.findViewById(R.id.btnAdd);
        recyclerView = root.findViewById(R.id.recyclerViewLabels);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        labelsColletion= db.collection("labels");

        labelList = new ArrayList<>();
        labelAdapter = new LabelAdapter(labelList);
        recyclerView.setLayoutManager(new LinearLayoutManager((getActivity())));
        recyclerView.setAdapter((labelAdapter));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLabelToFirebase();
            }
        });

        getLabelsFromFirebase();

        return root;
    }

    public void addLabelToFirebase(){
        String labelVal = etLabel.getText().toString();
        String descVal = etDesc.getText().toString();
        Boolean isChecked = false;

        if(!labelVal.isEmpty() && !descVal.isEmpty()){
            LabelModel newLabel = new LabelModel(labelVal,descVal,isChecked);

            labelsColletion.add(newLabel);

            etLabel.setText("");
            etDesc.setText("");
        }
        else{
            Toast.makeText(getContext(), "Tüm alanlar doldurulmalıdır!", Toast.LENGTH_SHORT).show();
        }
    }

    public void getLabelsFromFirebase(){
        labelsColletion.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                labelList.clear();
                for(DocumentSnapshot document: value){
                    LabelModel label = document.toObject(LabelModel.class);
                    labelList.add(label);
                }
                labelAdapter.notifyDataSetChanged();
            }
        });
    }

}