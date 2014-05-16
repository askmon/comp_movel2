package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.GameActivity;
import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;
import android.content.Intent;

public class BrickEntity extends Entity {

	private TouchSurfaceView view;
	private boolean destroyed;
	private static int ingame_count = 0;
	private int texture_id;
	private int hp, max_hp;
	private float impact_x, impact_y;
	private float center_x, center_y;
	private int impact_timer;
	private DyingBrickEntity dying_effect;

	private BrickEntity(float x, float y, int texture_id, int hp) {
		super(new Sprite(new Geometry(x, y, 0.05f, 0.05f), texture_id));
		this.texture_id = texture_id;
		this.max_hp = this.hp = hp;
		this.view = null;
		this.destroyed = false;
		this.dying_effect = null;
		this.center_x = x;
		this.center_y = y;
		this.impact_x = 0.0f;
		this.impact_y = 0.0f;
		this.impact_timer = 0;
	}
	
	public static BrickEntity makeZombie(float x, float y) {
		return new BrickEntity(x, y, R.drawable.zumbi, 1);
	}
	
	public static BrickEntity makeWitch(float x, float y) {
		return new BrickEntity(x, y, R.drawable.witch, 2);
	}
	
	public static BrickEntity makeTank(float x, float y) {
		return new BrickEntity(x, y, R.drawable.tank, 3);
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
		Geometry geom = getSprite().getGeometry();
		float oscilation = (float) (0.025*impact_timer/15.0f*Math.cos((15-impact_timer)/5.0f*2.0f*Math.PI));
		geom.setPosition(center_x + oscilation*impact_x, center_y + oscilation*impact_y);
		impact_timer = Math.max(0, impact_timer-1);
	}

	public void inflictDamage(int ballDamage, float impact_x, float impact_y) {
		hp -= ballDamage;
		if (!destroyed && hp <= 0) {
			view.removeEntity((Entity) this);
			view.addScore();
			destroyed = true;
			dying_effect = new DyingBrickEntity(getSprite().getGeometry()
					.getX(), getSprite().getGeometry().getY(), impact_x,
					impact_y, texture_id);
		} else {
			this.impact_timer = 15;
			this.impact_x = impact_x;
			this.impact_y = impact_y;
			getSprite().setColor(1.0f, 1.0f*hp/max_hp, 1.0f*hp/max_hp, 1.0f);
		}
	}

	@Override
	public void onGameRemove(TouchSurfaceView view) {
		if (--ingame_count <= 0)
			view.getContext().sendBroadcast(new Intent(GameActivity.WIN_EVENT));
		if (dying_effect != null)
			view.addEntity(dying_effect);
	}
}
