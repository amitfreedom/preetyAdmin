package com.adminlive.preetyadminpanel.ui.home;

import static com.adminlive.preetyadminpanel.Constant.SCREEN_TYPE;
import static com.adminlive.preetyadminpanel.Constant.USER_PLACEHOLDER_PATH;
import static com.adminlive.preetyadminpanel.global.FirebaseUserHelper.fetchUserDetails;
import static com.adminlive.preetyadminpanel.global.FirebaseUserHelper.setDisabledStatus;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentHomeBinding;
import com.adminlive.preetyadminpanel.global.FirebaseUserHelper;
import com.adminlive.preetyadminpanel.ui.home.adapter.LatestMemberAdapter;
import com.adminlive.preetyadminpanel.ui.home.adapter.TopOptionsAdapter;
import com.adminlive.preetyadminpanel.ui.home.models.TopOptionsModel;
import com.adminlive.preetyadminpanel.ui.home.models.UserDetailsModel;
import com.adminlive.preetyadminpanel.ui.latest_members.ViewAllLatestMembersActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";

    private FragmentHomeBinding binding;
    private ArrayList<TopOptionsModel> topOptionsModelArrayList;
    private List<UserDetailsModel> latestMember;
    private LatestMemberAdapter latestMemberAdapter;
    private TopOptionsAdapter topOptionsAdapter;
    private String userId="";
    private boolean isDisable=false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        topOptionsModelArrayList = new ArrayList<>();
        latestMember = new ArrayList<>();

        showTopOptions();
        latestMemberList();

        init();




        return root;
    }

    private void init() {
        binding.tvViewAll.setOnClickListener(view -> {
            startActivity(new Intent(requireActivity(), ViewAllLatestMembersActivity.class).putExtra(SCREEN_TYPE,"latest_member"));
        });
        binding.btnBlockUnblock.setOnClickListener(view -> {
            setDisabledStatus(requireActivity(),userId,isDisable);
            binding.etUserId.getText().clear();
            binding.cardViewBlockUser.setVisibility(View.GONE);
        });
        binding.btnContinue.setOnClickListener(view -> {
            String blockUserId = binding.etUserId.getText().toString().trim();
            if (blockUserId.isEmpty()){
                binding.etUserId.setError("Enter UserId");
            }
            else {
                fetchUserDetails(Long.parseLong(blockUserId),requireActivity(), new FirebaseUserHelper.UserDetailsListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onUserDetailsFetched(UserDetailsModel userDetails) {
                        binding.cardViewBlockUser.setVisibility(View.VISIBLE);
                        userId=userDetails.getUserId();
                        isDisable=userDetails.isDisabled();

                        try {
                            if (!Objects.equals(userDetails.getImage(), "")){
                                Glide.with(requireActivity()).load(userDetails.getImage()).into(binding.tvUserImage);
                            }else {
                                Glide.with(requireActivity()).load(USER_PLACEHOLDER_PATH).into(binding.tvUserImage);
                            }
                            binding.tvBlockName.setText(userDetails.getUsername().toUpperCase());
                            binding.tvBlockId.setText("ID : "+userDetails.getUid());
                            if (userDetails.isDisabled()){
                                binding.btnBlockUnblock.setText("UnBlock");
                            }else {
                                binding.btnBlockUnblock.setText("Block");
                            }
                        }catch (Exception e){
                            Log.i(TAG, "onUserDetailsFetched: "+e.getMessage());

                        }

                    }

                    @Override
                    public void onUserDetailsNotFetched(String message) {
                        binding.cardViewBlockUser.setVisibility(View.GONE);
                        Toast.makeText(requireActivity(), message, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    private void latestMemberList() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("login_details")
                .orderBy("loginTime",Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
//                            latestMember.clear();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                UserDetailsModel userDetailsModel = document.toObject(UserDetailsModel.class);
                                latestMember.add(userDetailsModel);
                            }

                            latestMemberAdapter = new LatestMemberAdapter(requireActivity(), latestMember);
                            binding.recyclerLatestMember.setAdapter(latestMemberAdapter);
                            latestMemberAdapter.notifyDataSetChanged();

                        } else {
                            Log.i(TAG, "latestMemberList: ");
                            // Handle the case where the query snapshot is null
                        }
                    } else {
                        // Handle the error
                        Exception exception = task.getException();
                        if (exception != null) {
                            exception.printStackTrace();
                        }
                    }
                });


    }

    private void showTopOptions() {
        // Add sample data
        topOptionsModelArrayList.add(new TopOptionsModel("720","Total Users", R.drawable.subscribers, Color.BLUE));
        topOptionsModelArrayList.add(new TopOptionsModel("764","Total Agency", R.drawable.post,R.color.purple_200));
        topOptionsModelArrayList.add(new TopOptionsModel("987","Total Host", R.drawable.pages,Color.YELLOW));
        topOptionsModelArrayList.add(new TopOptionsModel("8788","Agency Earning", R.drawable.comments,Color.GREEN));
        topOptionsModelArrayList.add(new TopOptionsModel("76664","Game Earning", R.drawable.post,Color.GREEN));
        topOptionsModelArrayList.add(new TopOptionsModel("980","Game Loss", R.drawable.post,Color.GREEN));

        topOptionsAdapter = new TopOptionsAdapter(requireActivity(), topOptionsModelArrayList);
        binding.recyclerTopOptions.setAdapter(topOptionsAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}