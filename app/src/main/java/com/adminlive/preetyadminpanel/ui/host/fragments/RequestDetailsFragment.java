package com.adminlive.preetyadminpanel.ui.host.fragments;

import static com.adminlive.preetyadminpanel.Constant.USER_PLACEHOLDER_PATH;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.adminlive.preetyadminpanel.databinding.FragmentRequestDetailsBinding;
import com.adminlive.preetyadminpanel.ui.host.modal.HostModal;
import com.adminlive.preetyadminpanel.ui.utils.FirestoreHelper;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class RequestDetailsFragment extends Fragment {

    private static final String TAG = "RequestDetailsFragment";

    private FragmentRequestDetailsBinding binding;
    private HostModal hostModal;
    private ProgressDialog progressDialog;
    String name="",phone="",agencyCode="",email="",docType="",cardNumber="",docImage="",photo="",userId="",uid="",screenStatus=null;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =FragmentRequestDetailsBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Bundle args = getArguments();
        if (args != null) {
            name=args.getString("name");
            phone=args.getString("phone");
            agencyCode=args.getString("agencyCode");
            email=args.getString("email");
            docType=args.getString("docType");
            cardNumber=args.getString("cardNumber");
            docImage=args.getString("docImage");
            photo=args.getString("photo");
            userId=args.getString("userId");
            uid=args.getString("uid");
            screenStatus=args.getString("screenStatus");
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        init();

        updateUI();

    }
    private void init(){
        binding.btnAccept.setOnClickListener(view -> {
            hostRequest("1");

        });

        binding.btnReject.setOnClickListener(view -> {
            hostRequest("0");
        });
    }

    private void hostRequest(String value) {
            showProgressBar();
            HostModal hostModal = new HostModal();
            hostModal.setRealName(name);
            hostModal.setPhoneNumber(phone);
            hostModal.setAgencyCode(agencyCode);
            hostModal.setEmailAddress(email);
            hostModal.setDocType(docType);
            hostModal.setIdCardNumber(cardNumber);
            hostModal.setIdCardImage(docImage);
            hostModal.setStatus(Objects.equals(value, "1") ?Constant.ACCEPTED:Constant.REJECTED);
            hostModal.setUid(uid);
            hostModal.setPhoto(photo);
            hostModal.setUserId(userId);

            db.collection(Constant.HOST_REGISTER).document(Objects.equals(value, "1") ?Constant.ACCEPTED:Constant.REJECTED).collection("host").document(userId).set(hostModal).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    hideProgressBar();
                    updateHostLive(userId,Objects.equals(value, "1")?"1":"0");
                    deleteAfterAccept();

                    db.collection(Constant.HOST_AGENCY).document(agencyCode).collection("host").document(userId).set(hostModal).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            if (Objects.equals(value, "1")){
                                FirestoreHelper.fetchAndUpdateHostCount("accepted");
                            }else{
                                FirestoreHelper.fetchAndUpdateHostCount("rejected");
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "onFailure: "+e);
                        }
                    });

                    AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
                    builder.setTitle("")
                            .setMessage(Objects.equals(value, "1") ?"Host request has been accepted successfully":"Host request has been rejected successfully")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    requireActivity().onBackPressed();
                                }
                            })
                            .show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    hideProgressBar();
                    Toast.makeText(requireActivity(), "error=>"+e, Toast.LENGTH_SHORT).show();

                }
            });
    }

    private void deleteAfterAccept() {
        // Delete the pending host document
        db.collection(Constant.HOST_REGISTER)
                .document("pending")
                .collection("host")
                .document(userId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.i(TAG, "onSuccess: deleted pending request");

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireActivity(), "error"+e.getMessage(), Toast.LENGTH_SHORT).show();

                        Log.e(TAG, "Error deleting pending host: " + e.getMessage());

                    }
                });
    }

    private void updateUI() {

        try {
            if (Objects.equals(screenStatus, Constant.PENDING)){
                binding.btnView.setVisibility(View.VISIBLE);
            }else {
                binding.btnView.setVisibility(View.GONE);
            }
            binding.tvName.setText(name);
            binding.tvUid.setText("ID : "+uid);
            binding.tvAgency.setText(agencyCode);
            binding.tvPhone.setText(phone);
            binding.tvDocType.setText(docType);
            binding.tvIdNumber.setText(cardNumber);

            if (!Objects.equals(docImage, "")){
                Glide.with(requireActivity()).load(docImage).into(binding.ivDocImage);
            }else {
                Glide.with(requireActivity()).load("https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/Placeholder_view_vector.svg/681px-Placeholder_view_vector.svg.png").into(binding.ivDocImage);
            }

            if (!Objects.equals(phone, "")){
                Glide.with(requireActivity()).load(photo).into(binding.ivMainImage);
            }else {
                Glide.with(requireActivity()).load(USER_PLACEHOLDER_PATH).into(binding.ivMainImage);
            }

        }catch (Exception e){
            Log.i(TAG, "updateUI: "+e);
        }
    }

    private void updateHostLive(String userId, String hostValue) {
        // Reference to the Firestore collection
        CollectionReference liveDetailsRef = db.collection(Constant.LOGIN_DETAILS);
        Query query = liveDetailsRef.whereEqualTo("userId", userId);

        query.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                for (QueryDocumentSnapshot document : task.getResult()) {
                    // Get the document ID for the matched document
                    String documentId = document.getId();
                    Map<String, Object> updateDetails = new HashMap<>();
                    updateDetails.put(Constant.HOST_LIVE, hostValue);

                    // Update the liveType field from 0 to 1
                    liveDetailsRef.document(documentId)
                            .update(updateDetails)
                            .addOnSuccessListener(aVoid -> {


                            })
                            .addOnFailureListener(e -> {

                                Log.e("8963457834534", "Error updating liveType for user with ID: " + userId, e);
                            });
                }
            } else {
                Log.e("8963457834534", "Error getting documents: ", task.getException());
            }
        });
    }


    private void showProgressBar() {
        progressDialog= new ProgressDialog(requireActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
    }
    private void hideProgressBar() {
        progressDialog.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}