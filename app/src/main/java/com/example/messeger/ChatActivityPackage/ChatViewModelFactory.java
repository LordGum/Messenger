package com.example.messeger.ChatActivityPackage;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ChatViewModelFactory implements ViewModelProvider.Factory {

    private final String currentId;
    private final String otherId;

    public ChatViewModelFactory (String currentId, String otherId) {
        this.currentId = currentId;
        this.otherId = otherId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new ChatViewModel(currentId, otherId);
    }
}
