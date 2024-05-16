package com.adminlive.preetyadminpanel.agency.ui.profile;

import static com.adminlive.preetyadminpanel.ui.utils.UserImageUtils.createUserImage;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adminlive.preetyadminpanel.ApplicationClass;
import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.auth.AuthActivity;
import com.adminlive.preetyadminpanel.databinding.FragmentProfileAgencyBinding;
import com.adminlive.preetyadminpanel.ui.agency.modal.AgencyCreateModel;
import com.adminlive.preetyadminpanel.ui.home.models.UserDetailsModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class ProfileAgencyFragment extends Fragment {

    private static final String TAG = "ProfileAgencyFragment";
    private FragmentProfileAgencyBinding binding;
    private CollectionReference usersRef;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentProfileAgencyBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
        fetchAgencyDetails("2X4KebFIJLWnhrEEarzKFZvWaZC2");
        binding.btnLogout.setOnClickListener(view1 -> {
            mAuth.signOut();
            Intent mainIntent = new Intent(getActivity(), AuthActivity.class);
            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainIntent);
            requireActivity().finish();
        });
        binding.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(getActivity(), UpdateUserDetailsActivity.class);
                startActivity(mainIntent);
            }
        });
    }

    private void init() {
        binding.tvUserCount.setText(ApplicationClass.getSingleton().getTotalUserInsideHost());
    }

    private void fetchAgencyDetails(String userId) {
        usersRef = FirebaseFirestore.getInstance().collection(Constant.AGENCY_DETAILS);
        usersRef.whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        Log.e("FirestoreListener", "Listen failed: " + error.getMessage());
                        return;
                    }

                    assert value != null;
                    for (DocumentSnapshot document : value) {
                        AgencyCreateModel userDetails = document.toObject(AgencyCreateModel.class);
                        updateUI(userDetails);

                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(AgencyCreateModel userDetails) {
        try {
            binding.txtUsername.setText(userDetails.getUsername());
            binding.txtUid.setText("Code : "+userDetails.getAgencyCode());
            Glide.with(requireActivity()).load(userDetails.getImage()!=""?userDetails.getImage():createUserImage(userDetails.getUsername(),100)).into(binding.profileImage);

        }catch (Exception e){
            Log.i(TAG, "updateUI: "+e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}