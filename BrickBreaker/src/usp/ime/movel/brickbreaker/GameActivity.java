package usp.ime.movel.brickbreaker;

import usp.ime.movel.brickbreaker.game.BallEntity;
import usp.ime.movel.brickbreaker.game.BatEntity;
import usp.ime.movel.brickbreaker.game.BrickEntity;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class GameActivity extends Activity {

	private TouchSurfaceView glSurfaceView;
	private MediaPlayer music;
	private int last_music_pos;
	private BroadcastReceiver event_receiver;
	private TextView score;
	private int points;

	public final static String DEFEAT_EVENT = "usp.ime.movel.brickbreaker.defeat_event";
	public static final String WIN_EVENT = "usp.ime.movel.brickbreaker.win_event";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        setContentView(R.layout.game);
		glSurfaceView = (TouchSurfaceView)findViewById(R.id.gamescreen);
		score = (TextView)findViewById(R.id.score);
		score.setText("Score: " + points);
		//setContentView(glSurfaceView);

		glSurfaceView.setFocusableInTouchMode(true);
		glSurfaceView.requestFocus();
		glSurfaceView.addEntity(new BallEntity());
		glSurfaceView.addEntity(new BatEntity());
		BrickEntity.resetCount();
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 10; j++)
				glSurfaceView.addEntity(new BrickEntity(-0.45f + j / 10.0f,
						0.2f + i / 10.0f));

		last_music_pos = 0;
		event_receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				GameActivity.this.finish();
			}
		};
	}

	@Override
	protected void onResume() {
		super.onResume();
		glSurfaceView.onResume();
		music = MediaPlayer.create(GameActivity.this, R.raw.cinderela);
		if (last_music_pos > 0) {
			music.seekTo(last_music_pos);
			Log.i("Music seeking back to ", "" + last_music_pos);
		}
		music.setVolume(0.3f, 0.3f);
		music.setLooping(true);
		music.start();
		registerReceiver(event_receiver, new IntentFilter(DEFEAT_EVENT));
		registerReceiver(event_receiver, new IntentFilter(WIN_EVENT));
	}

	@Override
	protected void onPause() {
		super.onPause();
		last_music_pos = music.getCurrentPosition();
		music.stop();
		music.release();
		music = null;
		glSurfaceView.onPause();
		unregisterReceiver(event_receiver);
	}
	
	public void setScore(int points){
		this.score.setText("Score: " + points);
	}
}