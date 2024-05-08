package com.adminlive.preetyadminpanel.ui.subadmin;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentSubAdminBinding;
import com.adminlive.preetyadminpanel.ui.users.adapter.UsersOptionAdapter;
import com.adminlive.preetyadminpanel.ui.users.models.OptionModels;

import java.util.ArrayList;


public class SubAdminFragment extends Fragment {
    private FragmentSubAdminBinding binding;
    private UsersOptionAdapter usersOption;
    private ArrayList<OptionModels> optionModelsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSubAdminBinding.inflate(inflater, container, false);
        optionModelsArrayList = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showTopOptions();
    }

    private void showTopOptions() {
        // Add sample data
        optionModelsArrayList.add(new OptionModels("Create Sub-Admin", R.drawable.subscribers, Color.BLUE));
        optionModelsArrayList.add(new OptionModels("View Sub-Admin", R.drawable.subscribers,R.color.purple_200));
        optionModelsArrayList.add(new OptionModels("Coin History", R.drawable.post,R.color.purple_200));
//        optionModelsArrayList.add(new OptionModels("", R.drawable.subscribers,Color.YELLOW));
        usersOption = new UsersOptionAdapter(requireActivity(), optionModelsArrayList, new UsersOptionAdapter.Select() {
            @Override
            public void OnPress(String title) {

            }
        });
        binding.recyclerUserOptions.setAdapter(usersOption);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}