package com.example.interiodesign;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Images#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Images extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Images() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Images.
     */
    // TODO: Rename and change types and number of parameters
    public static Images newInstance(String param1, String param2) {
        Images fragment = new Images();
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

    ImageView img1;
    ImageView img2;
    RecyclerView imageRecycler;
    imageAdapter image_adapter;
    List<String>images;
    LinearLayout linearLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_images, container, false);
//        getImages();
//        img1=view.findViewById(R.id.img1);
//        img2=view.findViewById(R.id.img2);
        imageRecycler=view.findViewById(R.id.imageRecycler);
        linearLayout=view.findViewById(R.id.imgLayout);


//          loadImages();
        imageRecycler.setHasFixedSize(true);
        imageRecycler.setLayoutManager(new GridLayoutManager(view.getContext(),3));
//        imageRecycler.setLayoutManager(new StaggeredGridLayoutManager(2,LinearLayout.VERTICAL));

        images=imageGallery.listOfImages(view.getContext());
        image_adapter=new imageAdapter(images,getContext());
        imageRecycler.setAdapter(image_adapter);
        image_adapter.notifyDataSetChanged();
//        image_adapter.setClickListener(this);

//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(getContext(), "tappp", Toast.LENGTH_SHORT).show();
//            }
//        });

        return view;
    }



    private void loadImages() {


    }


    @SuppressLint("Range")
    private void getImages() {
        String uri = MediaStore.Images.Media.DATA;
        // if GetImageFromThisDirectory is the name of the directory from which image will be retrieved
        String condition = uri + " like '%/homeAR/%'";
        String[] projection = {uri, MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media.SIZE};
        try {

            Cursor cursor = getContext().getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection,
                    condition, null, null);
            if (cursor != null) {
                boolean isDataPresent = cursor.moveToFirst();
                if (isDataPresent) {
                    do {
                        Toast.makeText(getActivity(), "hereee", Toast.LENGTH_SHORT).show();
                        Log.e("imagepath", cursor.getString(cursor.getColumnIndex(uri)));
//                        Picasso.get().load(cursor.getString(cursor.getColumnIndex(uri))).into(img1);

                    } while (cursor.moveToNext());
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}