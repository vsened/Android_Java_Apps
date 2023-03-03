package com.example.mymessenger;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginViewModel extends ViewModel {

    private static final String LOG_TAG = "MainActivityViewModel";
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private MutableLiveData<String> signInErrorMessage = new MutableLiveData<>();

    private final FirebaseAuth mAuth;
    public LiveData<String> getSignInErrorMessage() {
        return signInErrorMessage;
    }
    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public LoginViewModel() {
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    user.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });
    }

    public void login(String login, String password) {
        mAuth.signInWithEmailAndPassword(login, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signInErrorMessage.setValue(e.getMessage());
                    }
                });
    }

    private void resetEmail(String email) {
        mAuth.sendPasswordResetEmail("vasily.senederov@gmail.com")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(LOG_TAG, "Password was send to user email!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(LOG_TAG, e.getMessage());
                    }
                });
    }

}
