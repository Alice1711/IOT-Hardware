package com.example.rentwise.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.rentwise.CustomerInfo;
import com.example.rentwise.ManageCustomer;
import com.example.rentwise.ManageVehicle;

import com.example.rentwise.R;
import com.example.rentwise.TransactionInfo;

public class HomeFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public HomeFragment() {

    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Find cardInputInfo and set an OnClickListener
        LinearLayout cardInputInfo = view.findViewById(R.id.cardInputInfo);
        LinearLayout cardManageVehicle = view.findViewById(R.id.cardManageVehicle);
        LinearLayout cardInputInfoCus = view.findViewById(R.id.cardInputInfoCus);

        cardInputInfo.setOnClickListener(v -> {
            // Start InfoActivity when cardInputInfo is clicked
            Intent intent = new Intent(getActivity(), TransactionInfo.class);
            startActivity(intent);
        });

        cardInputInfoCus.setOnClickListener(v -> {
            // Start InfoActivity when cardInputInfo is clicked
            Intent intent = new Intent(getActivity(), ManageCustomer.class);
            startActivity(intent);
        });

        cardManageVehicle.setOnClickListener(v ->{
            Intent intent = new Intent(getActivity(), ManageVehicle.class);
            startActivity(intent);
        });

        return view;
    }
}
