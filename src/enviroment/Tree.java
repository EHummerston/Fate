package enviroment;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

import tools.ColourSets;
import tools.ColourTools;
import tools.LineTools;
import tools.Point2DD;

public class Tree {

	final static Color g1 = new Color(216,248,56),
			g2 = new Color(96,248,32),
			g3 = new Color(40,152,56),
			g4 = new Color(40,64,48),

			bl1 = new Color(72,64,128),
			bl2 = new Color(48,40,88),

			bk = new Color(40,32,40);
	
	

	
	public static BufferedImage tree(int width, int height, int i, int iMax, int rnd, Color[] cLeaves, Color[] cWood, Color outline)
	{
		Random rng = new Random(rnd);
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		
		Graphics2D g2 = img.createGraphics();
		double mult = (double)(iMax-(i))/iMax;
		g2.setColor(ColourTools.shade(mult,cWood,true));
		
		int branchBuffer; //= (int)((rng.nextDouble()+1)/4*width);
		
		//Create the trunk of the shape
		ArrayList<Point2DD> trunkShape = new ArrayList<Point2DD>();
		int trunkRes = rng.nextInt(3)+4;
		trunkShape.add(new Point2DD(width/2,height));
		for(int j = 1; j < trunkRes; j++)
		{
			//As the tree grows taller, each section of it becomes aligned more vertically.
			int tSX = -1;
			
				tSX = (int) (trunkShape.get(j-1).getXInt()) + (int) ((width/(j+4)) * (rng.nextDouble()-0.5));
			
			//System.out.println(trunkShape.get(j-1).getXInt() + " -> " + tSX);
			trunkShape.add(new Point2DD(tSX,height - (((height*0.8)*(j+1))/trunkRes)));
		}
		
		int branchCount = rng.nextInt(trunkRes) + 7;
		ArrayList<Point2DD> trunk = new ArrayList<Point2DD>();
		ArrayList<Point2DD> branches = new ArrayList<Point2DD>();
		
		int minX = width/2, maxX = width/2;
		for(int j = 0; j <= branchCount + 2; j++)
		{
			trunk.add(LineTools.bezierCurve(trunkShape, j, branchCount + 2));
			
			if(trunk.get(j).getXInt()<minX)
			{
				minX = trunk.get(j).getXInt();
			}
			if(trunk.get(j).getXInt()>maxX)
			{
				maxX = trunk.get(j).getXInt();
			}
		}
		
		if(minX < width-maxX)
		{
			branchBuffer = minX;
		}
		else
		{
			branchBuffer = width-maxX;		
		}
		branchBuffer -= width/10;
		
		g2.setStroke(new BasicStroke(1));
		int dir = (int) (((rng.nextInt(2)) - 0.5) * 2);
		
		g2.setStroke(new BasicStroke((float) height/ 100));
		for(int j = 0; j < branchCount; j++)
		{
			Point2DD branchBase = trunk.get(j+2);
			
			dir*=-1;
				
			
			int heightScaling = branchBuffer * (branchCount - j) / branchCount;
			int bOffset = (int) (heightScaling * ((rng.nextDouble()/2)+0.5)) * dir;
			int bX = branchBase.getXInt() + bOffset;
			branches.add(new Point2DD(bX,branchBase.getY()));
			
			ArrayList<Point2DD> drawBranch = new ArrayList<Point2DD>();
			ArrayList<Point2DD> drawBranch2 = new ArrayList<Point2DD>();
			for(int k = 0; k < j + 2; k++)
			{
				drawBranch.add(trunk.get(k));
			}
			drawBranch.add(branches.get(j));
			int branchRes = 20;
			for(int k = 0; k <= branchRes; k++)
			{
				drawBranch2.add(LineTools.bezierCurve(drawBranch,k,branchRes));
			}
			LineTools.drawLines(g2,drawBranch2);
		}
		float stroke = ((float)height / 30);
		g2.setStroke(new BasicStroke(stroke));
		LineTools.drawLines(g2,trunk);
		//System.out.println(trunkRes + " " + branchCount);
		
		img = ColourTools.outline(img, outline, false);
		//g2.drawImage(fillLeaves(width,height,(float)mult,trunk,branches,cLeaves, outline),null,0,0);
		g2.dispose();
		return img;
	}
	
	private static BufferedImage fillLeaves(int width, int height, float fract, ArrayList<Point2DD> trunk, ArrayList<Point2DD> branches, Color[] cLeaves, Color outline)
	{
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = img.createGraphics();
		Polygon polygon = new Polygon();
		
		float stroke = ((float)height / 30);
		if(height - 1 - trunk.get(trunk.size()-1).getY() < stroke)
		{
			trunk.get(trunk.size()-1).setY(0);
		}
		
		for(int i = 0; i < branches.size();i++)
		{
			if(branches.get(i).getX() < trunk.get(i+2).getX())
			{
				polygon.addPoint(branches.get(i).getXInt(),branches.get(i).getYInt());
			}
		}
		polygon.addPoint(trunk.get(trunk.size()-1).getXInt(),trunk.get(trunk.size()-1).getYInt());
		for(int i = (branches.size() - 1); i >= 0;i--)
		{
			if(branches.get(i).getX() > trunk.get(i+2).getX())
			{
				polygon.addPoint(branches.get(i).getXInt(),branches.get(i).getYInt());
			}
		}

		g2.setStroke(new BasicStroke(stroke*2));
		g2.setColor(ColourTools.shade(fract,cLeaves,true));
		g2.fillPolygon(polygon);
		g2.drawPolygon(polygon);
		img = ColourTools.outline(img, outline, true);
		g2.dispose();
		return img;
	}
	
}
