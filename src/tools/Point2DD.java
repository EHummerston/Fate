package tools;

public class Point2DD {

	private double x;
	private double y;

	public Point2DD(double x, double y)
	{
		this.x = x;
		this.y = y;
	}

	public double getX()
	{
		return x;
	}

	public int getXInt()
	{
		return (int) x;
	}

	public void setX(double x)
	{
		this.x = x;
	}
	
	public void offsetX(double offset)
	{
		this.x += offset;
	}

	public double getY()
	{
		return y;
	}

	public int getYInt()
	{
		return (int) y;
	}

	public void setY(double y)
	{
		this.y = y;
	}
	
	public void offsetY(double offset)
	{
		this.y += offset;
	}
	
	public void setLocation(double x, double y)
	{
		this.x = x;
		this.y = y;
	}
	
	public void rotate(double radians)
	{
		
		double x1 = (x*Math.cos(radians))-(y*Math.sin(radians));
		double y1 = (x*Math.sin(radians))+(y*Math.cos(radians));
		
		this.x = x1;
		this.y = y1;
	}
}
