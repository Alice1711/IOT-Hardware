package com.example.rentwise.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Motobike;
import com.example.rentwise.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Map;

public class VehicleDetailFragment extends BottomSheetDialogFragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_NUMBER_PLATE = "numberPlate";
    private static final String ARG_PICTURE = "picture";
    private static final String ARG_STATUS = "status";
    private static final String ARG_PRICE = "price";

    private String name;
    private String numberPlate;
    private String picture;
    private String status;
    private String price;

    private FirebaseRepository repository;
    private EditText edtNumPlate, edtVehicleName, edtPrice, edtPicture, edtStatus;

    public VehicleDetailFragment() {}

    public static VehicleDetailFragment newInstance(String name, String numberPlate, String picture, String status, String price) {
        VehicleDetailFragment fragment = new VehicleDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_NUMBER_PLATE, numberPlate);
        args.putString(ARG_PICTURE, picture);
        args.putString(ARG_STATUS, status);
        args.putString(ARG_PRICE, price);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            numberPlate = getArguments().getString(ARG_NUMBER_PLATE);
            picture = getArguments().getString(ARG_PICTURE);
            status = getArguments().getString(ARG_STATUS);
            price = getArguments().getString(ARG_PRICE);
        }
        repository = new FirebaseRepository<>("Motobike", Motobike.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vehicle_detail, container, false);

        // Set up EditText fields
        edtNumPlate = view.findViewById(R.id.edtNumPlate2);
        edtVehicleName = view.findViewById(R.id.edtVehicleName2);
        edtPrice = view.findViewById(R.id.edtPrice2);
        edtPicture = view.findViewById(R.id.edtPicture2);
        edtStatus = view.findViewById(R.id.edtStatus2);

        edtVehicleName.setText(name);
        edtNumPlate.setText(numberPlate);
        edtPrice.setText(price);
        edtPicture.setText(picture);
        edtStatus.setText(status);

        // Set up back button functionality
        ImageButton btnBackManage = view.findViewById(R.id.btnBackManage);
        btnBackManage.setOnClickListener(v -> dismiss());

        // Set up delete button functionality
        Button btnDelete = view.findViewById(R.id.btnDelete2);
        btnDelete.setOnClickListener(v -> deleteVehicle());

        // Set up update button with confirmation dialog
        Button btnUpdateInfo = view.findViewById(R.id.btnUpdateInfo);
        btnUpdateInfo.setOnClickListener(v -> confirmEdit());

        return view;
    }

    private void deleteVehicle() {
        repository.delete(numberPlate, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().setFragmentResult("vehicleDeleted", new Bundle());
                }
                dismiss();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "Xoá thất bại: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmEdit() {
        // Show a confirmation dialog before saving changes
        new AlertDialog.Builder(requireContext())
                .setTitle("Chỉnh sửa")
                .setMessage("Bạn có chắc chắn muốn lưu các thay đổi?")
                .setPositiveButton("Có", (dialog, which) -> saveChanges())
                .setNegativeButton("Không", null)
                .show();
    }

    private void saveChanges() {
        // Collect updated data from EditText fields
        String updatedName = edtVehicleName.getText().toString().trim();
        String updatedNumberPlate = edtNumPlate.getText().toString().trim();
        String updatedPrice = edtPrice.getText().toString().trim();
        String updatedPicture = edtPicture.getText().toString().trim();
        String updatedStatus = edtStatus.getText().toString().trim();

        if (updatedName.isEmpty() || updatedNumberPlate.isEmpty() || updatedPrice.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền tất cả các trường bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update fields in Firebase
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", updatedName);
        updates.put("numberPlate", updatedNumberPlate);
        updates.put("price", updatedPrice);
        updates.put("picture", updatedPicture);
        updates.put("status", updatedStatus);

        repository.update(numberPlate, updates, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().setFragmentResult("vehicleUpdated", new Bundle());
                }
                dismiss();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "Cập nhật thất bại: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
