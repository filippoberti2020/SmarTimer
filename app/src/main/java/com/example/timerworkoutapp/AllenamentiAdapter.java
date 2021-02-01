package com.example.timerworkoutapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.ContentFrameLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AllenamentiAdapter extends RecyclerView.Adapter <AllenamentiAdapter.MyViewHolder>{
    Context context;
    ArrayList<MieiAllenamenti> mieiAllenamenti;
String z;
    public  AllenamentiAdapter (Context c,ArrayList<MieiAllenamenti> p){
        context=c;
        mieiAllenamenti=p;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_does,viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.titleAllenamento.setText(this.mieiAllenamenti.get(position).getTitleAllenamento());
        Log.d("allenamento ",""+this.mieiAllenamenti.get(position).getTitleAllenamento());
        myViewHolder.dateAllenamento.setText(this.mieiAllenamenti.get(position).getDateAllenamento());
        myViewHolder.descAllenamento.setText(this.mieiAllenamenti.get(position).getDescAllenamento());
        z= myViewHolder.dateAllenamento.getText()+"";
    }

    @Override
    public int getItemCount() {
        return mieiAllenamenti.size();
    }

    class MyViewHolder extends  RecyclerView.ViewHolder{
        TextView titleAllenamento,dateAllenamento,descAllenamento;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            titleAllenamento=(TextView)itemView.findViewById(R.id.titledoes);
            dateAllenamento=(TextView)itemView.findViewById(R.id.datedoes);
            descAllenamento=(TextView)itemView.findViewById(R.id.descdoes);

        }

    }
    public String getData()
    {
        String s;
        s=z.toString();
        return s;
    }
    void deleteItem(int index) {

        mieiAllenamenti.remove(index);
        notifyItemRemoved(index);


    }


}
