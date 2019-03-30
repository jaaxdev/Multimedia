package com.jaaz.muscimgvid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.support.design.widget.BottomSheetBehavior;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class MusicMain extends AppCompatActivity implements View.OnClickListener{

    ListView list_songs;
    String[] items;
    View player;
    static MediaPlayer mp;
    SeekBar seekBar;
    Button previous, backward, pause, forward, next;
    Uri uri;
    int pos;
    Thread uptSeekBar;
    TextView titulo_cancion;
    ArrayList<File> songs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_main);

        player = findViewById(R.id.player);
        list_songs = findViewById(R.id.lista_canciones);
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

        previous.setEnabled(false);
        backward.setEnabled(false);
        pause.setEnabled(false);
        forward.setEnabled(false);
        next.setEnabled(false);

        songs = findCanciones(Environment.getExternalStorageDirectory());
        asignarCanciones( songs, list_songs );



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

        list_songs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pos = position;

                updateSeekBar();
                if( mp != null ){
                    mp.stop();
                    mp.release();
                }
                previous.setEnabled(true);
                backward.setEnabled(true);
                pause.setEnabled(true);
                forward.setEnabled(true);
                next.setEnabled(true);
                pause.setText("||");

                if( bottom.getState() == BottomSheetBehavior.STATE_COLLAPSED){
                    bottom.setState( BottomSheetBehavior.STATE_EXPANDED );
                }

                if( uptSeekBar.isAlive() ){
                    uptSeekBar.stop();
                    play(pos);
                } else {
                    play(pos);
                }

            }
        });

    }

    private void asignarCanciones( ArrayList<File> canciones, ListView lista ){
        items = new String[ canciones.size() ];

        for( int i=0; i<canciones.size(); i++ ){
            items[i] = canciones.get(i).getName().replace(".mp3", "");
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                getApplicationContext(), R.layout.cancion, R.id.txt_canciones, items);

        lista.setAdapter( arrayAdapter );
    }

    private ArrayList<File> findCanciones( File root ){
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
                pos = (pos + 1) % songs.size();
                uri = Uri.parse( songs.get( pos ).toString() );
                mp = MediaPlayer.create(getApplicationContext(), uri);
                titulo_cancion.setText( songs.get(pos).getName().replace(".mp3", "") );
                mp.start();
                seekBar.setMax( mp.getDuration() );
                break;

            case R.id.btn_previous:
                //mp.stop();
                //mp.release();
                mp.pause();
                mp.release();
                pos = (pos - 1 < 0) ? songs.size() - 1 : pos - 1;

                uri = Uri.parse( songs.get( pos ).toString() );
                mp = MediaPlayer.create(getApplicationContext(), uri);
                titulo_cancion.setText( songs.get(pos).getName().replace(".mp3", "") );
                mp.start();
                seekBar.setMax( mp.getDuration() );
                break;
        }
    }

    private void updateSeekBar(  ){
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
    }

    private void play(int itemPosition){
        uri = Uri.parse( songs.get( itemPosition ).toString() );
        mp = MediaPlayer.create(getApplicationContext(), uri);
        titulo_cancion.setText( songs.get(pos).getName().replace(".mp3", "") );
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_musica, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.goto_image:
                break;

            case R.id.goto_video:
                break;

            case R.id.informacion:
                new DialogInformation().show( getSupportFragmentManager(), "info_equipo" );

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public static class DialogInformation extends DialogFragment{

        String equipo = "Montaño Ramos Areli  Sharai" +
                        "\nRebollar Calzada Jaaziel Isai" +
                        "\nSandra Janeth Briseño González";
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
            builder.setTitle( "Equipo 7" )
                    .setMessage( equipo )
                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getDialog().cancel();
                        }
                    });

            return builder.create();
        }
    }
}