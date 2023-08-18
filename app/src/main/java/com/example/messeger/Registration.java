package com.example.messeger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.messeger.UserPackage.UsersActivity;
import com.google.firebase.auth.FirebaseUser;

public class Registration extends AppCompatActivity {

    private EditText nameEdit;
    private EditText lastNameEdit;
    private EditText ageEdit;
    private EditText emailEdit;
    private EditText passwordEdit;
    private Button button;

    private RegistrationViewModel viewModel;

    private static final String TAG = "RegistrationActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        init();

        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = getTrimText(nameEdit);
                String lastName = getTrimText(lastNameEdit);
                int age = -10000000;
                if(!getTrimText(ageEdit).equals("")) {
                    age = Integer.parseInt(getTrimText(ageEdit));
                }
                String email = getTrimText(emailEdit);
                String password = getTrimText(passwordEdit);

                if(name.equals("")) {
                    Toast.makeText(Registration.this, "name is null", Toast.LENGTH_LONG).show();
                } else if (lastName.equals("")) {
                    Toast.makeText(Registration.this, "lastname is null", Toast.LENGTH_LONG).show();
                } else if(age == -10000000) {
                    Toast.makeText(Registration.this,  "age is null", Toast.LENGTH_LONG).show();
                } else if(age <= 0) {
                    Toast.makeText(Registration.this,  "age is not correct", Toast.LENGTH_LONG).show();
                } else if(email.equals("")) {
                    Toast.makeText(Registration.this, "email is null", Toast.LENGTH_LONG).show();
                } else if(password.equals("")) {
                    Toast.makeText(Registration.this, "password is null", Toast.LENGTH_LONG).show();
                }
                else {
                    viewModel.registration(name, lastName, age, email, password);
                }
            }
        });

        observeViewModel();
    }

    private void init() {
        nameEdit = findViewById(R.id.name_reg);
        lastNameEdit = findViewById(R.id.lastName_reg);
        ageEdit = findViewById(R.id.age_reg);
        emailEdit = findViewById(R.id.email_reg);
        passwordEdit = findViewById(R.id.password_reg);
        button = findViewById(R.id.button_reg);
    }

    private static String getTrimText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Intent newIntent_toRegistration(Context context) {
        return new Intent(context, Registration.class);
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if(errorMessage != null) {
                    Toast.makeText(Registration.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser != null) {
                    Intent intent = UsersActivity.newIntent(Registration.this, firebaseUser.getUid());
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}