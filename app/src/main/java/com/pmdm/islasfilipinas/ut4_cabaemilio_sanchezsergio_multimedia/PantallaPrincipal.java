package com.pmdm.islasfilipinas.ut4_cabaemilio_sanchezsergio_multimedia;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.IOException;

/**
 * Clase principal de la aplicacion.
 * Muestra un espacio dedicado a la reproduccion de video y otro a la reproduccion de
 * audio.
 */
public class PantallaPrincipal extends Activity {

    private VideoView videoView; //VideoView para reproducir el video
    private MediaController mediaController; //MediaController para controlar la reproduccion de video
    private MediaPlayer mediaPlayer; //MediaPlayer para reproducir el audio
    private TextView tvEstadoAudio; //TextView que muestra el estado del audio
    private AsyncVideo asynVideo; //Clase que se encarga de cargar el video
    private SeekBar seekBar; //Barra de progreso del audio

    /**
     * Metodo que se ejecuta al iniciar la Activity.
     * Se encarga de cargar el layout "layout_principal", ademas relaciona los objetos de la interfaz con los
     * de la clase.
     * Ejecuta la clase asynVideo que extiende de AsynTask para que la carga de video se realice en segundo
     * plano.
     * Inicia el MediaPlayer para la reproduccion del audio mediante el metodo cargarMusica().
     * Inicia el hilo que actualiza la posicion de la barra de progreso del audio.
     * @param savedInstanceState Estado de la aplicacion.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_principal);

        // Obtenemos las referencias de los objetos de la pantalla
        tvEstadoAudio = (TextView) findViewById(R.id.tvEstadoAudio);
        videoView = (VideoView) findViewById(R.id.videoView);
        seekBar = (SeekBar) findViewById(R.id.seekBar);

        //Se instancia y ejecuta la clase asynVideo que carga el video
        asynVideo = new AsyncVideo();
        asynVideo.execute();

        cargarMusica(); //Se carga el audio

        //Hilo que se encarga de actualizar la barra de progreso del audio
        final Handler mHandler = new Handler();
        PantallaPrincipal.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try{
                    if(mediaPlayer != null){ //Si existe el mediaPlayer..
                        //Se actualiza la posicion del audio
                        int mCurrentPosition = mediaPlayer.getCurrentPosition();
                        seekBar.setProgress(mCurrentPosition);
                    }
                    //Pone en cola el hilo para ejecutarlo cada segundo
                    mHandler.postDelayed(this, 1000);
                } catch(IllegalStateException e) { }
            }
        });

        /**
         * Listener de la barra de progreso
         */
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}

            /**
             * Metodo que se ejecuta cuando cambia la posicion de la barra de progreso
             * @param seekBar barra de progreso del audio
             * @param progress Progreso del audio
             * @param fromUser True si el progreso de cambio ha sido iniciado por el usuario
             */
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Si existe el mediaPlayer y el progreso de cambio ha sido iniciado por el usuario..
                if(mediaPlayer != null && fromUser){
                    mediaPlayer.seekTo(progress); //Cambia la barra de progreso a la nueva posicion
                }
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


    private void cargarMusica(){
        mediaPlayer = MediaPlayer.create(this, R.raw.plata_o_plomo);

        seekBar.setMax(mediaPlayer.getDuration());

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp){
                try {
                    mediaPlayer.stop();
                    mediaPlayer.prepare();
                    mediaPlayer.seekTo(0);
                    tvEstadoAudio.setText("FINALIZADO");
                    seekBar.setProgress(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void play(View view){
        if (mediaPlayer.isPlaying()){
            Toast.makeText(PantallaPrincipal.this, "Ya estás escuchando música, ¿qué más quieres chaval?", Toast.LENGTH_SHORT).show();
        }
        else {
            mediaPlayer.start();
            tvEstadoAudio.setText("PLAY");
        }
    }

    public void stop(View view) throws IOException {
        if (mediaPlayer!=null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();
                mediaPlayer.seekTo(0);
                tvEstadoAudio.setText("STOP");
                seekBar.setProgress(0);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(PantallaPrincipal.this, "La música no suena, ¿por qué quieres pararla?", Toast.LENGTH_SHORT).show();
            tvEstadoAudio.setText("STOP");
            mediaPlayer.seekTo(0);
            seekBar.setProgress(0);
        }
    }

    public void pause(View view){
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            tvEstadoAudio.setText("PAUSA");
        }
        else {
            Toast.makeText(PantallaPrincipal.this, "La musica ya esta en pausa, no se puede parar lo parado", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Clase privada que extiende de AsyncTask para que su ejecucion se realice en segundo plano.
     * Se encarga de cargar el video en segundo plano.
     */
    private class AsyncVideo extends AsyncTask<String, Void, Void> {
        /**
         * Metodo que se ejecuta en segundo plano, se encarga de llamar al metodo cargarVideo() que
         * carga el video del enlace para mostrarlo.
         * @param params Parametros, en este caso no recibe ninguno
         * @return Devuelve null
         */
        @Override
        protected Void doInBackground(String... params) {
            cargarVideo();
            return null;
        }

        /**
         * Metodo que se encarga de crear el objeto MediaPlayer para la reproduccion del video,
         * tambien se configura el ancho del MediaPlayer, se aplica el controlador al MediaPlayer
         * y se establece la direccion del video.
         * Ademas hay un listener que se ejecuta cuando el video esta preparado para reproducirse.
         */
        public void cargarVideo(){
            //Se crea el controlador del video
            mediaController = new MediaController(PantallaPrincipal.this);

            //Establecemos el ancho del controlador
            mediaController.setAnchorView(videoView);

            //Se añade el controlador al VideoView
            videoView.setMediaController(mediaController);
            // Cargamos el contenido multimedia (el vídeo) en el VideoView

            //OTROS VIDEOS
            //videoView.setVideoURI(Uri.parse("http://desprogresiva.antena3.com/mp_seriesh4/2013/02/22/00029/001.mp4"));
            //videoView.setVideoURI(Uri.parse("http://www.ebookfrenzy.com/android_book/movie.mp4"));
            videoView.setVideoURI(Uri.parse("https://www.w3schools.com/html/mov_bbb.mp4"));

            //Listener que será invocado cuando el vídeo esté cargado y preparado para la reproducción
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    //Se indica que el video esta cargado
                    Toast.makeText(PantallaPrincipal.this, "El video ya se ha cargado", Toast.LENGTH_SHORT).show();
                    videoView.pause(); //Ponemos en pausa el video para que no se inicie automaticamente al cargar
                }
            });
        }
    }
}

