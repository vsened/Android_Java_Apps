package com.example.mymessenger;

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

public class ResetPasswordActivity extends AppCompatActivity {

    private final static String EXTRA_EMAIL = "email";
    private EditText editTextResetEmail;
    private Button buttonResetEmail;

    private ResetPasswordViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        initViews();
        viewModel = new ViewModelProvider(this).get(ResetPasswordViewModel.class);
        observeViewModel();
        String userEmail = getIntent().getStringExtra(EXTRA_EMAIL);
        editTextResetEmail.setText(userEmail);
        buttonResetEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextResetEmail.getText().toString().trim();
                viewModel.resetPassword(email);
            }
        });
    }

    private void observeViewModel() {
        viewModel.getResetPasswordError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast.makeText(
                            ResetPasswordActivity.this,
                            message,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        viewModel.getShouldCloseScreen().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean shouldClose) {
                if (shouldClose) {
                    Toast.makeText(
                            ResetPasswordActivity.this,
                            R.string.reset_link_send,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
    }

    private void initViews() {
        editTextResetEmail = findViewById(R.id.editTextResetEmail);
        buttonResetEmail = findViewById(R.id.buttonResetEmail);
    }

    public static Intent newIntent(Context context, String email) {
        Intent intent = new Intent(context, ResetPasswordActivity.class);
        intent.putExtra(EXTRA_EMAIL, email);
        return intent;
    }
}