package com.example.mymessenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordViewModel extends ViewModel {

    private FirebaseAuth mAuth;
    private MutableLiveData<String> resetPasswordError = new MutableLiveData<>();
    private MutableLiveData<Boolean> shouldCloseScreen = new MutableLiveData<>();

    public MutableLiveData<Boolean> getShouldCloseScreen() {
        return shouldCloseScreen;
    }

    public MutableLiveData<String> getResetPasswordError() {
        return resetPasswordError;
    }

    public ResetPasswordViewModel() {
        super();
        mAuth = FirebaseAuth.getInstance();
    }

    public void resetPassword(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        shouldCloseScreen.setValue(true);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        resetPasswordError.setValue(e.getMessage());
                    }
                });
    }
}
