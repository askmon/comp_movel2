package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.GameActivity;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;
import android.content.Intent;

public class BrickEntity extends Entity {

	private TouchSurfaceView view;
	private boolean destroyed;
	private static int ingame_count = 0;
<<<<<<< HEAD
	private int hp;
=======
	private DyingBrickEntity dying_effect;
>>>>>>> Efeito dos tijolos usa direção do impacto da bola

<<<<<<< HEAD
	public BrickEntity(float x, float y, int texture_id, int hp) {
		super(new Sprite(new Geometry(x, y, 0.05f, 0.05f),
				texture_id));
=======
	public BrickEntity(float x, float y) {
		super(new Sprite(new Geometry(x, y, 0.05f, 0.05f), R.drawable.pikachu));
>>>>>>> Efeito dos tijolos morrendo
		this.view = null;
		this.destroyed = false;
<<<<<<< HEAD
		this.hp = hp;
=======
		this.dying_effect = null;
>>>>>>> Efeito dos tijolos usa direção do impacto da bola
	}

	public static void resetCount() {
		ingame_count = 0;
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

	public void destroy(float impact_x, float impact_y) {
		hp -= 1;
		if (!destroyed && hp <= 0) {
			view.removeEntity((Entity)this);
			view.addScore();
			destroyed = true;
			dying_effect = new DyingBrickEntity(getSprite().getGeometry()
					.getX(), getSprite().getGeometry().getY(), impact_x, impact_y);
		}
	}

	@Override
	public void onGameRemove(TouchSurfaceView view) {
		if (--ingame_count <= 0)
			view.getContext().sendBroadcast(new Intent(GameActivity.WIN_EVENT));
		if (dying_effect != null)
			view.addEntity(dying_effect);
		// Log.i("Brick count:", "" + ingame_count);
	}
}
