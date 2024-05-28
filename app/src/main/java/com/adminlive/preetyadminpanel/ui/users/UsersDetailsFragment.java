package com.adminlive.preetyadminpanel.ui.users;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentUsersDetailsBinding;

import java.util.Objects;

public class UsersDetailsFragment extends Fragment {

    private static final String TAG = "UsersDetailsFragment";
    private FragmentUsersDetailsBinding binding;
    private String userId="";
    private String from="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsersDetailsBinding.inflate(inflater, container, false);
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getString("userId");
            from = args.getString("from");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (Objects.equals(from, "1")){
            binding.buttonView1.setVisibility(View.GONE);
            binding.buttonView.setVisibility(View.VISIBLE);
        }else {
            binding.buttonView.setVisibility(View.GONE);
            binding.buttonView1.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}