package tools;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

//Hopefully this link still works!
//		http://31.media.tumblr.com/1b5bc754cd562d2faffc0e7ab515c46a/tumblr_mprxp0kgPh1s1qf18o1_400.gif

public class LineTools {
	//How complicated the path is
	final static int POINTS_MAX = 12;
	
	//Number of updates before a new point is added
	//	Is NOT the lifespan of each point
	final static int TIME_MAX = 50;
	
	//How long the creature gets before the oldest
	//	points begin being removed.
	final static int TAIL_MAX =30;
	//Or this one for removing based on length,
	//	rather than the number of points.
	final static int TAIL_LENGTH_MAX =300;
	
	//These two values decide the range within which the new points 
	int screenWidth;
	int screenHeight;
	
	//The points which determine the path of the creature
	private ArrayList<Point2DD> points = new ArrayList<Point2DD>();
	
	//'t' is the frame counter, reset to 0 when it exceeds TIME_MAXIMUM.
	private int t = 0;
	
	//A list of points that mark the form of the 'snake'.
	//	The points have lines drawn between them when it is rendered,
	//	and the oldest are removed when the say array exceeds TAIL_MAX
	private  ArrayList<Point2DD> drawnPoints = new ArrayList<Point2DD>(); 

	public LineTools (int screenWidth, int screenHeight)
	{
		//These two should accomodate for the bars on the sides of the 
		this.screenWidth=screenWidth-16;
		this.screenHeight=screenHeight - 38;
		
		//Fill the path with randomly placed points
		for(int i = 0; i < POINTS_MAX; i++)
		{
			addPoint();
		}

	}

	//Called to draw the snake and additional data to the screen.
	public void draw (Graphics g2)
	{
		Graphics2D g = (Graphics2D) g2;
		//This section determines the "scaffolding lines" in the 
		//	same manner that the snake is guided.
		//The closer they are to the "centre", the darker they are drawn.
		ArrayList<Point2DD> drawPoint = points;
		g.setColor(Color.RED);
		while(drawPoint.size()>1)
		{ 
			int i = ((drawPoint.size()-1)*255)/POINTS_MAX-1;
			g.setColor(new Color(i,0,0));
			drawPoint = links(drawPoint,t, TIME_MAX);
			//drawLines(g,drawPoint);
		}
		
		//Our path's points are drawn in blue over the less static red lines 
		g.setColor(Color.BLUE);
		//drawLines(g,points);
		
		//Draws lines between each of the points on the line
		//	Lines rather that colouring the individual pixels because it goes
		//	too quickly to form a consistent line.
		g.setColor(Color.GREEN);
		drawLines(g,drawnPoints);
		/*
		//Outputs:
		g.drawString("t = " + t, 5, 10);
		g.drawString("Tail: " +drawnPoints.size(), 5, 30);
		int bytes = drawnPoints.size() * 16;
		int kilobytes = bytes / 1024;
		int megabytes = kilobytes / 1024;
		g.drawString("Bytes: " +bytes, 5, 45);
		if(kilobytes > 0)
		{
			g.drawString("KB: " +kilobytes, 5, 60);
		}
		if(megabytes > 0)
		{
			g.drawString("MB: " +megabytes, 5, 75);
		}
		*/
		
	}

	public void update()
	{
		if(t<TIME_MAX)
		{
			//Increment the time counter
			t++;
			
			//Determine where the next point is to be added.
			ArrayList<Point2DD> drawPoint = points;
			while(drawPoint.size()>1)
			{
				drawPoint = links(drawPoint,t,TIME_MAX);
			}
			
			//Then add it, duh.
			drawnPoints.add(drawPoint.get(0));
		}
		else
		{
			//Reset the counter if we are meant to.
			t = 0;
			
			//Remove the oldest point, and add a new one.
			points.remove(0);
			addPoint();
		}
		
		//These two methods can be used to remove points from the end of the snake,
		//	whether based on the total distance between each of the points,
		//	or simply by how many points there are.
		
		//capPoints();
		capLength();
		
	}

