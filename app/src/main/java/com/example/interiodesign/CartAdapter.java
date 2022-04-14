package com.example.interiodesign;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHodler> {
    private ArrayList<Product> cartList;
    private FirebaseData firebaseData;
    private Context context;

    public CartAdapter(Context context,ArrayList<Product> cartList) {
        this.context = context;
        this.cartList = cartList;
        this.firebaseData = new FirebaseData();
    }

    @NonNull
    @Override
    public CartViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_row, parent, false);
        return new CartViewHodler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHodler holder, int position) {

        Picasso.get().load(cartList.get(position).getImg())
                .into(holder.image);

        holder.title.setText(cartList.get(position).getTitle());
        holder.price.setText(cartList.get(position).getPrice().toString() + " Rs");
        holder.quantity.setText(cartList.get(position).getQuantity().toString());
        holder.expandable.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, cartList.get(position).getTexture().size()*162));

        ArrayAdapter textureAdapter = new ArrayAdapter<String>(context, R.layout.dropdown_items, cartList.get(position).getTexture());
        holder.textureList.setAdapter(textureAdapter);

        String materials[] = {"Material 1","Material 2","Material 3","Material 4","Material 5","Material 6","Material 7","Material 8","Material 9","Material 10"};
        ArrayAdapter materialAdapter;
        if(cartList.get(position).getTexture().size() == 1)
            materialAdapter = new ArrayAdapter<String>(context, R.layout.dropdown_items, new String[]{"All materials"});
        else
            materialAdapter = new ArrayAdapter<String>(context, R.layout.dropdown_items, materials);
        holder.materialList.setAdapter(materialAdapter);


        holder.cartItem.setOnClickListener(view -> {
            Log.e("manual", "responding to clcik");
            if(holder.expandable.getVisibility() == View.GONE)
                holder.expandable.setVisibility(View.VISIBLE);
            else
                holder.expandable.setVisibility(View.GONE);
        });


        holder.deleteButton.setOnClickListener(view -> {
            firebaseData.removeFromCart(FirebaseAuth.getInstance().getUid(), cartList.get(position).getKey());
            //notifyItemRemoved(position);
        });

        holder.increaseQuantityButton.setOnClickListener(view -> {
            Long quantity = cartList.get(position).getQuantity();
            firebaseData.changeQuantity(FirebaseAuth.getInstance().getUid(), cartList.get(position).getKey(), quantity + 1);
        });

        holder.decreaseQuantityButton.setOnClickListener(view -> {
            Long quantity = cartList.get(position).getQuantity();
            if (quantity == 1)
                return;
            firebaseData.changeQuantity(FirebaseAuth.getInstance().getUid(), cartList.get(position).getKey(), quantity - 1);
        });

    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }
    /*@Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }*/

    public class CartViewHodler extends RecyclerView.ViewHolder {

        private TextView title, price, quantity;
        private ImageView image;

        private ImageView deleteButton;
        private ImageButton increaseQuantityButton, decreaseQuantityButton;

        private LinearLayout cartItem, expandable;
        private ListView materialList, textureList;

        public CartViewHodler(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            price = itemView.findViewById(R.id.price);
            quantity = itemView.findViewById(R.id.quantity);

            deleteButton = itemView.findViewById(R.id.deleteButton);
            quantity = itemView.findViewById(R.id.quantity);
            increaseQuantityButton = itemView.findViewById(R.id.increaseQuantityButton);
            decreaseQuantityButton = itemView.findViewById(R.id.decreaseQuantityButton);

            cartItem = itemView.findViewById(R.id.cartItem);
            expandable = itemView.findViewById(R.id.expandable);
            materialList = itemView.findViewById(R.id.materialList);
            textureList = itemView.findViewById(R.id.textureList);

//            linearLayout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Product product = cartList.get(getAdapterPosition());
//                    cartList.setExpandable(!cartList.isExpandable());
//                    notifyItemChanged(getAdapterPosition());
//                }
//            });
        }
    }
}