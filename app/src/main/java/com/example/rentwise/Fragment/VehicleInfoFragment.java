package com.example.rentwise.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Motobike;
import com.example.rentwise.R;

public class VehicleInfoFragment extends Fragment {

    private EditText edtNumPlate, edtVehicleName, edtPrice, edtPicture;
    private Button btnAdd3;

    public VehicleInfoFragment() {}

    public static VehicleInfoFragment newInstance() {
        return new VehicleInfoFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_info, container, false);

        edtNumPlate = view.findViewById(R.id.edtNumPlate);
        edtVehicleName = view.findViewById(R.id.edtVehicleName);
        edtPrice = view.findViewById(R.id.edtPrice);
        edtPicture = view.findViewById(R.id.edtPicture);

        ImageButton btnBackVehicleInfo = view.findViewById(R.id.btnBackVehicleInfo);
        btnBackVehicleInfo.setOnClickListener(v -> {
            if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        btnAdd3 = view.findViewById(R.id.btnAdd3);
        btnAdd3.setOnClickListener(v -> addVehicleToFirebase());

        return view;
    }

    private void addVehicleToFirebase() {
        String numberPlate = edtNumPlate.getText().toString().trim();
        String name = edtVehicleName.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();
        String picture = edtPicture.getText().toString().trim();
        String status = "Offline";

        if (numberPlate.isEmpty() || name.isEmpty() || price.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng điền tất cả các trường", Toast.LENGTH_SHORT).show();
            return;
        }

        Motobike motobike = new Motobike(name, numberPlate, picture, status, price);

        FirebaseRepository<Motobike> repository = new FirebaseRepository<>("Motobike", Motobike.class);
        repository.save(numberPlate, motobike, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getActivity(), "Thêm xe thành công!", Toast.LENGTH_SHORT).show();
                clearFields();

                // Notify ManageVehicle to reload data
                Bundle result = new Bundle();
                result.putBoolean("dataUpdated", true);
                getParentFragmentManager().setFragmentResult("requestKey", result);

                // Optionally go back to ManageVehicle
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), "Không thể thêm xe: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        edtNumPlate.setText("");
        edtVehicleName.setText("");
        edtPrice.setText("");
        edtPicture.setText("");
    }
}
