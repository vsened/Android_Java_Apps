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

import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {

    private EditText editTextEmailRegistration;
    private EditText editTextPasswordRegistration;
    private EditText editTextNameRegistration;
    private EditText editTextLastNameRegistration;
    private EditText editTextAgeRegistration;
    private Button buttonSignUp;

    private RegistrationViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initViews();
        viewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);
        observeViewModel();
        setClickListeners();
    }

    private void initViews() {
        editTextEmailRegistration = findViewById(R.id.editTextEmailRegistration);
        editTextPasswordRegistration = findViewById(R.id.editTextPasswordRegistration);
        editTextNameRegistration = findViewById(R.id.editTextNameRegistration);
        editTextLastNameRegistration = findViewById(R.id.editTextLastNameRegistration);
        editTextAgeRegistration = findViewById(R.id.editTextAgeRegistration);
        buttonSignUp = findViewById(R.id.buttonSignUp);
    }

    private void observeViewModel() {
        viewModel.getSignUpErrorMessage().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String message) {
                if (message != null) {
                    Toast.makeText(
                            RegistrationActivity.this,
                            message,
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        });
        viewModel.getUser().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    Intent intent = UsersActivity.newIntent(
                            RegistrationActivity.this,
                            firebaseUser.getUid()
                    );
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    private void setClickListeners() {
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = getTrimmedValue(editTextEmailRegistration);
                String password = getTrimmedValue(editTextPasswordRegistration);
                String name = getTrimmedValue(editTextNameRegistration);
                String lastName = getTrimmedValue(editTextLastNameRegistration);
                int age = Integer.parseInt(getTrimmedValue(editTextAgeRegistration));
                viewModel.sighUp(email, password, name, lastName, age);
            }
        });
    }

    private String getTrimmedValue(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static Intent getIntent(Context context) {
        return new Intent(context, RegistrationActivity.class);
    }
}