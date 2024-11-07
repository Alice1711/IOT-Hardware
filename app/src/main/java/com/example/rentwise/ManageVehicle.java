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

import com.example.rentwise.Fragment.VehicleDetailFragment;
import com.example.rentwise.Fragment.VehicleInfoFragment;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Motobike;

import java.util.List;

public class ManageVehicle extends AppCompatActivity {

    private RecyclerView recyclerViewVehicleList;
    private MyAdapter<Motobike> adapter;
    private TextView tvEmptyMessage;
    private ImageButton btnBackManage;
    private Button btnAddVehicle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_vehicle);

        recyclerViewVehicleList = findViewById(R.id.recyclerViewVehicleList);
        recyclerViewVehicleList.setLayoutManager(new LinearLayoutManager(this));

        tvEmptyMessage = findViewById(R.id.tv_empty_message2);
        btnBackManage = findViewById(R.id.btnBackManage);
        btnAddVehicle = findViewById(R.id.btnAddVehicle);

        btnAddVehicle.setOnClickListener(v -> openVehicleInfoFragment());
        btnBackManage.setOnClickListener(v -> finish());

        loadData();

        // Listen for result from VehicleInfoFragment
        getSupportFragmentManager().setFragmentResultListener("requestKey", this, (requestKey, result) -> {
            if (result.getBoolean("dataUpdated")) {
                loadData(); // Reload data when a new vehicle is added
            }
        });

        getSupportFragmentManager().setFragmentResultListener("vehicleUpdated", this, (requestKey, result) -> loadData());

        // Listen for vehicle deletion event
        getSupportFragmentManager().setFragmentResultListener("vehicleDeleted", this, (requestKey, result) -> loadData());
    }

    private void openVehicleInfoFragment() {
        VehicleInfoFragment vehicleInfoFragment = new VehicleInfoFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, vehicleInfoFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void loadData() {
        FirebaseRepository<Motobike> motobikeRepository = new FirebaseRepository<>("Motobike", Motobike.class);
        motobikeRepository.getAll(new FirebaseRepository.OnFetchListListener<Motobike>() {
            @Override
            public void onSuccess(List<Motobike> data) {
                if (data.isEmpty()) {
                    tvEmptyMessage.setVisibility(View.VISIBLE);
                    recyclerViewVehicleList.setVisibility(View.GONE);
                } else {
                    tvEmptyMessage.setVisibility(View.GONE);
                    recyclerViewVehicleList.setVisibility(View.VISIBLE);
                    setupRecyclerView(data);
                }
            }

            @Override
            public void onFailure(String message) {
                Log.e("ManageVehicle", "Failed to fetch data: " + message);
                Toast.makeText(ManageVehicle.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecyclerView(List<Motobike> motobikeList) {
        adapter = new MyAdapter<>(motobikeList, R.layout.item_vehicle, new MyAdapter.Binder<Motobike>() {
            @Override
            public void bind(View itemView, Motobike motobike) {
                TextView nameTextView = itemView.findViewById(R.id.nameCustomer);
                TextView statusTextView = itemView.findViewById(R.id.tv_StatusOn);
                TextView locationCustomerTextView = itemView.findViewById(R.id.locationCustomer);
                ImageView statusIcon = itemView.findViewById(R.id.imgStatusOn);

                nameTextView.setText(motobike.getName());
                statusTextView.setText(motobike.getNumberPlate());

                if ("Online".equalsIgnoreCase(motobike.getStatus())) {
                    statusIcon.setImageResource(R.drawable.img_status_online);
                    locationCustomerTextView.setText("Trạng thái: đang hoạt động");
                } else {
                    statusIcon.setImageResource(R.drawable.img_status_offline);
                    locationCustomerTextView.setText("Trạng thái: không hoạt động");
                }

                itemView.setOnClickListener(v -> openVehicleDetailFragment(motobike));
            }
        });

        recyclerViewVehicleList.setAdapter(adapter);
    }

    private void openVehicleDetailFragment(Motobike motobike) {
        VehicleDetailFragment vehicleDetailFragment = VehicleDetailFragment.newInstance(
                motobike.getName(),
                motobike.getNumberPlate(),
                motobike.getPicture(),
                motobike.getStatus(),
                motobike.getPrice()
        );

        // Show the bottom sheet dialog
        vehicleDetailFragment.show(getSupportFragmentManager(), "VehicleDetailFragment");
    }
}

