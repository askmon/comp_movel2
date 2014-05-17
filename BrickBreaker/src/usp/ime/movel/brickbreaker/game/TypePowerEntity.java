package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class TypePowerEntity extends Entity {
	
	private int time = 0;
	
	public final static int MIKASA_POWER = 1;
	public final static int ANNIE_POWER = 2;
	public final static int LEVI_POWER = 3;
	public final static int ARMOR_POWER = 4;

	private TypePowerEntity(int type, int time) {
		super(new Sprite(new Geometry(0.0f, -0.875f, 0.1f, 0.1f),
				getTexId(type)));
		this.time = time;
	}
	
	public static TypePowerEntity makeMikasa(int time) {
		return new TypePowerEntity(MIKASA_POWER, time);
	}
	
	public static TypePowerEntity makeAnnie(int time) {
		return new TypePowerEntity(ANNIE_POWER, time);
	}
	
	public static TypePowerEntity makeLevi(int time) {
		return new TypePowerEntity(LEVI_POWER, time);
	}
	
	public static TypePowerEntity makeArmor(int time) {
		return new TypePowerEntity(ARMOR_POWER, time);
	}

	private static int getTexId(int type) {
		switch(type){
		case MIKASA_POWER:
			return R.drawable.mikasa;
		case ANNIE_POWER:
			return R.drawable.annie;
		case LEVI_POWER:
			return R.drawable.levi;
		case ARMOR_POWER:
			return R.drawable.armored;
		}
		return 0;
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		if(--time <= 0){
			destroy(view);
		}
	}

	private void destroy(TouchSurfaceView view){
		view.removeEntity((Entity)this);
	}

	@Override
	public void onGameAdd(TouchSurfaceView view) {
	}

	public int getTempoExistencia() {
		return time;
	}

	public void setTempoExistencia(int tempoExistencia) {
		this.time = tempoExistencia;
	}

}
