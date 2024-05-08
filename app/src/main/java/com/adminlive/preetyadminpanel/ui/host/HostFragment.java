package com.adminlive.preetyadminpanel.ui.host;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentHostBinding;
import com.adminlive.preetyadminpanel.ui.users.adapter.UsersOptionAdapter;
import com.adminlive.preetyadminpanel.ui.users.models.OptionModels;

import java.util.ArrayList;


public class HostFragment extends Fragment {

    private FragmentHostBinding binding;
    private UsersOptionAdapter usersOption;
    private ArrayList<OptionModels> optionModelsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHostBinding.inflate(inflater, container, false);
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
        optionModelsArrayList.add(new OptionModels("Pending Host Request", R.drawable.pending_history, Color.BLUE));
        optionModelsArrayList.add(new OptionModels("Accepted Host Request", R.drawable.accepted,R.color.purple_200));
        optionModelsArrayList.add(new OptionModels("Rejected Host Request", R.drawable.rejected,Color.YELLOW));

        usersOption = new UsersOptionAdapter(requireActivity(), optionModelsArrayList, new UsersOptionAdapter.Select() {
            @Override
            public void OnPress(String title) {
                Bundle args = new Bundle();
                if (title.equals("Pending Host Request")){
                    args.putString("status", "pending");
                }
                else if (title.equals("Accepted Host Request")){
                    args.putString("status", "accepted");
                }
                else if (title.equals("Rejected Host Request")){
                    args.putString("status", "rejected");
                }
                Navigation.findNavController(requireView()).navigate(R.id.action_nav_Host_to_hostRequestFragment,args);

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