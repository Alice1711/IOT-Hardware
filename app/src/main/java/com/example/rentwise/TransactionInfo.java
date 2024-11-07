package com.example.rentwise;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rentwise.Fragment.ChooseItemFragment;
import com.example.rentwise.ModelData.Customer;
import com.example.rentwise.ModelData.FirebaseRepository;
import com.example.rentwise.ModelData.Motobike;
import com.example.rentwise.ModelData.Transaction;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class TransactionInfo extends AppCompatActivity implements ChooseItemFragment.OnItemSelectedListener {

    private EditText edtCusId2;
    private EditText edtVehId;
    private EditText edtTranStartDay;
    private EditText edtTranEndDay;
    private EditText edtTranZone;
    private ImageButton btnBackCustomerInfo;
    private Button btnSave2;

    private FirebaseRepository<Transaction> transactionRepository;
    private final Calendar calendar = Calendar.getInstance();  // Calendar instance for date picking

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_info);

        edtCusId2 = findViewById(R.id.edtCusId2);
        edtVehId = findViewById(R.id.edtVehId);
        edtTranStartDay = findViewById(R.id.edtTranStartDay);
        edtTranEndDay = findViewById(R.id.edtTranEndDay);
        edtTranZone = findViewById(R.id.edtTranZone);
        btnBackCustomerInfo = findViewById(R.id.btnBackCustomerInfo);
        btnSave2 = findViewById(R.id.btnSave2);

        transactionRepository = new FirebaseRepository<>("Transaction", Transaction.class);

        edtCusId2.setOnClickListener(v -> {
            ChooseItemFragment chooseCustomerFragment = ChooseItemFragment.newInstance(ChooseItemFragment.TYPE_CUSTOMER);
            chooseCustomerFragment.show(getSupportFragmentManager(), "ChooseCustomer");
        });

        edtVehId.setOnClickListener(v -> {
            ChooseItemFragment chooseVehicleFragment = ChooseItemFragment.newInstance(ChooseItemFragment.TYPE_VEHICLE);
            chooseVehicleFragment.show(getSupportFragmentManager(), "ChooseVehicle");
        });

        btnBackCustomerInfo.setOnClickListener(v -> finish());

        btnSave2.setOnClickListener(v -> saveTransactionData());

        // Set up date pickers for startDay and endDay
        edtTranStartDay.setOnClickListener(v -> showDatePickerDialog(edtTranStartDay));
        edtTranEndDay.setOnClickListener(v -> showDatePickerDialog(edtTranEndDay));
    }

    private void showDatePickerDialog(EditText dateField) {
        // Create a DatePickerDialog with the current date as default
        new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDateField(dateField);
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateDateField(EditText dateField) {
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        dateField.setText(sdf.format(calendar.getTime()));
    }

    private void saveTransactionData() {
        String customerId = edtCusId2.getText().toString().trim();
        String vehicleId = edtVehId.getText().toString().trim();
        String startDate = edtTranStartDay.getText().toString().trim();
        String endDate = edtTranEndDay.getText().toString().trim();
        String zone = edtTranZone.getText().toString().trim();

        if (customerId.isEmpty() || vehicleId.isEmpty() || startDate.isEmpty() || endDate.isEmpty() || zone.isEmpty()) {
            Toast.makeText(this, "Hãy điền đầy đủ các thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        String transactionId = transactionRepository.getNewKey();
        if (transactionId == null) {
            Toast.makeText(this, "Failed to generate transaction ID.", Toast.LENGTH_SHORT).show();
            return;
        }

        Transaction transaction = new Transaction(transactionId, customerId, vehicleId, startDate, endDate, zone);

        transactionRepository.save(transactionId, transaction, new FirebaseRepository.OnOperationListener() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(TransactionInfo.this, "Transaction saved successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity after saving
            }

            @Override
            public void onFailure(String message) {
                Toast.makeText(TransactionInfo.this, "Failed to save transaction: " + message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemSelected(Object selectedItem) {
        if (selectedItem instanceof Customer) {
            edtCusId2.setText(((Customer) selectedItem).getCccd());
        } else if (selectedItem instanceof Motobike) {
            edtVehId.setText(((Motobike) selectedItem).getNumberPlate());
        }
    }
}
