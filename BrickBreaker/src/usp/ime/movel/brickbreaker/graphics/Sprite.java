package usp.ime.movel.brickbreaker.graphics;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;
import javax.microedition.khronos.opengles.GL10;
import com.demo.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLUtils;
import android.util.Log;

public class Sprite {

	private Geometry geom;
	private FloatBuffer vertexBuffer;
	

	private static final float[] vertices = {
		-1.0f, -1.0f,
		-1.0f, 1.0f,
		1.0f, -1.0f,
		1.0f, 1.0f
	};
	
	private FloatBuffer textureBuffer;
	private float texture[] = {    		
			0.0f, 1.0f,		// top left		(V2)
			0.0f, 0.0f,		// bottom left	(V1)
			1.0f, 1.0f,		// top right	(V4)
			1.0f, 0.0f		// bottom right	(V3)
	};
	private int texture_id;

	private static final int FLOAT_SIZE_BYTES = Float.SIZE / 8;
	private static final int MAX_TEXTURE_NUM = 32;

	private static int[] textures = new int[MAX_TEXTURE_NUM];
	private static int next_texture = 0;
	@SuppressLint("UseSparseArrays")
	private static Map<Integer,Integer> texture_cache = new HashMap<Integer,Integer>();

	public Sprite(Geometry geom, int texture_id) {
		this.geom = geom;
		this.texture_id = texture_id;
		System.out.println(texture_id);
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length
				* FLOAT_SIZE_BYTES);
		vbb.order(ByteOrder.nativeOrder());
		vertexBuffer = vbb.asFloatBuffer();
		vertexBuffer.put(vertices);
		vertexBuffer.position(0);
		
		vbb = ByteBuffer.allocateDirect(texture.length * 4);
		vbb.order(ByteOrder.nativeOrder());
		textureBuffer = vbb.asFloatBuffer();
		textureBuffer.put(texture);
		textureBuffer.position(0);
	}
	
	public Sprite(int id) {
		this(new Geometry(), id);
	}

	public void setPosition(float x, float y) {
		this.geom.setPosition(x, y);
	}
	
	public void move(float dx, float dy) {
		this.geom.setPosition(this.geom.getX()+dx, this.geom.getY()+dy);
	}
	
	public void setShape(float width, float height) {
		this.geom.setShape(width, height);
	}

	public void loadGLTexture(GL10 gl, Context context) {
		if (next_texture >= MAX_TEXTURE_NUM) {
			Log.e("Sprite", "No more textures!");
			return;
		}
		if (texture_cache.containsKey(this.texture_id)) return;
		// loading texture
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				this.texture_id);
		System.out.println(this.texture_id);

		// generate one texture pointer
		gl.glGenTextures(1, textures, next_texture++);
		// ...and bind it to our array
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[next_texture]);
		
		texture_cache.put(this.texture_id, next_texture);
		
		// create nearest filtered texture
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MIN_FILTER, GL10.GL_NEAREST);
		gl.glTexParameterf(GL10.GL_TEXTURE_2D, GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
		
		// Use Android GLUtils to specify a two-dimensional texture image from our bitmap 
		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bitmap, 0);
		
		// Clean up
		bitmap.recycle();
	}
	
	public void draw(GL10 gl) {
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glPushMatrix();
		gl.glLoadIdentity();
		gl.glTranslatef(this.geom.getX(), this.geom.getY(), 0.0f);
		gl.glScalef(this.geom.getWidth(), this.geom.getHeight(), 0.5f);
		
		gl.glBindTexture(GL10.GL_TEXTURE_2D, textures[texture_cache.get(texture_id)]);
		
		gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glVertexPointer(2, GL10.GL_FLOAT, 0, vertexBuffer);
		gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, textureBuffer);

		gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

		gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
		gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);

		gl.glPopMatrix();
	}

	public static void clearCache() {
		texture_cache.clear();
		next_texture = 0;
	}
}