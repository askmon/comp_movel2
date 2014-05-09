package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

import com.demo.R;

public class BatEntity extends Entity {

	public BatEntity() {
		super(new Sprite(new Geometry(0.0f, 0.9f, 0.1f, 0.02f),
				R.drawable.pikachu));
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		// TODO Auto-generated method stub

	}

}
