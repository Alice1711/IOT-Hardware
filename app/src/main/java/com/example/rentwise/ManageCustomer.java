package com.example.rentwise;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentwise.Fragment.CustomerDetailFragment;
import com.example.rentwise.Fragment.CustomerInfoFragment;
import com.example.rentwise.ModelData.Customer;
import com.example.rentwise.ModelData.FirebaseRepository;

import java.util.List;

public class ManageCustomer extends AppCompatActivity {

    private RecyclerView recyclerViewCusList;
    private MyAdapter<Customer> adapter;
    private TextView tvEmptyMessage2;
    private ImageButton btnBackManageCus;
    private Button btnAddCus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_customer);

        recyclerViewCusList = findViewById(R.id.recyclerViewCusList);
        recyclerViewCusList.setLayoutManager(new LinearLayoutManager(this));

        tvEmptyMessage2 = findViewById(R.id.tv_empty_message2);
        btnBackManageCus = findViewById(R.id.btnBackManageCus);
        btnAddCus = findViewById(R.id.btnAddCus);

        btnAddCus.setOnClickListener(v -> openCustomerInfoFragment());
        btnBackManageCus.setOnClickListener(v -> finish());

        loadData();

        getSupportFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, result) -> {
            if (result.getBoolean("dataUpdated")) {
                loadData();
            }
        });

        getSupportFragmentManager().setFragmentResultListener("customerUpdated", this, (requestKey, result) -> loadData());
        getSupportFragmentManager().setFragmentResultListener("customerDeleted", this, (requestKey, result) -> loadData());
    }

    private void openCustomerInfoFragment() {
        CustomerInfoFragment customerInfoFragment = new CustomerInfoFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, customerInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadData() {
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
                    setupRecyclerView(data);
                }
            }

            @Override
            public void onFailure(String message) {
                Log.e("ManageCustomer", "Failed to fetch data: " + message);
                Toast.makeText(ManageCustomer.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(List<Customer> customerList) {
        adapter = new MyAdapter<>(customerList, R.layout.item_vehicle, new MyAdapter.Binder<Customer>() {
            @Override
            public void bind(View itemView, Customer customer) {
                TextView nameTextView = itemView.findViewById(R.id.nameCustomer);
                TextView genderTextView = itemView.findViewById(R.id.locationCustomer);
                TextView cccdTextView = itemView.findViewById(R.id.tv_StatusOn);
                ImageView imgStatusOn = itemView.findViewById(R.id.imgStatusOn);

                nameTextView.setText(customer.getName());
                genderTextView.setText("Giới tính: " + customer.getGender());
                cccdTextView.setText("CCCD: " + customer.getCccd());
                imgStatusOn.setVisibility(View.INVISIBLE);

                itemView.setOnClickListener(v -> openCustomerDetailFragment(customer));
            }
        });

        recyclerViewCusList.setAdapter(adapter);
    }


    private void openCustomerDetailFragment(Customer customer) {
        CustomerDetailFragment customerDetailFragment = CustomerDetailFragment.newInstance(
                customer.getCccd(),
                customer.getName(),
                customer.getGender(),
                customer.getPhoneNumber()
        );

        customerDetailFragment.show(getSupportFragmentManager(), "CustomerDetailFragment");
    }
}
