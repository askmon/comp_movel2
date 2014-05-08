package usp.ime.movel.brickbreaker.game;

import java.util.Calendar;
import java.util.Random;

import com.demo.R;

import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;

public class BallEntity extends Entity {

	public BallEntity() {
		super(new Sprite(new Geometry(0.0f, 0.0f, 0.02f, 0.02f),
				R.drawable.pikachu));
	}

	@Override
	public void onUpdate() {
		Random rng = new Random();
		rng.setSeed(Calendar.getInstance().getTimeInMillis());
		float dx = rng.nextFloat() / 100.0f - 0.005f;
		float dy = rng.nextFloat() / 100.0f - 0.005f;
		getSprite().move(dx, dy);
	}

}
