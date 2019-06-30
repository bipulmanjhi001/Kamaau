package com.larvaesoft.kamaau.Activity;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

// Importing Google GMS Auth API Libraries.
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import larvaesoft.com.kamaau.R;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.larvaesoft.kamaau.model.ConnectionDetector;
import com.larvaesoft.kamaau.ui.dashboard.Home;
import java.io.File;

public class SignUpActivity extends AppCompatActivity {

    public static final int RequestSignInCode = 7;
    public FirebaseAuth firebaseAuth;
    public GoogleApiClient googleApiClient;
    com.google.android.gms.common.SignInButton signInButton;
    SharedPreferences preferences;
    String email;

    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    CoordinatorLayout dashboard_sign_up;
    ProgressBar progressBar;
    private StorageReference mStorageRef;
    Uri getphoto;
    TextView read_terms;
    Button google_sign_in_button;
    RadioButton check_conditions;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        preferences=getApplicationContext().getSharedPreferences("register_save", Context.MODE_PRIVATE);
        email=preferences.getString("email","");

        google_sign_in_button=(Button)findViewById(R.id.google_sign_in_button);
        google_sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( googleApiClient.isConnected()) {
                    UserSignInMethod();
                    //Plus.AccountApi.clearDefaultAccount(googleApiClient);
                    googleApiClient.disconnect();
                    googleApiClient.connect();
                }
            }
        });
        check_conditions=(RadioButton)findViewById(R.id.check_conditions);
        check_conditions.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(check_conditions.isChecked()){
                    google_sign_in_button.setVisibility(View.VISIBLE);
                }
            }
        });
        read_terms=(TextView)findViewById(R.id.read_terms);
        read_terms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CheckTerms();
            }
        });
        dashboard_sign_up=(CoordinatorLayout)findViewById(R.id.dashboard_sign_up);
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        progressBar = (ProgressBar) findViewById(R.id.progressBar_cyclic);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        if (isInternetPresent) {

            if(!email.isEmpty()){

                Intent intent=new Intent(SignUpActivity.this,Home.class);
                startActivity(intent);
                finish();

            }else {
                signInButton = (SignInButton) findViewById(R.id.sign_in_button);
                signInButton = (com.google.android.gms.common.SignInButton) findViewById(R.id.sign_in_button);
                firebaseAuth = FirebaseAuth.getInstance();

                // Creating and Configuring Google Sign In object.
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                // Creating and Configuring Google Api Client.
                googleApiClient = new GoogleApiClient.Builder(SignUpActivity.this)
                        .enableAutoManage(SignUpActivity.this, new GoogleApiClient.OnConnectionFailedListener() {
                                    @Override
                                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                                    }
                                }
                    /* OnConnectionFailedListener */
                        )
                        .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                        .build();
                // Adding Click listener to User Sign in Google button.
                signInButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        UserSignInMethod();
                    }
                });
            }
        }else{
            // Ask user to connect to Internet
            Snackbar snackbar = Snackbar
                    .make(dashboard_sign_up, "No internet connection ", Snackbar.LENGTH_LONG)
                    .setAction("DISMISS", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            TextView snackbarActionTextView = (TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_action);
            snackbarActionTextView.setTextSize(14);

            snackbarActionTextView.setTextColor(Color.RED);
            snackbarActionTextView.setTypeface(snackbarActionTextView.getTypeface(), Typeface.BOLD);

            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            textView.setMaxLines(1);
            textView.setTextSize(14);
            textView.setSingleLine(true);
            textView.setTypeface(null, Typeface.BOLD);
            snackbar.show();
        }
    }
    // Sign In function Starts From Here.
    public void UserSignInMethod(){

        // Passing Google Api Client into Intent.
        Intent AuthIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(AuthIntent, RequestSignInCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestSignInCode){
            GoogleSignInResult googleSignInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (googleSignInResult.isSuccess()){
                GoogleSignInAccount googleSignInAccount = googleSignInResult.getSignInAccount();
                FirebaseUserAuth(googleSignInAccount);
            }
        }
    }

    public void FirebaseUserAuth(GoogleSignInAccount googleSignInAccount) {
        AuthCredential authCredential = GoogleAuthProvider.getCredential(googleSignInAccount.getIdToken(), null);
        firebaseAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task AuthResultTask) {

                        if (AuthResultTask.isSuccessful()){

                            progressBar.setVisibility(View.VISIBLE);
                            // Getting Current Login user details.
                            FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                            String name=firebaseUser.getDisplayName();
                            String email=firebaseUser.getEmail();
                            String gid=firebaseUser.getUid();
                            getphoto=firebaseUser.getPhotoUrl();
                            String getphotourl=getphoto.toString();

                            Intent intent=new Intent(SignUpActivity.this,RegisterActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("email",email);
                            intent.putExtra("gid",gid);
                            intent.putExtra("getphoto",getphotourl);
                            startActivity(intent);
                            finish();

                        }else {
                            Toast.makeText(SignUpActivity.this,"Something Went Wrong",Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    public void UploadImage(){

        Uri file = Uri.fromFile(new File(String.valueOf(getphoto)));
        StorageReference riversRef = mStorageRef.child("images/photo.jpg");

        riversRef.putFile(file)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get a URL to the uploaded content
                        Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                    }
                });
    }

    public void CheckTerms(){

        Uri uri = Uri.parse("http://www.larvaesoft.com/");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }
}
