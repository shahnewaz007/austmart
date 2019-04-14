package com.ayon.austmart.activities;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ayon.austmart.Fragments.HomeFragment;
import com.ayon.austmart.Fragments.InboxFragment;
import com.ayon.austmart.Fragments.WishlistFragment;
import com.ayon.austmart.Models.Post;
import com.ayon.austmart.R;
import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.internal.Storage;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Currency;

import de.hdodenhof.circleimageview.CircleImageView;



public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final int PReqCode = 2;  //for requesting permission to access external storage
    private static final int REQUESCODE = 2;  //used in startActivityForResult for gallery intent
    FirebaseAuth mAuth;
        FirebaseUser currentUser;
        Dialog popAddPost;

        ImageView popupUserPhoto, popupProductImage, popupAddButton;
        TextView popupProductName, popupDescription, popupPrice;
        ProgressBar popupProgressbar;

    private Uri pickedImgUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //ini

        mAuth =FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();



        //initialize pop up post

        inipopup();

        setupPopupImageClick();


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popAddPost.show();

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        updateNavHeader();

        //set the home fragment as the default




        getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit(); //open the home fragment



    }

    private void setupPopupImageClick() {

        popupProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //we need to open the gallery


                checkAndRequestForPermission();


            }
        });

    }


    public void checkAndRequestForPermission()
    {
        if(ContextCompat.checkSelfPermission(Home.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            if(ActivityCompat.shouldShowRequestPermissionRationale(Home.this,Manifest.permission.READ_EXTERNAL_STORAGE))
            {
                Toast.makeText(Home.this,"Please accept for required permission",Toast.LENGTH_SHORT ).show();
            }

            else
            {
                ActivityCompat.requestPermissions(Home.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},PReqCode);
            }


        }

        else
            openGallery();


    }


    private void openGallery() {

        //Open gallery intent and wait for user to pick an image

        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,REQUESCODE);


    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data !=null) {
            //user has successfully picked an image...
            //saving its reference to a Uri variable

            pickedImgUri = data.getData();
            popupProductImage.setImageURI(pickedImgUri);



        }
        else{
            // USER HAS NOT PICKED IMAGE

        }
    }


    private void inipopup() {

        popAddPost = new Dialog(this);
        popAddPost.setContentView(R.layout.popup_add_post);
        popAddPost.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popAddPost.getWindow().setLayout(Toolbar.LayoutParams.MATCH_PARENT, Toolbar.LayoutParams.WRAP_CONTENT);
        popAddPost.getWindow().getAttributes().gravity = Gravity.TOP;


        //initialize popup widgets

        popupUserPhoto = popAddPost.findViewById(R.id.post_user_photo);
        popupProductImage = popAddPost.findViewById(R.id.popup_product_image);
        popupProductName = popAddPost.findViewById(R.id.product_name_post);
        popupDescription = popAddPost.findViewById(R.id.description_post);
        popupPrice = popAddPost.findViewById(R.id.price_post);
        popupAddButton = popAddPost.findViewById(R.id.post_icon);
        popupProgressbar = popAddPost.findViewById(R.id.progressBar_post);


        //load user image

        Glide.with(Home.this).load(currentUser.getPhotoUrl()).into(popupUserPhoto);


        // Add post click Listener

        popupAddButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupAddButton.setVisibility(View.INVISIBLE);
                popupProgressbar.setVisibility(View.VISIBLE);


                if (!popupProductName.getText().toString().isEmpty() && !popupDescription.getText().toString().isEmpty()
                        && !popupPrice.getText().toString().isEmpty() && pickedImgUri != null) {

                    //everything is ok
                    //add to database

                    //access firebase storage

                    StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("Product Image");
                    final StorageReference imageFilePath = storageReference.child(pickedImgUri.getLastPathSegment());
                    imageFilePath.putFile(pickedImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            imageFilePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    String imageDownloadLink = uri.toString();
                                    String productPrice = popupPrice.getText().toString();
                                    productPrice += " TK";


                                    //now creating post object


                                    Post post = new Post(popupProductName.getText().toString(),
                                            popupDescription.getText().toString(),
                                            productPrice,
                                            currentUser.getUid(), imageDownloadLink,
                                            currentUser.getPhotoUrl().toString(),currentUser.getDisplayName().toString());

                                    //upload post to firebase data base

                                    addPost(post);







                                }
                            });

                        }
                    });




                } else {
                    showMessage("Please fill all the fields properly!");
                    popupAddButton.setVisibility(View.VISIBLE);
                    popupProgressbar.setVisibility(View.INVISIBLE);

                }


            }
        });
    }



    private void addPost(Post post)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Product Posts").push();

        //get unique id and update post key

        String Key = myRef.getKey();
        post.setPostKey(Key);

        //add post key to firebase database

        myRef.setValue(post).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                showMessage("Post Added Successfully!");
                popupProgressbar.setVisibility(View.INVISIBLE);
                popupAddButton.setVisibility(View.VISIBLE);
                popupProductName.setText("");
                popupPrice.setText("");
                popupDescription.setText("");
                popupProductImage.setImageURI(null);


                popAddPost.dismiss();

            }
        });




    }


        private void showMessage(String message) {
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();

        }








    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            final AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setMessage("Are you sure you want to exit?");
            builder.setCancelable(true);

            builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {


                    finish();

                }
            });

            builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_Home){

            getSupportActionBar().setTitle("Home");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new HomeFragment()).commit();


        } else if (id == R.id.nav_WishList) {
            getSupportActionBar().setTitle("Wish List");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new WishlistFragment()).commit();


        } else if (id == R.id.nav_Inbox) {

            getSupportActionBar().setTitle("Inbox");
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new InboxFragment()).commit();

        } else if (id == R.id.nav_signOut) {

            FirebaseAuth.getInstance().signOut();
            Intent baseActivity = new Intent(this, BaseActivity.class);
            startActivity(baseActivity);
            finish();


        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateNavHeader()
    {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView navUsername = headerView.findViewById(R.id.nav_user_name);
        TextView navUserMail = headerView.findViewById(R.id.nav_user_mail);
        CircleImageView navUserPhoto = headerView.findViewById(R.id.nav_user_photo);


        navUserMail.setText((currentUser.getEmail()));
        navUsername.setText(currentUser.getDisplayName());

        //using Glide to load user image

        Glide.with(this).load(currentUser.getPhotoUrl()).into(navUserPhoto);




    }






}
