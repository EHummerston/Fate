package enviroment;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Random;

import tools.ColourSets;
import tools.ColourTools;
import tools.Point2DInt;

public class Background {

	public static BufferedImage sky (int width, int height, Color[] c, Time time, int rnd)
	{
		Random rng = new Random(rnd);
		
		double light = time.getLight();
		
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		double[][] pic = new double[width][height];
		double[] skyCurve = ColourTools.curve(height, true);

		Graphics2D g2 = img.createGraphics();
		
		int moonCount = (int)(10*( Math.pow(2,-(rng.nextInt(3)+rng.nextDouble())) ));
		
		double[][][] spheres = new double[moonCount][][];
		Point2DInt[] sphOffs = new Point2DInt[spheres.length];
		double sphsLiteX = rng.nextDouble()*1.5-0.5;
		double sphsLiteY = rng.nextDouble()*1.5-0.5;
		for(int s = 0; s < spheres.length; s++)
		{
			int sphsDia = rng.nextInt(height/2)+10;
			spheres[s] = ColourTools.sphere(sphsDia, sphsDia, (int)(sphsDia*sphsLiteX), (int)(sphsDia*sphsLiteY));
			sphOffs[s] = new Point2DInt(rng.nextInt(width+sphsDia)-(sphsDia/2), rng.nextInt(height-sphsDia));
		}
		for(int s = 0; s < spheres.length; s++)
		{
			for(int x = 0; x < spheres[s].length; x++)
			{
				for(int y = 0; y < spheres[s][x].length; y++)
				{
					if(spheres[s][x][y]>0)
					{
						int offX = x+sphOffs[s].getX();
						int offY = y+sphOffs[s].getY();
						if(offX>=0&&offX<width&&offY>=0&&offY<height)
						{
							double sphL = (Math.pow(spheres[s][x][y],3))*8;
							if(sphL > 1)
							{
								sphL = 1;
							}
							pic[offX][offY] = sphL*(1-(light*0.5));
						}
					}
				}
			}
		}
		
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				double mult = (1.05-skyCurve[height-1-y])*light;

				if(pic[x][y]>mult)
				{
					mult = pic[x][y];
				}
				//Noise
				//mult += (rng.nextDouble() - 0.5)*1/(c.length+3);

				g2.setColor(ColourTools.shadeTrans(mult,c,2,x,y));

				g2.drawLine(x,height-1-y,x,height-1-y);
			}
		}
		g2.dispose();
		return img;


	}

	public static BufferedImage clouds (int width, int height, Color[] c, int rnd)
	{
		Random rng = new Random(rnd);
		BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);


		return img;
	}

	public static BufferedImage reflection(BufferedImage input, int width, int height, Color[] c)
	{
		Image scaledImg = input.getScaledInstance(width,height, Image.SCALE_SMOOTH);
		BufferedImage imageBuff = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = imageBuff.createGraphics();
		g2.drawImage(scaledImg, 0, 0, new Color(0,0,0), null);

		int minLum = 1000, maxLum = 0;
		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				int thisLum = ColourTools.colorValue(new Color(imageBuff.getRGB(x, y)));

				if(minLum>thisLum)
				{
					minLum = thisLum;
				}

				if(maxLum<thisLum)
				{
					maxLum = thisLum;
				}

			}
		}

		g2.dispose();

		BufferedImage imageBuff2 = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g3 = imageBuff2.createGraphics();
		g3.drawImage(scaledImg, 0, 0, new Color(0,0,0), null);

		for(int x = 0; x < width; x++)
		{
			for(int y = 0; y < height; y++)
			{
				double Val = ColourTools.colorValue(new Color(imageBuff.getRGB(x, y))); 
				double mult = (Val-minLum)/(maxLum-minLum);

				//g3.setColor(ColourTools.shade(mult, c, true));
				g3.setColor(ColourTools.shadeTrans(mult, c, 2,x,y));
				int dX = x;
				if(y%8==1||y%8==3)
				{
					dX+=2;
				}
				else if(y%8==2)
				{
					dX+=3;
				}
				else if(y%8==5||y%8==7)
				{
					dX-=2;
				}
				else if(y%8==6)
				{
					dX-=3;
				}
				int dX2 = dX;
				if(x==0||x==width-1)
				{
					dX2 = x;
				}
				g3.drawLine(dX, height - 1 - y, dX2, height - 1 - y);
			}
		}

		g3.dispose();

		return imageBuff2;
	}
}
