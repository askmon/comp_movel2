package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.graphics.NullSprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class SpawnerEntity extends Entity {

	private int tempo = 0;

	public SpawnerEntity() {
		super(new NullSprite());
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		tempo++;
		if(tempo >= 360){
			tempo = 0;
			view.addEntity(new PowerEntity(view.getSpaceWidth()));
		}
	}

}
