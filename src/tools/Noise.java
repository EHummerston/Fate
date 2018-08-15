package tools;

import java.util.Random;

public class Noise {

	public static double[][] gradientNoise(int width, int height, int n, int rnd)
	{
		Random rng = new Random(rnd);
		double[][] a = new double[width][height];
		
		double[][] b = new double[n][n];
		
		for(int i = 0; i < n; i++)
		{
			for(int j = 0; j < n; j++)
			{
				b[i][j] = rng.nextDouble();
			}
		}
		
		for(int x = 0; x < a.length; x++)
		{
			int xMaxDis = x;
			if(x > a.length/2)
			{
				xMaxDis = a.length-x;
			}
			
			for(int y = 0; y < a[x].length; y++)
			{
				int yMaxDis = y;
				if(y > a[x].length/2)
				{
					yMaxDis = a[x].length-y;
				}
				double[] values = new double[(int)Math.pow(n, 2)];
				for(int i = 0; i < b.length; i++)
				{
					for(int j = 0; j < b[i].length; j++)
					{
						
					}
				}
				
				for(int i = 0; i < values.length; i++)
				{
					
				}
				
			}
		}
		
		return a;
	}
	
}
