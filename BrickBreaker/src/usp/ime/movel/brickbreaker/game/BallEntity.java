package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.GameActivity;
import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;
import android.content.Intent;
import android.media.MediaPlayer;

public class BallEntity extends Entity {

	private float speed_x, speed_y;

	private static final float INITIAL_SPEED = 2.0e-2f;

	private MediaPlayer ploc = null;

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

		sprite_geom.translate(INITIAL_SPEED * speed_x, INITIAL_SPEED * speed_y);

		if (sprite_geom.getX() < -view.getSpaceWidth()
				|| sprite_geom.getX() > view.getSpaceWidth()) {
			sprite_geom.setPosition(last_x, last_y);
			speed_x = -speed_x;
		}

		if (sprite_geom.getY() > view.getSpaceHeight()) {
			sprite_geom.setPosition(last_x, last_y);
			speed_y = -speed_y;
		}

		if (sprite_geom.getY() < -view.getSpaceHeight() / 2.0f) {
			view.getContext().sendBroadcast(
					new Intent(GameActivity.DEFEAT_EVENT));
		}

		view.visitEntities(BrickEntity.class, new EntityVisitor() {
			@Override
			public void visit(Entity entity) {
				if (sprite_geom.collidesWith(entity.getSprite().getGeometry())) {
					sprite_geom.setPosition(last_x, last_y);
					collideWithBrick((BrickEntity) entity);
				}
			}
		});

		view.visitEntities(BatEntity.class, new EntityVisitor() {
			@Override
			public void visit(Entity entity) {
				Geometry other_geometry = entity.getSprite().getGeometry();
				if (sprite_geom.collidesWith(other_geometry))
					collideWithBat(other_geometry);
			}
		});
	}

	@Override
	public void onGameAdd(TouchSurfaceView view) {
		ploc = MediaPlayer.create(view.getContext(), R.raw.cork);
	}

	protected void collideWithBrick(BrickEntity brick) {
		Geometry brick_geom = brick.getSprite().getGeometry();
		Geometry ball_geom = getSprite().getGeometry();
		boolean left = ball_geom.getX() < brick_geom.getX()
				- brick_geom.getWidth();
		boolean right = ball_geom.getX() > brick_geom.getX()
				+ brick_geom.getWidth();
		boolean bottom = ball_geom.getY() < brick_geom.getY()
				- brick_geom.getHeight();
		boolean top = ball_geom.getY() > brick_geom.getY()
				+ brick_geom.getHeight();
		if ((left || right) && !bottom && !top)
			speed_x = -speed_x;
		else if ((bottom || top) && !left && !right)
			speed_y = -speed_y;
		else
			bounce(brick_geom);
		brick.destroy();
	}

	private void collideWithBat(Geometry bat_geom) {
		bounce(bat_geom);
		ploc.seekTo(0);
		ploc.start();
	}

	private void bounce(Geometry other) {
		Geometry geom = getSprite().getGeometry();
		float dx = geom.getX() - other.getX();
		float dy = geom.getY() - other.getY();
		float angle = (float) Math.atan2(dy, dx);
		float speed_angle = (float) (Math.atan2(speed_y, speed_x) + Math.PI);
		float angle_diff = speed_angle - angle;
		float bounce_angle = angle - angle_diff;
		speed_x = (float) Math.cos(bounce_angle);
		speed_y = (float) Math.sin(bounce_angle);
		float dist = geom.getCollisionRadius() + other.getCollisionRadius();
		geom.setPosition(other.getX() + dist * (float) Math.cos(angle),
				other.getY() + dist * (float) Math.sin(angle));
	}

}
