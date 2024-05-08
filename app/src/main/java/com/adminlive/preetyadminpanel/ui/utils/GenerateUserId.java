package com.adminlive.preetyadminpanel.ui.utils;

import android.util.Log;

import com.adminlive.preetyadminpanel.Constant;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.Objects;

public class GenerateUserId {
    public interface UserIdCallback {
        void onUserIdReceived(int userId);
        void onFailure(Exception e);
    }

    public static void getLastUserId(FirebaseFirestore firestore, String collectionPath, UserIdCallback callback) {
        CollectionReference collection = firestore.collection(collectionPath);
        collection.orderBy(Constant.UID, Query.Direction.DESCENDING)
                .limit(1)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.contains(Constant.UID)) {
                                int lastUserId = Objects.requireNonNull(document.getLong(Constant.UID)).intValue();
                                Log.i("check_uid", "onComplete: uid =" + lastUserId);
                                callback.onUserIdReceived(lastUserId);
                                return;
                            }
                        }
                    }
                    callback.onFailure(task.getException());
                });
    }


}