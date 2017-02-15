package com.pmdm.islasfilipinas.ut4_cabaemilio_sanchezsergio_multimedia;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import java.io.IOException;

public class MainActivity extends Activity {

    private VideoView videoView;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    private TextView t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        t = (TextView) findViewById(R.id.tvEstadoAudio);
        // Obtenemos la referencia al widget VideoView
        videoView = (VideoView) findViewById(R.id.videoView);

        // Creamos el objeto MediaController
        mediaController = new MediaController(this);

        // Establecemos el ancho del MediaController
        mediaController.setAnchorView(videoView);

        mediaPlayer = MediaPlayer.create(this, R.raw.plata_o_plomo);

        // Al contenedor VideoView le añadimos los controles
        videoView.setMediaController(mediaController);
        // Cargamos el contenido multimedia (el vídeo) en el VideoView

        //videoView.setVideoURI(Uri.parse("http://desprogresiva.antena3.com/mp_seriesh4/2013/02/22/00029/001.mp4"));
        //videoView.setVideoURI(Uri.parse("http://www.ebookfrenzy.com/android_book/movie.mp4"));
        videoView.setVideoURI(Uri.parse("https://www.w3schools.com/html/mov_bbb.mp4"));

        // Registramos el callback que será invocado cuando el vídeo esté cargado y
        // preparado para la reproducción
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                Toast.makeText(MainActivity.this, "El video ya se ha cargado", Toast.LENGTH_SHORT).show();
                videoView.pause();
            }
        });

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.release();
                t.setText("FINALIZADO");
            }
        });
    }

    // Programamos el método onTouchEvent(), para que se muestre el MediaControl
    // cuando el usuario pulse en la pantalla
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mediaController.show();
        return false;
    }

    public void play(View view){
        if (mediaPlayer.isPlaying()){
            Toast.makeText(MainActivity.this, "Ya estás escuchando música, ¿qué más quieres chaval?", Toast.LENGTH_SHORT).show();
        }
        else {
            mediaPlayer.start();
            t.setText("PLAY");
        }
    }

    public void stop(View view) throws IOException {
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
                t.setText("STOP");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(MainActivity.this, "La música no suena, ¿por qué quieres pararla?", Toast.LENGTH_SHORT).show();
        }
    }

    public void pause(View view){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            t.setText("PAUSA");
        }
        else {
            Toast.makeText(MainActivity.this, "La musica ya esta en pausa, no se puede parar lo parado", Toast.LENGTH_SHORT).show();
        }
    }
}

