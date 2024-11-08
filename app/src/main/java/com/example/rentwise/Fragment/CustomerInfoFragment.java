package com.example.rentwise.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rentwise.ModelData.Customer;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.R;

public class CustomerInfoFragmentFragment extends Fragment {

    private EditText edtCusId, edtCusName, edtCusGender, edtCusPhone;
    private Button btnSave2;

    public CustomerInfoFragmentFragment() {}

    public static CustomerInfoFragmentFragment newInstance() {
        return new CustomerInfoFragmentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_info_fragment, container, false);

        // Initialize EditText and Button fields
        edtCusId = view.findViewById(R.id.edtCusId);
        edtCusName = view.findViewById(R.id.edtCusName);
        edtCusGender = view.findViewById(R.id.edtCusGender);
        edtCusPhone = view.findViewById(R.id.edtCusPhone);
        btnSave2 = view.findViewById(R.id.btnSave2);

        // Back button functionality
        ImageButton btnBackCustomerInfo = view.findViewById(R.id.btnBackCustomerInfo);
        btnBackCustomerInfo.setOnClickListener(v -> {
            if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        // Save button functionality
        btnSave2.setOnClickListener(v -> addCustomerToFirebase());

        return view;
    }

    private void addCustomerToFirebase() {
        // Retrieve data from input fields
        String customerId = edtCusId.getText().toString().trim();
        String customerName = edtCusName.getText().toString().trim();
        String customerGender = edtCusGender.getText().toString().trim();
        String customerPhone = edtCusPhone.getText().toString().trim();

        // Check for required fields
        if (customerId.isEmpty() || customerName.isEmpty() || customerGender.isEmpty() || customerPhone.isEmpty()) {
            Toast.makeText(getActivity(), "Vui lòng điền tất cả các trường", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a Customer object
        Customer customer = new Customer(customerId, customerName, customerGender, customerPhone);

        // Save to Firebase
        FirebaseRepository<Customer> repository = new FirebaseRepository<>("Customer", Customer.class);
        repository.save(customerId, customer, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getActivity(), "Thêm khách hàng thành công!", Toast.LENGTH_SHORT).show();
                clearFields();

                // Optionally go back to the previous screen
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), "Không thể thêm khách hàng: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Clear input fields after saving
    private void clearFields() {
        edtCusId.setText("");
        edtCusName.setText("");
        edtCusGender.setText("");
        edtCusPhone.setText("");
    }
}
