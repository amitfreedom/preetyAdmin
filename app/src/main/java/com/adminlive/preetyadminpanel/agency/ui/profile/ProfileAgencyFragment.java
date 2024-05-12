package com.adminlive.preetyadminpanel.agency.ui.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adminlive.preetyadminpanel.ApplicationClass;
import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentProfileAgencyBinding;

public class ProfileAgencyFragment extends Fragment {

    private static final String TAG = "ProfileAgencyFragment";
    private FragmentProfileAgencyBinding binding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileAgencyBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init() {
        binding.tvUserCount.setText(ApplicationClass.getSingleton().getTotalUserInsideHost());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}