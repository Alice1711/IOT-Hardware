package com.example.rentwise.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rentwise.ModelData.Customer;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Motobike;
import com.example.rentwise.MyAdapter;
import com.example.rentwise.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class ChooseItemFragment extends BottomSheetDialogFragment {

    private static final String ARG_TYPE = "type";
    public static final String TYPE_CUSTOMER = "customer";
    public static final String TYPE_VEHICLE = "vehicle";

    private String type;
    private RecyclerView recyclerViewChooseItem;
    private MyAdapter<?> adapter;

    public ChooseItemFragment() {
        // Required empty public constructor
    }

    public static ChooseItemFragment newInstance(String type) {
        ChooseItemFragment fragment = new ChooseItemFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString(ARG_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choose_item, container, false);

        recyclerViewChooseItem = view.findViewById(R.id.recyclerViewChooseItem);
        recyclerViewChooseItem.setLayoutManager(new LinearLayoutManager(getContext()));

        // Load data and set up RecyclerView based on type
        if (TYPE_CUSTOMER.equals(type)) {
            getCustomerList();
        } else if (TYPE_VEHICLE.equals(type)) {
            getVehicleList();
        }

        return view;
    }

    private void getCustomerList() {
        FirebaseRepository<Customer> customerRepository = new FirebaseRepository<>("Customer", Customer.class);
        customerRepository.getAll(new FirebaseRepository.OnFetchListListener<Customer>() {
            @Override
            public void onSuccess(List<Customer> customers) {
                setupCustomerRecyclerView(customers);  // Set up RecyclerView after data is loaded
            }

            @Override
            public void onFailure(String message) {
                Log.e("ChooseItemFragment", "Failed to fetch customer data: " + message);
            }
        });
    }

    private void getVehicleList() {
        FirebaseRepository<Motobike> vehicleRepository = new FirebaseRepository<>("Motobike", Motobike.class);
        vehicleRepository.getAll(new FirebaseRepository.OnFetchListListener<Motobike>() {
            @Override
            public void onSuccess(List<Motobike> vehicles) {
                // Filter for only offline vehicles
                List<Motobike> offlineVehicles = new ArrayList<>();
                for (Motobike vehicle : vehicles) {
                    if ("Offline".equalsIgnoreCase(vehicle.getStatus())) {
                        offlineVehicles.add(vehicle);
                    }
                }

                setupVehicleRecyclerView(offlineVehicles);
            }

            @Override
            public void onFailure(String message) {
                Log.e("ChooseItemFragment", "Failed to fetch vehicle data: " + message);
            }
        });
    }

    private void setupCustomerRecyclerView(List<Customer> customerList) {
        adapter = new MyAdapter<>(customerList, R.layout.item_vehicle, new MyAdapter.Binder<Customer>() {
            @Override
            public void bind(View itemView, Customer customer) {
                TextView nameTextView = itemView.findViewById(R.id.nameCustomer);
                TextView locationCustomerTextView = itemView.findViewById(R.id.locationCustomer);
                TextView tv_StatusOn = itemView.findViewById(R.id.tv_StatusOn);
                ImageView imgStatusOn = itemView.findViewById(R.id.imgStatusOn);

                // Bind customer data
                nameTextView.setText(customer.getName());
                locationCustomerTextView.setText(customer.getGender());
                tv_StatusOn.setText(customer.getCccd());
                imgStatusOn.setVisibility(View.INVISIBLE);

                itemView.setOnClickListener(v -> onItemSelected(customer));
            }
        });

        recyclerViewChooseItem.setAdapter(adapter);
    }

    private void setupVehicleRecyclerView(List<Motobike> motobikeList) {
        adapter = new MyAdapter<>(motobikeList, R.layout.item_vehicle, new MyAdapter.Binder<Motobike>() {
            @Override
            public void bind(View itemView, Motobike motobike) {
                TextView nameTextView = itemView.findViewById(R.id.nameCustomer);
                TextView statusTextView = itemView.findViewById(R.id.tv_StatusOn);
                TextView locationCustomerTextView = itemView.findViewById(R.id.locationCustomer);
                ImageView statusIcon = itemView.findViewById(R.id.imgStatusOn);

                nameTextView.setText(motobike.getName());
                statusTextView.setText(motobike.getNumberPlate());
                locationCustomerTextView.setText("Trạng thái: không hoạt động");
                statusIcon.setImageResource(R.drawable.img_status_offline);

                itemView.setOnClickListener(v -> onItemSelected(motobike));
            }
        });

        recyclerViewChooseItem.setAdapter(adapter);
    }

    private void onItemSelected(Object selectedItem) {
        if (getActivity() instanceof OnItemSelectedListener) {
            ((OnItemSelectedListener) getActivity()).onItemSelected(selectedItem);
        }
        dismiss();
    }

    public interface OnItemSelectedListener {
        void onItemSelected(Object selectedItem);
    }
}
