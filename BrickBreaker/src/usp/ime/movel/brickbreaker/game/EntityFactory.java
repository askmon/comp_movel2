package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.graphics.Sprite;

public class EntityFactory {
	
	public Entity makeBall () {
		Sprite ball_sprite = new Sprite();
		ball_sprite.setShape(0.02f, 0.02f);
		return new Entity(ball_sprite);
	}

}
