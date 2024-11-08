package com.example.rentwise.Fragment;

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

import com.example.rentwise.ModelData.Customer;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Map;

public class CustomerDetailFragment extends BottomSheetDialogFragment {

    private static final String ARG_CCCD = "cccd";
    private static final String ARG_NAME = "name";
    private static final String ARG_GENDER = "gender";
    private static final String ARG_PHONE = "phone";

    private String cccd;
    private String name;
    private String gender;
    private String phone;

    private FirebaseRepository<Customer> repository;
    private EditText edtCccd, edtName, edtGender, edtPhone;

    public CustomerDetailFragment() {}

    public static CustomerDetailFragment newInstance(String cccd, String name, String gender, String phone) {
        CustomerDetailFragment fragment = new CustomerDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CCCD, cccd);
        args.putString(ARG_NAME, name);
        args.putString(ARG_GENDER, gender);
        args.putString(ARG_PHONE, phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            cccd = getArguments().getString(ARG_CCCD);
            name = getArguments().getString(ARG_NAME);
            gender = getArguments().getString(ARG_GENDER);
            phone = getArguments().getString(ARG_PHONE);
        }
        repository = new FirebaseRepository<>("Customer", Customer.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_detail, container, false);

        edtCccd = view.findViewById(R.id.edtCusId2);
        edtName = view.findViewById(R.id.edtCusName2);
        edtGender = view.findViewById(R.id.edtCusGender2);
        edtPhone = view.findViewById(R.id.edtCusPhone2);

        // Set initial text values
        edtCccd.setText(cccd);
        edtName.setText(name);
        edtGender.setText(gender);
        edtPhone.setText(phone);



        // Set up delete button functionality
        Button btnDeleteCus2 = view.findViewById(R.id.btnDeleteCus2);
        btnDeleteCus2.setOnClickListener(v -> confirmDeletion());

        // Set up update button with confirmation dialog
        Button btnUpdateInfoCus = view.findViewById(R.id.btnUpdateInfoCus);
        btnUpdateInfoCus.setOnClickListener(v -> confirmEdit());

        return view;
    }

    private void deleteCustomer() {
        repository.delete(cccd, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Xóa thành công!", Toast.LENGTH_SHORT).show();
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().setFragmentResult("customerDeleted", new Bundle());
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
        new AlertDialog.Builder(requireContext())
                .setTitle("Chỉnh sửa")
                .setMessage("Bạn có chắc chắn muốn lưu các thay đổi?")
                .setPositiveButton("Có", (dialog, which) -> saveChanges())
                .setNegativeButton("Không", null)
                .show();
    }

    private void confirmDeletion() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa khách hàng")
                .setMessage("Bạn có chắc chắn muốn xóa khách hàng này?")
                .setPositiveButton("Có", (dialog, which) -> deleteCustomer())
                .setNegativeButton("Không", null)
                .show();
    }

    private void saveChanges() {
        String updatedName = edtName.getText().toString().trim();
        String updatedGender = edtGender.getText().toString().trim();
        String updatedPhone = edtPhone.getText().toString().trim();
        String updatedCccd = edtCccd.getText().toString().trim();

        if (updatedName.isEmpty() || updatedGender.isEmpty() || updatedPhone.isEmpty() || updatedCccd.isEmpty()) {
            Toast.makeText(getContext(), "Vui lòng điền tất cả các trường bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", updatedName);
        updates.put("gender", updatedGender);
        updates.put("phone", updatedPhone);
        updates.put("cccd", updatedCccd);

        repository.update(cccd, updates, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().setFragmentResult("customerUpdated", new Bundle());
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
