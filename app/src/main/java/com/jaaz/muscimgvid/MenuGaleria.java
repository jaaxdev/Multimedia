package com.jaaz.muscimgvid;

import android.os.Environment;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Menu {

    ArrayList<File> imagenesSD;

    private int idImagen;
    private String titulo;


    public Menu(){
        idImagen = 0;
        titulo = "";
    }

    public Menu( int id, String title ){
        this.idImagen = id;
        this.titulo = title;
    }


    public int getIdImagen(){
        return idImagen;
    }

    public String getTitulo(){
        return titulo;
    }

    public ArrayList<Menu> listaMenu(){

        //imagenesSD = findImagenes(Environment.getExternalStorageDirectory());
        Menu menu;
        ArrayList<Menu> menus = new ArrayList<>();
        Integer[] idImagenes = new Integer[]{R.drawable.ic_launcher, R.drawable.image_icon, R.drawable.video_icon};
        String[] titulos = new String[]{ "launcher", "img", "video" };

        for( int i=0; i<idImagenes.length; i++ ){
            menu = new Menu( idImagenes[i], titulos[i] );
            menus.add( menu );
        }


        return menus;
    }

    private String[] asignarTitulos( ArrayList<File> imgs ){

        String[] titulos = new String[ imgs.size() ];

        for( int i=0; i<imgs.size(); i++ ){
            titulos[i] = imgs.get(i).getName().replace( ".png", "" );
        }

        return titulos;
    }

    private Integer[] asignarID( ArrayList<File> imgs ){

        Integer[] ID = new Integer[ imgs.size() ];
        try{
            for(int i=0; i<imgs.size(); i++){
                ID[i] = Integer.parseInt( imgs.get(i).getName() );
            }
        } catch (NumberFormatException nfe){
            nfe.printStackTrace();
        }
        return ID;
    }

    private ArrayList<File> findImagenes(File root){
        ArrayList<File> al = new ArrayList<>();
        File[] files = root.listFiles();

        for( File singleFile: files ){
            if( singleFile.isDirectory() && !singleFile.isHidden() ){
                al.addAll( findImagenes(singleFile) );
            } else {
                if( singleFile.getName().endsWith(".png") ){
                    al.add( singleFile );
                }
            }
        }

        return al;
    }
}
