package com.adminlive.preetyadminpanel.global;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.adminlive.preetyadminpanel.ApplicationClass;
import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.ui.home.models.UserDetailsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseUserHelper {
private Context context;
    public FirebaseUserHelper(Context context) {
        this.context=context;
    }

    public interface UserDetailsListener {
        void onUserDetailsFetched(UserDetailsModel userDetails);
        void onUserDetailsNotFetched(String message);
    }


    // Function to enable or disable a user in Firestore
    //kjfgjgfj
    public static void setDisabledStatus(String userId, boolean disabled) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constant.LOGIN_DETAILS).document(userId)
                .update("disabled", disabled)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(ApplicationClass.getAppContext(), "Status updated", Toast.LENGTH_SHORT).show();

                    // User status updated successfully
                })
                .addOnFailureListener(e -> {

                    Toast.makeText(ApplicationClass.getAppContext(), "Somethings went wrong", Toast.LENGTH_SHORT).show();

                    // Handle update failure
                });


    }

    public static void fetchUserDetails(long uid,Context context, UserDetailsListener listener) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(Constant.LOGIN_DETAILS)
                .whereEqualTo("uid", uid)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            for (DocumentSnapshot document : querySnapshot) {
                                UserDetailsModel userDetails = document.toObject(UserDetailsModel.class);
                                // Pass the fetched user details to the listener
                                listener.onUserDetailsFetched(userDetails);

                            }
                        } else {
                            String errorMessage = "No user details found";
//                            Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show();
                            listener.onUserDetailsNotFetched(errorMessage);
                        }
                    } else {
//                        Toast.makeText(context, "Error fetching user details", Toast.LENGTH_SHORT).show();
                        // Handle error
                        Exception exception = task.getException();
                        listener.onUserDetailsNotFetched(exception.getMessage());
                        Log.e("FirebaseUserHelper", "Error fetching user details: " + exception.getMessage());
                    }
                });
    }


}