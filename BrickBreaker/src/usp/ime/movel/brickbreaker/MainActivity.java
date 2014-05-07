package usp.ime.movel.brickbreaker;

import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;
import com.demo.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

public class MainActivity extends Activity {

    private GLSurfaceView glSurfaceView;
    MediaPlayer music;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        glSurfaceView = new TouchSurfaceView( this );
        setContentView( glSurfaceView );
        
        glSurfaceView.requestFocus();
        glSurfaceView.setFocusableInTouchMode( true );
        music = MediaPlayer.create(MainActivity.this, R.raw.cinderela );
        music.start();
    }


    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        music.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        music.stop();
        glSurfaceView.onPause();
    }
}