package com.example.interiodesign;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.Collections;


public class HomeFragment extends Fragment {
    Button logout;
    private FirebaseData firebaseData;
    private RecyclerView recentlyViewedRecyclerView;
    private static final int[] BUTTON_IDS = {
            R.id.chairsCardView,
            R.id.sofasCardView,
    };
    //List<CardView> cardViewList = new ArrayList<CardView>();

    FragmentManager fragmentManager;

    public void callRespectiveFragment(View v){
        Log.e("manual",this.getClass().getSimpleName()+" "+ (String) v.getTag());
       // String categoryname = v.getTag().toString();
        Bundle data = new Bundle();
        data.putString("categoryName", v.getTag().toString());
        CategoryFragment categoryFragment = new CategoryFragment();
        categoryFragment.setArguments(data);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.frameLayout_container_home,categoryFragment)
                .addToBackStack(null)
                .commit();

       /* String fragmentName = (String)v.getTag();

        Object frag = null;
        try {
            frag = Class.forName(getActivity().getPackageName()+"."+fragmentName).newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout_container_home, (Fragment) frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();*/
    }
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        for(int id:BUTTON_IDS){
            CardView cardView = view.findViewById(id);
            cardView.setOnClickListener(this::callRespectiveFragment);
        }
        recentlyViewedRecyclerView = view.findViewById(R.id.recentlyViewedRecyclerView);
        recentlyViewedRecyclerView.setHasFixedSize(true);
        recentlyViewedRecyclerView.setLayoutManager(new GridLayoutManager(view.getContext(),2));


        firebaseData = new FirebaseData();
        firebaseData.getRecentlyViewedList(FirebaseAuth.getInstance().getUid());
        firebaseData.getProductMutableLiveList().observe(getViewLifecycleOwner(), new Observer<ArrayList<Product>>() {
            @Override
            public void onChanged(ArrayList<Product> products) {
                if(products.size() == 0){

                }
                Collections.reverse(products);
                recentlyViewedRecyclerView.setAdapter(new InterioDesignAdapter(R.layout.collection_row,products,false,null));
            }
        });
        firebaseData.getErrorMutableLive().observe(getViewLifecycleOwner(), new Observer<DatabaseError>() {
            @Override
            public void onChanged(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Log.e("manual",this. getClass(). getSimpleName()+" "+"inside oncreate in home fragment");
//        logout = view.findViewById(R.id.logout_button);
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Toast.makeText(getContext(), "logged out successfully", Toast.LENGTH_SHORT).show();
//                Intent i = new Intent(getContext(),LoginRegister.class);
//                startActivity(i);
//            }
//        });



        return view;
    }
}
