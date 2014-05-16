package usp.ime.movel.brickbreaker.graphics;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Set;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import usp.ime.movel.brickbreaker.GameActivity;
import usp.ime.movel.brickbreaker.R;
import usp.ime.movel.brickbreaker.game.Entity;
import usp.ime.movel.brickbreaker.game.EntityVisitor;
import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class TouchSurfaceView extends GLSurfaceView {

	private Renderer renderer;
	private GL10 last_gl;

	private int screenWidth;
	private int screenHeight;
	
	private int points = 0;
	private GameActivity context;

	private float[] unprojectViewMatrix = new float[16];
	private float[] unprojectProjMatrix = new float[16];

	private Map<Class<?>, Set<Entity>> entities;
	private List<OnTouchActionListener> touch_listeners;
	private Queue<Entity> to_be_removed;

	private LifeDisplay life_display;

	public static final float TIME_PER_FRAME = 1000.0f / 30.0f;

	public TouchSurfaceView(Context context, AttributeSet attr) {
		super(context, attr);
		renderer = new Renderer(context);
		last_gl = null;
		setRenderer(renderer);
		entities = new HashMap<Class<?>, Set<Entity>>();
		touch_listeners = new LinkedList<OnTouchActionListener>();
		to_be_removed = new LinkedList<Entity>();
		life_display = new LifeDisplay(this, 3);
		this.context = (GameActivity)context;
	}
	
	public void addScore(int quantity){
		points += quantity;
		context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                context.setScore(points);
            }
        });
	}

	public float getSpaceWidth() {
		return (float) screenWidth / screenHeight;
	}

	public float getSpaceHeight() {
		return 1.0f;
	}
	
	public LifeDisplay getLifeDisplay() {
		return this.life_display;
	}
	
	public void addEntity(Entity entity) {
		if (last_gl != null)
			entity.getSprite().loadGLTexture(last_gl, context);
		Set<Entity> entity_set = entities.get(entity.getClass());
		if (entity_set == null) {
			entity_set = new HashSet<Entity>();
			entities.put(entity.getClass(), entity_set);
		}
		entity_set.add(entity);
		entity.onGameAdd(this);
	}

	public void removeEntity(Entity entity) {
		to_be_removed.add(entity);
	}

	private void doRemoveEntity(Entity entity) {
		Set<Entity> entity_set = entities.get(entity.getClass());
		if (entity_set == null)
			return;
		entity_set.remove(entity);
		entity.onGameRemove(this);
	}

	public void visitEntities(Class<?> entity_class, EntityVisitor visitor) {
		for (Entity entity : entities.get(entity_class))
			visitor.visit(entity);
	}

	public GL10 getLastGL() {
		return this.last_gl;
	}

	public void visitEntities(EntityVisitor visitor) {
		for (Entry<Class<?>, Set<Entity>> entry : entities.entrySet())
			for (Entity entity : entry.getValue())
				visitor.visit(entity);
	}

	public void addOnTouchMotionListener(OnTouchActionListener listener) {
		touch_listeners.add(listener);
	}

	private class Renderer implements GLSurfaceView.Renderer {

		private Sprite background;
		private Context context;
		private float previous_time;
		private float lag;
		
		private final int MAX_STEPS_PER_FRAME = 10;

		public Renderer(Context context) {
			this.context = context;
			this.lag = 0.0f;
			this.background = new Sprite(R.drawable.city);
		}

		private float currentTime() {
			return (float) (System.nanoTime() / 1.0e6);
		}

		@Override
		public void onDrawFrame(final GL10 gl) {
			float current = currentTime();
			float elapsed = current - this.previous_time;
			this.previous_time = current;
			this.lag += elapsed;

			for (int i = 0; i < MAX_STEPS_PER_FRAME && lag >= TIME_PER_FRAME; i++) {
				// update
				visitEntities(new EntityVisitor() {
					@Override
					public void visit(Entity entity) {
						entity.onUpdate(TouchSurfaceView.this);
					}
				});
				for (Entity removed : to_be_removed)
					doRemoveEntity(removed);
				to_be_removed.clear();
				lag -= TIME_PER_FRAME;
				
			}

			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			background.draw(gl);
			life_display.draw(gl);
			
			visitEntities(new EntityVisitor() {
				@Override
				public void visit(Entity entity) {
					entity.getSprite().draw(gl);
				}
			});
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {
			gl.glViewport(0, 0, width, height);
			screenWidth = width;
			screenHeight = height;

			float ratio = (float) width / height;
			gl.glMatrixMode(GL10.GL_PROJECTION);
			gl.glLoadIdentity();
			gl.glOrthof(-ratio, ratio, -1.0f, 1.0f, 1.0f, -1.0f);

			Matrix.orthoM(unprojectProjMatrix, 0, -ratio, ratio, 1.0f, -1.0f,
					-1.0f, 1.0f);
			Matrix.setIdentityM(unprojectViewMatrix, 0);

			background.setShape(ratio, 0.75f);
		}

		@Override
		public void onSurfaceCreated(final GL10 gl, EGLConfig config) {
			Sprite.clearCache();
			last_gl = gl;
			visitEntities(new EntityVisitor() {
				@Override
				public void visit(Entity entity) {
					entity.getSprite().loadGLTexture(gl, Renderer.this.context);
				}
			});

			background.loadGLTexture(gl, this.context);

			gl.glEnable(GL10.GL_TEXTURE_2D);
			gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
			gl.glEnable(GL10.GL_BLEND);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glDisable(GL10.GL_DITHER);
			gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);

			gl.glClearColor(0.4f, 0.4f, 0.4f, 1.0f);
			gl.glDisable(GL10.GL_CULL_FACE);
			gl.glShadeModel(GL10.GL_SMOOTH);
			gl.glDisable(GL10.GL_DEPTH_TEST);

			this.previous_time = currentTime();
		}

		public void touchActionDown(final int pointer_id, final float x,
				final float y) {
			// Log.i("Touch down:", "id=" + pointer_id);
			queueEvent(new Runnable() {
				@Override
				public void run() {
					for (OnTouchActionListener listener : touch_listeners)
						listener.onTouchActionDown(pointer_id, x, y);
				}
			});
		}

		public void touchActionUp(final int pointer_id, final float x,
				final float y) {
			// Log.i("Touch up:", "id=" + pointer_id);
			queueEvent(new Runnable() {
				@Override
				public void run() {
					for (OnTouchActionListener listener : touch_listeners)
						listener.onTouchActionUp(pointer_id, x, y);
				}
			});
		}
	}

	private float[] screenToSurface(float screen_x, float screen_y) {
		final int[] viewport = { 0, 0, screenWidth, screenHeight };

		float[] resultWorldPos = { 0.0f, 0.0f, 0.0f, 0.0f };

		GLU.gluUnProject(screen_x, screen_y, 0, unprojectViewMatrix, 0,
				unprojectProjMatrix, 0, viewport, 0, resultWorldPos, 0);
		resultWorldPos[0] /= resultWorldPos[3];
		resultWorldPos[1] /= resultWorldPos[3];
		resultWorldPos[2] /= resultWorldPos[3];
		resultWorldPos[3] = 1.0f;

		return resultWorldPos;
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {
		switch (e.getAction()) {
			case MotionEvent.ACTION_MOVE:
				break;
			case MotionEvent.ACTION_DOWN:
				for (int p = 0; p < e.getPointerCount(); p++)
					executeActionDown(e, p);
				break;
			case MotionEvent.ACTION_POINTER_DOWN: {
				executeActionDown(e, e.getActionIndex());
				break;
			}
			case MotionEvent.ACTION_UP:
				for (int p = 0; p < e.getPointerCount(); p++)
					executeActionUp(e, p);
				break;
			case MotionEvent.ACTION_POINTER_UP: {
				executeActionUp(e, e.getActionIndex());
				break;
			}
		}

		return true;
	}

	private void executeActionUp(MotionEvent e, int p) {
		final float[] position = screenToSurface(e.getX(p),
				screenHeight - e.getY(p));
		renderer.touchActionUp(e.getPointerId(p), position[0],
				position[1]);
	}

	private void executeActionDown(MotionEvent e, int p) {
		final float[] position = screenToSurface(e.getX(p),
				screenHeight - e.getY(p));
		renderer.touchActionDown(e.getPointerId(p), position[0],
				position[1]);
	}
}