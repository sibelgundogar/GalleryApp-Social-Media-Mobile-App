package com.example.galleryapp.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.galleryapp.R;
import com.example.galleryapp.activity.LogInActivity;
import com.example.galleryapp.databinding.FragmentGalleryBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class GalleryFragment extends Fragment {
    LinearLayout llvGallery;
    Spinner spLabel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);

        llvGallery = root.findViewById(R.id.llvGallery);
        spLabel = root.findViewById(R.id.spLabel);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("photos").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                String photoURL = document.getString("photoURL");
                ArrayList<String> labels = (ArrayList<String>) document.get("labels");
                String userid = document.getString("userid");

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, labels);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spLabel.setAdapter(adapter);

                if (userid != null) {
                    db.collection("users").document(userid).get().addOnCompleteListener(userTask -> {
                        if (userTask.isSuccessful()) {
                            DocumentSnapshot userDoc = userTask.getResult();
                            if (userDoc != null && userDoc.exists()) {
                                if (userDoc.contains("name")) {
                                    String username = userDoc.getString(("name"));

                                    View galleryItem = getLayoutInflater().inflate(R.layout.item_gallery, null);
                                    ImageView imgGallery = galleryItem.findViewById(R.id.imgGallery);
                                    TextView txtUser = galleryItem.findViewById(R.id.txtUser);
                                    TextView txtLabels = galleryItem.findViewById(R.id.txtLabels);

                                    if (photoURL != null) {
                                        Picasso.get().load(photoURL).into(imgGallery);

                                    }
                                    txtUser.setText(username);
                                    txtLabels.setText(labels != null ? String.join(", ", labels) : "");

                                    llvGallery.addView(galleryItem);
                                } else {
                                    Toast.makeText(getContext(), "Kullanıcı blgileri alınırken bir hata oluştu!", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "Kullanıcı blgileri alınırken bir hata oluştu!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "Kullanıcı blgileri alınırken bir hata oluştu!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Fotoğraflar alınırken bir hata oluştu!", Toast.LENGTH_SHORT).show();
        });
        return root;
    }

}