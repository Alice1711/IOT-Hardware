package com.example.rentwise.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.rentwise.ModelData.Customer;
import com.example.rentwise.ModelData.Motobike;
import com.example.rentwise.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TransactionInfoFragment extends Fragment implements ChooseItemFragment.OnItemSelectedListener {

    private EditText edtCusId2, edtVehId, edtTranStartDay, edtTranEndDay, edtTranZone;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transaction_info, container, false);

        edtCusId2 = view.findViewById(R.id.edtCusId2);
        edtVehId = view.findViewById(R.id.edtVehId);
        edtTranStartDay = view.findViewById(R.id.edtTranStartDay);
        edtTranEndDay = view.findViewById(R.id.edtTranEndDay);
        edtTranZone = view.findViewById(R.id.edtTranZone);
        Button btnSave2 = view.findViewById(R.id.btnSave2);

        edtCusId2.setOnClickListener(v -> openChooseItemFragment(ChooseItemFragment.TYPE_CUSTOMER));
        edtVehId.setOnClickListener(v -> openChooseItemFragment(ChooseItemFragment.TYPE_VEHICLE));

        btnSave2.setOnClickListener(v -> saveTransactionData());

        edtTranStartDay.setOnClickListener(v -> showDatePickerDialog(edtTranStartDay));
        edtTranEndDay.setOnClickListener(v -> showDatePickerDialog(edtTranEndDay));

        return view;
    }

    private void openChooseItemFragment(String type) {
        ChooseItemFragment chooseItemFragment = ChooseItemFragment.newInstance(type);
        chooseItemFragment.setTargetFragment(this, 0); // Set this as target fragment
        chooseItemFragment.show(getParentFragmentManager(), "ChooseItemFragment");
    }

    private void showDatePickerDialog(EditText dateField) {
        new DatePickerDialog(requireContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateField(dateField);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

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

    private void saveTransactionData() {
        Toast.makeText(requireContext(), "Lưu thành công!", Toast.LENGTH_SHORT).show();
    }
}
