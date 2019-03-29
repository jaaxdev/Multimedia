package com.jaaz.muscimgvid;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MusicPlayer extends AppCompatActivity implements View.OnClickListener{

    static MediaPlayer mp;
    SeekBar seekBar;
    Button previous, backward, pause, forward, next;
    ArrayList<File> canciones;
    Uri uri;
    int pos;
    Thread uptSeekBar;
    TextView titulo_cancion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        previous = findViewById(R.id.btn_previous);
        backward = findViewById(R.id.btn_backward);
        pause = findViewById(R.id.btn_pause);
        forward = findViewById(R.id.btn_forward);
        next = findViewById(R.id.btn_next);
        seekBar = findViewById(R.id.seek_bar);
        titulo_cancion = findViewById(R.id.titulo);

        previous.setOnClickListener(this);
        backward.setOnClickListener(this);
        pause.setOnClickListener(this);
        forward.setOnClickListener(this);
        next.setOnClickListener(this);

        uptSeekBar = new Thread(){
            @Override
            public void run() {
                int duracionTotal = mp.getDuration();
                int posActual = 0;

                while( posActual < duracionTotal ){
                    try{
                        sleep(500);

                        posActual = mp.getCurrentPosition();
                        seekBar.setProgress( posActual );

                    } catch (InterruptedException ie ){
                        ie.printStackTrace();
                    } catch (IllegalStateException ile){
                        ile.printStackTrace();
                    }
                }
            }
        };

        if( mp != null ){
            mp.stop();
            mp.release();
        }

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        canciones = (ArrayList) bundle.getParcelableArrayList("lista_canciones");
        pos = bundle.getInt("pos", 0);

        uri = Uri.parse( canciones.get( pos ).toString() );
        mp = MediaPlayer.create(getApplicationContext(), uri);
        titulo_cancion.setText( canciones.get(pos).getName().replace(".mp3", "") );
        mp.start();
        seekBar.setMax( mp.getDuration() );

        uptSeekBar.start();

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mp.seekTo(seekBar.getProgress());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id){
            case R.id.btn_pause:
                if( mp.isPlaying() ){
                    pause.setText(">");
                    mp.pause();
                } else {
                    pause.setText("||");
                    mp.start();
                }
                break;

            case R.id.btn_forward:
                mp.seekTo( mp.getCurrentPosition() + 5000 );
                break;

            case R.id.btn_backward:
                mp.seekTo( mp.getCurrentPosition() - 5000 );
                break;

            case R.id.btn_next:
                mp.pause();
                mp.release();
                pos = (pos + 1) % canciones.size();
                uri = Uri.parse( canciones.get( pos ).toString() );
                mp = MediaPlayer.create(getApplicationContext(), uri);
                titulo_cancion.setText( canciones.get(pos).getName().replace(".mp3", "") );
                mp.start();
                seekBar.setMax( mp.getDuration() );
                break;

            case R.id.btn_previous:
                //mp.stop();
                //mp.release();
                mp.pause();
                mp.release();
                pos = (pos - 1 < 0) ? canciones.size() - 1 : pos - 1;

                uri = Uri.parse( canciones.get( pos ).toString() );
                mp = MediaPlayer.create(getApplicationContext(), uri);
                titulo_cancion.setText( canciones.get(pos).getName().replace(".mp3", "") );
                mp.start();
                seekBar.setMax( mp.getDuration() );
                break;
        }
    }
}