	//This method accepts an ArrayList of Points, and returns one
	//	of the same size, less one.
	//
	//Effectively, the points of the new array are determined by offsetting
	//	all the points of the old array (except the first point) towards the one before it.
	//	The offset is based on how far through the point is expressed as a fraction
	//	In this way, the amount shifted towards the point earlier in the list will be grow
	//	through iterations.
	//	subtracted from this offset (or brought back towards the point later in the list)
	//	is the 
	
	//Given an array of size x.
	//	New array will be of size x - 1 ( call this y )
	//	for each i between 1 and y
	//	the offset between points x[i-1] and x[i] will be:
	//	o = (1 - [i/y]) + ((t/T_MAX) * (1/y))
	//That is to say, approach x[i] from x[i-1],
	//	but only travel the total distance * o.
	
	//Hope that helps!
	
	public static Point2DD bezierCurve (ArrayList<Point2DD> givenList, int t0, int tMax)
	{
		while(givenList.size() > 1)
		{
			givenList = links(givenList,t0,tMax);
		}
		return givenList.get(0);
	}
	
	private static ArrayList<Point2DD> links(ArrayList<Point2DD> oldList, int t0, int tMax)
	{
		ArrayList<Point2DD> newList = new ArrayList<Point2DD>();

		for(int i = 1; i < oldList.size(); i++)
		{
			double x1 = oldList.get(i-1).getX();
			double y1 = oldList.get(i-1).getY();

			double x2 = oldList.get(i).getX();
			double y2 = oldList.get(i).getY();
			
			//This makes the thing go from the origin to the end
			//	in one loop of the 't' counter.
			double t1 = (double) t0/tMax;
			
			
			//int totalLines = oldList.size()-1;
			//double fraction = (tMax*totalLines) / totalLines;
			
			//double t1 = (t0 + ((totalLines-i))*fraction) / (tMax*totalLines);

			double x = ((x2 - x1) * (t1));
			double y = ((y2 - y1) * (t1));

			newList.add(new Point2DD(x+x1,y+y1));
		}

		return newList;

	}
	
	//This method draws a given ArrayList of points to another given Graphics object.
	public static void drawLines(Graphics2D g, ArrayList<Point2DD> drawList)
	{
		for(int i = 1; i < drawList.size(); i++)
		{
			int x1 = (int) drawList.get(i-1).getX();
			int y1 = (int) drawList.get(i-1).getY();

			int x2 = (int) drawList.get(i).getX();
			int y2 = (int) drawList.get(i).getY();

			g.drawLine(x1,y1,x2,y2);
		}
	}
	

	//Adds a point to the path, placed randomly within the limits of the screen.
	private void addPoint()
	{
		Random rng = new Random();
		
		double x = (rng.nextDouble() * screenWidth);
		double y = rng.nextDouble()*screenHeight;
		Point2DD newPoint = new Point2DD(x,y);

		points.add(newPoint);
	}
	
	@SuppressWarnings("unused")
	private void capPoints()
	{
		if(drawnPoints.size()>TAIL_MAX)
		{
			drawnPoints.remove(0);
		}
	}
	
	
	private void capLength()
	{
		double length = 0;
		for(int i = 1; i < drawnPoints.size(); i++)
		{
			//Use Pythagorean's rule (a^2 + b^2 == c^2) to determine
			//	the distance between this point and the previous one.
			double xDif = drawnPoints.get(i-1).getX() - drawnPoints.get(i).getX();
			double yDif = drawnPoints.get(i-1).getY() - drawnPoints.get(i).getY();
			double distance =  Math.sqrt((xDif*xDif)+(yDif*yDif));
			length += distance;
		}
		if(length > TAIL_LENGTH_MAX)
		{
			//This could also be done by moving the oldest point so that it is
			//	closer to the point before it, and it would look smoother.
			drawnPoints.remove(0);
			
			//We must check again if we have exceeded 
			capLength();
		}
	}
}
