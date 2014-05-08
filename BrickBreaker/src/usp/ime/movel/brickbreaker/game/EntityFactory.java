package usp.ime.movel.brickbreaker.game;

import com.demo.R;

import usp.ime.movel.brickbreaker.graphics.Sprite;

public class EntityFactory {
	
	public Entity makeBall () {
		Sprite ball_sprite = new Sprite(R.drawable.pikachu);
		ball_sprite.setShape(0.02f, 0.02f);
		return new Entity(ball_sprite);
	}
	
	public Entity makeBackground () {
		Sprite background = new Sprite(R.drawable.city);
		background.setShape(1f, 1f);
		return new Entity(background);
	}

}
