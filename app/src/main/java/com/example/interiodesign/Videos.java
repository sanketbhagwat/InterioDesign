package com.example.interiodesign;

import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Videos#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Videos extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Videos() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Videos.
     */
    // TODO: Rename and change types and number of parameters
    public static Videos newInstance(String param1, String param2) {
        Videos fragment = new Videos();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    RecyclerView videoRecycler;
    imageAdapter image_adapter;
    List<String> videos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_videos, container, false);
        videoRecycler=view.findViewById(R.id.videoRecycler);

//          loadImages();
        videoRecycler.setHasFixedSize(true);
        videoRecycler.setLayoutManager(new GridLayoutManager(view.getContext(),3));
//        videoRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayout.VERTICAL));

        videos=imageGallery.listOfVideos(view.getContext());
        image_adapter=new imageAdapter(videos,getContext());
        videoRecycler.setAdapter(image_adapter);

        return view;

    }
}