package usp.ime.movel.brickbreaker.game;

import java.util.Random;

import android.util.Log;
import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class PowerEntity extends Entity {

	private float speed_x, speed_y;

	private static final float INITIAL_SPEED = 2.0e-2f;

	private int destroy_this = 0;
	
	private int collide = 0;
	
	private Entity this_entity;
	
	private int cooldown;

	public PowerEntity(float width) {
		super(new Sprite(new Geometry(getX(width), 0.0f, 0.04f, 0.04f),
				R.drawable.interrogation));
		Log.i("X", ""+ width);
		setInitialSpeed();
		destroy_this = 0;
	}
	
	private static float getX(float width) {
		return (-width + (float)(Math.random() * 2 * width));
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

		sprite_geom.translate(INITIAL_SPEED * speed_x, INITIAL_SPEED * speed_y);

		if (sprite_geom.getY() < -view.getSpaceHeight()*0.75f)
			destroy(view);

		view.visitEntities(BatEntity.class, new EntityVisitor() {
			@Override
			public void visit(Entity entity) {
				Geometry other_geometry = entity.getSprite().getGeometry();
				if (sprite_geom.collidesWith(other_geometry)) {
					destroy_this = 1;
					collide = 1;
					this_entity = entity;
				}
			}
		});
		if(collide == 1){
			collideWithBat((BatEntity)this_entity, view);
			collide = 0;
		}
		if(destroy_this == 1){
			destroy(view);
			destroy_this = 0;
		}
	}

	private void addRandomPower(TouchSurfaceView view, BatEntity bat) {
		Random rand = new Random(); 
		Double pickedNumber = rand.nextDouble();
		if(pickedNumber < 0.33){
			bat.setMikasa(295);
			view.addEntity(TypePowerEntity.makeMikasa(295));
		}
		else if(pickedNumber < 0.66){
			view.visitEntities(BallEntity.class, new EntityVisitor() {
				@Override
				public void visit(Entity entity) {
					this_entity = (BallEntity) entity;
				}
			});
			setAnnieBall((BallEntity)this_entity);
			view.addEntity(TypePowerEntity.makeAnnie(200));
		}
		
		else{
			view.visitEntities(BallEntity.class, new EntityVisitor() {
				@Override
				public void visit(Entity entity) {
					this_entity = (BallEntity) entity;
				}
			});
			setLeviBall((BallEntity)this_entity);
			view.addEntity(TypePowerEntity.makeLevi(290));
		}
	}

	private void destroy(TouchSurfaceView view){
		view.removeEntity((Entity)this);
	}
	
	private void setInitialSpeed() {
		speed_x = 0.0f;
		speed_y = -0.5f;
	}

	@Override
	public void onGameAdd(TouchSurfaceView view) {
		//ploc = MediaPlayer.create(view.getContext(), R.raw.cork);
		//fall = MediaPlayer.create(view.getContext(), R.raw.punch);
	}

	private void collideWithBat(BatEntity bat, TouchSurfaceView view) {
		addRandomPower(view, bat);
	}
	
	private void setAnnieBall(BallEntity ball) {
		ball.setAnnie(200);
	}
	
	private void setLeviBall(BallEntity ball) {
		ball.setLevi(290);
	}
	
	@Override
	public void onGameRemove(TouchSurfaceView view) {
	}

}
