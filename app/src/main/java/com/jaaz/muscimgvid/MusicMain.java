package com.jaaz.muscimgvid;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.design.widget.BottomSheetBehavior;

import java.io.File;
import java.util.ArrayList;

public class MusicMain extends AppCompatActivity {

    ListView list_song;
    String[] items;
    View player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);
        player = findViewById(R.id.player);

        final BottomSheetBehavior bottom = BottomSheetBehavior.from( player );

        bottom.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                Log.i( "BottomSheetCallback", "slideOffSet: "+v );

            }
        });

        list_song = findViewById(R.id.lista_canciones);

        final ArrayList<File> canciones = findCanciones(Environment.getExternalStorageDirectory());

        items = new String[ canciones.size() ];

        for( int i=0; i<canciones.size(); i++ ){
            //toastCancion( canciones.get(i).getName() );
            items[i] = canciones.get(i).getName().replace(".mp3", "");

        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.cancion, R.id.txt_canciones, items);

        list_song.setAdapter( arrayAdapter );



        list_song.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //startActivity( new Intent(getApplicationContext(), MusicPlayer.class)
                  //      .putExtra("pos", position).putExtra("lista_canciones", canciones) );

                if( bottom.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottom.setState( BottomSheetBehavior.STATE_EXPANDED );
                }
            }
        });

    }

    public ArrayList<File> findCanciones( File root ){
        ArrayList<File> al = new ArrayList<>();
        File[] files = root.listFiles();

        for( File singleFile: files ){
            if( singleFile.isDirectory() && !singleFile.isHidden() ){
                al.addAll( findCanciones(singleFile) );
            } else {
                if( singleFile.getName().endsWith(".mp3") && !singleFile.getName().contains("AUD-")){
                    al.add( singleFile );
                }
            }
        }

        return al;
    }
}