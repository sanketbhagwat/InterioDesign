package com.example.interiodesign;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

public class imageAdapter extends RecyclerView.Adapter<imageAdapter.ViewHolder> {

    private List<String> images;
    private Context context;
//    private List<String >videos;
//    private ItemClickListener clickListener;

    public imageAdapter(List<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

//    public void setClickListener(ItemClickListener clickListener) {
//        this.clickListener = clickListener;
//    }

    @NonNull
    @NotNull
    @Override
    public imageAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.img_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull imageAdapter.ViewHolder holder, int position) {
        String image=images.get(position);
        Log.e("images",image);
//        Picasso.get().load(image).into(holder.image);
//        Picasso.get().load(image)
//                .into(holder.image);
        Glide.with(context).load(image).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, image, Toast.LENGTH_SHORT).show();
//                new Intent(Intent.ACTION_VIEW);

//                Intent intent=new Intent();
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                File file = new File(image);

                MimeTypeMap mime = MimeTypeMap.getSingleton();
                String ext = file.getName().substring(file.getName().lastIndexOf(".") + 1);
                String type = mime.getMimeTypeFromExtension(ext);
                Log.e("manual8", String.valueOf(FileProvider.getUriForFile(view.getContext(), context.getPackageName(),file)));
                Log.e("manual8", String.valueOf(Uri.parse(file.getPath())));


                intent.setDataAndType(FileProvider.getUriForFile(view.getContext(), context.getPackageName() , file), type);
//                intent.setData(Uri.parse(image);
//                intent.setType(type);
                Intent chooser = Intent.createChooser(intent, "View Using");

                view.getContext().startActivity(chooser);


            }
        });

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        ImageView image;
//        LinearLayout linearLayout;
//        public imageAdapter(@NonNull @NotNull View itemView) {
//            super(itemView);
//        }

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            image=itemView.findViewById(R.id.imageG);
//            linearLayout=itemView.findViewById(R.id.imgLayout);

//            linearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    getAc
//                }
//            });




        }


//        @Override
//        public void onClick(View view) {
//            if (clickListener != null) clickListener.onClick(view, getAdapterPosition());
//
//        }
    }
}
