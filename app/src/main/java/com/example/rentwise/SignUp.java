package com.example.rentwise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentwise.ModelData.Adnim;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private TextInputEditText edtEmail, edtPass, edtRePass;
    private TextInputLayout layoutEmail, layoutPass, layoutRePass;
    private FirebaseAuth mAuth;
    private Button btnSignUp;
    private TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Get references to UI elements
        edtEmail = findViewById(R.id.edtEmail2);
        edtPass = findViewById(R.id.edtPass2);
        edtRePass = findViewById(R.id.edtRePass2);
        layoutEmail = findViewById(R.id.layoutEmail2);
        layoutPass = findViewById(R.id.layoutPass2);
        layoutRePass = findViewById(R.id.layoutRePass2);
        btnSignUp = findViewById(R.id.btnSignIn);
        tvLogin = findViewById(R.id.tvLogin);

        // Redirect to login activity if the user already has an account
        tvLogin.setOnClickListener(v -> {
            Intent intent = new Intent(SignUp.this, Login.class);
            startActivity(intent);
        });

        // Register click listener for sign-up button
        btnSignUp.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();
        String confirmPassword = edtRePass.getText().toString().trim();

        if (!validateInput(email, password, confirmPassword)) {
            return;
        }

        // Create a new user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToDatabase(user);
                            navigateToHome();
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Đăng ký thất bại.";
                        Toast.makeText(SignUp.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInput(String email, String password, String confirmPassword) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.setError("Vui lòng nhập email hợp lệ.");
            return false;
        } else {
            layoutEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            layoutPass.setError("Mật khẩu phải có ít nhất 6 ký tự.");
            return false;
        } else {
            layoutPass.setError(null);
        }

        if (!password.equals(confirmPassword)) {
            layoutRePass.setError("Mật khẩu nhập lại không khớp.");
            return false;
        } else {
            layoutRePass.setError(null);
        }
        return true;
    }

    private void saveUserToDatabase(FirebaseUser firebaseUser) {
        String userId = firebaseUser.getUid();
        String email = firebaseUser.getEmail();

        // Assuming Adnim is your model for user data
        Adnim user = new Adnim(email, edtPass.getText().toString().trim());

        FirebaseRepository<Adnim> repository = new FirebaseRepository<>("Admin", Adnim.class);
        repository.save(userId, user, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(SignUp.this, "Đăng ký thành công.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(SignUp.this, "Lưu dữ liệu thất bại: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToHome() {
        Toast.makeText(SignUp.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUp.this, Login.class);
        startActivity(intent);
        finish();
    }
}
