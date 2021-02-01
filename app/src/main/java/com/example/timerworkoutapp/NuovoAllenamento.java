package com.example.timerworkoutapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Random;

public class NuovoAllenamento extends AppCompatActivity {


    EditText  titleallenamento,descrizioneallenamento,dataallenamento;
    Button btnAggiungi,btnCancel;
    DatabaseReference reference;
    Integer doesNum=new Random().nextInt();//random id
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuovo_allenamento);

        titleallenamento=findViewById(R.id.titleallenamento);
        descrizioneallenamento =findViewById(R.id.descrizioneallenamento);
        dataallenamento=findViewById(R.id.dataallenamento);
        btnAggiungi=(Button)findViewById(R.id.btnSalvaAllenamento);
        btnCancel=(Button)findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(NuovoAllenamento.this,MainActivity.class);
                startActivity(a);
                finish();
            }
        });
        btnAggiungi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference= FirebaseDatabase.getInstance().getReference().child("WorkoutApp").child("Allenamento"+doesNum);
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        dataSnapshot.getRef().child("titleAllenamento").setValue(titleallenamento.getText().toString());
                        dataSnapshot.getRef().child("descAllenamento").setValue(descrizioneallenamento.getText().toString());
                        dataSnapshot.getRef().child("dateAllenamento").setValue(dataallenamento.getText().toString());



                        Intent a = new Intent(NuovoAllenamento.this,MainActivity.class);
                        startActivity(a);



                        finish();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
}