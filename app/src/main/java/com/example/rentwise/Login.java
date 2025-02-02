package com.example.rentwise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentwise.ModelData.FirebaseRepository;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private TextInputEditText edtEmail, edtPass;
    private TextInputLayout layoutEmail, layoutPass;
    private FirebaseAuth mAuth;
    private Button btnLogin;
    private TextView tvCreat;
    private LinearLayout btnGoogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        edtEmail = findViewById(R.id.edtEmail);
        edtPass = findViewById(R.id.edtPass);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPass = findViewById(R.id.layoutPass);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreat = findViewById(R.id.tvCreat);
        btnGoogle = findViewById(R.id.btnGoogle);

        tvCreat.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, SignUp.class);
            startActivity(intent);
        });

        btnLogin.setOnClickListener(v -> loginUser());

    }

    private void loginUser() {
        String email = edtEmail.getText().toString().trim();
        String password = edtPass.getText().toString().trim();

        if (!validateInput(email, password)) {
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            navigateToHome();
                        } else {
                            Toast.makeText(Login.this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Xác thực thất bại.";
                        Toast.makeText(Login.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private boolean validateInput(String email, String password) {
        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            layoutEmail.setError("Hãy nhập Email");
            return false;
        } else {
            layoutEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 6) {
            layoutPass.setError("Mật khẩu phải ít nhất 6 ký tự");
            return false;
        } else {
            layoutPass.setError(null);
        }
        return true;
    }

    private void navigateToHome() {
        Toast.makeText(Login.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Login.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
