package com.ayon.austmart.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ayon.austmart.Adapters.PostAdapter;
import com.ayon.austmart.Adapters.UserAdapter;
import com.ayon.austmart.Models.Chat;
import com.ayon.austmart.Models.Post;
import com.ayon.austmart.Models.User;
import com.ayon.austmart.Notifications.Token;
import com.ayon.austmart.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InboxFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView mRecyclerView;
    private UserAdapter mUserAdapter;
    private List<User>users;
    private List<User>dummyusers;
    private List<User>dummyusers2;


    FirebaseUser currentUser;
    DatabaseReference mReference;

    private List<String>userList;
    private List<String>userListRev;




    private OnFragmentInteractionListener mListener;

    public InboxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
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

        LinearLayoutManager lin = new LinearLayoutManager(getActivity());
        lin.setStackFromEnd(true);
        lin.setReverseLayout(true);

       View view = inflater.inflate(R.layout.fragment_inbox, container, false);


       mRecyclerView = view.findViewById(R.id.recycler_view_inbox);

        mRecyclerView.setLayoutManager(lin);
       mRecyclerView.setHasFixedSize(true);










        currentUser = FirebaseAuth.getInstance().getCurrentUser();

       userList = new ArrayList<>();
        userListRev = new ArrayList<>();

       mReference = FirebaseDatabase.getInstance().getReference("Chats");

       mReference.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               userList.clear();
               userListRev.clear();

               for(DataSnapshot snapshot  : dataSnapshot.getChildren()){
                   Chat chat = snapshot.getValue(Chat.class);


                       if (chat.getSender().equals(currentUser.getUid())) {

                               userList.add(chat.getReceiver());
                       }


                       if (chat.getReceiver().equals(currentUser.getUid())) {

                               userList.add(chat.getSender());
                       }

               }
              Collections.reverse(userList);

               for(String ID : userList)
               {
                   if(!userListRev.contains(ID))
                       userListRev.add(ID);
               }


               readChats();

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });

        updateToken(FirebaseInstanceId.getInstance().getToken());

        return view;
    }


    private void updateToken(String token)
    {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Tokens");

        Token token1 = new Token(token);
        reference.child(currentUser.getUid()).setValue(token1);

    }



    private void readChats()
    {

        users= new ArrayList<>();
        dummyusers = new ArrayList<>();
        dummyusers2 = new ArrayList<>();


        mReference = FirebaseDatabase.getInstance().getReference("Users");




        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                users.clear();



                for(String id : userListRev){
                     int counter =0;


                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        User muser = snapshot.getValue(User.class);



                        if(muser.getUserId().equals(id)){
                            users.add(muser);

                        }


                    }


                }





                Collections.reverse(users);
                mUserAdapter = new UserAdapter(getContext(),users,true);
                mRecyclerView.setAdapter(mUserAdapter);




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
