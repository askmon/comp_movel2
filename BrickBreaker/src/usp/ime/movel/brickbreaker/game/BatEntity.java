package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.OnTouchMotionListener;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

import com.demo.R;

public class BatEntity extends Entity implements OnTouchMotionListener {

	private float speed_x;
	private static final float SPEED = 2.0e-2f;

	public BatEntity() {
		super(new Sprite(new Geometry(0.0f, -0.7f, 0.1f, 0.1f),
				R.drawable.pikachu));
		this.speed_x = 0.0f;
		Geometry geom = getSprite().getGeometry(); 
		geom.setCollision(0.0f, 0, geom.getOuterRadius());
	}

	@Override
	public void onGameAdd(TouchSurfaceView view) {
		view.addOnTouchMotionListener(this);
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		final Geometry sprite_geom = getSprite().getGeometry();
		final float last_x = sprite_geom.getX(), last_y = sprite_geom.getY();
		sprite_geom.translate(SPEED*speed_x, 0.0f);
		speed_x = 0.0f;

		if (sprite_geom.getX() < -view.getSpaceWidth()
				|| sprite_geom.getX() > view.getSpaceWidth()) {
			sprite_geom.setPosition(last_x, last_y);
		}
	}

	@Override
	public void onMotionTouch(float x, float y) {
		speed_x = (x <= 0.0f) ? -1.0f : 1.0f;
	}

}
