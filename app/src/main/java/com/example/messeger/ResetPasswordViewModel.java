package com.example.messeger;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordViewModel extends ViewModel {

    private final FirebaseAuth auth = FirebaseAuth.getInstance();

    private static final String TAG = "ResetPass_tag";

    public ResetPasswordViewModel() {
    }

    MutableLiveData<String> error = new MutableLiveData<>();
    MutableLiveData<Boolean> success = new MutableLiveData<>();

    public LiveData<String> getError() {
        return error;
    }
    public MutableLiveData<Boolean> isSuccess() {
        return success;
    }

    public void resetPassword(String email) {
        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                success.setValue(true);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                success.setValue(false);
                error.setValue(e.getMessage());
            }
        });
    }
}
