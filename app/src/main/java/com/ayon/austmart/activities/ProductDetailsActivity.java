package com.ayon.austmart.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ayon.austmart.Models.Post;
import com.ayon.austmart.R;
import com.ayon.austmart.activities.Chat.MessageActivity;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {

    String postKey;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;



    ImageView imgSeller, imgproduct, imgMessage,addwish;
    TextView txtProductName, txtProductPrice, txtdate, txtProductDetails, txtSellerName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        imgproduct = findViewById(R.id.details_product_image);
        imgSeller = findViewById(R.id.details_seller_user_photo);
        imgMessage = findViewById(R.id.details_message_icon);

        txtdate =findViewById(R.id.details_date);
        txtProductName = findViewById(R.id.details_Product_name);
        txtProductPrice = findViewById(R.id.details_Product_price);
        txtProductDetails =findViewById(R.id.details_Product_details);
        txtSellerName = findViewById(R.id.details_seller_name);
        addwish = findViewById(R.id.addtowishlist);

        mAuth =FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();


        final String productImage = getIntent().getExtras().getString("productImage");
        Glide.with(this).load(productImage).into(imgproduct);

        final String productTitle = getIntent().getExtras().getString("Product Name");
        txtProductName.setText(productTitle);

        final String productPrice = getIntent().getExtras().getString("productPrice");
        txtProductPrice.setText(productPrice);

        final String sellerImg = getIntent().getExtras().getString("User Photo");
        Glide.with(this).load(sellerImg).into(imgSeller);

        final String productDescription = getIntent().getExtras().getString("Description");
        txtProductDetails.setText(productDescription);

        postKey = getIntent().getExtras().getString("Product key");

        String date = timestampToString(getIntent().getExtras().getLong("date"));
        txtdate.setText(date);

        final String userName = getIntent().getExtras().getString("User Name");
        txtSellerName.setText(userName);

        final String userID = getIntent().getExtras().getString("UserID");


        imgMessage.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View v) {

                if (userID.equals(currentUser.getUid())) {




                    Toast.makeText(getApplicationContext(), "You can't message yourself!!", Toast.LENGTH_LONG).show();

                }
                else{


                    Intent messageActivity = new Intent(ProductDetailsActivity.this, MessageActivity.class);

                    messageActivity.putExtra("UserName", userName);
                    messageActivity.putExtra("UserImg", sellerImg);
                    messageActivity.putExtra("UserID", userID);





                    startActivity(messageActivity);


            }


            }
        });









        addwish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                //now creating post object


                Post post = new Post(productTitle,
                        productDescription,
                        productPrice,
                        userID, productImage,
                        sellerImg,userName);

                //upload post to firebase data base

                addPost(post);


            }
        });








    }


    private String timestampToString(long time)
    {
        Calendar calendar =Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = android.text.format.DateFormat.format("dd-MM-yyyy",calendar).toString();
        return date;


    }



    private void addPost(Post post) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        String wishlist = currentUser.getUid()+" "+currentUser.getDisplayName()+" Wish List";

        DatabaseReference myRef = database.getReference(wishlist).push();

        //get unique id and update post key

        String Key = myRef.getKey();
        post.setPostKey(Key);

        //add post key to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                showMessage("Added to Wish List!");




            }
        });
    }


    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

    }


}
