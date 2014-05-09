package usp.ime.movel.brickbreaker.graphics;

import java.util.LinkedList;
import java.util.List;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import com.demo.R;

import usp.ime.movel.brickbreaker.game.BallEntity;
import usp.ime.movel.brickbreaker.game.Entity;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.Log;
import android.view.MotionEvent;

public class TouchSurfaceView extends GLSurfaceView {

	private Renderer renderer;

	private int screenWidth;
	private int screenHeight;

	private float[] unprojectViewMatrix = new float[16];
	private float[] unprojectProjMatrix = new float[16];

	private List<Entity> entities;

	public TouchSurfaceView(Context context) {
		super(context);
		renderer = new Renderer(context);
		setRenderer(renderer);
		entities = new LinkedList<Entity>();
	}

	public float getSpaceWidth() {
		return (float) screenWidth / screenHeight;
	}

	public float getSpaceHeight() {
		return 1.0f;
	}
	
	public void addEntity(Entity entity) {
		entities.add(entity);
	}

	private class Renderer implements GLSurfaceView.Renderer {

		private Sprite quad;
		private Sprite background;
		private Context context;
		private float previous_time;
		private float lag;

		private static final float TIME_PER_FRAME = 1000.0f / 30.0f;

		public Renderer(Context context) {
			this.context = context;
			this.lag = 0.0f;
			this.quad = new Sprite(R.drawable.pikachu);
			this.background = new Sprite(R.drawable.city);
		}

		private float currentTime() {
			return (float) (System.nanoTime() / 1.0e6);
		}

		@Override
		public void onDrawFrame(GL10 gl) {
			float current = currentTime();
			float elapsed = current - this.previous_time;
			this.previous_time = current;
			this.lag += elapsed;

			while (lag >= TIME_PER_FRAME) {
				// update
				for (Entity entity : entities)
					entity.onUpdate(TouchSurfaceView.this);
				lag -= TIME_PER_FRAME;
			}

			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			background.draw(gl);
			quad.draw(gl);
			for (Entity entity : entities)
				entity.getSprite().draw(gl);
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			screenWidth = width;
			screenHeight = height;

			float ratio = (float) width / height;
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrthof(-ratio, ratio, -1.0f, 1.0f, -1.0f, 1.0f);

			Matrix.orthoM(unprojectProjMatrix, 0, -ratio, ratio, -1.0f, 1.0f,
					-1.0f, 1.0f);
			Matrix.setIdentityM(unprojectViewMatrix, 0);

			background.setShape(ratio, 1.0f);
		}

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			Sprite.clearCache();
			for (Entity entity : entities)
				entity.getSprite().loadGLTexture(gl, this.context);
			quad.loadGLTexture(gl, this.context);
			background.loadGLTexture(gl, this.context);

			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

			gl.glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
			gl.glDisable(GL10.GL_CULL_FACE);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glDisable(GL10.GL_DEPTH_TEST);

			this.previous_time = currentTime();
		}

		public void updateQuadPosition(final float x, final float y) {
			queueEvent(new Runnable() {
				@Override
				public void run() {
					quad.setPosition(x, y);
				}
			});
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			final float screenX = e.getX();
			final float screenY = screenHeight - e.getY();

			final int[] viewport = { 0, 0, screenWidth, screenHeight };

			float[] resultWorldPos = { 0.0f, 0.0f, 0.0f, 0.0f };

			GLU.gluUnProject(screenX, screenY, 0, unprojectViewMatrix, 0,
					unprojectProjMatrix, 0, viewport, 0, resultWorldPos, 0);
			resultWorldPos[0] /= resultWorldPos[3];
			resultWorldPos[1] /= resultWorldPos[3];
			resultWorldPos[2] /= resultWorldPos[3];
			resultWorldPos[3] = 1.0f;

			renderer.updateQuadPosition(resultWorldPos[0], resultWorldPos[1]);
			break;
		}

		return true;
	}
}