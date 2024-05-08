package com.adminlive.preetyadminpanel.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adminlive.preetyadminpanel.ApplicationClass;
import com.adminlive.preetyadminpanel.Constant;
import com.adminlive.preetyadminpanel.R;
import com.adminlive.preetyadminpanel.agency.AgencyMainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Objects;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {
    private static final int SPLASH_SCREEN_TIME_OUT = 2000;
    //Animations
    Animation topAnimantion,bottomAnimation,middleAnimation;
    private RelativeLayout relativeLayout;
    private TextView txt_logo;
    private FirebaseAuth mAuth;
    TextView marqueeTextView;
    FirebaseFirestore db;
    private String work="0";
    private String dev="0";
    private String role="";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mAuth = FirebaseAuth.getInstance();
        relativeLayout=findViewById(R.id.main_logo);
        txt_logo=findViewById(R.id.txt_logo);

        if (ApplicationClass.getSharedpref().getString(Constant.ROLE)!=null){
            role =  ApplicationClass.getSharedpref().getString(Constant.ROLE);
        }

        TextView marqueeText = findViewById(R.id.marqueeText);

        // Set up marquee animation
//        startMarqueeAnimation(marqueeText);

        marqueeTextView = findViewById(R.id.marqueeTextView);


        // Call the method to start the marquee animation
//        startMarqueeAnimation1();

        topAnimantion = AnimationUtils.loadAnimation(this, R.anim.top_animantion);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animantion);
        bottomAnimation = AnimationUtils.loadAnimation(this, R.anim.bottom_animantion);
        relativeLayout.setAnimation(topAnimantion);
        txt_logo.setAnimation(bottomAnimation);

        db = FirebaseFirestore.getInstance();
        CollectionReference collectionRef = db.collection("stop_app");



        collectionRef.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Iterate through the documents
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {

                        work = documentSnapshot.getString("working");
                        dev = documentSnapshot.getString("dev");

                        moveNext(work,dev);

                    }
                })
                .addOnFailureListener(e -> {
                    work="0";
                    dev="0";
                    // Handle any errors
                    //
                    //
                });



    }

    private void moveNext(String work, String dev) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser user = mAuth.getCurrentUser();
//
//                if (Objects.equals(work, "0")) {
//                    Toast.makeText(SplashActivity.this, "Work in progress so you can't use this time try after somme time and contact with Admin", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                if (Objects.equals(dev, "0")) {
////                    Toast.makeText(SplashActivity.this, "Work in progress so you can't use this time try after somme time and contact with Admin", Toast.LENGTH_SHORT).show();
//                    return;
//                }

                if (user == null) {
                    Intent intent = new Intent(SplashActivity.this, AuthActivity.class);
                    startActivity(intent);
                    finish();
                }
                else {
                    checkUserExistenceInFirestore(user,role);
//                    ApplicationClass.getSharedpref().saveString(Constant.USER_ID, user.getUid());
//                    Intent mainIntent = new Intent(SplashActivity.this, HomeActivity.class);
//                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(mainIntent);
//                    finish();
                }
            }
        }, 3000);

    }

    private void checkUserExistenceInFirestore(FirebaseUser user, String role1) {

        FirebaseFirestore.getInstance().collection(Objects.equals(role1, "Agency") ?Constant.AGENCY_DETAILS: Objects.equals(role1, "Admin") ?Constant.ADMIN_DETAILS:Constant.RESELLER_DETAILS)

                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (task.getResult().isEmpty()) {
                            Toast.makeText(this, "This agency not exist", Toast.LENGTH_SHORT).show();
                        } else {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                // Get email and user name from the document
                                String role = document.getString("role");
                                if (Objects.equals(role, "agency")){
                                    ApplicationClass.getSharedpref().saveString(Constant.USER_ID, user.getUid());
                                    Intent mainIntent = new Intent(SplashActivity.this, AgencyMainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mainIntent);
                                    finish();
                                }

                                else if (Objects.equals(role, "admin")){
                                    ApplicationClass.getSharedpref().saveString(Constant.USER_ID, user.getUid());
//                                    Intent mainIntent = new Intent(SplashActivity.this, AgencyMainActivity.class);
//                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(mainIntent);
//                                    finish();
                                }
                                else if (Objects.equals(role, "reseller")){
                                    ApplicationClass.getSharedpref().saveString(Constant.USER_ID, user.getUid());
//                                    Intent mainIntent = new Intent(SplashActivity.this, AgencyMainActivity.class);
//                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    startActivity(mainIntent);
//                                    finish();
                                }
                            }
                        }
                    }  // Handle Firestore query failure

                });
//        checkLastId(user);
    }

    private void startMarqueeAnimation1() {
        // Calculate the duration of the animation based on the length of the text
        int textLength = marqueeTextView.getText().length();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        long duration = (textLength * 1000) / screenWidth * 100; // Adjust the factor as needed

        // Create a translate animation
        TranslateAnimation animation = new TranslateAnimation(
                screenWidth,          // fromXDelta
                -screenWidth,         // toXDelta
                0,                    // fromYDelta
                0                     // toYDelta
        );

        animation.setInterpolator(new LinearInterpolator());
        animation.setDuration(duration);
        animation.setRepeatCount(TranslateAnimation.INFINITE);
        animation.setFillAfter(true);

        // Set animation listener if needed
        animation.setAnimationListener(new TranslateAnimation.AnimationListener() {
            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {
            }

            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                // Handle animation end if needed
            }

            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
                // Handle animation repeat if needed
            }
        });

        // Start the animation
        marqueeTextView.startAnimation(animation);
    }

    private void startMarqueeAnimation(TextView textView) {
        // Measure the text width
        textView.measure(0, 0);

        // Calculate the duration based on text width and screen width
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int textWidth = textView.getMeasuredWidth();
        long duration = (textWidth + screenWidth) * 3; // Adjust the multiplier as needed

        // Set up marquee animation
        ObjectAnimator animatorX = ObjectAnimator.ofFloat(textView, "translationX", -screenWidth, textWidth);
        animatorX.setInterpolator(new LinearInterpolator());
        animatorX.setDuration(duration);
        animatorX.setRepeatMode(ValueAnimator.RESTART);
        animatorX.setRepeatCount(ValueAnimator.INFINITE);

        // Start the animation
        animatorX.start();
    }
}