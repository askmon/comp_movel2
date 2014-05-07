package usp.ime.movel.brickbreaker.game;

import java.util.Calendar;
import java.util.Random;

import usp.ime.movel.brickbreaker.graphics.Sprite;

public class Entity {

	private Sprite sprite;

	public Entity(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public void update() {
		Random rng = new Random();
		rng.setSeed(Calendar.getInstance().getTimeInMillis());
		float dx = rng.nextFloat()/100.0f - 0.005f;
		float dy = rng.nextFloat()/100.0f - 0.005f;
		this.sprite.move(dx, dy);
	}
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
}
