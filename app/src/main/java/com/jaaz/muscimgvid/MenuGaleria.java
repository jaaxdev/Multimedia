package com.jaaz.muscimgvid;

import android.util.Log;

import java.io.File;
import java.util.ArrayList;

public class MenuGaleria {

    ArrayList<File> imagenesSD;

    private int idImagen;
    private String titulo;


    public MenuGaleria(){
        idImagen = 0;
        titulo = "";
    }

    public MenuGaleria(int id, String title ){
        this.idImagen = id;
        this.titulo = title;
    }


    public int getIdImagen(){
        return idImagen;
    }

    public String getTitulo(){
        return titulo;
    }

    public ArrayList<MenuGaleria> listaMenu(){

        //imagenesSD = findImagenes(Environment.getExternalStorageDirectory());
        MenuGaleria menu;
        ArrayList<MenuGaleria> menus = new ArrayList<>();
        Integer[] idImagenes = new Integer[]{
                R.drawable._1,
                R.drawable._2,
                R.drawable._3,
                R.drawable._4,
                R.drawable._5,
                R.drawable._6,
                R.drawable._7,
                R.drawable._8,
                R.drawable._9,
                R.drawable._10,
                R.drawable._11,
                R.drawable._12,
                R.drawable._13,
                R.drawable._14,
                R.drawable._15,
                R.drawable._16,
        };



        //String[] titulos = new String[]{ "launcher", "img", "video" };
        String[] titulos = getStringTitles();

        for( int i=0; i<idImagenes.length; i++ ){
            menu = new MenuGaleria( idImagenes[i], titulos[i] );
            menus.add( menu );
        }

        return menus;
    }

    private Integer[] getDrawableImgs(){
        Integer[] img = new Integer[16];
        try{
            for( int i=0; i<16; i++ ){
                img[i] = Integer.parseInt( "R.drawable._"+(i+1) );
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }

        return img;
    }

    private String[] getStringTitles(){
        String[] titulos = new String[16];

        for( int i=0; i<16; i++ ){
            //titulos[i] = "R.string._"+(i+1);
            titulos[i] = "imagen"+(i+1);
        }

        return titulos;
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
