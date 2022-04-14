package com.example.interiodesign;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;

public class LoginRegister extends AppCompatActivity {
    public static FragmentManager fragmentManager;
    private FirebaseAuth fauth;
    private GoogleSignInClient mGoogleSignInClient;
    SignInButton signInButton;
    private DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        fauth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = fauth.getCurrentUser();
        
        if(currentUser != null){
            Log.e("manual",this. getClass(). getSimpleName()+" "+currentUser.getUid());
            Log.e("manual",this. getClass(). getSimpleName()+" "+"user is already logged in.taking to home");
            Intent i = new Intent(getBaseContext(),Home.class);
            startActivity(i);
            finish();
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_register);

        ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK ) {
                            // There are no request codes
                            Intent data = result.getData();
                            Task<GoogleSignInAccount> signInTask = GoogleSignIn.getSignedInAccountFromIntent(data);
                            try {
                                GoogleSignInAccount signInAccount = signInTask.getResult(ApiException.class);
                                Log.e("manual",this. getClass(). getSimpleName()+" "+this. getClass(). getSimpleName()+" "+"google account accepted");
                                firebaseAuthWithGoogle(signInAccount.getIdToken());
                            } catch (ApiException e) {
                                Log.e("manual",this. getClass(). getSimpleName()+" "+"google error");
                                e.printStackTrace();
                            }
                        }
                    }
                });



        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions
                .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id_string))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this,gso);
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                someActivityResultLauncher.launch(signInIntent);
            }
        });


        fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.add(R.id.frameLayout_container,new LoginFragment(),null);
        fragmentTransaction.commit();
    }
    private void firebaseAuthWithGoogle(String idToken) {
        fauth = FirebaseAuth.getInstance();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        fauth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.e("manual",this. getClass(). getSimpleName()+" "+ "mergin with firebase:success");
                            Toast.makeText(getBaseContext(), "success login.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getBaseContext(),Home.class);
                            startActivity(i);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e("manual",this. getClass(). getSimpleName()+" "+ "mergin failed in firebase:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }
}