package com.ayon.austmart.Adapters;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayon.austmart.Models.Post;
import com.ayon.austmart.R;
import com.bumptech.glide.Glide;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.MyViewHolder> {

    Context mContext;

    List<Post>mData;


    public PostAdapter(Context mContext, List<Post> mData) {
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
        }
    }
}
