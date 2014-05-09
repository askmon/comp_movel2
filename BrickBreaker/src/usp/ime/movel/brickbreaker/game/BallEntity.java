package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;
import android.content.Context;
import android.media.MediaPlayer;

import com.demo.R;

public class BallEntity extends Entity {

	private float speed_x, speed_y;

	private static final float INITIAL_SPEED = 2.0e-2f;

	private MediaPlayer ploc = null;
	
	private Context context;
	
	public BallEntity() {
		super(new Sprite(new Geometry(0.0f, 0.0f, 0.02f, 0.02f),
				R.drawable.soccer));
		this.speed_x = 0.0f;
		this.speed_y = -1.0f;
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		final Geometry sprite_geom = getSprite().getGeometry();
		final float last_x = sprite_geom.getX(), last_y = sprite_geom.getY();

		sprite_geom.translate(INITIAL_SPEED*speed_x, INITIAL_SPEED*speed_y);

		if (sprite_geom.getX() < -view.getSpaceWidth()
				|| sprite_geom.getX() > view.getSpaceWidth()) {
			sprite_geom.setPosition(last_x, last_y);
			speed_x = -speed_x;
		}

		if (sprite_geom.getY() < -view.getSpaceHeight()
				|| sprite_geom.getY() > 0.0f) {
			sprite_geom.setPosition(last_x, last_y);
			speed_y = -speed_y;
		}

		view.visitEntities(BatEntity.class, new EntityVisitor() {
			@Override
			public void visit(Entity entity) {
				Geometry other_geometry = entity.getSprite().getGeometry();
				if (sprite_geom.collidesWith(other_geometry)) {
					sprite_geom.setPosition(last_x, last_y);
					collideWithBat(other_geometry);
				}
			}
		});
	}

	@Override
	public void onGameAdd(TouchSurfaceView view) {
		context = view.getContext();
		ploc = MediaPlayer.create(context, R.raw.cork );
	}

	private void collideWithBat(Geometry bat_geom) {
		Geometry geom = getSprite().getGeometry();
		float dx = geom.getX() - bat_geom.getX();
		//float dy = geom.getY() - bat_geom.getY();
		speed_x += dx*2.0f;
		speed_y *= -1;
		normalizeSpeed();
		ploc.seekTo(0);
		ploc.start();
	}
	
	private void normalizeSpeed() {
		float length = (float) Math.sqrt(speed_x*speed_x + speed_y*speed_y);
		speed_x /= length;
		speed_y /= length;
	}
	
}
