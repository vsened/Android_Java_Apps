package com.example.mymessenger;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UsersViewModel extends ViewModel {

    private static final String TAG = "UsersViewModel";

    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();
    public LiveData<FirebaseUser> getUser() {
        return user;
    }
    public LiveData<List<User>> getUsers() {
        return users;
    }
    private final FirebaseAuth mAuth;
    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference usersReference;

    public UsersViewModel() {
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        usersReference = firebaseDatabase.getReference("Users");
        mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user.setValue(firebaseAuth.getCurrentUser());
            }
        });
        usersReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                if (currentUser == null) {
                    return;
                }
                List<User> downloadedUsers = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    User downloadedUser = dataSnapshot.getValue(User.class);
                    if (downloadedUser == null) {
                        return;
                    }
                    if (!downloadedUser.getId().equals(currentUser.getUid())) {
                        downloadedUsers.add(downloadedUser);
                    }
                }
                users.setValue(downloadedUsers);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void logOut() {
        setUserOnline(false);
        mAuth.signOut();
    }

    public void setUserOnline(boolean isOnline) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            return;
        }
        usersReference.child(currentUser.getUid()).child("online").setValue(isOnline);
    }
}
