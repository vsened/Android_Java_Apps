package com.example.mymessenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationViewModel extends ViewModel {

    private final FirebaseAuth mAuth;
    private final FirebaseDatabase database;
    private final DatabaseReference usersReference;
    private MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private MutableLiveData<String> signUpErrorMessage = new MutableLiveData<>();

    public LiveData<String> getSignUpErrorMessage() {
        return signUpErrorMessage;
    }
    public LiveData<FirebaseUser> getUser() {
        return user;
    }

    public RegistrationViewModel() {
        super();
        mAuth = FirebaseAuth.getInstance();
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });
        database = FirebaseDatabase.getInstance();
        usersReference = database.getReference("Users");
    }

    public void sighUp(
            String email,
            String password,
            String name,
            String lastName,
            int age
    ) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        FirebaseUser firebaseUser = authResult.getUser();
                        if (firebaseUser == null) {
                            return;
                        }
                        User newUser = new User(
                                firebaseUser.getUid(),
                                name,
                                lastName,
                                age,
                                false
                        );
                        usersReference.child(newUser.getId()).setValue(newUser);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signUpErrorMessage.setValue(e.getMessage());
                    }
                });

    }
}
