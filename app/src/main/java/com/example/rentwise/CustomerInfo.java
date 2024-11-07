package com.example.rentwise;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class CustomerInfo extends AppCompatActivity {

    private Button btnContinue2;
    private ImageButton btnBackCustomerInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);

        // Find btnContinue2 and set an OnClickListener
        btnContinue2 = findViewById(R.id.btnSave2);
        btnContinue2.setOnClickListener(v -> {
            // Start ListCategoryVehicle when btnContinue2 is clicked
            Intent intent = new Intent(CustomerInfo.this, ListCategoryVehicle.class);
            startActivity(intent);
        });

        btnBackCustomerInfo = findViewById(R.id.btnBackCustomerInfo);
        btnBackCustomerInfo.setOnClickListener(v -> finish());
    }
}
