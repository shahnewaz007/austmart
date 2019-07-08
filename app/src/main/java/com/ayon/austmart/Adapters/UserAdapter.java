package com.ayon.austmart.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

import com.ayon.austmart.Models.Chat;
import com.ayon.austmart.Models.Post;
import com.ayon.austmart.Models.User;
import com.ayon.austmart.R;
import com.ayon.austmart.activities.Chat.InboxActivity;
import com.ayon.austmart.activities.Chat.MessageActivity;
import com.ayon.austmart.activities.ProductDetailsActivity;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mUsers;
    private boolean isChat;
    String theLastMessage;
    public int unread=0;

    public UserAdapter(Context context, List<User>users, boolean isChat) {
        mContext = context;
        mUsers = users;
        this.isChat = isChat;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.row_user_item,parent,false);

        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

       // User user= mUsers.get(position);
       // holder.userid.setText(user.getUserId());
        holder.userid.setText(mUsers.get(position).getUserName());
        holder.userName.setText(mUsers.get(position).getUserId());

        Glide.with(mContext).load(mUsers.get(position).getUserPhoto()).into(holder.pro_pic);


        if(isChat)
        {
            lastMessage(mUsers.get(position).getUserId(),holder.last_msg);
        }
        else
        {
            holder.last_msg.setVisibility(View.GONE);
        }

        if(isChat)
        {
            if(mUsers.get(position).getStatus().equals("Online")){
                holder.userOff.setVisibility(View.GONE);
                holder.userOn.setVisibility(View.VISIBLE);
            }
            else
            {
                holder.userOff.setVisibility(View.VISIBLE);
                holder.userOn.setVisibility(View.GONE);
            }
        }
        else
        {
            holder.userOff.setVisibility(View.GONE);
            holder.userOn.setVisibility(View.GONE);
        }



    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView userid;
        public ImageView pro_pic;
        public TextView userName;
        public ImageView userOn;
        public ImageView userOff;
        private TextView last_msg;


        public ViewHolder(View itemView){
            super(itemView);

            userid =itemView.findViewById(R.id.username_row);
            pro_pic =itemView.findViewById(R.id.avatar_row_user);
            userName =itemView.findViewById(R.id.userid_row);
            userOn=itemView.findViewById(R.id.user_on);
            userOff=itemView.findViewById(R.id.user_off);
            last_msg=itemView.findViewById(R.id.last_msg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent messageActivity = new Intent(mContext, MessageActivity.class);
                    int position = getAdapterPosition();

                    

                    messageActivity.putExtra("UserName",mUsers.get(position).getUserName());
                    messageActivity.putExtra("UserImg",mUsers.get(position).getUserPhoto());
                    messageActivity.putExtra("UserID",mUsers.get(position).getUserId());





                   mContext.startActivity(messageActivity);






                }
            });




        }
    }


    //Check for the last message

    private void lastMessage(final String userID, final TextView last_msg)
    {
        theLastMessage = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Chats");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chat chat = snapshot.getValue(Chat.class);

                    if(chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userID) ||
                            chat.getReceiver().equals(userID) && chat.getSender().equals(firebaseUser.getUid()))
                    {
                        theLastMessage = chat.getMessage();
                        if(chat.getSender().equals(userID) &&chat.isIsseen()==false)
                        {
                            unread = 1;
                        }
                    }
                }

                switch (theLastMessage) {
                    case "default": last_msg.setText("No Message");
                    break;

                    default: last_msg.setText(theLastMessage);
                    if(unread == 1) {
                        last_msg.setTextColor(Color.parseColor("#FF89AF1F"));
                        last_msg.setTypeface(Typeface.DEFAULT_BOLD);
                        unread =0;

                    }

                    break;

                }
                theLastMessage = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}