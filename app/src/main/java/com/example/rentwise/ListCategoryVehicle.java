package com.example.rentwise;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Motobike;
import com.example.rentwise.MyAdapter;

import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;
import java.util.stream.Collectors;

public class ListCategoryVehicle extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyAdapter<Motobike> adapter;
    private TextView tvEmptyMessage;
    private ImageButton btnBackCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_category_vehicle);

        recyclerView = findViewById(R.id.recycleview_listVehicle);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        tvEmptyMessage = findViewById(R.id.tv_empty_message);

        btnBackCategory.setOnClickListener(v -> finish());

        loadData();
    }

    private void loadData() {
        FirebaseRepository<Motobike> motobikeRepository = new FirebaseRepository<>("Motobike", Motobike.class);
        motobikeRepository.getAll(new FirebaseRepository.OnFetchListListener<Motobike>() {
            @Override
            public void onSuccess(List<Motobike> data) {
                // Filter for only "offline" status vehicles
                List<Motobike> offlineMotobikes = data.stream()
                        .filter(motobike ->"Offline".equalsIgnoreCase(motobike.getStatus()))
                        .collect(Collectors.toList());

                if (offlineMotobikes.isEmpty()) {
                    tvEmptyMessage.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                } else {
                    tvEmptyMessage.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    setupRecyclerView(offlineMotobikes);
                }
            }

            @Override
            public void onFailure(String message) {
                Log.e("ListCategoryVehicle", "Failed to fetch data: " + message);
            }
        });
    }

    private void setupRecyclerView(List<Motobike> motobikeList) {
        adapter = new MyAdapter<>(motobikeList, R.layout.item_categogy_vehicle, new MyAdapter.Binder<Motobike>() {
            @Override
            public void bind(View itemView, Motobike motobike) {
                TextView nameTextView = itemView.findViewById(R.id.tv_nameVehicle);
                TextView numberPlateTextView = itemView.findViewById(R.id.tv_numberPlate);

                nameTextView.setText(motobike.getName());
                numberPlateTextView.setText(motobike.getNumberPlate());
            }
        });

        recyclerView.setAdapter(adapter);
    }
}
