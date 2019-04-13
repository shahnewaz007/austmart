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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayon.austmart.Models.Post;
import com.ayon.austmart.Models.User;
import com.ayon.austmart.R;
import com.ayon.austmart.activities.Chat.InboxActivity;
import com.ayon.austmart.activities.ProductDetailsActivity;
import com.bumptech.glide.Glide;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUsers;

    public UserAdapter(Context context, List<User>users) {
        mContext = context;
        mUsers = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_user_item,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        //User user= mUsers.get(position);
       // holder.userid.setText(user.getUserId());
        holder.userid.setText(mUsers.get(position).getUserName());
        Glide.with(mContext).load(mUsers.get(position).getUserPhoto()).into(holder.pro_pic);

    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userid;
        public ImageView pro_pic;

        public ViewHolder(View itemView){
            super(itemView);

            userid =itemView.findViewById(R.id.username_row);
            pro_pic =itemView.findViewById(R.id.avatar_row_user);


        }
    }

}