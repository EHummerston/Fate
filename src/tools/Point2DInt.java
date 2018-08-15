package tools;

public class Point2DInt {
	private int x;
	private int y;

	public Point2DInt (int x, int y)
	{
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void offsetX(int x)
	{
		this.x += x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void offsetY(int y)
	{
		this.y += y;
	}
	
	public void setPoint(int x, int y)
	{
		this.x = x;
		this.y = y;
	}

}
