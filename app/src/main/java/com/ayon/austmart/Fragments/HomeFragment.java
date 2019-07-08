package com.ayon.austmart.Fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ayon.austmart.Adapters.PostAdapter;
import com.ayon.austmart.Models.Post;
import com.ayon.austmart.R;
import com.ayon.austmart.activities.BaseActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    RecyclerView postRecyclerView, searchRecyclerView;

    PostAdapter postAdapter;

    FirebaseDatabase mFirebaseDatabase;
    DatabaseReference mDatabaseReference;

    List<Post>PostList;

    private EditText productSearch;
    private Button productSearchButton;
    private Button productSearchExitButton;
    private LinearLayout searchbar;












    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        LinearLayoutManager lin = new LinearLayoutManager(getActivity());
        lin.setStackFromEnd(true);
        lin.setReverseLayout(true);

        View fragmentView = inflater.inflate(R.layout.fragment_home,container,false);

        postRecyclerView = fragmentView.findViewById(R.id.PostRV);
        //searchRecyclerView = fragmentView.findViewById(R.id.SearchRV);
        productSearch = fragmentView.findViewById(R.id.product_search);
        productSearchButton = fragmentView.findViewById(R.id.product_search_button);
        searchbar =fragmentView.findViewById(R.id.searchbar1);
        productSearchExitButton = fragmentView.findViewById(R.id.product_search_exit_button);

        /*
        postRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        postRecyclerView.setHasFixedSize(true);

        */

        postRecyclerView.setLayoutManager(lin);
        postRecyclerView.setHasFixedSize(true);


        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Product Posts");

        productSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbar.setVisibility(View.VISIBLE);

                String searchProduct = productSearch.getText().toString();

                if(TextUtils.isEmpty(searchProduct)){
                    Toast.makeText(getContext(),"Please write a product name to search",Toast.LENGTH_SHORT).show();
                } else {
                    //searchRecyclerView.setVisibility(View.VISIBLE);
                    postRecyclerView.setVisibility(View.VISIBLE);
                    SearchForProduct(searchProduct);
                }
            }
        });



        productSearchExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchbar.setVisibility(View.INVISIBLE);
                productSearch.setText("");


                mDatabaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        PostList = new ArrayList<>();

                        for(DataSnapshot postsnap: dataSnapshot.getChildren()){

                            Post post = postsnap.getValue(Post.class);
                            PostList.add(post);

                        }


                        postAdapter = new PostAdapter(getActivity(),PostList);
                        postRecyclerView.setAdapter(postAdapter);





                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });
















        return fragmentView;

    }

    private void SearchForProduct(String searchProductSample) {
        String searchProduct = searchProductSample.toLowerCase();

        Toast.makeText(getContext(),"Searching",Toast.LENGTH_SHORT).show();
        Query SearchForProduct = mDatabaseReference.orderByChild("search").startAt(searchProduct).endAt(searchProduct+"\uf8ff");
        SearchForProduct.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                PostList = new ArrayList<>();

                for(DataSnapshot postsnap: dataSnapshot.getChildren()){

                    Post post = postsnap.getValue(Post.class);
                    PostList.add(post);

                }


                postAdapter = new PostAdapter(getActivity(),PostList);
                postRecyclerView.setAdapter(postAdapter);
                //searchRecyclerView.setAdapter(postAdapter);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    @Override
    public void onStart(){
        super.onStart();
        //postRecyclerView.setVisibility(View.VISIBLE);
        //searchRecyclerView.setVisibility(View.INVISIBLE);

        //Get List Post from database

        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                PostList = new ArrayList<>();

                for(DataSnapshot postsnap: dataSnapshot.getChildren()){

                    Post post = postsnap.getValue(Post.class);
                    PostList.add(post);

                }


                postAdapter = new PostAdapter(getActivity(),PostList);
                postRecyclerView.setAdapter(postAdapter);





            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });






    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }








}
