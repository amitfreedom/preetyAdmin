package com.adminlive.preetyadminpanel.ui.users;

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
import com.adminlive.preetyadminpanel.databinding.FragmentUsersBinding;
import com.adminlive.preetyadminpanel.ui.home.adapter.TopOptionsAdapter;
import com.adminlive.preetyadminpanel.ui.users.adapter.UsersOptionAdapter;
import com.adminlive.preetyadminpanel.ui.users.models.OptionModels;

import java.util.ArrayList;
import java.util.Objects;

public class UsersFragment extends Fragment {

    private FragmentUsersBinding binding;
    private UsersOptionAdapter usersOption;
    private ArrayList<OptionModels> optionModelsArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentUsersBinding.inflate(inflater, container, false);
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
        optionModelsArrayList.add(new OptionModels("View Users", R.drawable.subscribers, Color.BLUE));
        optionModelsArrayList.add(new OptionModels("Top Users", R.drawable.comments,R.color.purple_200));
        optionModelsArrayList.add(new OptionModels("Live Users", R.drawable.pages,Color.YELLOW));
        optionModelsArrayList.add(new OptionModels("Live History", R.drawable.post,Color.GREEN));
//        optionModelsArrayList.add(new OptionModels("User Report", R.drawable.post,Color.GREEN));
//        optionModelsArrayList.add(new OptionModels("Push Message", R.drawable.post,Color.GREEN));

        usersOption = new UsersOptionAdapter(requireActivity(), optionModelsArrayList, new UsersOptionAdapter.Select() {
            @Override
            public void OnPress(String title) {
                if (Objects.equals(title, "View Users")){
                    Navigation.findNavController(requireView()).navigate(R.id.action_nav_User_to_allUsersFragment);
                }
                else if (Objects.equals(title, "Top Users")){
                    Navigation.findNavController(requireView()).navigate(R.id.action_nav_User_to_allUsersFragment);
                }else if (Objects.equals(title, "Live Users")){
                    Navigation.findNavController(requireView()).navigate(R.id.action_nav_User_to_liveUserListFragment);
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