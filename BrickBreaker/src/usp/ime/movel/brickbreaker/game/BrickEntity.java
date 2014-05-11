package usp.ime.movel.brickbreaker.game;

import android.content.Intent;
import android.util.Log;
import usp.ime.movel.brickbreaker.GameActivity;
import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class BrickEntity extends Entity {
	
	private TouchSurfaceView view;
	private static int ingame_count = 0;

	public BrickEntity(float x, float y) {
		super(new Sprite(new Geometry(x, y, 0.05f, 0.05f),
				R.drawable.pikachu));
		view = null;
	}

	@Override
	public void onGameAdd(TouchSurfaceView view) {
		this.view = view;
		ingame_count++;
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		// TODO Auto-generated method stub
	}

	public void destroy() {
		view.removeEntity((Entity)this);
	}

	@Override
	public void onGameRemove(TouchSurfaceView view) {
		if (--ingame_count <= 0)
			view.getContext().sendBroadcast(
					new Intent(GameActivity.WIN_EVENT));
		Log.i("Brick count:", "" + ingame_count);
	}

}
