package com.jaaz.muscimgvid;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DialogFragmentGaleria extends DialogFragment {

    View view;
    TextView titulo;
    ImageView imagen;
    RecyclerView galeria;
    ArrayList<Menu> menus;
    RecyclerAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dialog_fragment_galeria, container, false);
        titulo = view.findViewById(R.id.titulo);
        imagen = view.findViewById(R.id.imagen);
        galeria = view.findViewById(R.id.galeria);

        galeria.setLayoutManager( new LinearLayoutManager( getContext(), LinearLayoutManager.HORIZONTAL, false));
        menus = new Menu().listaMenu();
        adapter = new RecyclerAdapter(menus, new RecyclerAdapter.OnClickRecycler() {
            @Override
            public void OnClickItemRecycler(Menu menu) {
                Glide.with( getContext() ).load( menu.getIdImagen() ).into(imagen);
                titulo.setText( menu.getTitulo() );
            }
        });
        galeria.setAdapter( adapter );

        return view;
    }


}
