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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogInActivity extends AppCompatActivity {

    EditText etEmail, etPass;
    Button btnLogIn, btnSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        etEmail = findViewById(R.id.etLogEmail);
        etPass = findViewById(R.id.etLogPass);
        btnLogIn = findViewById(R.id.btnLogLogIn);
        btnSignUp = findViewById(R.id.btnLogSignUp);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                finish();
            }
        });

        btnLogIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailVal = etEmail.getText().toString().toLowerCase();
                String passVal = etPass.getText().toString();

                if(emailVal.isEmpty() || passVal.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Email ve Password alanlarını doldurunuz!", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseAuth auth = FirebaseAuth.getInstance();

                auth.signInWithEmailAndPassword(emailVal,passVal).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                     if(task.isSuccessful()){
                         startActivity(new Intent(getApplicationContext(), MainActivity.class));
                     }else{
                         Toast.makeText(getApplicationContext(), "Email veya Password alanlanı hatalı! !", Toast.LENGTH_SHORT).show();
                     }

                    }
                });
            }
        });

    }
}