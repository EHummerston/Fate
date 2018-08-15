package instance;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.Random;

import tools.ColourSets;
import tools.ColourTools;
import tools.Game;
import enviroment.Background;
import enviroment.Time;
import enviroment.Tree;

public class StageArt {
	private Random rng;

	private int[] grass, grass2;

	private BufferedImage[] staticBackgrounds;

	//Horizon is the distance from the bottom of the screen to the beginning of the sky.
	private int horizon;

	private BufferedImage[] scrolls;

	private Color[] cGrass, cWood, cLeaves, cSky, cMountains, cEarth;
	private Color outline;

	public StageArt(int drawWidth, int drawHeight, Time time, int hills, int rnd)
	{
		rng = new Random(rnd);
		horizon = drawHeight/3;
		
		float light = time.getLight();
		
		float H1, H2, S1, S2, V1, V2;
		
		//cSky = ColourSets.ENVIRONMENT_SKY;
		H1 = 203 + (45*(1-light));
		H2 = H1 - 2;
		V1 = light*0.8f + 0.15f;
		cSky = ColourSets.HSVSet(H1, .51f, V1, H2, .24f, .98f, 5);
		
		//cMountains = ColourSets.ENVIRONMENT_SEA;
		H1 = 222 + (45*(1-light));
		H2 = H1 - 14;
		cMountains = ColourSets.HSVSet(H1, .57f, V1, H2, .36f, .95f, 3);
		
		//cGrass = ColourSets.ENVIRONMENT_GRASS;
		H1 = 206 - (light*100);
		H2 = H1-20;
		//S1 = .6f - (light*.25f);
		S1 = .35f;
		S2 = S1*1.5f;
		V1 = .7f*(light/2+0.5f);
		V2 = V1 * 1.3f;
		cGrass = ColourSets.HSVSet(H1,S1,V1,H2,S2,V2,3);
		
		//cWood = ColourSets.ENVIRONMENT_WOOD;
		H1 = 30*light;
		H2 = H1*1.5f;
		S1 = .4f*(light/2+0.5f);
		S2 = S1*1.2f; 
		V1=.31f*(light/2+0.5f);
		V2 = V1*2.8f;
		cWood = ColourSets.HSVSet(30,S1,V1,45,S2,V2,12);
		
		cLeaves = ColourSets.ENVIRONMENT_LEAVES;
		
		
		H1 = 180 - (light*12);
		S1 = .2f - (light*.15f);
		V1 = .16f+(light*.23f);
		//outline = new Color(40,32,40);
		outline = ColourTools.HSVtoRGB(H1, S1, V1);
		//outline = new Color(95,100,99);
		
		if(Game.SPOOKY)
		{
			cSky = ColourSets.ENVIRONMENT_SKY_SPOOKY;
			cGrass = ColourSets.ENVIRONMENT_GRASS_SPOOKY;
			cWood = ColourSets.ENVIRONMENT_WOOD_SPOOKY;
			cMountains = ColourSets.ENVIRONMENT_SEA_SPOOKY;
			cLeaves = ColourSets.ENVIRONMENT_LEAVES_SPOOKY;
		}
		cEarth = cWood;
		System.out.println("Drawing sky...");
		staticBackgrounds = new BufferedImage[4];

		staticBackgrounds[0] = Background.sky(drawWidth,drawHeight-horizon,cSky,time,rng.nextInt());
		staticBackgrounds[2] = Background.reflection(staticBackgrounds[0],drawWidth,horizon,cMountains);
		//		staticBackgrounds[1] = Tree.tree(drawWidth,drawHeight,1,1,rng.nextInt());
		//		staticBackgrounds[2] = Tree.tree(drawWidth,drawHeight,1,1,rng.nextInt());
		//		staticBackgrounds[3] = Tree.tree(drawWidth,drawHeight,1,1,rng.nextInt());
		System.out.println("Sky finished!");

		System.out.println("Making scrolling backgrounds...");
		initParalax(hills+1, drawWidth, drawHeight);
		System.out.println("Scrolling backgrounds done!");

		System.out.println("Setting grass levels...");
		grass = initGrass(331,0,3,rng.nextInt());
		grass2 = initGrass2(197,0,3,rng.nextInt());
		System.out.println("Grass initialised!");
	}	

