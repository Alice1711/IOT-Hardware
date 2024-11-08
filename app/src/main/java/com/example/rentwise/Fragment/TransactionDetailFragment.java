package com.example.rentwise.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.example.rentwise.ModelData.Customer;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Transaction;
import com.example.rentwise.ModelData.Motobike;
import com.example.rentwise.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Map;

public class TransactionDetailFragment extends BottomSheetDialogFragment implements ChooseItemFragment.OnItemSelectedListener{

    private static final String ARG_TRANSACTION_ID = "transactionId";
    private static final String ARG_ID_CUSTOMER = "idCustomer";
    private static final String ARG_ID_MOTOBIKE = "idMotobike";
    private static final String ARG_START_DAY = "startDay";
    private static final String ARG_END_DAY = "endDay";
    private static final String ARG_ZONE_RENT = "zoneRent";

    private String transactionId;
    private String idCustomer;
    private String idMotobike;
    private String startDay;
    private String endDay;
    private String zoneRent;

    private FirebaseRepository<Transaction> transactionRepository;
    private FirebaseRepository<Motobike> motobikeRepository;
    private EditText edtCusId, edtVehId, edtTranStartDay, edtTranEndDay, edtTranZone;


    public static TransactionDetailFragment newInstance(String transactionId, String idCustomer, String idMotobike, String startDay, String endDay, String zoneRent) {
        TransactionDetailFragment fragment = new TransactionDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TRANSACTION_ID, transactionId);
        args.putString(ARG_ID_CUSTOMER, idCustomer);
        args.putString(ARG_ID_MOTOBIKE, idMotobike);
        args.putString(ARG_START_DAY, startDay);
        args.putString(ARG_END_DAY, endDay);
        args.putString(ARG_ZONE_RENT, zoneRent);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            transactionId = getArguments().getString(ARG_TRANSACTION_ID);
            idCustomer = getArguments().getString(ARG_ID_CUSTOMER);
            idMotobike = getArguments().getString(ARG_ID_MOTOBIKE);
            startDay = getArguments().getString(ARG_START_DAY);
            endDay = getArguments().getString(ARG_END_DAY);
            zoneRent = getArguments().getString(ARG_ZONE_RENT);
        }
        transactionRepository = new FirebaseRepository<>("Transaction", Transaction.class);
        motobikeRepository = new FirebaseRepository<>("Motobike", Motobike.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_detail, container, false);

        // Initialize EditTexts
        edtCusId = view.findViewById(R.id.edtCusId3);
        edtVehId = view.findViewById(R.id.edtVehId3);
        edtTranStartDay = view.findViewById(R.id.edtTranStartDay2);
        edtTranEndDay = view.findViewById(R.id.edtTranEndDay2);
        edtTranZone = view.findViewById(R.id.edtTranZone2);

        // Set initial text values
        edtCusId.setText(idCustomer);
        edtVehId.setText(idMotobike);
        edtTranStartDay.setText(startDay);
        edtTranEndDay.setText(endDay);
        edtTranZone.setText(zoneRent);

        // Set click listeners to open ChooseItemFragment for selection
        edtCusId.setOnClickListener(v -> openChooseItemFragment(ChooseItemFragment.TYPE_CUSTOMER));
        edtVehId.setOnClickListener(v -> openChooseItemFragment(ChooseItemFragment.TYPE_VEHICLE));

        // Setup buttons
        Button btnDeleteTransaction = view.findViewById(R.id.btnDeleteTransaction);
        btnDeleteTransaction.setOnClickListener(v -> confirmDeletion());

        Button btnSaveEdit = view.findViewById(R.id.btnSaveEdit);
        btnSaveEdit.setOnClickListener(v -> confirmEdit());

        return view;
    }

    private void openChooseItemFragment(String type) {
        ChooseItemFragment chooseItemFragment = ChooseItemFragment.newInstance(type);
        chooseItemFragment.setTargetFragment(this, 0); // Set this fragment as the target
        chooseItemFragment.show(getParentFragmentManager(), "ChooseItemFragment");
    }

    @Override
    public void onItemSelected(Object selectedItem) {
        // Check the type of the selected item and update the corresponding field
        if (selectedItem instanceof Customer) {
            Customer customer = (Customer) selectedItem;
            edtCusId.setText(customer.getCccd()); // Update customer ID field
        } else if (selectedItem instanceof Motobike) {
            Motobike motobike = (Motobike) selectedItem;
            edtVehId.setText(motobike.getNumberPlate()); // Update vehicle ID field
        }
    }

    private void confirmEdit() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Chỉnh sửa")
                .setMessage("Bạn có chắc chắn muốn lưu các thay đổi?")
                .setPositiveButton("Có", (dialog, which) -> saveTransactionChanges())
                .setNegativeButton("Không", null)
                .show();
    }

    private void saveTransactionChanges() {
        // Collect updated data from EditText fields
        String updatedCustomerID = edtCusId.getText().toString().trim();
        String updatedVehicleID = edtVehId.getText().toString().trim();
        String updatedStartDay = edtTranStartDay.getText().toString().trim();
        String updatedEndDay = edtTranEndDay.getText().toString().trim();
        String updatedZone = edtTranZone.getText().toString().trim();

        // Prepare a map with the updated values
        Map<String, Object> updates = new HashMap<>();
        updates.put("idCustomer", updatedCustomerID);
        updates.put("idMotobike", updatedVehicleID);
        updates.put("startDay", updatedStartDay);
        updates.put("endDay", updatedEndDay);
        updates.put("zoneRent", updatedZone);

        // Update in Firebase
        transactionRepository.update(transactionId, updates, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Cập nhật giao dịch thành công!", Toast.LENGTH_SHORT).show();
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().setFragmentResult("transactionUpdated", new Bundle());
                }
                dismiss();
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "Cập nhật giao dịch thất bại: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDeletion() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Xóa giao dịch")
                .setMessage("Bạn có chắc chắn muốn xóa giao dịch này?")
                .setPositiveButton("Có", (dialog, which) -> deleteTransaction())
                .setNegativeButton("Không", null)
                .show();
    }

    private void deleteTransaction() {
        if (transactionId == null || transactionId.isEmpty()) {
            Toast.makeText(getContext(), "Transaction ID is missing.", Toast.LENGTH_SHORT).show();
            return;
        }

        transactionRepository.delete(transactionId, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Giao dịch đã được xóa", Toast.LENGTH_SHORT).show();
                updateVehicleStatusToOffline(idMotobike);
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().setFragmentResult("transactionUpdated", new Bundle());
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(getContext(), "Xóa thất bại: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateVehicleStatusToOffline(String vehicleID) {
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "offline");

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
}
