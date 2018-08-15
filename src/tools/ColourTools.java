package tools;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

public class ColourTools {

	public static int colorValue(Color c)
	{
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();
		return r+g+b;
	}

	public static boolean isTransparent(int pixel)
	{

		if( (pixel>>24) == 0x00 )
		{
			return true;
		}
		else return false;
	}

	public static Color shade(double r, Color[] c, boolean lumScale)
	{
		if(c.length <= 0)
		{
			return Color.BLACK;
		}
		else
		{
			Color ret = (c[0]);

			if(lumScale)
			{
				double[] factors = new double[c.length-1];
				factors[0] = 1;
				for(int i =2; i < c.length; i++)
				{
					factors[i-1] = (double)(colorValue(c[i])-colorValue(c[i-1]))/(colorValue(c[i-1])-colorValue(c[i-2]));
				}
				double finalFactor = 1;
				for(int i = 0; i < factors.length; i++)
				{
					finalFactor *= factors[i];
				}

				//This represents how much 'space' the brightest colour takes up, based on the pattern of the other colours.
				int max = colorValue(c[c.length-1])+(int)((colorValue(c[1])-colorValue(c[0]))*finalFactor);
				int min = colorValue(c[0]);

				r = r*(max-min) + min;

				for(int i = c.length-1; i>=0;i--)
				{
					if(r > colorValue(c[i]))
					{
						ret = (c[i]);

						break;
					}
				}
			}
			else
			{
				if(r>1)
				{
					r = 1;
				}
				if(r<0)
				{
					r = 0;
				}
				ret = c[(int)((c.length-1)*r)];
			}
			return ret;
		}
	}

	public static Color shadeTrans(double r, Color[] c, int depth, int x, int y)
	{
		if(c.length <= 0)
		{
			return Color.BLACK;
		}
		else
		{
			Color ret = (c[0]);

			double[] factors = new double[c.length-1];
			factors[0] = 1;
			for(int i =2; i < c.length; i++)
			{
				factors[i-1] = (double)(colorValue(c[i])-colorValue(c[i-1]))/(colorValue(c[i-1])-colorValue(c[i-2]));
			}
			double finalFactor = 1;
			for(int i = 0; i < factors.length; i++)
			{
				finalFactor *= factors[i];
			}

			//This represents how much 'space' the brightest colour takes up, based on the pattern of the other colours.
			int max = colorValue(c[c.length-1])+(int)((colorValue(c[1])-colorValue(c[0]))*finalFactor);
			int min = colorValue(c[0]);

			r = r*(max-min) + min;

			for(int i = c.length-1; i>=0;i--)
			{
				if(r > colorValue(c[i]))
				{
					ret = (c[i]);

					break;
				}
				else if(i>0)
				{
					int newCV = ((colorValue(c[i])-colorValue(c[i-1]))/2)+colorValue(c[i-1]);
					if(r > newCV)
					{
						if((( ((x%depth==0)&&(y%depth==0))||((x%depth!=0)&&(y%depth!=0)) )))
						{

							ret = (c[i]);

							break;
						}
					}
				}
			}

			return ret;
		}
	}	

	public static BufferedImage outline (BufferedImage img, Color blk, boolean edge)
	{
		Graphics2D g2d = img.createGraphics();
		g2d.setColor(blk);

		int blkVal = blk.getRGB();

		boolean[][] draws = new boolean[img.getWidth()][img.getHeight()];

		for(int x = 0; x < img.getWidth();x++)
		{
			for(int y = 0; y < img.getHeight(); y++)
			{
				boolean draw = false;
				if(isTransparent(img.getRGB(x, y)))
				{
					if(x != 0)
					{
						if(!isTransparent(img.getRGB(x-1,y))&&(img.getRGB(x-1, y)!=blkVal))
						{
							draw = true;
						}
					}
					if(y != 0)
					{
						if(!isTransparent(img.getRGB(x,y-1))&&(img.getRGB(x, y-1)!=blkVal))
						{
							draw = true;
						}
					}
					if(x != img.getWidth()-1)
					{
						if(!isTransparent(img.getRGB(x+1,y))&&(img.getRGB(x+1, y)!=blkVal))
						{
							draw = true;
						}
					}
					if(y != img.getHeight()-1)
					{
						if(!isTransparent(img.getRGB(x,y+1))&&(img.getRGB(x, y+1)!=blkVal))
						{
							draw = true;
						}
					}


				}

				if(edge)
				{
					if(x == 0 || y == 0 || x == img.getWidth()-1 || y == img.getHeight()-1)
					{
						if(!isTransparent(img.getRGB(x,y)))
						{
							draw = true;
						}
					}
				}

				draws[x][y] = draw;
			}
		}
		for(int x = 0; x < img.getWidth();x++)
		{
			for(int y = 0; y < img.getHeight(); y++)
			{
				if(draws[x][y])
				{
					g2d.drawLine(x,y,x,y);
				}
			}
		}
		g2d.dispose();
		return img;
	}

	public static double[] curve(int length, boolean half)
	{
		double[] ret = new double[length];

		int finish = length;
		if(!half)
		{
			finish /= 2;
		}

		for(int i = 0; i < length; i++)
		{
			if(i<finish)
			{
				ret[i] = (Math.sqrt(1-Math.pow((double)i/(finish-1), 2)));
			}
			else
			{
				ret[i] = 1-(Math.sqrt(1-Math.pow((double)(i-finish)/(finish-1), 2)));
			}
		}


		return ret;
	}

	public static double[][] sphere(int width, int height, int liteX, int liteY)
	{
		double[][] pic = new double[width][height];

		int rX = width/2;
		int rY = height/2; 

		for(int x = 1; x < width; x++)
		{
			int xDis = (x - rX);
			double a = Math.pow(((double)xDis/rX),2);

			for(int y = 1; y < height; y++)
			{

				int yDis = (y - rY);

				double b = Math.pow(((double)yDis/rY),2);

				if(a+b <= 1)
				{

					int xDisL = (x - liteX);
					double aL = Math.pow(((double)xDisL/width),2);					

					int yDisL = (y - liteY);
					double bL = Math.pow(((double)yDisL/height),2);

					double ret = 1-(aL+bL);
					if(ret>1)
					{
						ret = 1;
					}
					else if(ret <= 0.01)
					{
						ret=0.01;
					}

					pic[x][y] = ret;
				}
				else
				{
					pic[x][y] = 0;
				}
			}
		}

		return pic;
	}

	public static Color HSVtoRGB( float h, float s, float v )
	{
		if(h<0)
		{
			h+=360;
		}
		if(h>=360)
		{
			h-=360;
		}
		int i;
		float f, p, q, t;
		float r,g,b;
		if( s == 0 )
		{
			// achromatic (grey)
			return new Color((int)(255*v),(int)(255*v),(int)(255*v));
		}
		h /= 60;			// sector 0 to 5
		i = (int)Math.floor( h );
		f = h - i;			// factorial part of h
		p = v * ( 1 - s );
		q = v * ( 1 - s * f );
		t = v * ( 1 - s * ( 1 - f ) );
		switch( i )
		{
		case 0:
			r = v;
			g = t;
			b = p;
			break;
		case 1:
			r = q;
			g = v;
			b = p;
			break;
		case 2:
			r = p;
			g = v;
			b = t;
			break;
		case 3:
			r = p;
			g = q;
			b = v;
			break;
		case 4:
			r = t;
			g = p;
			b = v;
			break;
		default:		// case 5:
			r = v;
			g = p;
			b = q;
			break;
		}
		return new Color((int)(255*r),(int)(255*g),(int)(255*b));
	}


}
