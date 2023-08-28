package com.example.messeger.UserPackage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.messeger.ChatActivityPackage.ChatActivity;
import com.example.messeger.MainActivity;
import com.example.messeger.R;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class UsersActivity extends AppCompatActivity {

    private UsersViewModel viewModel;
    private UsersAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;

    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference("Users");

    private String currentId;

    private final static String EXTRA_CURRENT_ID = "currentId";
    private static final String TAG = "UsersAct_tag";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        viewModel = new ViewModelProvider(this).get(UsersViewModel.class);
        observeViewModel();

        currentId = getIntent().getStringExtra(EXTRA_CURRENT_ID);

        init();
        OnClickListener();
    }

    private void init() {
        recyclerView = findViewById(R.id.recyclerView_Users);
        adapter = new UsersAdapter();
        recyclerView.setAdapter(adapter);
        progressBar = findViewById(R.id.progressBar_users);
    }

    public static Intent newIntent(Context context, String currentId) {
        Intent intent = new Intent(context, UsersActivity.class);
        intent.putExtra(EXTRA_CURRENT_ID, currentId);
        return intent;
    }

    private void observeViewModel() {
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if(firebaseUser == null) {
                    Intent intent = MainActivity.newIntent(UsersActivity.this);
                    startActivity(intent);
                    finish();
                }
            }
        });
        viewModel.getUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                if(users.isEmpty()) {
                    Log.d(TAG, "users is null");
                    progressBar.setVisibility(View.VISIBLE);
                } else {
                    adapter.setUsers(users);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setOnlineStatus(true);
    }

    @Override
    protected void onPause() {
        viewModel.setOnlineStatus(false);
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.logout_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logout_menu) {
            viewModel.logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void OnClickListener() {
        adapter.setOnClickUserListener(new UsersAdapter.OnClickUserListener() {
            @Override
            public void OnClick(User user) {

                Intent intent = new ChatActivity().newIntent(UsersActivity.this, currentId, user.getId());
                startActivity(intent);
            }
        });
    }
}