	private void initParalax(int pics, int drawWidth, int drawHeight)
	{
		scrolls = new BufferedImage[pics];

		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
		GraphicsConfiguration gc = gs.getDefaultConfiguration();



		for(int i = 0; i < scrolls.length; i++)
		{

			System.out.println("Drawing background layer " + i + " from the front.");
			if(i == scrolls.length - 1)
			{
				System.out.println("\tThe last background layer.");
			}

			int height = drawHeight;
			//			int height = (int)(drawHeight*(1+rng.nextDouble()/2)) * (i+1) / scrolls.length / 2 + drawHeight/3;
			int width = (int)(drawWidth * (1+rng.nextDouble()/2));
			int groundHeight = (horizon * (i+2)) / (scrolls.length+1);

			int hillPosPow = rng.nextInt(1)+2;
			int hillNegPow = rng.nextInt(1)+2;
			float hillPosScale = rng.nextFloat() * (float)(scrolls.length-(i-1))/(scrolls.length+1);
			float hillNegScale = rng.nextFloat() * (float)(scrolls.length-(i-1))/(scrolls.length+1);
			int hillOffset = rng.nextInt(width);

			BufferedImage img = gc.createCompatibleImage(width, height, Transparency.BITMASK);
			Graphics2D g2 = img.createGraphics();

			int treeCount = (int)Math.sqrt(width*width+height*height) / 70;
			System.out.println("This layer has " + treeCount + " trees.");
			for(int j = 0; j < treeCount; j++)
			{
				int heightFract = rng.nextInt(10)+3;
				int treeCenX  = (width*j)/treeCount; 

				double sinMult = Math.sin((double)((hillOffset-treeCenX)%width)/(width)*(Math.PI*2));
				if(sinMult<0)
				{
					sinMult = -Math.abs(Math.pow(sinMult, hillNegPow));
					sinMult*=hillNegScale;	
				}
				else
				{
					sinMult = Math.abs(Math.pow(sinMult, hillPosPow));
					sinMult*=hillPosScale;	
				}
				int hill = groundHeight + (int)(horizon * sinMult/2);

				int treeHeight = (height-hill) * (heightFract-1) / heightFract;
				int treeWidth = treeHeight;
				int x = treeCenX - treeWidth/2;

				BufferedImage tree = Tree.tree(treeWidth, treeHeight, i, scrolls.length, rng.nextInt(), cLeaves, cWood, outline);
				g2.drawImage(tree, null, x, height-(hill-5)-treeHeight);
				if(x<0)
				{
					g2.drawImage(tree, null, width+x, height-(hill-5)-treeHeight);
				}
				if(x+treeWidth>width)
				{
					g2.drawImage(tree, null, x-width, height-(hill-5)-treeHeight);

				}
				System.out.print("Tree " + (j+1) + " drawn, ");
			}
			System.out.println();
			//Draw the grass at the bottom.
			int[] btmGrass = initGrass(width,0,(scrolls.length-i),rng.nextInt());
			int[] btmGrass2 = initGrass2(width,0,(scrolls.length-i),rng.nextInt());

			BufferedImage grassImg = gc.createCompatibleImage(width, height, Transparency.BITMASK);
			Graphics2D grassG2 = grassImg.createGraphics();
			
			for(int j = 0; j < width; j++)
			{
				
				double sinMult = Math.sin((double)((hillOffset-j)%width)/(width)*(Math.PI*2));
				if(sinMult<0)
				{
					sinMult = -Math.abs(Math.pow(sinMult, hillNegPow));
					sinMult*=hillNegScale;	
				}
				else
				{
					sinMult = Math.abs(Math.pow(sinMult, hillPosPow));
					sinMult*=hillPosScale;	
				}
				int hill = groundHeight + (int)(horizon * sinMult/2);
				
				double grassC = (double)(scrolls.length - (i))/scrolls.length; 
				int grassH = hill + btmGrass[j] + 2;
				grassG2.setColor(ColourTools.shade(grassC,cGrass,false));
				grassG2.drawLine(j, height, j, height - 1 - (grassH));

				double grassC2 = grassC * 2 / 3; 
				grassG2.setColor(ColourTools.shade(grassC2,cGrass,false));
				grassG2.drawLine(j, height, j, height - 1 - (grassH-2-btmGrass2[j]/2+(btmGrass2[j]%2)));

				double grassC3 = grassC / 3; 
				grassG2.setColor(ColourTools.shade(grassC3,cGrass,false));
				grassG2.drawLine(j, height, j, height - 1 - (grassH-2-btmGrass2[j]/2));
				
				
			}

			grassG2.dispose();
			if(i == (scrolls.length-1))
			{
				ColourTools.outline(grassImg, outline, false);
			}
			g2.drawImage(grassImg,null,0,0);
			g2.dispose();
			scrolls[i] = img;

		}
	}

