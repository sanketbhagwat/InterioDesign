package com.example.interiodesign;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class LoginFragment extends Fragment {
    public FragmentManager fragmentManager;
    private FirebaseAuth fauth;
    TextInputEditText email,password,confirm_password;
    Button register,login;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        fauth = FirebaseAuth.getInstance();
        register = view.findViewById(R.id.register_button);
        login = view.findViewById(R.id.login_button);
        email = view.findViewById(R.id.email);
        password = view.findViewById(R.id.password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email.getText().toString().trim().isEmpty()||password.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Please fill all the fields.", Toast.LENGTH_SHORT).show();
                    return;
                }

                fauth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getContext(), "success login.", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(getContext(),Home.class);
                                    startActivity(i);

                                } else {
                                    String exception = task.getException().toString();
                                    String[] error = exception.split(":");
                                    Toast.makeText(getContext(), error[1], Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //getActivity().getFragmentManager().popBackStackImmediate();
                fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout_container,new RegisterFragment());
                //fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });



        return view;
    }
}