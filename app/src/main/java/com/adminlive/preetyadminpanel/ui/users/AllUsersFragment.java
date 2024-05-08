package com.adminlive.preetyadminpanel.ui.users;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentAllUsersBinding;
import com.adminlive.preetyadminpanel.ui.home.adapter.LatestMemberAdapter;
import com.adminlive.preetyadminpanel.ui.home.models.UserDetailsModel;
import com.adminlive.preetyadminpanel.ui.host.adapter.HostRequestAdapter;
import com.adminlive.preetyadminpanel.ui.host.modal.HostModal;
import com.adminlive.preetyadminpanel.ui.users.adapter.AllUsersAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;


public class AllUsersFragment extends Fragment {

    private FragmentAllUsersBinding binding;
    private FirebaseFirestore db;
    private ArrayList<UserDetailsModel> userModalArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAllUsersBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        userModalArrayList = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllUsers();
            }
        });

        getAllUsers();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllUsers() {
        binding.swipeRefreshLayout.setRefreshing(true);
        db.collection(Constant.LOGIN_DETAILS)
                .orderBy("loginTime", Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userModalArrayList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert the document to your host model class
                            UserDetailsModel userDetailsModel = document.toObject(UserDetailsModel.class);
                            userModalArrayList.add(userDetailsModel);
                        }

                        AllUsersAdapter latestMemberAdapter = new AllUsersAdapter(requireActivity(), userModalArrayList);
                        binding.recyclerUsersList.setAdapter(latestMemberAdapter);
                        latestMemberAdapter.notifyDataSetChanged();

                        if (userModalArrayList.size()==0){
                            binding.recyclerUsersList.setVisibility(View.GONE);
                            binding.notFound.setVisibility(View.VISIBLE);
                        }else {
                            binding.recyclerUsersList.setVisibility(View.VISIBLE);
                            binding.notFound.setVisibility(View.INVISIBLE);
                        }

                        binding.swipeRefreshLayout.setRefreshing(false);
                    } else {
                        // Handle errors
                        Log.e("tag", "Error getting pending hosts: ", task.getException());
                    }
                });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
