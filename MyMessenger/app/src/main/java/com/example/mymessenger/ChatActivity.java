package com.example.mymessenger;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChatActivity extends AppCompatActivity {

    private static final String EXTRA_CURRENT_USER_ID = "current_user_id";
    private static final String EXTRA_ANOTHER_USER_ID = "another_user_id";

    private TextView textViewUserInfo;
    private View userStatus;
    private RecyclerView recyclerViewMessages;
    private EditText editTextMessage;
    private ImageView imageViewSendMessage;
    private MessageAdapter messageAdapter;
    private String currentUserId;
    private String anotherUserId;

    private ChatViewModel viewModel;
    private ChatViewModelFactory viewModelFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initViews();
        Intent intent = getIntent();
        currentUserId = intent.getStringExtra(EXTRA_CURRENT_USER_ID);
        anotherUserId = intent.getStringExtra(EXTRA_ANOTHER_USER_ID);
        viewModelFactory = new ChatViewModelFactory(currentUserId, anotherUserId);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(ChatViewModel.class);
        messageAdapter = new MessageAdapter(currentUserId);
        recyclerViewMessages.setAdapter(messageAdapter);
        imageViewSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextMessage.getText().toString().trim();
                Message message = new Message(currentUserId, anotherUserId, text);
                viewModel.sendMessage(message);
            }
        });
        observeViewModel();
    }

    private void observeViewModel() {
        viewModel.getMessages().observe(this, new Observer<List<Message>>() {
            @Override
            public void onChanged(List<Message> messages) {
                messageAdapter.setMessages(messages);
            }
        });
        viewModel.getErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(ChatActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        });
        viewModel.getMessageSend().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isSend) {
                if (isSend) {
                    editTextMessage.setText("");
                }
            }
        });
        viewModel.getAnotherUser().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                String userInfo = String.format(
                        "%s %s",
                        user.getName(),
                        user.getLastName()
                );
                textViewUserInfo.setText(userInfo);
                int circleBg;
                if (user.isOnline()) {
                    circleBg = R.drawable.circle_green;
                } else {
                    circleBg = R.drawable.circle_red;
                }
                Drawable drawable = ContextCompat.getDrawable(ChatActivity.this, circleBg);
                userStatus.setBackground(drawable);
            }
        });
    }

    private void initViews() {
        textViewUserInfo = findViewById(R.id.textViewUserInfo);
        userStatus = findViewById(R.id.userStatus);
        recyclerViewMessages = findViewById(R.id.recyclerViewMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        imageViewSendMessage = findViewById(R.id.imageViewSendMessage);
    }

    @Override
    protected void onResume() {
        super.onResume();
        viewModel.setUserOnline(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        viewModel.setUserOnline(false);
    }

    public static Intent newIntent(Context context, String currentUserId, String anotherUserId) {
        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra(EXTRA_CURRENT_USER_ID, currentUserId);
        intent.putExtra(EXTRA_ANOTHER_USER_ID, anotherUserId);
        return intent;
    }
}