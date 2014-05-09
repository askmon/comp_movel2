package usp.ime.movel.brickbreaker.graphics;

public class Geometry {

	private float x;
	private float y;
	private float width;
	private float height;

	public Geometry(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
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

	public void setShape(float width, float height) {
		this.width = width;
		this.height = height;
	}

	public boolean collidesWith(Geometry other) {
		if (x + width < other.x - other.width)
			return false;
		if (y + height < other.y - other.height)
			return false;
		if (x - width > other.x + other.width)
			return false;
		if (y - height > other.y + other.height)
			return false;
		return true;
	}

}
