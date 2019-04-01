package com.jaaz.muscimgvid;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>{
    private ArrayList<Menu> listaMenu;
    private OnClickRecycler listener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from( viewGroup.getContext()).inflate( R.layout.adaptador, viewGroup, false );

        return new MyViewHolder( view );
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        Menu menu = listaMenu.get( i );
        myViewHolder.bind( menu, listener );
    }

    @Override
    public int getItemCount() {
        return listaMenu.size();
    }

    public interface OnClickRecycler{

        void OnClickItemRecycler( Menu menu );
    }

    RecyclerAdapter( ArrayList<Menu> menus, OnClickRecycler onCR ){
        this.listaMenu = menus;
        this.listener = onCR;
    }


    static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imagengaleria);
        }

        void bind( final Menu menu, final OnClickRecycler recycler ){

            Glide.with( imageView.getContext() ).load( menu.getIdImagen()).into( imageView );

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recycler.OnClickItemRecycler( menu );
                }
            });

        }
    }
}
