package usp.ime.movel.brickbreaker.graphics;

import javax.microedition.khronos.opengles.GL10;

import android.content.Context;

public class NullSprite extends Sprite {

	public NullSprite() {
		super(0);
	}
	
	@Override
	public void loadGLTexture(GL10 gl, Context context) {
		
	}
	
	@Override
	public void draw(GL10 gl) {
		
	}

}
