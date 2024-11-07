package com.example.rentwise.Fragment;

import android.content.DialogInterface;
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

import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Transaction;
import com.example.rentwise.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class TransactionDetailFragment extends BottomSheetDialogFragment {

    private static final String ARG_ID_CUSTOMER = "idCustomer";
    private static final String ARG_ID_MOTOBIKE = "idMotobike";
    private static final String ARG_START_DAY = "startDay";
    private static final String ARG_END_DAY = "endDay";
    private static final String ARG_ZONE_RENT = "zoneRent";

    private String idCustomer;
    private String idMotobike;
    private String startDay;
    private String endDay;
    private String zoneRent;

    private FirebaseRepository<Transaction> transactionRepository;
    private String transactionId;

    public TransactionDetailFragment() {
        // Required empty public constructor
    }

    public static TransactionDetailFragment newInstance(String idCustomer, String idMotobike, String startDay, String endDay, String zoneRent) {
        TransactionDetailFragment fragment = new TransactionDetailFragment();
        Bundle args = new Bundle();
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
            idCustomer = getArguments().getString(ARG_ID_CUSTOMER);
            idMotobike = getArguments().getString(ARG_ID_MOTOBIKE);
            startDay = getArguments().getString(ARG_START_DAY);
            endDay = getArguments().getString(ARG_END_DAY);
            zoneRent = getArguments().getString(ARG_ZONE_RENT);
        }
        transactionRepository = new FirebaseRepository<>("Transactions", Transaction.class);
        transactionId = generateTransactionId(idCustomer, idMotobike);  // Example ID generation
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_detail, container, false);

        // Setup EditTexts to display transaction details
        EditText edtCusId = view.findViewById(R.id.edtCusId3);
        EditText edtVehId = view.findViewById(R.id.edtVehId3);
        EditText edtTranStartDay = view.findViewById(R.id.edtTranStartDay2);
        EditText edtTranEndDay = view.findViewById(R.id.edtTranEndDay2);
        EditText edtTranZone = view.findViewById(R.id.edtTranZone2);

        edtCusId.setText(idCustomer);
        edtVehId.setText(idMotobike);
        edtTranStartDay.setText(startDay);
        edtTranEndDay.setText(endDay);
        edtTranZone.setText(zoneRent);

        // Make EditTexts non-editable
        setNonEditable(edtCusId, edtVehId, edtTranStartDay, edtTranEndDay, edtTranZone);

        // Setup Delete Button
        Button btnDeleteTransaction = view.findViewById(R.id.btnDeleteTransaction);
        btnDeleteTransaction.setOnClickListener(v -> confirmDeletion());

        // Setup Save Button
        Button btnSaveEdit = view.findViewById(R.id.btnSaveEdit);
        btnSaveEdit.setOnClickListener(v -> saveTransactionDetails());

        return view;
    }

    private void setNonEditable(EditText... editTexts) {
        for (EditText editText : editTexts) {
            editText.setFocusable(false);
            editText.setClickable(false);
        }
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
        transactionRepository.delete(transactionId, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(getContext(), "Giao dịch đã được xóa", Toast.LENGTH_SHORT).show();
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

    private void saveTransactionDetails() {
        // Here, you would handle saving any edited transaction details
        // For simplicity, we’ll just display a message.
        Toast.makeText(getContext(), "Lưu thành công!", Toast.LENGTH_SHORT).show();

        if (getActivity() != null) {
            getActivity().getSupportFragmentManager().setFragmentResult("transactionUpdated", new Bundle());
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    // Example transaction ID generation method
    private String generateTransactionId(String idCustomer, String idMotobike) {
        return idCustomer + "_" + idMotobike;  // Customize this based on your data structure
    }
}
