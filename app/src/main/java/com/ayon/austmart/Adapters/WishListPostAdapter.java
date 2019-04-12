package com.ayon.austmart.Adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayon.austmart.Models.Post;
import com.ayon.austmart.R;
import com.ayon.austmart.activities.ProductDetailsActivity;
import com.ayon.austmart.activities.WishListProductdetailsActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class WishListPostAdapter extends RecyclerView.Adapter<WishListPostAdapter.MyViewHolder> {

    Context mContext;

    List<Post>mData;




    public WishListPostAdapter(Context mContext, List<Post> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_post_items,parent, false);







        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.productTitle.setText(mData.get(position).getProductName());
        holder.productPrice.setText(mData.get(position).getPrice());
        Glide.with(mContext).load(mData.get(position).getProductPhoto()).into(holder.postImage);
        Glide.with(mContext).load(mData.get(position).getUserPhoto()).into(holder.imgPostProfile);





    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView productTitle;
        TextView productPrice;
        ImageView postImage;
        ImageView imgPostProfile;




        public MyViewHolder(View itemView) {
            super(itemView);

            productTitle = itemView.findViewById(R.id.Product_name_row);
            productPrice = itemView.findViewById(R.id.Product_price_row);
            postImage = itemView.findViewById(R.id.product_image_row);
            imgPostProfile = itemView.findViewById(R.id.row_user_photo);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent productDetailsActivity = new Intent(mContext, WishListProductdetailsActivity.class);
                    int position = getAdapterPosition();

                    productDetailsActivity.putExtra("Product Name",mData.get(position).getProductName());
                    productDetailsActivity.putExtra("productImage",mData.get(position).getProductPhoto());
                    productDetailsActivity.putExtra("Description",mData.get(position).getDescription());
                    productDetailsActivity.putExtra("Product key",mData.get(position).getPostKey());
                    productDetailsActivity.putExtra("User Photo",mData.get(position).getUserPhoto());
                    productDetailsActivity.putExtra("productPrice",mData.get(position).getPrice());
                    productDetailsActivity.putExtra("UserID",mData.get(position).getUserID());

                    productDetailsActivity.putExtra("User Name",mData.get(position).getUserName());

                    long timestamp = (long) mData.get(position).getTimeStamp();
                    productDetailsActivity.putExtra("date",timestamp);

                    mContext.startActivity(productDetailsActivity);






                }
            });


            imgPostProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext,"Profile Pic Pressed!",Toast.LENGTH_LONG).show();
                }
            });
        }





    }



}
