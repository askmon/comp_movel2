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

	private float initial_speed = 2.0e-2f;

	private int annie_time = 0;
	private int levi_time = 0;
	private int armor_time = 0;
	
	private MediaPlayer ploc = null;
	private MediaPlayer fall = null;

	private int cooldown;

	private int damage = 1;

	public BallEntity() {
		super(new Sprite(new Geometry(0.0f, 0.0f, 0.02f, 0.02f),
				R.drawable.soccer));
		resetCooldown();
	}
	
	public void resetCooldown() {
		cooldown = (int)(2000.0f/TouchSurfaceView.TIME_PER_FRAME);
	}
	
	public float getSpeed() {
		if (annie_time > 0)
			return this.initial_speed * 1.5f;
		return this.initial_speed;
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		if (cooldown > 0) {
			cooldown--;
			if (cooldown == 0) {
				setInitialSpeed();
			}
			return;
		}
		final Geometry sprite_geom = getSprite().getGeometry();
		final float last_x = sprite_geom.getX(), last_y = sprite_geom.getY();

		sprite_geom.translate(getSpeed() * speed_x, getSpeed() * speed_y);

		if (sprite_geom.getX() > view.getSpaceWidth()) {
			//sprite_geom.setPosition(last_x, last_y);
			speed_x = -Math.abs(speed_x);
		}
		
		if (sprite_geom.getX() < -view.getSpaceWidth()){
			//sprite_geom.setPosition(last_x, last_y);
			speed_x = Math.abs(speed_x);
		}

		if (sprite_geom.getY() > view.getSpaceHeight()*0.75f) {
			sprite_geom.setPosition(last_x, last_y);
			speed_y = -speed_y;
		}

		if (sprite_geom.getY() < -view.getSpaceHeight()*0.75f)
			loseLife(view);

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
				if (sprite_geom.collidesWith(other_geometry)) {
					sprite_geom.setPosition(last_x, last_y);
					collideWithBat(other_geometry);
				}
			}
		});
		
		annie_time--;
		levi_time--;
		armor_time--;
	}

	public void setInitialSpeed() {
		speed_x = 0.0f;
		speed_y = -1.0f;
	}

	private void loseLife(TouchSurfaceView view) {
		if (view.getLifeDisplay().changeCount(-1) < 0)
			view.getContext().sendBroadcast(
					new Intent(GameActivity.DEFEAT_EVENT));
		else {
			getSprite().setPosition(0.0f, 0.0f);
			speed_x = 0.0f;
			speed_y = 0.0f;
			cooldown = (int)(2000.0f/TouchSurfaceView.TIME_PER_FRAME);
		}
	}

	@Override
	public void onGameAdd(TouchSurfaceView view) {
		ploc = MediaPlayer.create(view.getContext(), R.raw.cork);
		fall = MediaPlayer.create(view.getContext(), R.raw.punch);
	}

	protected void collideWithBrick(BrickEntity brick) {
		float old_spd_x = speed_x, old_spd_y = speed_y;
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
		if (fall != null) {
			fall.seekTo(0);
			fall.start();
		}
		if(levi_time > 0)
			brick.inflictDamage(damage * 2, old_spd_x, old_spd_y);
		else if(armor_time > 0){
			brick.inflictDamage(0, old_spd_x, old_spd_y);
		}
		else{
			brick.inflictDamage(damage, old_spd_x, old_spd_y);
		}
	}

	private void collideWithBat(Geometry bat_geom) {
		bounce(bat_geom);
		if (ploc != null) {
			ploc.seekTo(0);
			ploc.start();
		}
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

	public int getAnnie() {
		return annie_time;
	}
	
	public void setAnnie(int time) {
		annie_time = time;
	}

	public int getLevi() {
		return levi_time;
	}
	
	public void setLevi(int time) {
		levi_time = time;
	}
	
	public int getArmor() {
		return armor_time;
	}
	
	public void setArmor(int time) {
		armor_time = time;
	}
}
