package usp.ime.movel.brickbreaker;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.opengles.GL10;

import usp.ime.movel.brickbreaker.graphics.Geometry;

class Quad {

	private Geometry geom;

	private FloatBuffer vertexBuffer;
	private FloatBuffer colorBuffer;

	private static final float[] vertices = {
		-1.0f, -1.0f,
		-1.0f, 1.0f,
		1.0f, -1.0f,
		1.0f, 1.0f
	};

	private static final int FLOAT_SIZE_BYTES = Float.SIZE / 8;

	public Quad(float r, float g, float b) {
		this.geom = new Geometry();

		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length
				* FLOAT_SIZE_BYTES);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);

		float[] colors = {
				r, g, b, 1.0f,
				r, g, b, 1.0f,
				r, g, b, 1.0f,
				r, g, b, 1.0f,
		};

		ByteBuffer cbb = ByteBuffer.allocateDirect(colors.length
				* FLOAT_SIZE_BYTES);
		cbb.order(ByteOrder.nativeOrder());
		colorBuffer = cbb.asFloatBuffer();
		colorBuffer.put(colors);
		colorBuffer.position(0);
	}

	public void setPosition(float x, float y) {
		this.geom.setPosition(x, y);
	}

	public void draw(GL10 gl) {
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef(this.geom.getX(), this.geom.getY(), 0.0f);
		gl.glScalef(this.geom.getWidth(), this.geom.getHeight(), 0.5f);

		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_COLOR_ARRAY);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glColorPointer(4, GL10.GL_FLOAT, 0, colorBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_COLOR_ARRAY);

		gl.glPopMatrix();
	}
}