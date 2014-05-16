package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class TypePowerEntity extends Entity {

	
	private int tempoExistencia = 0;

	public TypePowerEntity(int type) {
		super(new Sprite(new Geometry(0.0f, -0.875f, 0.1f, 0.1f),
				getTexId(type)));
		switch(type){
			case 1:
				this.setTempoExistencia(295);
				break;
			case 2:
				this.setTempoExistencia(200);
				break;
		}
	}

	private static int getTexId(int type) {
		switch(type){
		case 1:
			return R.drawable.mikasa;
		case 2:
			return R.drawable.annie;
		}
		return 0;
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		if(view.getTempo() >= tempoExistencia){
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
		return tempoExistencia;
	}

	public void setTempoExistencia(int tempoExistencia) {
		this.tempoExistencia = tempoExistencia;
	}
	
	@Override
	public void onGameRemove(TouchSurfaceView view) {
		view.setPowerup(0);
	}

}
