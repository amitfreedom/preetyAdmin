package com.adminlive.preetyadminpanel.agency.ui.home;

import static com.adminlive.preetyadminpanel.Constant.USER_PLACEHOLDER_PATH;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentAgencyHostDetailsBinding;
import com.adminlive.preetyadminpanel.ui.home.models.UserDetailsModel;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;


public class AgencyHostDetailsFragment extends Fragment {
    private static final String TAG = "AgencyHostDetailsFragme";
    private FragmentAgencyHostDetailsBinding binding;
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private FirebaseAuth mAuth;
    private UserDetailsModel userDetails;
    String userId="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAgencyHostDetailsBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        usersRef = db.collection(Constant.LOGIN_DETAILS);
        Bundle args = getArguments();
        if (args != null) {
            userId = args.getString("userId");
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fetchUserDetails(userId);

    }

    private void fetchUserDetails(String userId) {

        usersRef.whereEqualTo("userId", userId)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        Log.e("FirestoreListener", "Listen failed: " + error.getMessage());
                        return;
                    }

                    assert value != null;
                    for (DocumentSnapshot document : value) {
                        userDetails = document.toObject(UserDetailsModel.class);
                        updateUI(userDetails);

                    }
                });
    }

    @SuppressLint("SetTextI18n")
    private void updateUI(UserDetailsModel userDetails) {
        try {

            if (!Objects.equals(userDetails.getImage(), "")){
                Glide.with(requireActivity()).load(userDetails.getImage()).into(binding.ivMainImage);
            }else {
                Glide.with(requireActivity()).load("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/681px-Placeholder_view_vector.svg.png").into(binding.ivMainImage);
            }

            binding.tvName.setText(userDetails.getUsername());
            binding.tvUid.setText("ID : "+String.valueOf(userDetails.getUid()));
            binding.tvReceivedCoins.setText(""+userDetails.getReceiveCoin());
            binding.tvSenderCoins.setText(""+userDetails.getSenderCoin());
            binding.tvReceivedGameCoins.setText(""+userDetails.getReceiveGameCoin());
            binding.tvEmail.setText(userDetails.getEmail());
            binding.tvPhone.setText(userDetails.getPhone());
            binding.tvCountry.setText(userDetails.getCountry_name());
            binding.mainCoins.setText(userDetails.getCoins());

        }catch (Exception e){
            Log.i(TAG, "updateUI: "+e);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding=null;
    }
}