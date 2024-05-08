package com.adminlive.preetyadminpanel.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.adminlive.preetyadminpanel.ApplicationClass;
import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.agency.AgencyMainActivity;
import com.adminlive.preetyadminpanel.databinding.ActivityAuthBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

public class AuthActivity extends AppCompatActivity {
    private static final String TAG = "AuthActivity";
    private ActivityAuthBinding binding;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;
    private String selectedRole="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        onClicks();
    }

    void  onClicks(){

        binding.selectRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedRole = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.txtEmail.getText().toString().trim();
                String password = binding.txtPassword.getText().toString().trim();
                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(AuthActivity.this, "Please enter email and password.", Toast.LENGTH_SHORT).show();
                }
                else if (selectedRole.equals("Select role")) {
                    Toast.makeText(AuthActivity.this, "Please select role", Toast.LENGTH_SHORT).show();
                }
                else {
                    signIn(email, password);
                }

            }
        });
    }

    private void signIn(String email, String password) {
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser user = mAuth.getCurrentUser();
                            progressDialog.dismiss();
                            assert user != null;
                            checkUserExistenceInFirestore(user);
                        } else {
                            String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                            switch (errorCode) {
                                case "ERROR_INVALID_CUSTOM_TOKEN":
                                    Toast.makeText(AuthActivity.this, "The custom token format is incorrect. Please check the documentation.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_CUSTOM_TOKEN_MISMATCH":
                                    Toast.makeText(AuthActivity.this, "The custom token corresponds to a different audience.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_CREDENTIAL":
                                    Toast.makeText(AuthActivity.this, "The supplied auth credential is malformed or has expired.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_INVALID_EMAIL":
                                    Toast.makeText(AuthActivity.this, "The email address is badly formatted.", Toast.LENGTH_LONG).show();
                                    binding.txtEmail.setError("The email address is badly formatted.");
                                    binding.txtEmail.requestFocus();
                                    break;

                                case "ERROR_WRONG_PASSWORD":
                                    Toast.makeText(AuthActivity.this, "The password is invalid or the user does not have a password.", Toast.LENGTH_LONG).show();
                                    binding.txtPassword.setError("password is incorrect ");
                                    binding.txtPassword.requestFocus();
                                    binding.txtPassword.setText("");
                                    break;

                                case "ERROR_USER_MISMATCH":
                                    Toast.makeText(AuthActivity.this, "The supplied credentials do not correspond to the previously signed in user.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_REQUIRES_RECENT_LOGIN":
                                    Toast.makeText(AuthActivity.this, "This operation is sensitive and requires recent authentication. Log in again before retrying this request.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_ACCOUNT_EXISTS_WITH_DIFFERENT_CREDENTIAL":
                                    Toast.makeText(AuthActivity.this, "An account already exists with the same email address but different sign-in credentials. Sign in using a provider associated with this email address.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_EMAIL_ALREADY_IN_USE":
                                    Toast.makeText(AuthActivity.this, "The email address is already in use by another account.   ", Toast.LENGTH_LONG).show();
                                    binding.txtEmail.setError("The email address is already in use by another account.");
                                    binding.txtEmail.requestFocus();
                                    break;

                                case "ERROR_CREDENTIAL_ALREADY_IN_USE":
                                    Toast.makeText(AuthActivity.this, "This credential is already associated with a different user account.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_DISABLED":
                                    Toast.makeText(AuthActivity.this, "The user account has been disabled by an administrator.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_TOKEN_EXPIRED":

                                case "ERROR_INVALID_USER_TOKEN":
                                    Toast.makeText(AuthActivity.this, "The user\\'s credential is no longer valid. The user must sign in again.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_USER_NOT_FOUND":
                                    Toast.makeText(AuthActivity.this, "There is no user record corresponding to this identifier. The user may have been deleted.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_OPERATION_NOT_ALLOWED":
                                    Toast.makeText(AuthActivity.this, "This operation is not allowed. You must enable this service in the console.", Toast.LENGTH_LONG).show();
                                    break;

                                case "ERROR_WEAK_PASSWORD":
                                    Toast.makeText(AuthActivity.this, "The given password is invalid.", Toast.LENGTH_LONG).show();
                                    binding.txtPassword.setError("The password is invalid it must 6 characters at least");
                                    binding.txtPassword.requestFocus();
                                    break;

                                default:
                                    Toast.makeText(AuthActivity.this, "Somethings went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }

                        progressDialog.dismiss();


                    }
                });
    }

    // Check if a user exists in Firestore based on email
    private void checkUserExistenceInFirestore(FirebaseUser user) {
        FirebaseFirestore.getInstance().collection(Constant.AGENCY_DETAILS)
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (task.getResult().isEmpty()) {
                            mAuth.signOut();
                            Toast.makeText(this, "This agency not exist", Toast.LENGTH_SHORT).show();
                        } else {
                            ApplicationClass.getSharedpref().saveString(Constant.ROLE, selectedRole);
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Get email and user name from the document
                                String role = document.getString("role");
                                if (Objects.equals(role, "agency") && selectedRole.equals("Agency")){
                                    Toast.makeText(AuthActivity.this, "You have logged in successfully",
                                            Toast.LENGTH_SHORT).show();
                                    ApplicationClass.getSharedpref().saveString(Constant.USER_ID, user.getUid());
                                    Intent mainIntent = new Intent(AuthActivity.this, AgencyMainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();

                                }

                                else if (Objects.equals(role, "admin") && selectedRole.equals("Admin")){
                                    ApplicationClass.getSharedpref().saveString(Constant.USER_ID, user.getUid());
//                                    Intent mainIntent = new Intent(AuthActivity.this, AgencyMainActivity.class);
//                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(mainIntent);
//                                    finish();
                                }
                                else if (Objects.equals(role, "reseller") && selectedRole.equals("Reseller")){
                                    ApplicationClass.getSharedpref().saveString(Constant.USER_ID, user.getUid());
//                                    Intent mainIntent = new Intent(AuthActivity.this, AgencyMainActivity.class);
//                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(mainIntent);
//                                    finish();
                                }else {
                                    Toast.makeText(this, "Some things went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }  // Handle Firestore query failure

                });
//        checkLastId(user);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding=null;
    }
}