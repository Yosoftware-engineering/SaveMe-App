package com.example.yosef;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Signup extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone1,editTextPhone2;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;
    Button addcontact;
    boolean flag=false;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       //onRestoreInstanceState(savedInstanceState);

        setContentView(R.layout.signup_activity);

        editTextName = findViewById(R.id.edit_text_name);
        editTextEmail = findViewById(R.id.edit_text_email);
        editTextPassword = findViewById(R.id.edit_text_password);
        addcontact = findViewById(R.id.addcontact);
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();

       /* Spinner spinner = findViewById(R.id.spinner1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.blood, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
*/


        findViewById(R.id.button_register).setOnClickListener(this);

        addcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
                flag=true;
                String lastpostion="Signup";
                Intent intent = new Intent(Signup.this, Updatenumbers.class);
                intent.putExtra("last postion", lastpostion);
                startActivity(intent);

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }

    }
    @Override
    protected void onResume (){
        super.onResume();
        loadData();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();


    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        if (name.isEmpty()) {
            editTextName.setError(getString(R.string.input_error_name));
            editTextName.requestFocus();
            return;
        }

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
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    name,
                                    email,
                                    password

                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Signup.this, getString(R.string.registration_success), Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));


                                    } else {
                                        //display a failure message
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register:
                if(flag) {
                    registerUser();
                }
                else{
                    Toast.makeText(Signup.this, "please click on 'add ' some people contact' and add least onr number", Toast.LENGTH_LONG).show();
                }
                break;
        }

    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putString("name", editTextName.getText().toString());
        savedInstanceState.putString("email", editTextEmail.getText().toString());
        savedInstanceState.putString("password", editTextPassword.getText().toString());
        Toast.makeText(Signup.this, "save do the job", Toast.LENGTH_LONG).show();
        // etc.
    }
 /*   @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

                editTextName.setText(savedInstanceState.getString("name"));
                editTextEmail.setText(savedInstanceState.getString("email"));
                editTextPassword.setText(savedInstanceState.getString("password"));
                Toast.makeText(Signup.this, "restore data do the job", Toast.LENGTH_LONG).show();


    }*/
    public void saveData() {
         sharedPreferences = getSharedPreferences("save instance", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString("1", editTextName.getText().toString());
        editor.putString("2", editTextEmail.getText().toString());
        editor.putString("3", editTextPassword.getText().toString());
        editor.putBoolean("4", true);
        editor.commit();

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }
    public void loadData() {
         sharedPreferences = getSharedPreferences("save instance", MODE_PRIVATE);
        editTextName.setText( sharedPreferences.getString("1","name"));
        editTextEmail.setText( sharedPreferences.getString("2","email"));
        editTextPassword.setText( sharedPreferences.getString("3","password"));
        flag=sharedPreferences.getBoolean("4",true);
        Toast.makeText(Signup.this, "load data", Toast.LENGTH_LONG).show();
        return;
    }
}
