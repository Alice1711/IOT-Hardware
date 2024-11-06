package com.example.rentwise.Fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.rentwise.R;

public class ButtonsUpdateFragment extends BottomSheetDialogFragment {

    public ButtonsUpdateFragment() {
        // Required empty public constructor
    }

    public static ButtonsUpdateFragment newInstance(String param1, String param2) {
        ButtonsUpdateFragment fragment = new ButtonsUpdateFragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_buttons_update, container, false);
    }
}
