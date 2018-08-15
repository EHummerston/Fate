package instance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import tools.Point2DInt;

public class Stage {

	//	final Color g1 = new Color(216,248,56),
	//			g2 = new Color(96,248,32),
	//			g3 = new Color(40,152,56),
	//			g4 = new Color(40,64,48),
	//
	//			br1 = new Color(200,176,104),
	//			br2 = new Color(152,128,72),
	//			br3 = new Color(112,96,48),
	//			br4 = new Color(80,64,48),
	//
	//			bl1 = new Color(72,64,128),
	//			bl2 = new Color(48,40,88),
	//
	//			bk = new Color(40,32,40);

	private ArrayList<Platform> platforms = new ArrayList<Platform>();
	private final int stageWidth, stageHeight, gridWidth;
	private final int[] base;
	private Random rng;

	private int baseScaleHoriz, baseScaleVert;

	public class Platform {

		public Point2DInt origin;
		public int length;

		private Platform(int x, int y, int length)
		{
			this.origin = new Point2DInt(x,y);
			this.length = length;
		}
	}

	public Stage(int width, int height, int hills, int rndInt)
	{
		rng = new Random(rndInt);
		baseScaleVert = 64/(5-hills);
		baseScaleHoriz = baseScaleVert*(4-hills);

		gridWidth = (int)(width/baseScaleHoriz);
		this.stageWidth = gridWidth * baseScaleHoriz;
		this.stageHeight = height;

		//TODO: Initialise base of stage and platforms properly.

		base = new int[gridWidth+1];
		for(int i = 0; i < base.length; i++)
		{
			base[i] = rng.nextInt(stageHeight/baseScaleVert)+1;
		}

		System.out.println((stageHeight/baseScaleVert)+" variations in stage height possible.");
		for(int i = 0; i < stageWidth/100; i++)
		{
			int pW = rng.nextInt(baseScaleHoriz*3)+baseScaleHoriz/2;
			int pX = rng.nextInt(stageWidth-pW);
			int pY = rng.nextInt(stageHeight/2)+baseScaleVert+this.getBase(pX);
			platforms.add(new Platform(pX,pY,pW));
		}

		//This will sort the platforms so that the highest entries are first.
		//	These are drawn first so there 'supports' don't block out the lower ones.
		Comparator<Platform> heightCompare = new Comparator<Platform>() {

			@Override
			public int compare(Platform arg0, Platform arg1) {
				return (int)(arg1.origin.getY() - arg0.origin.getY());
			}

		};
		Collections.sort(platforms, heightCompare);


	}

	public ArrayList<Platform> getPlatforms() {
		return platforms;
	}

	public static ArrayList<Platform> getPlatformsSpecific(int x, ArrayList<Platform> inList)
	{
		ArrayList<Platform> newList = new ArrayList<Platform>();
		for(int i = 0; i < inList.size(); i++)
		{
			if((inList.get(i).origin.getX() <= x)&&
					(inList.get(i).origin.getX() + inList.get(i).length >= x))
			{
				newList.add(inList.get(i));
			}
		}

		return newList;
	}

	public void setPlatforms(ArrayList<Platform> platforms) {
		this.platforms = platforms;
	}

	public int getStageWidth() {
		return stageWidth;
	}

	public int getStageHeight() {
		return stageHeight;
	}

	public int getBase(int i) {

		if((i < 0)||(i>=stageWidth))
		{
			return stageHeight*2;
		}
		else
		{
			int currentBaseID = (i/baseScaleHoriz);
			int depth = i%baseScaleHoriz;
			int retInt = base[currentBaseID]*baseScaleVert;

			if(base[currentBaseID]==base[currentBaseID+1]-1)
			{	
				//This is lower than the next, slope upward.
				retInt += baseScaleVert * depth / baseScaleHoriz;
			}
			else if(base[currentBaseID]==base[currentBaseID+1]+1)
			{
				//This is higher than the next, slope downward.
				retInt -= baseScaleVert * depth / baseScaleHoriz;
			}
			else if(depth>(baseScaleHoriz/2))
			{
				retInt =  base[currentBaseID+1]*baseScaleVert;
			}


			return retInt;
		}
		//return base[i%stageWidth];
	}

	public int getBaseScaleHoriz() {
		return baseScaleHoriz;
	}

	public int getBaseScaleVert() {
		return baseScaleVert;
	}


}
