package com.example.rentwise.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.rentwise.R;

public class ButtonsUpdateFragment extends BottomSheetDialogFragment {

    private OnButtonClickListener listener;

    // Định nghĩa Interface callback
    public interface OnButtonClickListener {
        void onAddButtonClick();
        void onDeleteButtonClick();
        void onCancelButtonClick();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buttons_update, container, false);

        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnDelete = view.findViewById(R.id.btnDelete);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        // Gọi callback khi nhấn các nút
        btnAdd.setOnClickListener(v -> {
            if (listener != null) {
                listener.onAddButtonClick();
            }
            dismiss();
        });

        btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteButtonClick();
            }
            dismiss();
        });

        btnCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onCancelButtonClick();
            }
            dismiss();
        });

        return view;
    }

    // Phương thức để gán listener từ Activity
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener = listener;
    }

    public static ButtonsUpdateFragment newInstance(String param1, String param2) {
        ButtonsUpdateFragment fragment = new ButtonsUpdateFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }
}