package usp.ime.movel.brickbreaker.game;

import usp.ime.movel.brickbreaker.graphics.NullSprite;
import usp.ime.movel.brickbreaker.graphics.TouchSurfaceView;

public class SpawnerEntity extends Entity {

	private int tempo = 0;
	private int powerup = 0;

	public SpawnerEntity() {
		super(new NullSprite());
	}

	@Override
	public void onUpdate(TouchSurfaceView view) {
		tempo++;
		if(tempo >= 300){
			tempo = 0;
			view.addEntity(new PowerEntity(view.getSpaceWidth()));
		}
		switch(powerup){
		case 0:
			break;
		case 1:
			
			break;
		case 2:
			
			break;
		}
	}

}