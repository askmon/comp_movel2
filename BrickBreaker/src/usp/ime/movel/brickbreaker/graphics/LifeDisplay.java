package usp.ime.movel.brickbreaker.graphics;

import javax.microedition.khronos.opengles.GL10;

import usp.ime.movel.brickbreaker.R;

public class LifeDisplay extends Sprite {

	private TouchSurfaceView view;
	private int count;
	
	public LifeDisplay(TouchSurfaceView view, int count) {
		super(R.drawable.soccer);
		this.view = view;
		this.count = count;
		getGeometry().setShape(0.02f, 0.02f);
	}
	
	public int changeCount(int diff) {
		return this.count += diff;
	}
	
	@Override
	public void draw(GL10 gl) {
		getGeometry().setPosition(-view.getSpaceWidth()+0.1f, view.getSpaceHeight()-0.175f);
		for (int i = 0; i < count; i++) {
			super.draw(gl);
			getGeometry().translate(0.06f, 0.0f);
		}
	}

}
