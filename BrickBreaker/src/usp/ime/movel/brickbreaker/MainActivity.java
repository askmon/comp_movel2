package usp.ime.movel.brickbreaker;

import usp.ime.movel.brickbreaker.game.BallEntity;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

import com.demo.R;

import android.app.Activity;
import android.media.MediaPlayer;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

    private TouchSurfaceView glSurfaceView;
    private MediaPlayer music;
    private int last_music_pos;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );

        glSurfaceView = new TouchSurfaceView( this );
        setContentView( glSurfaceView );
        
        glSurfaceView.requestFocus();
        glSurfaceView.setFocusableInTouchMode(true);
        glSurfaceView.addEntity(new BallEntity());
        
        last_music_pos = 0;
    }


    @Override
    protected void onResume() {
        super.onResume();
        glSurfaceView.onResume();
        music = MediaPlayer.create(MainActivity.this, R.raw.cinderela );
        if (last_music_pos > 0) {
        	music.seekTo(last_music_pos);
        	Log.i("Music seeking back to ", ""+last_music_pos);
        }
        music.start();
    }


    @Override
    protected void onPause() {
        super.onPause();
        last_music_pos = music.getCurrentPosition();
        music.stop();
        music.release();
        music = null;
        glSurfaceView.onPause();
    }
}