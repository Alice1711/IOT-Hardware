package com.example.rentwise;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class SignUp extends AppCompatActivity {

    TextView tvLogin;
    Button btnSignIn;
    TextInputEditText edtEmail2, edtPass2, edtRePass2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        tvLogin = findViewById(R.id.tvLogin);
        btnSignIn = findViewById(R.id.btnSignIn);
        edtEmail2 = findViewById(R.id.edtEmail2);
        edtPass2 = findViewById(R.id.edtPass2);
        edtRePass2 = findViewById(R.id.edtRePass2);
        mAuth = FirebaseAuth.getInstance();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUp.this, Login.class);
                startActivity(intent);
            }
        });
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        String email = edtEmail2.getText().toString();
        String pass = edtPass2.getText().toString();
        String rePass = edtRePass2.getText().toString();

        if (email.isEmpty()) {
            edtEmail2.setError("Email is required");
            return;
        }
        if (pass.isEmpty()) {
            edtPass2.setError("Password is required");
            return;
        }
        if (rePass.isEmpty()) {
            edtRePass2.setError("Re-Password is required");
            return;
        }

        if (!pass.equals(rePass)) {
            edtRePass2.setError("Passwords do not match");
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignUp.this, "Sign Up Success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            String errorMessage = task.getException() != null ? task.getException().getMessage() : "Sign Up Failed";
                            Toast.makeText(SignUp.this, errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}