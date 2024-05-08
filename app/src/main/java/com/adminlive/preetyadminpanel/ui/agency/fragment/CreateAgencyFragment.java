package com.adminlive.preetyadminpanel.ui.agency.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.databinding.FragmentCreateAgencyBinding;
import com.adminlive.preetyadminpanel.ui.utils.AlertDialogHelper;
import com.adminlive.preetyadminpanel.ui.utils.FirestoreHelper;
import com.adminlive.preetyadminpanel.ui.utils.GenerateUserId;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.hbb20.CountryCodePicker;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreateAgencyFragment extends Fragment {
    private static final String TAG = "CreateAgencyFragment";
   private FragmentCreateAgencyBinding binding;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private ProgressDialog progressDialog;
    String countryName = "";
    String countryCode="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding =  FragmentCreateAgencyBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        progressDialog= new ProgressDialog(requireActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    private void init(){

        countryName = binding.ccp.getSelectedCountryName();
        countryCode = binding.ccp.getSelectedCountryCode();
        binding.btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = binding.txtUsername.getText().toString().trim();
                String agencyCode = binding.txtAgencyNumber.getText().toString().trim();
                String phone = binding.txtPhone.getText().toString().trim();
                String emailAddress = binding.txtEmail.getText().toString().trim();
                String password = binding.txtPassword.getText().toString().trim();
                String confirm_password = binding.txtConfirmPassword.getText().toString().trim();

                if (username.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please enter Full name.", Toast.LENGTH_SHORT).show();
                }else if (agencyCode.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please enter agency code", Toast.LENGTH_SHORT).show();
                }
                else if (emailAddress.isEmpty()) {
                    Toast.makeText(requireActivity(), "Email address can't be empty.", Toast.LENGTH_SHORT).show();

                } else if (!Patterns.EMAIL_ADDRESS.matcher(emailAddress).matches()) {
                    Toast.makeText(requireActivity(), "Please enter valid email address", Toast.LENGTH_SHORT).show();
                } else if (phone.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please enter phone number", Toast.LENGTH_SHORT).show();
                } else if (password.isEmpty()) {
                    Toast.makeText(requireActivity(), "Please enter password", Toast.LENGTH_SHORT).show();
                }else if (!password.equals(confirm_password)) {
                    Toast.makeText(requireActivity(), "Password is mismatch please check once", Toast.LENGTH_SHORT).show();
                } else {
                    createAccount(emailAddress, password);
                }

            }
        });
    }

    private void createAccount(String email, String password) {

        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(requireActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser user = mAuth.getCurrentUser();
//                    ApplicationClass.getSharedpref().saveString(AppConstants.USER_ID, user.getUid());
                    checkUserExistenceInFirestore(user);
                } else {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                    switch (errorCode) {

                        case "ERROR_INVALID_CUSTOM_TOKEN":
                            Toast.makeText(requireActivity(), "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_CUSTOM_TOKEN_MISMATCH":
                            Toast.makeText(requireActivity(), "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_CREDENTIAL":
                            Toast.makeText(requireActivity(), "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_INVALID_EMAIL":
                            Toast.makeText(requireActivity(), "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                            binding.txtEmail.setError("The email address is badly formatted.");
                            binding.txtEmail.requestFocus();
                            break;

                        case "ERROR_WRONG_PASSWORD":
                            Toast.makeText(requireActivity(), "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                            binding.txtPassword.setError("password is incorrect ");
                            binding.txtPassword.requestFocus();
                            binding.txtPassword.setText("");
                            break;

                        case "ERROR_USER_MISMATCH":
                            Toast.makeText(requireActivity(), "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_REQUIRES_RECENT_LOGIN":
                            Toast.makeText(requireActivity(), "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                            Toast.makeText(requireActivity(), "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_EMAIL_ALREADY_IN_USE":
                            Toast.makeText(requireActivity(), "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                            binding.txtEmail.setError("The email address is already in use by another account.");
                            binding.txtEmail.requestFocus();
                            break;

                        case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                            Toast.makeText(requireActivity(), "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_DISABLED":
                            Toast.makeText(requireActivity(), "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_TOKEN_EXPIRED":

                        case "ERROR_INVALID_USER_TOKEN":
                            Toast.makeText(requireActivity(), "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_USER_NOT_FOUND":
                            Toast.makeText(requireActivity(), "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_OPERATION_NOT_ALLOWED":
                            Toast.makeText(requireActivity(), "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                            break;

                        case "ERROR_WEAK_PASSWORD":
                            Toast.makeText(requireActivity(), "The given password is invalid.", Toast.LENGTH_LONG).show();
                            binding.txtPassword.setError("The password is invalid it must 6 characters at least");
                            binding.txtPassword.requestFocus();
                            break;

                        default:
                            Toast.makeText(requireActivity(), "Somethings went wrong", Toast.LENGTH_SHORT).show();
                    }
                }

                progressDialog.dismiss();
            }
        });

    }

    private void checkUserExistenceInFirestore(FirebaseUser user) {
        FirebaseFirestore.getInstance().collection(Constant.AGENCY_DETAILS)
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult().isEmpty()) {
                            Log.i("Onboard", "checkUserExistenceInFirestore: if"+task.getResult());
                            // User doesn't exist in Firestore
                            // You might perform actions like creating a new user document
                            checkLastId(user);
                        } else {
                            Log.i("Onboard", "checkUserExistenceInFirestore:  else"+task.getResult());
                            // User exists in Firestore
                            // You can retrieve the user's information if needed
//                            moveNextPage();
                            Toast.makeText(requireActivity(), "This Agency Already exist", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        // Handle Firestore query failure
                    }
                });
//        checkLastId(user);
    }

    private void checkLastId(FirebaseUser user){
        GenerateUserId.getLastUserId(firestore, Constant.AGENCY_DETAILS, new GenerateUserId.UserIdCallback() {
            @Override
            public void onUserIdReceived(int userId) {
                Log.i("MainActivity", "Last user ID: " + userId);

                // Example: Get the next user ID (increment by 1)
                long nextUserId = userId + 1;
                Log.i("MainActivity", "Next user ID: " + nextUserId);
                updateUI(user,nextUserId);

            }

            @Override
            public void onFailure(Exception e) {
                Log.i("MainActivity", "Exception: " + e);
                if (e==null){
                    updateUI(user,10000);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user, long nextUserId) {
//        long timestamp = System.currentTimeMillis();
        Date currentDate = new Date();
        long timestamp = currentDate.getTime();
        Map<String, Object> loginDetails = new HashMap<>();
        loginDetails.put("userId", user.getUid());
        loginDetails.put("uid", nextUserId);
        loginDetails.put("role", "agency");
        loginDetails.put("username", binding.txtUsername.getText().toString().trim());
        loginDetails.put("agencyCode", binding.txtAgencyNumber.getText().toString().trim());
        loginDetails.put("email", user.getEmail());
        loginDetails.put("phone", binding.txtPhone.getText().toString().trim());
        loginDetails.put("countryCode", "+"+countryCode);
        loginDetails.put("countryName", countryName);
        loginDetails.put("image", user.getPhotoUrl()!=null?user.getPhotoUrl():"");
        loginDetails.put("regId", "");
        loginDetails.put("deviceId", "");
        loginDetails.put("coins", "0");
        loginDetails.put("agencyEarning", 0);
        loginDetails.put("totalHost", 0);
        loginDetails.put("docId", "");
        loginDetails.put("joinDate", timestamp);
        loginDetails.put("loginDate", timestamp);

        // Add the login details to Firestore
        firestore.collection(Constant.AGENCY_DETAILS)
                .add(loginDetails)
                .addOnSuccessListener(documentReference -> {
                    if (documentReference!=null){
                        FirestoreHelper.fetchAndUpdateHostCount("agency");

                    }
                    // Login details added successfully
                    AlertDialogHelper.showAgencyCreatedDialog(requireActivity(), "Agency has been created successfully", new AlertDialogHelper.OnOkButtonClickListener() {
                        @Override
                        public void onOkButtonClicked() {
                            requireActivity().onBackPressed();
                        }
                    });



                })
                .addOnFailureListener(e -> {
                    Toast.makeText(requireActivity(), "Error adding login details"+e,Toast.LENGTH_SHORT).show();
                    // Handle failure
                    Log.e("MainActivity", "Error adding login details", e);
                });


    }

    private void moveNextPage() {
//        Intent mainIntent = new Intent(requireActivity(), HomeActivity.class);
//        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mainIntent);
//        finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countryName = binding.ccp.getSelectedCountryName();
                countryCode = binding.ccp.getSelectedCountryCode();
                Log.i("MainActivity", "countryName : " + countryName);
            }
        });
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}