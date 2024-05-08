package com.adminlive.preetyadminpanel.ui.agency.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentAgencyListBinding;
import com.adminlive.preetyadminpanel.ui.agency.adapter.CreatedAgencyAdapter;
import com.adminlive.preetyadminpanel.ui.agency.modal.AgencyCreateModel;
import com.adminlive.preetyadminpanel.ui.home.models.UserDetailsModel;
import com.adminlive.preetyadminpanel.ui.users.adapter.AllUsersAdapter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AgencyListFragment extends Fragment {

    private static final String TAG = "AgencyListFragment";
    private FragmentAgencyListBinding binding;
    private FirebaseFirestore db;
    private ArrayList<AgencyCreateModel> userModalArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgencyListBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        userModalArrayList = new ArrayList<>();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllUsers();
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getAllUsers() {
        db.collection(Constant.AGENCY_DETAILS)
                .orderBy("joinDate", Query.Direction.DESCENDING)
                .limit(100)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        userModalArrayList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert the document to your host model class
                            AgencyCreateModel agencyCreateModel = document.toObject(AgencyCreateModel.class);
                            userModalArrayList.add(agencyCreateModel);
                        }

                        CreatedAgencyAdapter createdAgencyAdapter = new CreatedAgencyAdapter(requireActivity(), userModalArrayList);
                        binding.recyclerAgencyList.setAdapter(createdAgencyAdapter);
                        createdAgencyAdapter.notifyDataSetChanged();

                        if (userModalArrayList.size()==0){
                            binding.recyclerAgencyList.setVisibility(View.GONE);
                            binding.notFound.setVisibility(View.VISIBLE);
                        }else {
                            binding.recyclerAgencyList.setVisibility(View.VISIBLE);
                            binding.notFound.setVisibility(View.INVISIBLE);
                        }

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