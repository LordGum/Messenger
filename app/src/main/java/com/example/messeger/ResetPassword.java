package com.example.messeger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPassword extends AppCompatActivity {

    private EditText emailEdit;
    private Button button;

    private String email;

    private ResetPasswordViewModel viewModel;

    private static final String EXTRA_EMAIL = "email";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        init();

        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);

        email = getIntent().getStringExtra(EXTRA_EMAIL);
        if(email == null || email.equals("")) {
            Log.d("ResetPassword", "email is null");
        }
        emailEdit.setText(email);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = getTrimText(emailEdit);
                if(email.equals("")) {
                    Toast.makeText(ResetPassword.this, "email is null", Toast.LENGTH_LONG).show();
                } else {
                    viewModel.resetPassword(email);
                }
            }
        });

        observeViewModel();
    }

    private void init() {
        emailEdit = findViewById(R.id.email_reset);
        button = findViewById(R.id.button_reset);
    }

    private static String getTrimText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Intent newIntent_toResetPassword(Context context, String email) {
        Intent intent = new Intent(context, ResetPassword.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }

    private void observeViewModel() {
        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if(errorMessage != null) {
                    Toast.makeText(ResetPassword.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

        viewModel.isSuccess().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean success) {
                if(success) {
                    Toast.makeText(ResetPassword.this, "The reset link is successfully sent", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}