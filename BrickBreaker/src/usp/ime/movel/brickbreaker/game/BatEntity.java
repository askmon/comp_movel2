package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.OnTouchActionListener;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class BatEntity extends Entity implements OnTouchActionListener {

	private float[] move_dirs;
	private int inclination;
	private float speed = 1.0e-2f;
	private int mikasa = 0;

	public BatEntity() {
		super(
				new Sprite(new Geometry(0.0f, -0.5f, 0.1f, 0.1f),
						R.drawable.eren));
		move_dirs = new float[2];
		move_dirs[0] = 0.0f;
		move_dirs[1] = 0.0f;
		inclination = 0;
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
		
		if (inclination > 0)
			inclination--;
		else if (inclination < 0)
			inclination++;
		
		for (int i = 0; i < move_dirs.length; i++) {
			sprite_geom.translate(move_dirs[i]*speed, 0.0f);
			inclination += (int)2*move_dirs[i];
			inclination = Math.max(-10, Math.min(10, inclination));
		}

		if (sprite_geom.getX() < -view.getSpaceWidth()
				|| sprite_geom.getX() > view.getSpaceWidth()) {
			sprite_geom.setPosition(last_x, last_y);
		}
		
		getSprite().getGeometry().setRotation(-15.0f*inclination/10.0f);
		
		if(view.getTempo() >= 295){
			this.setMikasa(0);
		}
	}

	@Override
	public void onTouchActionDown(int pointer_id, float x, float y) {
		if (y <= 0.75f)
			return;
		if (pointer_id <= move_dirs.length)
			move_dirs[pointer_id] = (x < 0) ? -1.0f : 1.0f;
	}

	@Override
	public void onTouchActionUp(int pointer_id, float x, float y) {
		if (pointer_id <= move_dirs.length)
			move_dirs[pointer_id] = 0.0f;
	}

	public int getMikasa() {
		return mikasa;
	}

	public void setMikasa(int mikasa) {
		if (mikasa == 1){
			this.speed = this.speed * 2;
		}
		else{
			this.speed = 1.0e-2f;;
		}
	}

}
