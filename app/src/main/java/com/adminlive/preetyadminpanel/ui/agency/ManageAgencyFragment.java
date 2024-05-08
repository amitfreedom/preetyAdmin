package com.adminlive.preetyadminpanel.ui.agency;

import static androidx.navigation.fragment.FragmentKt.findNavController;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentManageAgencyBinding;
import com.adminlive.preetyadminpanel.ui.users.adapter.UsersOptionAdapter;
import com.adminlive.preetyadminpanel.ui.users.models.OptionModels;
import com.adminlive.preetyadminpanel.ui.utils.FirestoreHelper;

import java.util.ArrayList;


public class ManageAgencyFragment extends Fragment {

    private FragmentManageAgencyBinding binding;
    private UsersOptionAdapter usersOption;
    private ArrayList<OptionModels> optionModelsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentManageAgencyBinding.inflate(inflater, container, false);

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
        optionModelsArrayList.add(new OptionModels("Create Agency", R.drawable.subscribers, Color.BLUE));
        optionModelsArrayList.add(new OptionModels("View Agency", R.drawable.subscribers,R.color.purple_200));
//        optionModelsArrayList.add(new OptionModels("", R.drawable.subscribers,Color.YELLOW));
        usersOption = new UsersOptionAdapter(requireActivity(), optionModelsArrayList, new UsersOptionAdapter.Select() {
            @Override
            public void OnPress(String title) {
                if (title.equalsIgnoreCase("Create Agency")){
//                    FirestoreHelper.fetchAndUpdateHostCount("agency");

                    Navigation.findNavController(requireView()).navigate(R.id.action_nav_manage_agency_to_createAgencyFragment);
                }
                else if (title.equalsIgnoreCase("View Agency")){
                    Navigation.findNavController(requireView()).navigate(R.id.action_nav_manage_agency_to_agencyListFragment);
                }
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