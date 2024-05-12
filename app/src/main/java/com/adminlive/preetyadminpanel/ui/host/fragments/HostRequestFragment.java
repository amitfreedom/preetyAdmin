package com.adminlive.preetyadminpanel.ui.host.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentHostRequestBinding;
import com.adminlive.preetyadminpanel.ui.host.adapter.HostRequestAdapter;
import com.adminlive.preetyadminpanel.ui.host.modal.HostModal;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;


public class HostRequestFragment extends Fragment {

    private static final String TAG = "HostRequestFragment";
    private FragmentHostRequestBinding binding;
    private FirebaseFirestore db;
    HostModal hostModal;
    private String status="";

    private ArrayList<HostModal> hostModalArrayList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentHostRequestBinding.inflate(inflater, container, false);
        db = FirebaseFirestore.getInstance();
        hostModalArrayList = new ArrayList<>();

        Bundle args = getArguments();
        if (args != null) {
            status = args.getString("status");
        }

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getHostRequest(status);
            }
        });

        getHostRequest(status);
    }
    @SuppressLint("NotifyDataSetChanged")
    private void getHostRequest(String status) {
        binding.swipeRefreshLayout.setRefreshing(true);
        db.collection(Constant.HOST_REGISTER)
                .document(status)
                .collection("host")
                .whereEqualTo("status", status)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        hostModalArrayList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Convert the document to your host model class
                            hostModal = document.toObject(HostModal.class);
                            hostModalArrayList.add(hostModal);
                        }

                        HostRequestAdapter hostRequestAdapter = new HostRequestAdapter(requireActivity(), hostModalArrayList, new HostRequestAdapter.Select() {
                            @Override
                            public void pressItem(HostModal hostModal) {
                                Bundle args = new Bundle();
                                args.putString("name",hostModal.getRealName());
                                args.putString("phone",hostModal.getPhoneNumber());
                                args.putString("agencyCode",hostModal.getAgencyCode());
                                args.putString("email",hostModal.getEmailAddress());
                                args.putString("docType",hostModal.getDocType());
                                args.putString("cardNumber",hostModal.getIdCardNumber());
                                args.putString("docImage",hostModal.getIdCardImage());
                                args.putString("photo",hostModal.getPhoto());
                                args.putString("userId",hostModal.getUserId());
                                args.putString("uid",hostModal.getUid());
                                args.putString("screenStatus",hostModal.getStatus());
                                args.putString("joiningDate",hostModal.getJoiningDate());

                                Navigation.findNavController(requireView()).navigate(R.id.action_hostRequestFragment_to_requestDetailsFragment,args);

                            }
                        });
                        binding.recyclerHostRequest.setAdapter(hostRequestAdapter);
                        hostRequestAdapter.notifyDataSetChanged();

                        if (hostModalArrayList.size()==0){
                            binding.recyclerHostRequest.setVisibility(View.GONE);
                            binding.notFound.setVisibility(View.VISIBLE);
                        }else {
                            binding.recyclerHostRequest.setVisibility(View.VISIBLE);
                            binding.notFound.setVisibility(View.INVISIBLE);
                        }

                        binding.swipeRefreshLayout.setRefreshing(false);
                    } else {
                        // Handle errors
                        Log.e(TAG, "Error getting pending hosts: ", task.getException());
                    }
                });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}