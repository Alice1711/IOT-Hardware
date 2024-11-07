package com.example.rentwise;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentwise.Fragment.ButtonsUpdateFragment;
import com.example.rentwise.ModelData.Customer;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Motobike;

import java.util.List;

public class ManageCustomer extends AppCompatActivity implements ButtonsUpdateFragment.OnButtonClickListener{

    private RecyclerView recyclerViewCusList;
    private MyAdapter<Customer> adapter;
    private TextView tvEmptyMessage2;
    private ImageButton btnBackManageCus;
    private Button btnUpdateCus;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_customer);

        recyclerViewCusList = findViewById(R.id.recyclerViewCusList);
        recyclerViewCusList.setLayoutManager(new LinearLayoutManager(this));

        tvEmptyMessage2 = findViewById(R.id.tv_empty_message2);
        btnBackManageCus = findViewById(R.id.btnBackManageCus);
        btnUpdateCus = findViewById(R.id.btnUpdateCus);


        btnUpdateCus.setOnClickListener(v -> {
            ButtonsUpdateFragment bottomSheet = ButtonsUpdateFragment.newInstance("param1", "param2");
            bottomSheet.setOnButtonClickListener(this); // Gán callback vào Fragment
            bottomSheet.show(getSupportFragmentManager(), "ButtonsUpdateFragment");
        });


        btnBackManageCus.setOnClickListener(v -> finish());

        loadData();
    }

    @Override
    public void onAddButtonClick() {

        Intent intent = new Intent(this, CustomerInfo.class);
        startActivity(intent);
    }

    @Override
    public void onDeleteButtonClick() {

    }

    @Override
    public void onCancelButtonClick() {

    }

    private void loadData () {
        FirebaseRepository<Customer> customerRepository = new FirebaseRepository<>("Customer", Customer.class);
        customerRepository.getAll(new FirebaseRepository.OnFetchListListener<Customer>() {
            @Override
            public void onSuccess(List<Customer> data) {
                if (data.isEmpty()) {
                    tvEmptyMessage2.setVisibility(View.VISIBLE);
                    recyclerViewCusList.setVisibility(View.GONE);
                } else {
                    tvEmptyMessage2.setVisibility(View.GONE);
                    recyclerViewCusList.setVisibility(View.VISIBLE);
                    setupRecyclerView(data);  // Truyền toàn bộ danh sách vào setupRecyclerView
                }
            }

            @Override
            public void onFailure(String message) {
                Log.e("ManageCustomer", "Failed to fetch data: " + message);
            }
        });
    }

    private void setupRecyclerView (List < Customer > customerList) {
        adapter = new MyAdapter<>(customerList, R.layout.item_vehicle, new MyAdapter.Binder<Customer>() {
            @Override
            public void bind(View itemView, Customer customer) {
                TextView nameTextView = itemView.findViewById(R.id.nameCustomer);
                TextView locationCustomerTextView = itemView.findViewById(R.id.locationCustomer); // Thêm TextView này
                TextView tv_StatusOn = itemView.findViewById(R.id.tv_StatusOn);
                ImageView imgStatusOn = itemView.findViewById(R.id.imgStatusOn);
                // Đặt tên và số biển số xe
                nameTextView.setText(customer.getName());
                locationCustomerTextView.setText(customer.getGender());
                tv_StatusOn.setText(customer.getCccd());
                imgStatusOn.setVisibility(View.INVISIBLE);

            }
        });

        recyclerViewCusList.setAdapter(adapter);
    }
}

