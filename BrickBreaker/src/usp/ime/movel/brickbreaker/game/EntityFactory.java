package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.graphics.Sprite;

public class EntityFactory {
	
	public Entity makeBall (int id) {
		Sprite ball_sprite = new Sprite(id);
		ball_sprite.setShape(0.02f, 0.02f);
		return new Entity(ball_sprite);
	}
	
	public Entity makeBackground (int id) {
		Sprite background = new Sprite(id);
		background.setShape(1f, 1f);
		return new Entity(background);
	}

}
