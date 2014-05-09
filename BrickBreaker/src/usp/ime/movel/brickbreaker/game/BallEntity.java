package usp.ime.movel.brickbreaker.game;

import com.demo.R;

import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class BallEntity extends Entity {

	private float speed_x, speed_y;
	
	private static final float INITIAL_SPEED = 2.0e-2f;

	public BallEntity() {
		super(new Sprite(new Geometry(0.0f, 0.0f, 0.02f, 0.02f),
				R.drawable.pikachu));
		this.speed_x = 0.0f;
		this.speed_y = -INITIAL_SPEED;
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		Geometry sprite_geom = getSprite().getGeometry();
		float last_x = sprite_geom.getX(), last_y = sprite_geom.getY();
		
		sprite_geom.translate(speed_x, speed_y);
		
		if (sprite_geom.getX() < -view.getSpaceWidth()
				|| sprite_geom.getX() > view.getSpaceWidth()) {
			sprite_geom.setPosition(last_x, last_y);
			speed_x = -speed_x;
		}
		
		if (sprite_geom.getY() < -view.getSpaceHeight()
				|| sprite_geom.getY() > view.getSpaceHeight()) {
			sprite_geom.setPosition(last_x, last_y);
			speed_y = -speed_y;
		}
	}

}
