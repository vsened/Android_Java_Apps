package com.example.mymessenger;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<User> anotherUser = new MutableLiveData<>();
    private MutableLiveData<Boolean> messageSend = new MutableLiveData<>();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference referenceUser = firebaseDatabase.getReference("Users");
    private DatabaseReference referenceMessages = firebaseDatabase.getReference("Messages");

    private String currentUserId;
    private String anotherUserId;

    public ChatViewModel(String currentUserId, String anotherUserId) {
        this.currentUserId = currentUserId;
        this.anotherUserId = anotherUserId;
        referenceUser.child(anotherUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                anotherUser.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        referenceMessages
                .child(currentUserId)
                .child(anotherUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Message> messagesFromDB = new ArrayList<>();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Message message = dataSnapshot.getValue(Message.class);
                            messagesFromDB.add(message);
                        }
                        messages.setValue(messagesFromDB);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public LiveData<List<Message>> getMessages() {
        return messages;
    }

    public LiveData<User> getAnotherUser() {
        return anotherUser;
    }

    public LiveData<Boolean> getMessageSend() {
        return messageSend;
    }

    public void setUserOnline(boolean isOnline) {
        referenceUser.child(currentUserId).child("online").setValue(isOnline);
    }

    public void sendMessage(Message message) {
        referenceMessages
                .child(message.getSenderId())
                .child(message.getReceiverId())
                .push()
                .setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        referenceMessages
                                .child(message.getReceiverId())
                                .child(message.getSenderId())
                                .push()
                                .setValue(message)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        messageSend.setValue(true);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        errorMessage.setValue(e.getMessage());
                                    }
                                });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        errorMessage.setValue(e.getMessage());
                    }
                });

    }
}
