package com.example.messeger;

import static com.example.messeger.Registration.newIntent_toRegistration;
import static com.example.messeger.ResetPassword.newIntent_toResetPassword;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messeger.ChatActivityPackage.ChatViewModel;
import com.example.messeger.ChatActivityPackage.ChatViewModelFactory;
import com.example.messeger.ChatActivityPackage.Message;
import com.example.messeger.ChatActivityPackage.MessageAdapter;
import com.example.messeger.UserPackage.User;
import com.example.messeger.UserPackage.UsersActivity;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText emailEdit;
    private EditText passwordEdit;
    private TextView forgot_password;
    private TextView registration;
    private Button button;

    private String email;
    private String password;

    private static final String TAG = "MainActivityLog";

    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        setListeners();

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        observeViewModel();


    }

    private void init() {
        emailEdit = findViewById(R.id.email_login);
        passwordEdit = findViewById(R.id.password_login);
        forgot_password = findViewById(R.id.forgotPassword_login);
        registration = findViewById(R.id.registration_login);
        button = findViewById(R.id.Button_login);
    }

    private static String getTrimText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void setListeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = getTrimText(emailEdit);
                password = getTrimText(passwordEdit);

                if(email.equals("") || password.equals("")) {
                    Toast.makeText(MainActivity.this, "email or password is null", Toast.LENGTH_LONG).show();
                } else {
                    viewModel.login(email, password);
                }
            }
        });
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = newIntent_toRegistration(MainActivity.this);
                startActivity(intent);
            }
        });
        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = getTrimText(emailEdit);
                Intent intent = newIntent_toResetPassword(MainActivity.this, email);
                startActivity(intent);
            }
        });
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String error) {
                if(error != null) {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
                    Log.d(TAG, error);
                }
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null) {
                    Log.d(TAG, "user is authorized");
                    Intent intent = UsersActivity.newIntent(MainActivity.this, firebaseUser.getUid());
                    startActivity(intent);
                    finish();
                } else {
                    Log.d(TAG, "problem with user authorization");
                }
            }
        });
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }


}