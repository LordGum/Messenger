package com.example.messeger.UserPackage;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.messeger.UserPackage.User;
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

    private final FirebaseAuth auth;
    private final FirebaseDatabase firebaseDatabase;
    private final DatabaseReference usersBaseReference;

    private final MutableLiveData<FirebaseUser> user = new MutableLiveData<>();
    private final MutableLiveData<List<User>> users = new MutableLiveData<>();

    private static final String TAG = "Users_tag";


    public UsersViewModel() {
        auth = FirebaseAuth.getInstance();
        auth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null) {
                    user.setValue(firebaseAuth.getCurrentUser());
                }
            }
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        usersBaseReference = firebaseDatabase.getReference("Users");
        usersBaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                FirebaseUser currentUser = auth.getCurrentUser();
                if(currentUser == null) {
                    return;
                }

                List<User> usersFromDb = new ArrayList<>();
                for(DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    User user = dataSnapshot.getValue(User.class);
                    if(user != null) {
                        if (!user.getId().equals(currentUser.getUid())) {
                            usersFromDb.add(user);
                        }
                    }
                }
                for(User user: usersFromDb) {
                    Log.d(TAG, user.toString());
                }
                users.setValue(usersFromDb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public LiveData<FirebaseUser> getUser() {
        return user;
    }
    public LiveData<List<User>> getUsers() {
        return users;
    }
    public void setOnlineStatus(boolean onlineStatus) {
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser == null) {
            return;
        }
        usersBaseReference.child(firebaseUser.getUid()).child("online").setValue(onlineStatus);
    }

    public void logout() {
        setOnlineStatus(false);
        auth.signOut();
    }
}
