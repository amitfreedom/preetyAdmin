package com.adminlive.preetyadminpanel.ui.latest_members;

import static com.adminlive.preetyadminpanel.Constant.SCREEN_TYPE;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;

import com.adminlive.preetyadminpanel.databinding.ActivityViewAllLatestMembersBinding;
import com.adminlive.preetyadminpanel.ui.home.adapter.LatestMemberAdapter;
import com.adminlive.preetyadminpanel.ui.home.models.UserDetailsModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ViewAllLatestMembersActivity extends AppCompatActivity {
    private ActivityViewAllLatestMembersBinding binding;
    private List<UserDetailsModel> latestMember;
    private LatestMemberAdapter latestMemberAdapter;
    private boolean isLoading = false;
    private int page = 500;
    private int currentPage = 0;
    private int pageSize = 10;
    private String screenType="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityViewAllLatestMembersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getIntent().getStringExtra(SCREEN_TYPE)!=null){
            screenType = getIntent().getStringExtra(SCREEN_TYPE);
        }else {
            screenType ="";
        }

        setSupportActionBar(binding.toolbar);

        // Enable the back arrow
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        latestMember = new ArrayList<>();

        latestMemberList(page,screenType);
        // Implement scroll listener to trigger load more
//        binding.recyclerLatestMember.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
//                if (layoutManager != null) {
//                    int visibleItemCount = layoutManager.getChildCount();
//                    int totalItemCount = layoutManager.getItemCount();
//                    int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
//
//                    if (!isLoading && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount) {
//                        // Load more data
//                        isLoading = true;
//                        loadMoreData();
//                        fetchUsersByDateTime((page+10));
////                        latestMemberList((page+10));
//                    }
//                }
//            }
//        });

    }

    // Handle back arrow click event
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void loadMoreData() {

    }
    private DocumentSnapshot lastVisibleDocument;

    private void fetchUsersByDateTime(int pageSize) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        Query query = firestore.collection("login_details")
                .orderBy("loginTime", Query.Direction.DESCENDING)
                .limit(3);

        if (lastVisibleDocument != null) {
            // Start after the last visible document to paginate
            query = query.startAfter(3);
        }

        query.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
//                            List<UserDetailsModel> latestMembers = new ArrayList<>();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                UserDetailsModel userDetailsModel = document.toObject(UserDetailsModel.class);
                                latestMember.add(userDetailsModel);
                            }

                            // Update the last visible document for next pagination
                            if (!querySnapshot.isEmpty()) {
                                lastVisibleDocument = querySnapshot.getDocuments().get(querySnapshot.size() - 1);
                            }

                            latestMemberAdapter.notifyDataSetChanged();
                            // Process the fetched user records
                            // (e.g., update UI, pass to adapter)

                        } else {
                            Log.i("TAG", "fetchUsersByDateTime: QuerySnapshot is null");
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


    @SuppressLint("NotifyDataSetChanged")
    private void latestMemberList(int page, String screenType) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection("login_details")
                .orderBy("loginTime", Query.Direction.DESCENDING)
                .limit(page)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            latestMember.clear();
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                UserDetailsModel userDetailsModel = document.toObject(UserDetailsModel.class);
                                latestMember.add(userDetailsModel);
                            }

                            latestMemberAdapter = new LatestMemberAdapter(this, latestMember);
                            binding.recyclerLatestMember.setAdapter(latestMemberAdapter);
                            latestMemberAdapter.notifyDataSetChanged();

                        } else {
                            Log.i("jhgfx", "latestMemberList: ");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}