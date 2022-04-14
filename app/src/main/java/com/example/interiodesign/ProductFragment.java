package com.example.interiodesign;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProductFragment extends Fragment {
    ImageView product_image;
    Button showAR_button,show3D_button,addToCartButton;
    DatabaseReference databaseReference;


    public ProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        //FirebaseApp.initializeApp(this);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        product_image = view.findViewById(R.id.product_image);
        showAR_button = view.findViewById(R.id.showAR_button);
        show3D_button = view.findViewById(R.id.show3D_button);
        addToCartButton = view.findViewById(R.id.addToCartButton);
        //product_image.setImageResource(R.drawable.loading);



        Product productObject = getArguments().getParcelable("productObject");
        Log.e("manual",this. getClass(). getSimpleName()+" "+productObject.toString()+"in product fragment");
        String modelName = productObject.getKey();
        Picasso.get().load(productObject.getImg())
                .into(product_image);



        showAR_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),showAR.class);
                Log.e("manual",this. getClass(). getSimpleName()+" "+modelName);
                intent.putExtra("productObject",productObject);
                startActivity(intent);

            }
        });
        show3D_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getContext(),show3D.class);
                Log.e("manual",this. getClass(). getSimpleName()+" "+modelName);
                intent.putExtra("modelName",modelName);
                startActivity(intent);
            }
        });

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> sample = new ArrayList<>();
                sample.add("default");
                FirebaseData firebaseData = new FirebaseData();
                productObject.setQuantity(1L);
                productObject.setTexture(sample);
               // firebaseData.addToRecentlyViewedList(FirebaseAuth.getInstance().getUid(),productObject);
                firebaseData.addToCartList(FirebaseAuth.getInstance().getUid(),productObject);
            }
        });



        return view;
    }

}