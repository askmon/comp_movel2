package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.OnTouchMotionListener;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

import com.demo.R;

public class BatEntity extends Entity implements OnTouchMotionListener {

	private float last_x;

	public BatEntity() {
		super(new Sprite(new Geometry(0.0f, -0.7f, 0.1f, 0.1f),
				R.drawable.pikachu));
		this.last_x = 0.0f;
		Geometry geom = getSprite().getGeometry(); 
		geom.setCollision(0.0f, 0, geom.getOuterRadius());
	}

	@Override
	public void onGameAdd(TouchSurfaceView view) {
		view.addOnTouchMotionListener(this);
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onMotionTouch(float x, float y) {
		getSprite().getGeometry().translate(x - last_x, 0);
		last_x = x;
	}

}
