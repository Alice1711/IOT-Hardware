package com.example.rentwise;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Motobike;

import java.util.List;

public class ManageVehicle extends AppCompatActivity {

    private RecyclerView recyclerViewVehicleList;
    private MyAdapter<Motobike> adapter;
    private TextView tvEmptyMessage;
    private ImageButton btnBackManage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage_vehicle);

        recyclerViewVehicleList = findViewById(R.id.recyclerViewVehicleList);
        recyclerViewVehicleList.setLayoutManager(new LinearLayoutManager(this));

        tvEmptyMessage = findViewById(R.id.tv_empty_message2);
        btnBackManage = findViewById(R.id.btnBackManage);

        btnBackManage.setOnClickListener(v -> finish());

        loadData();
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
                    setupRecyclerView(data);  // Truyền toàn bộ danh sách vào setupRecyclerView
                }
            }

            @Override
            public void onFailure(String message) {
                Log.e("ManageVehicle", "Failed to fetch data: " + message);
            }
        });
    }

    private void setupRecyclerView(List<Motobike> motobikeList) {
        adapter = new MyAdapter<>(motobikeList, R.layout.item_vehicle, new MyAdapter.Binder<Motobike>() {
            @Override
            public void bind(View itemView, Motobike motobike) {
                TextView nameTextView = itemView.findViewById(R.id.nameCustomer);
                TextView statusTextView = itemView.findViewById(R.id.tv_StatusOn);
                TextView locationCustomerTextView = itemView.findViewById(R.id.locationCustomer); // Thêm TextView này
                ImageView statusIcon = itemView.findViewById(R.id.imgStatusOn);

                // Đặt tên và số biển số xe
                nameTextView.setText(motobike.getName());
                statusTextView.setText(motobike.getNumberPlate());

                // Đặt trạng thái hoạt động hoặc không hoạt động
                if ("Online".equalsIgnoreCase(motobike.getStatus())) {
                    statusIcon.setImageResource(R.drawable.img_status_online);
                    locationCustomerTextView.setText("Trạng thái: đang hoạt động"); // Trạng thái đang hoạt động
                } else {
                    statusIcon.setImageResource(R.drawable.img_status_offline);
                    locationCustomerTextView.setText("Trạng thái: không hoạt động"); // Trạng thái không hoạt động
                }
            }
        });

        recyclerViewVehicleList.setAdapter(adapter);
    }
}