	public static int[] initGrass(int length, int min, int max, int rnd)
	{
		int[] out = new int[length];

		Random rngS = new Random(rnd);

		int curVal = (max-min)/2 + min;
		for(int i = 0; i < length; i++)
		{

			out[i] = curVal;
			curVal += rngS.nextInt(3)-1;
			if(curVal < min)
			{
				curVal = min;
			}
			else if(curVal > max)
			{
				curVal = max;
			}
		}

		//Set the last int to be equal to the first
		out[length-1] = out[0];

		//Now work backwards until there is no difference greater than one between two ints
		for(int i = length-2; Math.abs(out[i+1] - out[i]) > 1 && i > 0; i-- )
		{

			int diff = out[i] - out[i+1];
			out[i] = out[i+1] + diff/Math.abs(diff);
		}
		return out;
	}

	private static int[] initGrass2(int length, int min, int max, int rnd)
	{
		int[] out = new int[length];

		Random rngS = new Random(rnd);

		for(int i = 0; i < length; i++)
		{
			out[i] = rngS.nextInt(2*(max-min))+min*2;
		}
		return out;
	}

	public BufferedImage getStaticBackgrounds(int i)
	{
		return staticBackgrounds[i];
	}

	public int getStaticBackgroundsCount()
	{
		return staticBackgrounds.length;
	}

	public int getGrass(int i)
	{
		return 2 + (grass[Math.abs(i)%grass.length]);
	}


	public int getGrass2(int i)
	{
		return 2 + (grass2[i%grass2.length])/2;
	}

	public boolean getGrass2Bool(int i)
	{
		return ((grass2[i%grass2.length])%2 == 1);
	}

	public BufferedImage getScroll(int i)
	{
		return scrolls[i];
	}

	public int getScrollSize() {

		return scrolls.length;
	}

	public Color[] getcGrass() {
		return cGrass;
	}

	public void setcGrass(Color[] cGrass) {
		this.cGrass = cGrass;
	}

	public Color[] getcWood() {
		return cWood;
	}

	public void setcWood(Color[] cWood) {
		this.cWood = cWood;
	}

	public Color[] getcLeaves() {
		return cLeaves;
	}

	public void setcLeaves(Color[] cLeaves) {
		this.cLeaves = cLeaves;
	}

	public Color[] getcSky() {
		return cSky;
	}

	public void setcSky(Color[] cSky) {
		this.cSky = cSky;
	}

	public Color[] getcEarth() {
		return cEarth;
	}

	public void setcEarth(Color[] cEarth) {
		this.cEarth = cEarth;
	}

	public Color getOutline() {
		return outline;
	}

	public void setOutline(Color outline) {
		this.outline = outline;
	}

}
