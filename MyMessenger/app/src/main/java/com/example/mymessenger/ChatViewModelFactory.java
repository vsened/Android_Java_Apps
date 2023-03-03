package com.example.mymessenger;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory implements ViewModelProvider.Factory {

    private String currentUserId;
    private String anotherUserId;

    public ChatViewModelFactory(String currentUserId, String anotherUserId) {
        this.currentUserId = currentUserId;
        this.anotherUserId = anotherUserId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(currentUserId, anotherUserId);
    }
}
