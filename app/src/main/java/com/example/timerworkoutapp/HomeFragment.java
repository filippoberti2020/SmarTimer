package com.example.timerworkoutapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
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
    private  View rootView;
    TextView titoloHome,sottotitoloHome,finePagina;

    DatabaseReference reference;
    RecyclerView recyclerViewMieiAllen;
    ArrayList<MieiAllenamenti> list;
    AllenamentiAdapter mieiAllenamentiAdapter;
    Button btnAddNew;
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
    public View onCreateView(LayoutInflater inflater,@NonNull ViewGroup container,
                             @NonNull Bundle savedInstanceState) {



        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        titoloHome = (TextView) rootView.findViewById(R.id.titoloHome);
        sottotitoloHome = (TextView) rootView.findViewById(R.id.sottotitoloHome);
        finePagina = (TextView) rootView.findViewById(R.id.finePagina);
        btnAddNew = (Button) rootView.findViewById(R.id.btnAddNew);

        //lavoro con i dati
        recyclerViewMieiAllen=rootView.findViewById(R.id.mieiallenamentiRecyclerView);
        recyclerViewMieiAllen.setLayoutManager(new LinearLayoutManager(rootView.getContext()));
        list=new ArrayList<MieiAllenamenti>();


        ItemTouchHelper itemTouchHelper=new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewMieiAllen);

        //prendo i dati dal database
        reference= FirebaseDatabase.getInstance().getReference().child("WorkoutApp");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange( DataSnapshot dataSnapshot) {
                // set code to retrive data and replace layout
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    MieiAllenamenti p = dataSnapshot1.getValue(MieiAllenamenti.class);
                    Log.d("uz",""+dataSnapshot1.getValue(MieiAllenamenti.class));
                    Log.d("uz",""+p.titleAllenamento);
                    list.add(p);
                }
                Log.d("LISTA",""+list.toString());
                Context contexts = getActivity().getApplicationContext();
                mieiAllenamentiAdapter = new AllenamentiAdapter(contexts, list);
                recyclerViewMieiAllen.setAdapter(mieiAllenamentiAdapter);
                Log.d("","SET ADAPTER");
                Log.d("",mieiAllenamentiAdapter.getItemCount()+"");
                mieiAllenamentiAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getActivity(), "non ci sono dati",
                        Toast.LENGTH_LONG).show();
            }
        });



        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
                Intent a =new Intent(getActivity(),NuovoAllenamento.class);
                startActivity(a);

            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }
ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT |
        ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
           int position=viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:
                    reference.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                        }
                    });
                    mieiAllenamentiAdapter.notifyItemRemoved(position);
                    break;
                case ItemTouchHelper.RIGHT:

                    break;

            }
        }
    };
}