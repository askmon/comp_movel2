package usp.ime.movel.brickbreaker.graphics;

public class Geometry {

	private float x;
	private float y;
	private float width;
	private float height;
	private float rotation;
	private float collision_x;
	private float collision_y;
	private float collision_radius;

	public Geometry(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.rotation = 0.0f;
		this.collision_x = 0.0f;
		this.collision_y = 0.0f;
		this.collision_radius = Math.min(width, height);
	}

	public Geometry() {
		this(0.0f, 0.0f, 0.1f, 0.1f);
	}

	public float getX() {
		return this.x;
	}

	public float getY() {
		return this.y;
	}
	
	public float getRotation() {
		return this.rotation;
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void translate(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}

	public float getWidth() {
		return this.width;
	}

	public float getHeight() {
		return this.height;
	}
	
	public float getOuterRadius() {
		return (float) (width*Math.sqrt(2));
	}

	public void setShape(float width, float height) {
		this.width = width;
		this.height = height;
	}
	
	public void setRotation(float rotation) {
		this.rotation = rotation;
	}
	
	public void setCollision(float x, float y, float radius) {
		this.collision_x = x;
		this.collision_y = y;
		this.collision_radius = radius;
	}

	public boolean collidesWith(Geometry other) {
		float dx = (x + collision_x) - (other.x + other.collision_x);
		float dy = (y + collision_y) - (other.y + other.collision_y);
		if (dx*dx + dy*dy <= Math.pow(collision_radius + other.collision_radius, 2.0f))
			return true;
		return false;
	}

	public float getCollisionRadius() {
		return this.collision_radius;
	}

}
