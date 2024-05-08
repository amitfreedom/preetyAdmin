package com.adminlive.preetyadminpanel.ui.users;

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
import com.adminlive.preetyadminpanel.databinding.FragmentLiveUserListBinding;
import com.adminlive.preetyadminpanel.ui.home.models.UserDetailsModel;
import com.adminlive.preetyadminpanel.ui.users.adapter.ActiveUserAdapter;
import com.adminlive.preetyadminpanel.ui.users.adapter.AllUsersAdapter;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LiveUserListFragment extends Fragment {

    private FragmentLiveUserListBinding binding;
    private static final int LIMIT = 50;
    private FirebaseFirestore mFirestore;
    private Query mQuery;
    private ActiveUserAdapter mAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLiveUserListBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAllUsers();
    }

    private void getAllUsers() {
        mFirestore = FirebaseFirestore.getInstance();

        mQuery = mFirestore.collection(Constant.LIVE_DETAILS)
                .orderBy("startTime", Query.Direction.DESCENDING)
                .whereEqualTo("liveStatus","online")
                .limit(LIMIT);

        mAdapter = new ActiveUserAdapter(mQuery, new ActiveUserAdapter.OnActiveUserSelectedListener() {
            @Override
            public void onActiveUserSelected(DocumentSnapshot user) {

            }
        }) {
            @Override
            protected void onDataChanged() {
                // Show/hide content if the query returns empty.
                if (getItemCount() == 0) {
                    binding.recyclerLiveList.setVisibility(View.GONE);
                    binding.notFound.setVisibility(View.VISIBLE);
                } else {
                    binding.recyclerLiveList.setVisibility(View.VISIBLE);
                    binding.notFound.setVisibility(View.GONE);
                }
            }

            @Override
            protected void onError(FirebaseFirestoreException e) {
                Log.e("FirebaseFirestoreException", "onError: "+e );
                // Show a snackbar on errors
//                Snackbar.make(binding.getRoot(),
//                        "Error: check logs for info.", Snackbar.LENGTH_LONG).show();
            }


        };
//        binding.recyclerRestaurants.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerLiveList.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        // Start listening for Firestore updates
        if (mAdapter != null) {
            mAdapter.startListening();
        }


    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mAdapter != null) {
            mAdapter.stopListening();
        }
        binding=null;
    }

}