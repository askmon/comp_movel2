package usp.ime.movel.brickbreaker.game;

import android.util.Log;
import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.graphics.Geometry;
import usp.ime.movel.brickbreaker.graphics.Sprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class TypePowerEntity extends Entity {

	
	private int tempoExistencia = 0;

	public TypePowerEntity(int type) {
		super(new Sprite(new Geometry(0.0f, -0.875f, 0.1f, 0.1f),
				R.drawable.mikasa));
		this.setTempoExistencia(295);
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		if(view.getTempo() >= tempoExistencia){
			Log.i("Tempo mikasa:", ""+view.getTempo());
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
