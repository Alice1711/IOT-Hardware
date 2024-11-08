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

import com.example.rentwise.Fragment.TransactionDetailFragment;
import com.example.rentwise.Fragment.TransactionInfoFragment;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Transaction;

import java.util.List;

public class ManageTransaction extends AppCompatActivity {

    private RecyclerView recyclerViewTransactionList;
    private MyAdapter<Transaction> adapter;
    private TextView tvEmptyMessage;
    private ImageButton btnBackManageTran;
    private Button btnAddTransaction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_transaction);

        recyclerViewTransactionList = findViewById(R.id.recyclerViewTransactionList);
        recyclerViewTransactionList.setLayoutManager(new LinearLayoutManager(this));

        tvEmptyMessage = findViewById(R.id.tv_empty_message2);
        btnBackManageTran = findViewById(R.id.btnBackManageTran);
        btnAddTransaction = findViewById(R.id.btnAddTran);

        btnAddTransaction.setOnClickListener(v -> openTransactionInfoFragment());
        btnBackManageTran.setOnClickListener(v -> finish());


        loadData();

        // Listen for data updates
        getSupportFragmentManager().setFragmentResultListener("transactionUpdated", this, (requestKey, result) -> loadData());
    }

    private void openTransactionInfoFragment() {
        TransactionInfoFragment transactionInfoFragment = new TransactionInfoFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, transactionInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadData() {
        FirebaseRepository<Transaction> transactionRepository = new FirebaseRepository<>("Transaction", Transaction.class);
        transactionRepository.getAll(new FirebaseRepository.OnFetchListListener<Transaction>() {
            @Override
            public void onSuccess(List<Transaction> data) {
                if (data.isEmpty()) {
                    tvEmptyMessage.setVisibility(View.VISIBLE);
                    recyclerViewTransactionList.setVisibility(View.GONE);
                } else {
                    tvEmptyMessage.setVisibility(View.GONE);
                    recyclerViewTransactionList.setVisibility(View.VISIBLE);
                    setupRecyclerView(data);
                }
            }

            @Override
            public void onFailure(String message) {
                Log.e("ManageTransaction", "Failed to fetch data: " + message);
                Toast.makeText(ManageTransaction.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(List<Transaction> transactionList) {
        adapter = new MyAdapter<>(transactionList, R.layout.item_vehicle, (itemView, transaction) -> {
            TextView nameTextView = itemView.findViewById(R.id.nameCustomer);
            TextView statusTextView = itemView.findViewById(R.id.tv_StatusOn);
            TextView locationCustomerTextView = itemView.findViewById(R.id.locationCustomer);
            ImageView statusIcon = itemView.findViewById(R.id.imgStatusOn);

            nameTextView.setText(transaction.getId_rent());
            statusTextView.setText(transaction.getIdCustomer());
            locationCustomerTextView.setText(transaction.getStartDay());

            itemView.setOnClickListener(v -> openTransactionDetailFragment(transaction));
        });
        recyclerViewTransactionList.setAdapter(adapter);
    }

    private void openTransactionDetailFragment(Transaction transaction) {
        TransactionDetailFragment transactionDetailFragment = TransactionDetailFragment.newInstance( transaction.getId_rent(),
                transaction.getIdCustomer(), transaction.getIdMotobike(), transaction.getStartDay(), transaction.getEndDay(), transaction.getZoneRent()
        );

        transactionDetailFragment.show(getSupportFragmentManager(), "TransactionDetailFragment");
    }
}
