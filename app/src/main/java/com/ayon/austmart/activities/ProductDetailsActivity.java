package com.ayon.austmart.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ayon.austmart.R;
import com.ayon.austmart.activities.Chat.MessageActivity;
import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.MessageFormat;
import java.util.Calendar;
import java.util.Locale;

public class ProductDetailsActivity extends AppCompatActivity {

    String postKey;


    ImageView imgSeller, imgproduct, imgMessage;
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


        String productImage = getIntent().getExtras().getString("productImage");
        Glide.with(this).load(productImage).into(imgproduct);

        String productTitle = getIntent().getExtras().getString("Product Name");
        txtProductName.setText(productTitle);

        String productPrice = getIntent().getExtras().getString("productPrice");
        txtProductPrice.setText(productPrice);

        final String sellerImg = getIntent().getExtras().getString("User Photo");
        Glide.with(this).load(sellerImg).into(imgSeller);

        String productDescription = getIntent().getExtras().getString("Description");
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
                Intent messageActivity = new Intent(ProductDetailsActivity.this, MessageActivity.class);

                messageActivity.putExtra("UserName", userName);
                messageActivity.putExtra("UserImg", sellerImg);
                messageActivity.putExtra("UserID", userID);


                startActivity(messageActivity);


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
}
