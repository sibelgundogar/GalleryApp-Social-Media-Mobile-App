package com.example.galleryapp.ui;

import android.Manifest;
import android.content.Intent;
import android.content.LocusId;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.galleryapp.R;
import com.example.galleryapp.model.LabelAdapter;
import com.example.galleryapp.model.LabelModel;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class PhotoFragment extends Fragment {

    Button btnSave, btnCamera;
    LinearLayout photoLayout;
    RecyclerView recyclerView;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    LabelAdapter labelAdapter;
    Bitmap photoBitmap;
    CollectionReference labelCollection;
    CollectionReference photosCollection;

    private static final int REQUEST_IMAGE_CAPTURE =1;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_photo,container,false);

        photoLayout = root.findViewById(R.id.photoLayout);
        btnCamera = root.findViewById(R.id.btnCamera);
        btnSave = root.findViewById(R.id.btnSave);
        recyclerView=root.findViewById(R.id.labelRecyclerView);

        photosCollection = FirebaseFirestore.getInstance().collection("photos");
        labelCollection = FirebaseFirestore.getInstance().collection("labels");

        auth = FirebaseAuth.getInstance();
        currentUser= auth.getCurrentUser();

        labelAdapter = new LabelAdapter(new ArrayList<>());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(labelAdapter);


        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkedCameraPermission()){
                    dispatchTakePictureIntent();
                }else{
                    requestCameraPermission();
                }
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePhotoDataToFirebase();
            }
        });

        lodLabelsFromFirebase();

        return root;
    }

    public void dispatchTakePictureIntent(){
        Intent takePicInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraLauncher.launch(takePicInt);
    }

    public ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if(result.getResultCode() == getActivity().RESULT_OK){
                    Intent data = result.getData();
                    Bundle extras = data.getExtras();
                    if(extras!=null){
                        photoBitmap = (Bitmap) extras.get("data");
                        showPhoto();
                    }
                }
            }
    );

    public void showPhoto(){
        ImageView imageView = new ImageView(getContext());
        imageView.setLayoutParams(new LinearLayout.LayoutParams(550,550));
        imageView.setPadding(2,2,2,2);
        imageView.setImageBitmap(photoBitmap);

        photoLayout.addView(imageView);
        createLabelsCheckBoxes();
    }

    public void createLabelsCheckBoxes(){
        boolean isCheckBoxVisible = true;

        for(int i=0; i<recyclerView.getChildCount();i++){
            View view = recyclerView.getChildAt(i);
            if(view instanceof FrameLayout){
                FrameLayout frameLayout = (FrameLayout) view;

                CheckBox checkBox= frameLayout.findViewById(R.id.checkBox);

                if(isCheckBoxVisible){
                    checkBox.setVisibility(View.VISIBLE);
                }
                else{
                    checkBox.setVisibility(View.INVISIBLE);
                }
            }
        }
    }



public void savePhotoDataToFirebase() {
    if (currentUser != null) {
        String userid = currentUser.getUid();

        Map<String, Object> photoData = new HashMap<>();
        photoData.put("userid", userid);

        List<String> selectedLabels = new ArrayList<>();

        for (int i = 0; i < recyclerView.getChildCount(); i++) {
            View view = recyclerView.getChildAt(i);

            if (view instanceof FrameLayout) {
                FrameLayout frameLayout = (FrameLayout) view;

                CheckBox checkBox = frameLayout.findViewById(R.id.checkBox);
                TextView tvLabel = frameLayout.findViewById(R.id.tvLabel);

                if (checkBox.isChecked()) {
                    selectedLabels.add(tvLabel.getText().toString());
                }
            }

        }

        photoData.put("labels",selectedLabels);
        uploadImageToFirebaseStorage(photoData);
    }
}



public void uploadImageToFirebaseStorage(Map<String , Object> photoData){
        if(photoBitmap!= null){
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
            byte[] data = baos.toByteArray();

            String imgName = System.currentTimeMillis() + ".jpg";
            StorageReference photoReference = FirebaseStorage.getInstance().getReference().child(imgName);

            UploadTask uploadTask = photoReference.putBytes(data);

            uploadTask.addOnSuccessListener(taskSnapshot -> {
                photoReference.getDownloadUrl().addOnSuccessListener(uri -> {
                    String imgURL = uri.toString();
                    photoData.put("photoURL", imgURL);
                   addPhotoDataToFireabse(photoData);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Fotoğraf yüklenirken bir hata oluştu!", Toast.LENGTH_SHORT).show();
            });
        }
        else{
            Toast.makeText(getContext(), "Lütfen fotoğraf ekleyin!", Toast.LENGTH_SHORT).show();
        }
}

public void addPhotoDataToFireabse(Map<String , Object> photoData){
        photosCollection.add(photoData).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Yeni fotoğraf gallerye eklenmiştir", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Fotoğraf kaydedilirken bir sorun oluştu!", Toast.LENGTH_SHORT).show();
        });
}

public void lodLabelsFromFirebase(){
        labelCollection.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                List < LabelModel> labelList = new ArrayList<>();

                for(DocumentSnapshot document : task.getResult()){
                    String label = document.getString("label");

                    if(label != null && !label.isEmpty()){
                        labelList.add(new LabelModel(label,""));
                    }
                }
                labelAdapter.setLabels(labelList);
            }
            else{
                Toast.makeText(getContext(), "Etiketler alınırken bir hata oluştu!", Toast.LENGTH_SHORT).show();
            }
        });


}
    public boolean checkedCameraPermission(){
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    public void requestCameraPermission(){
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
    }
}