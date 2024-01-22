package com.example.galleryapp.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.galleryapp.MainActivity;
import com.example.galleryapp.R;
import com.example.galleryapp.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignUpActivity extends AppCompatActivity {
    EditText etName, etSurname, etEmail, etPass;
    Button btnLogIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        etName=findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etEmail = findViewById(R.id.etSignEmail);
        etPass = findViewById(R.id.etSignPass);
        btnLogIn = findViewById(R.id.btnSignLogIn);
        btnSignUp = findViewById(R.id.btnSignSignUp);


        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LogInActivity.class));
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameVal = etName.getText().toString();
                String surnameVal = etSurname.getText().toString();
                String emailVal = etEmail.getText().toString().toLowerCase();
                String passVal = etPass.getText().toString();

                if(nameVal.isEmpty() || surnameVal.isEmpty() || emailVal.isEmpty() || passVal.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Tüm alanlarını doldurunuz!", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.createUserWithEmailAndPassword(emailVal,passVal).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            String userid = task.getResult().getUser().getUid();

                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            CollectionReference reference = db.collection("users");

                            UserModel user = new UserModel(nameVal, surnameVal, emailVal, userid);

                            reference.document(userid).set(user);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }else {
                            Toast.makeText(getApplicationContext(), "Kayıt sırasında bir hata oluştu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }
        });
    }
}