package com.jaaz.muscimgvid;

import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.File;
import java.util.ArrayList;

public class VideoMain extends AppCompatActivity {

    VideoView videoView;
    ListView listaVideos;
    MediaController mc;
    ArrayList<File> videos;
    String[] items;
    String[] videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_main);

        videoView = findViewById(R.id.video);
        listaVideos = findViewById(R.id.lista_videos);

        mc = new MediaController(this);

        videos = findVideos(Environment.getExternalStorageDirectory());
        asignarVideos( videos, listaVideos );


        listaVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoPlay( position );
            }
        });

    }

    private ArrayList<File> findVideos( File root ){
        ArrayList<File> al = new ArrayList<>();
        File[] files = root.listFiles();

        for( File singleFile: files ){
            if( singleFile.isDirectory() && !singleFile.isHidden() ){
                al.addAll( findVideos(singleFile) );
            } else {
                if( singleFile.getName().endsWith(".mp4") ){
                    al.add( singleFile );
                }
            }
        }
        return al;
    }

    private void asignarVideos( ArrayList<File> videos, ListView listaVideos ){
        items = new String[ videos.size() ];
        videoPath = new String[ videos.size() ];

        for( int i=0; i<videos.size(); i++ ){
            items[i] = videos.get(i).getName();
            videoPath[i] = videos.get(i).getAbsolutePath();
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.cancion, R.id.txt_canciones, items);

        listaVideos.setAdapter( arrayAdapter );
    }

    private void videoPlay( int idPath ){
        //Uri uri = Uri.parse( path );

        //videoView.setVideoURI(uri);
        videoView.setVideoPath( videoPath[idPath] );
        videoView.setMediaController(mc);
        mc.setAnchorView(videoView);
        videoView.start();
    }

    @Override
    public void onBackPressed() {
        this.finish();
    }
}