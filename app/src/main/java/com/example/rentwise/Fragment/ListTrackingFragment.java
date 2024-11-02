package com.example.rentwise.Fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.rentwise.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class ListTrackingFragment extends Fragment implements ListVehicleTrackFragment.OnVehicleItemClickListener {

    private GoogleMap googleMap;

    private OnMapReadyCallback callback = map -> {
        googleMap = map;
        LatLng initialLocation = new LatLng(-34, 151);
        googleMap.addMarker(new MarkerOptions().position(initialLocation).title("Marker in Sydney"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(initialLocation));
    };

    @Override
    public void onVehicleItemClick(double latitude, double longitude) {
        if (googleMap != null) {
            LatLng vehicleLocation = new LatLng(latitude, longitude);
            googleMap.clear();
            googleMap.addMarker(new MarkerOptions().position(vehicleLocation).title("Vehicle Location"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(vehicleLocation, 15)); // Set zoom level
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list_tracking, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }

        ImageButton showBottomSheetButton = view.findViewById(R.id.showBottomSheetButton);
        showBottomSheetButton.setOnClickListener(v -> {
            ListVehicleTrackFragment bottomSheet = ListVehicleTrackFragment.newInstance("param1", "param2");
            bottomSheet.setOnVehicleItemClickListener(this); // Set the listener
            bottomSheet.show(getChildFragmentManager(), bottomSheet.getTag());
        });
    }
}