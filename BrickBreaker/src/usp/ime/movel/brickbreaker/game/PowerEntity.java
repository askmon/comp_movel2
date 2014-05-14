package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class PowerEntity extends Entity {

	private float speed_x, speed_y;

	private static final float INITIAL_SPEED = 2.0e-2f;

	private int destroy_this = 0;
	
	private int cooldown;

	public PowerEntity() {
		super(new Sprite(new Geometry(0.0f, 0.0f, 0.02f, 0.02f),
				R.drawable.soccer));
		setInitialSpeed();
		destroy_this = 0;
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
					collideWithBat(other_geometry);
					destroy_this = 1;
				}
			}
		});
		if(destroy_this == 1){
			destroy(view);
		}
	}

	private void addRandomPower(TouchSurfaceView view) {
		// TODO Auto-generated method stub
		
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

	private void collideWithBat(Geometry bat_geom) {

	}

}
