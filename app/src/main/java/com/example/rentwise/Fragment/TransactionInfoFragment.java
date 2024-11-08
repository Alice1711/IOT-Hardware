package com.example.rentwise.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.rentwise.ModelData.Customer;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Motobike;
import com.example.rentwise.ModelData.Transaction;
import com.example.rentwise.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TransactionInfoFragment extends Fragment implements ChooseItemFragment.OnItemSelectedListener {

    private EditText edtCusId2, edtVehId, edtTranStartDay, edtTranEndDay, edtTranZone;
    private final Calendar calendar = Calendar.getInstance();
    private FirebaseRepository<Transaction> transactionRepository;
    private FirebaseRepository<Motobike> motobikeRepository;
    private static int transactionCount = 0; // Global counter for transaction count

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_info, container, false);

        // Initialize views
        edtCusId2 = view.findViewById(R.id.edtCusId2);
        edtVehId = view.findViewById(R.id.edtVehId);
        edtTranStartDay = view.findViewById(R.id.edtTranStartDay);
        edtTranEndDay = view.findViewById(R.id.edtTranEndDay);
        edtTranZone = view.findViewById(R.id.edtTranZone);
        Button btnSaveTran = view.findViewById(R.id.btnSaveTran);

        transactionRepository = new FirebaseRepository<>("Transaction", Transaction.class);
        motobikeRepository = new FirebaseRepository<>("Motobike", Motobike.class);

        // Initialize the transaction count if it’s zero
        if (transactionCount == 0) {
            initializeTransactionCount();
        }

        // Set click listeners for inputs
        edtCusId2.setOnClickListener(v -> openChooseItemFragment(ChooseItemFragment.TYPE_CUSTOMER));
        edtVehId.setOnClickListener(v -> openChooseItemFragment(ChooseItemFragment.TYPE_VEHICLE));

        // Set click listener for Save button
        btnSaveTran.setOnClickListener(v -> saveTransactionData());

        // Set click listeners for date pickers
        edtTranStartDay.setOnClickListener(v -> showDatePickerDialog(edtTranStartDay));
        edtTranEndDay.setOnClickListener(v -> showDatePickerDialog(edtTranEndDay));

        // Back button functionality
        ImageButton btnBackTranInfo = view.findViewById(R.id.btnBackTranInfo);
        btnBackTranInfo.setOnClickListener(v -> {
            if (getActivity() != null && getActivity().getSupportFragmentManager() != null) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

        return view;
    }

    // Initialize transaction count from Firebase only once
    private void initializeTransactionCount() {
        transactionRepository.getAll(new FirebaseRepository.OnFetchListListener<Transaction>() {
            @Override
            public void onSuccess(List<Transaction> transactions) {
                transactionCount = transactions.size();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "Không thể tải số lượng giao dịch: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Open fragment to select items
    private void openChooseItemFragment(String type) {
        ChooseItemFragment chooseItemFragment = ChooseItemFragment.newInstance(type);
        chooseItemFragment.setTargetFragment(this, 0);
        chooseItemFragment.show(getParentFragmentManager(), "ChooseItemFragment");
    }

    // Show date picker dialog
    private void showDatePickerDialog(EditText dateField) {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateField(dateField);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    // Update date field with selected date
    private void updateDateField(EditText dateField) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        dateField.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onItemSelected(Object selectedItem) {
        if (selectedItem instanceof Customer) {
            edtCusId2.setText(((Customer) selectedItem).getCccd());
        } else if (selectedItem instanceof Motobike) {
            edtVehId.setText(((Motobike) selectedItem).getNumberPlate());
        }
    }

    // Save transaction data
    private void saveTransactionData() {
        addTransactionToFirebase();
    }

    private void addTransactionToFirebase() {
        String customerID = edtCusId2.getText().toString().trim();
        String vehicleID = edtVehId.getText().toString().trim();
        String startDate = edtTranStartDay.getText().toString().trim();
        String endDate = edtTranEndDay.getText().toString().trim();
        String zone = edtTranZone.getText().toString().trim();

        if (customerID.isEmpty() || vehicleID.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || zone.isEmpty()) {
            Toast.makeText(requireContext(), "Vui lòng điền tất cả các trường", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique transaction ID using the current timestamp and customerID
        String transactionId = "transaction_" + System.currentTimeMillis() + "_" + customerID;

        // Create Transaction object with the generated transactionId as id_rent
        Transaction transaction = new Transaction(transactionId, customerID, vehicleID, startDate, endDate, zone);

        // Save using transactionId (id_rent) as the Firebase key
        transactionRepository.save(transactionId, transaction, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getActivity(), "Thêm giao dịch thành công!", Toast.LENGTH_SHORT).show();
                updateVehicleStatusToOnline(vehicleID);
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
                Toast.makeText(getActivity(), "Không thể thêm giao dịch: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Update vehicle status to "online"
    private void updateVehicleStatusToOnline(String vehicleID) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "Online");

        motobikeRepository.update(vehicleID, updates, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getActivity(), "Cập nhật trạng thái xe thành công!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getActivity(), "Không thể cập nhật trạng thái xe: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearFields() {
        edtCusId2.setText("");
        edtVehId.setText("");
        edtTranStartDay.setText("");
        edtTranEndDay.setText("");
        edtTranZone.setText("");
    }
}
