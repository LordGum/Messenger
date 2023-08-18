package com.example.messeger.ChatActivityPackage;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messeger.R;
import com.example.messeger.UserPackage.User;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private TextView nameOfFriend;
    private View onlineStatus;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private ImageView sendView;
    private EditText editText2;

    private String currentId;
    private String otherId;

    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;

    private final static String EXTRA_CURRENT_ID = "currentId";
    private final static String EXTRA_OTHER_ID = "otherId";
    private final static String TAG = "ChatActivity_tag";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        init();
        currentId = getIntent().getStringExtra(EXTRA_CURRENT_ID);
        otherId = getIntent().getStringExtra(EXTRA_OTHER_ID);

        adapter = new MessageAdapter(currentId);
        recyclerView.setAdapter(adapter);

        viewModelFactory = new ChatViewModelFactory(currentId, otherId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);
        observeViewModel();

        sendMessage(currentId, otherId);
    }

    private void init() {
        nameOfFriend = findViewById(R.id.name_chat);
        onlineStatus = findViewById(R.id.online_chat);
        recyclerView = findViewById(R.id.recycler_chat);
        sendView = findViewById(R.id.imageView_chat);
    }

    public Intent newIntent(Context context, String currentUserId, String otherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_ID, currentUserId);
        intent.putExtra(EXTRA_OTHER_ID, otherUserId);
        return intent;
    }

    private void observeViewModel() {

        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                adapter.setMessages(messages);
            }
        });

        viewModel.getError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String errorMessage) {
                if(errorMessage != null) {
                    Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                }
            }
        });

        viewModel.getIsSent().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                editText2.setText("");
            }
        });

        viewModel.getOtherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfo = String.format("%s %s ", user.getName(), user.getLastname());
                nameOfFriend.setText(userInfo);

                int bgResId;
                if(user.isOnline()) {
                    bgResId = R.drawable.circle_green;
                } else {
                    bgResId = R.drawable.circle_red;
                }
                Drawable background = ContextCompat.getDrawable(ChatActivity.this, bgResId);
                onlineStatus.setBackground(background);
            }
        });
    }

    private void sendMessage(String currentId, String otherId) {
        sendView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText2 = findViewById(R.id.message_chat);
                Message message1 = new Message(editText2.getText().toString(), currentId, otherId);
                viewModel.sendMessage(message1);

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setOnlineStatus(false);
    }

    @Override
    protected void onResume() {
        viewModel.setOnlineStatus(true);
        super.onResume();
    }
}