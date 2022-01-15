package com.example.yosef;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;



public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private FirebaseAuth mAuth;
    private EditText editTextEmail, editTextPassword;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);

        findViewById(R.id.button_create).setOnClickListener(this);
        findViewById(R.id.button_login).setOnClickListener(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user


        }
    }


    private void signinUser() {
       final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if (email.isEmpty()) {
            editTextEmail.setError(getString(R.string.input_error_email));
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError(getString(R.string.input_error_email_invalid));
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError(getString(R.string.input_error_password));
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError(getString(R.string.input_error_password_length));
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);


        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            progressBar.setVisibility(View.GONE);

                            startActivity(new Intent(getApplicationContext(),Userinterface.class));
                        }
                        else {
                            Toast.makeText(LoginActivity.this ,"Email / Password Are Wrong , Check them again",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);

                        }

                        // ...
                    }
                });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.button_login:
                signinUser();
                break;

            case R.id.button_create:

                startActivity(new Intent(getApplicationContext(),Signup.class));
                break;

        }
    }
}
