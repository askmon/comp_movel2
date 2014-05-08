package usp.ime.movel.brickbreaker.game;

import java.util.Calendar;
import java.util.Random;

import usp.ime.movel.brickbreaker.graphics.Sprite;

public abstract class Entity {

	private Sprite sprite;

	public Entity(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public abstract void onUpdate();
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
}
