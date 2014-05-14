package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class DyingBrickEntity extends Entity {

	private int time;
	private float x, y;

	public DyingBrickEntity(float x, float y) {
		super(new Sprite(new Geometry(x, y, 0.05f, 0.05f), R.drawable.pikachu));
		this.time = 0;
		this.x = x;
		this.y = y;
		getSprite().setColor(0.5f, 0.5f, 0.5f, 0.9f);
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		Geometry geom = getSprite().getGeometry();
		geom.setPosition(x, y + (0.02f * time - 0.005f * time * time));
		getSprite().setColor(0.5f, 0.5f, 0.5f, 0.9f - 0.05f*time);
		time++;
		if (time >= 10)
			view.removeEntity(this);
	}

}
