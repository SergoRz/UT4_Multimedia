package com.pmdm.islasfilipinas.ut4_cabaemilio_sanchezsergio_multimedia;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;


public class MainActivity extends Activity {

    private VideoView videoView;
    private MediaController mediaController;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                videoView.start();
            }
        });



        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                TextView t = (TextView) findViewById(R.id.textView);
                mediaPlayer.release();
                t.setText("La reproducción ha terminado, acabo de hacer un release()");
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
        TextView t = (TextView) findViewById(R.id.textView);
        if (mediaPlayer.isPlaying()){
            t.setText("Ya estás escuchando música, ¿qué más quieres chaval?");
        }
        else {
            mediaPlayer.start();
            t.setText("Tu MP está parado, tranqui que le hago un start()");
        }
    }

    public void stop(View view) throws IOException {
        TextView t = (TextView) findViewById(R.id.textView);
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();

            try {
                mediaPlayer.prepare();
                t.setText("La música estaba sonando pero acabas de hacer un stop() y un prepare() a tu MP");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        else {
            t.setText("La música no suena, el MP está parado, ¿por qué haces un stop()?");
        }
    }

    public void pause(View view){
        TextView t = (TextView) findViewById(R.id.textView);
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            t.setText("Acabas de pausar tu MP");
        }
        else {
            t.setText("Tu MP no está en ejecución, luego no lo puedes pausar");
        }
    }
}

