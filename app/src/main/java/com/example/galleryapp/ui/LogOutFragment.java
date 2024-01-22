package com.example.galleryapp.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.galleryapp.MainActivity;
import com.example.galleryapp.R;
import com.example.galleryapp.activity.SplashActivity;
import com.google.firebase.auth.FirebaseAuth;

public class LogOutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_log_out,container,false);

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), SplashActivity.class));
        getActivity().finish();
        return root;
    }

}