package usp.ime.movel.brickbreaker;

import java.util.Random;

import usp.ime.movel.brickbreaker.game.BallEntity;
import usp.ime.movel.brickbreaker.game.BatEntity;
import usp.ime.movel.brickbreaker.game.BrickEntity;
import usp.ime.movel.brickbreaker.game.SpawnerEntity;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;
import usp.ime.movel.brickbreaker.model.SQLiteHelper;
import usp.ime.movel.brickbreaker.model.Score;
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
	private TextView highscore;
	private int points;
	private int level = 1;
	private SQLiteHelper db;


	public final static String DEFEAT_EVENT = "usp.ime.movel.brickbreaker.defeat_event";
	public static final String WIN_EVENT = "usp.ime.movel.brickbreaker.win_event";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game);
		glSurfaceView = (TouchSurfaceView) findViewById(R.id.gamescreen);
		score = (TextView) findViewById(R.id.score);
		highscore = (TextView) findViewById(R.id.highscore);
		highscore.setText("Score: " + points);
		db = new SQLiteHelper(this);
		highscore.setText("Highscore: " + db.getHigherScore());
		
		// setContentView(glSurfaceView);

		glSurfaceView.setFocusableInTouchMode(true);
		glSurfaceView.requestFocus();
		glSurfaceView.addEntity(new BallEntity());
		glSurfaceView.addEntity(new BatEntity());
		BrickEntity.resetCount();

		createBricks(level);

		last_music_pos = 0;
		event_receiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if(intent.getAction().equals(WIN_EVENT)){
					glSurfaceView.resetBall();
					level++;
					createBricks(level);
				}
				else{
					Score newScore = new Score();
					db = new SQLiteHelper(context);
					newScore.setScore(points);
					db.addScore(newScore);
					db.close();
					level = 1;
					GameActivity.this.finish();
				}
			}
		};
	}

	private void createBricks(int level) {
		Random rand = new Random(); 
		for (int i = 0; i < 5; i++)
			for (int j = 0; j < 10; j++) {
				BrickEntity brick;
				double brickType = ((float)level/20.0f) + rand.nextDouble();
				if (brickType < 0.7)
					brick = BrickEntity.makeZombie(-0.45f + j / 10.0f,
							0.2f + i / 10.0f);
				else if (brickType >= 0.7 && brickType <= 0.9)
					brick = BrickEntity.makeWitch(-0.45f + j / 10.0f,
							0.2f + i / 10.0f);
				else
					brick = BrickEntity.makeTank(-0.45f + j / 10.0f,
							0.2f + i / 10.0f);
				glSurfaceView.addEntity(brick);
			}
		glSurfaceView.addEntity(new SpawnerEntity());
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

	public void setScore(int points) {
		this.score.setText("Score: " + points);
	}
}
