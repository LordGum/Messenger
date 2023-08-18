package com.example.messeger.ChatActivityPackage;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.messeger.UserPackage.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ChatViewModel extends ViewModel {

    private MutableLiveData<User> otherUser = new MutableLiveData<>();
    private MutableLiveData<String> error = new MutableLiveData<>();
    private MutableLiveData<List<Message>> messages = new MutableLiveData<>();
    private MutableLiveData<Boolean> isSent = new MutableLiveData<>();

    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference referenceUsers = firebaseDatabase.getReference("Users");
    private DatabaseReference referenceMessages = firebaseDatabase.getReference("Messages");

    private String currentId;
    private String otherId;

    public ChatViewModel(String currentId, String otherId) {
        this.currentId = currentId;
        this.otherId = otherId;

        referenceUsers.child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                otherUser.setValue(user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        referenceMessages.child(currentId).child(otherId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Message> messageList = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    Message message = dataSnapshot.getValue(Message.class);
                    messageList.add(message);
                }
                messages.setValue(messageList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public MutableLiveData<User> getOtherUser() {
        return otherUser;
    }
    public MutableLiveData<String> getError() {
        return error;
    }
    public MutableLiveData<List<Message>> getMessages() {
        return messages;
    }
    public MutableLiveData<Boolean> getIsSent() {
        return isSent;
    }

    public void setOnlineStatus(boolean onlineStatus) {
        referenceUsers.child(currentId).child("online").setValue(onlineStatus);
    }

    public void sendMessage(Message message) {
        referenceMessages
                .child(currentId)
                .child(otherId)
                .push().setValue(message)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        referenceMessages
                                .child(otherId)
                                .child(currentId)
                                .push().setValue(message)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        isSent.setValue(true);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        error.setValue(e.getMessage());
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        error.setValue(e.getMessage());
                    }
                });
    }
}
