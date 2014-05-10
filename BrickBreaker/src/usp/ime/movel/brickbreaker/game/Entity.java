package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public abstract class Entity {

	private Sprite sprite;

	public Entity(Sprite sprite) {
		this.sprite = sprite;
	}
	
	public abstract void onGameAdd(TouchSurfaceView view);
	
	public void onGameRemove(TouchSurfaceView view) {
		
	}
	
	public abstract void onUpdate(TouchSurfaceView view);
	
	public Sprite getSprite() {
		return this.sprite;
	}
	
}
