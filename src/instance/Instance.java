package instance;

import instance.Stage.Platform;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import javax.imageio.ImageIO;

import tools.ColourSets;
import tools.ColourTools;
import tools.Game;
import tools.LineTools;
import tools.Point2DD;
import tools.Point2DInt;
import Character.Title;
import enviroment.Background;
import enviroment.Time;

public class Instance {

	ArrayList<Actor> actors = new ArrayList<Actor>();
	Title[] titles;
	String[] names;
	BufferedImage texts;
	Stage stage;
	StageArt stageArt;
	final Time time;
	Random rng;
	private Point2DD camera;
	private int drawWidth = Game.SCREEN_WIDTH;
	private int drawHeight = Game.SCREEN_HEIGHT;

	double camMove = 40;

	boolean drawForeground = true,
			drawBackground = true,
			drawBackBackground = true,
			drawData = false,
			
			end = false;

	//This will sort the actors so that the highest entries are first.
	//	These are drawn first so the actors lower are drawn on top, and so are more visible.
	Comparator<Actor> actorHeightSort = new Comparator<Actor>() {

		@Override
		public int compare(Actor arg0, Actor arg1) {
			return (arg1.getOrigin().getY() - arg0.getOrigin().getY());
		}                                                                                                                                                                                            ;

	};

	public Instance(Time time, int rnd)
	{
		rng = new Random(rnd);
		System.out.print("Time is: ");
		System.out.println(time.toString());
		this.time = time;
		
		System.out.println("Creating stage...");
		int hills = rng.nextInt(3);
		stage = new Stage(3000, 64,hills, rng.nextInt());
		System.out.println("Platforms and floor done!...");

		System.out.println("Initialising level art...");
		stageArt = new StageArt(drawWidth, drawHeight,time, hills,rng.nextInt());
		System.out.println("Art made!...");

		camera = new Point2DD(0,0);
		//		actors.add(new Actor(30,80));
		//		actors.add(new Actor(10,60));
		int actorCount = 2 + rng.nextInt(drawWidth/200)+drawWidth/200;
		System.out.println("Making " +actorCount+" actors.");
		titles = new Title[actorCount];
		names = new String[actorCount];

		for(int i = 0; i < actorCount; i++)
		{

			int wordCount = rng.nextInt(4)+1;

			int[] title = new int[wordCount];
			for(int j = 0; j< wordCount;j++)
			{
				title[j] = rng.nextInt();
			}
			titles[i] = new Title(title,title,(rng.nextInt(5)==0));
			names[i] = Name();
			char[] printStr = titles[i].toString(true).toCharArray();
			printStr[0] -= 32;
			System.out.println(printStr);
			int x;

			if(i==0)
			{
				x = 16+rng.nextInt(32);

			}
			else
			{
				x = actors.get(i-1).getOrigin().getX()+64+rng.nextInt(16);
			}
			int y = stage.getBase(x)+rng.nextInt(50)+10;
			actors.add(new Actor(x,y,stageArt.getOutline()));
		}
		texts = drawTitles();
		System.out.println("Actors generated.");
		//		String[] wordeth = {"warrior","dragon","fire"};
		//		String[] worth = {" of "};
		//		System.out.println(new Title(Title.toIntArray(wordeth),Title.toIntArrayConjunctions(worth),true).toString(true));
		//saveSpriteSheet();
	}

	public void update()
	{
		moveActors();
		updateCamera();
		
		if(camera.getXInt()+drawWidth+10 >= stage.getStageWidth())
		{
			end = true;
		}
	}

	private void moveActors()
	{
		for(int i = 0; i < actors.size(); i++)
		{
			if(!actors.get(i).isAir())
			{
				if(rng.nextInt(30) == 0)
				{
					actors.get(i).jump(rng.nextInt(2)+1);
				}
				else
				{
					actors.get(i).setXVel(actors.get(i).getMoveSpeed());
				}
			}
			actors.get(i).doMovement(stage);

		}
	}

	private void updateCamera()
	{

		if(actors.size()!=0)
		{
			double avgX = 0;
			double avgY = 0;
			for(int i = 0; i < actors.size(); i++)
			{
				avgX += actors.get(i).getOrigin().getX();
				avgY += actors.get(i).getOrigin().getY();
			}
			avgX /= actors.size();
			avgY /= actors.size();

			avgX -= drawWidth/2;
			avgY -= drawHeight/2;

			if((avgX < camera.getX())||(avgX > stage.getStageWidth()-drawWidth))
			{
				avgX = camera.getX();

			}
			if(avgY < 0)
			{
				avgY = 0;
			}
			camera.setLocation(avgX, avgY);
		}
	}

	public void draw(Graphics2D g2, int drawWidth, int drawHeight, float fps)
	{
		this.drawWidth = drawWidth;
		this.drawHeight = drawHeight;

		if(drawBackBackground)
		{
			//Draw the unmoving backgrounds
			drawStaticBackground(g2);
		}

		if(drawBackground)
		{

			//Draw the scrolling backgrounds
			drawScrollingBackgrounds(g2);
		}

		if(drawForeground)
		{
			//Draw the platforms and their supports
			drawPlatforms(g2,true);

			//Draw some actors, yo.
			drawActors(g2);

			//Draw the bottom of the stage.
			drawBase(g2,true);
		}

		if(drawData)
		{
			drawBase(g2,false);
			
			g2.setColor(new Color(128,0,0));
			int bSH = stage.getBaseScaleHoriz();
			for(int i = 0; i < (drawWidth/bSH)+1; i+=1)
			{
				int x = (bSH-(camera.getXInt()%bSH)) + bSH*i;
				int y = stage.getBase((camera.getXInt()/bSH+1)*bSH+(i*bSH));
				g2.drawLine(x, drawHeight-1, x, drawHeight - y);
			}
			
			drawPlatforms(g2,false);
			drawHitboxes(g2);
			g2.drawImage(texts, null, 0, 0);
			String fpsString = new String("FPS: ");
			fpsString += (int)Math.ceil(fps);
			g2.setColor(Color.RED);
			g2.drawString(fpsString, 0, drawHeight - 1);
			g2.drawString(time.toString(), 0, drawHeight - 15);
			BufferedImage actorTestSprite = actors.get(0).getSprite(AnimationSkeleton.ANIMATION_STAND);
			g2.drawImage(actorTestSprite,null,drawWidth-1-actorTestSprite.getWidth(),0);


		}
		//drawStaticBackground(g2);
	}

	private void drawStaticBackground(Graphics2D g2)
	{

		BufferedImage sky = stageArt.getStaticBackgrounds(0);
		BufferedImage farClouds = stageArt.getStaticBackgrounds(1);
		BufferedImage mountains = stageArt.getStaticBackgrounds(2);
		BufferedImage nearClouds = stageArt.getStaticBackgrounds(3);
		g2.drawImage(sky,null,0,0);
		g2.drawImage(mountains,null,0,drawHeight-1-mountains.getHeight());


	}

	private void drawScrollingBackgrounds(Graphics2D g2)
	{
		for(int i = stageArt.getScrollSize()-1; i >= 0; i--)
		{
			BufferedImage drawd = stageArt.getScroll(i);

			int offsetX = (int)(camera.getX()/(i+2));
			int offsetY = (int)(camera.getY()/((i+2)));

			int x = -offsetX - drawd.getWidth();

			while(x < this.drawWidth)
			{
				g2.drawImage(drawd,null, x,this.drawHeight - 1 -(drawd.getHeight()-offsetY));
				x+=drawd.getWidth();
			}






		}
	}

	private void drawPlatforms(Graphics2D g2, boolean detail)
	{
		ArrayList<Platform> platforms = stage.getPlatforms();

		//Cycle through each platform
		for(int i = 0; i < platforms.size();i++)
		{
			//Get the platform information
			Platform drawded = platforms.get(i);

			//This is where on the screen the platform is drawn 
			int pX = (drawded.origin.getX() - camera.getXInt());
			int pY = drawHeight - 1 - (drawded.origin.getY() - camera.getYInt());
			int pX2 = pX + drawded.length;

			//Confirm that the platform is within the bounds of the screen
			if(( pX < this.drawWidth ) && ( pX2 > 0 ) && (pY < drawHeight) )
			{
				if(detail)
				{
					//For each pixel from the start to finish of the platform
					for(int j = 0; j <= drawded.length;j++)
					{


						//This is our current pixel's x value.
						int pX3 = j + pX;

						//Draw an outline if it's the end of one of the platforms
						if(j == 0 || j == drawded.length)
						{
							g2.setColor(stageArt.getOutline());
						}
						//Otherwise, draw it as the earth colour.
						else
						{
							g2.setColor(stageArt.getcEarth()[stageArt.getcEarth().length-1]);
						}

						//Draw the earthy line from the platform's height to the ground
						g2.drawLine(pX3, pY + 1,  pX3, drawHeight);	

						//Drawing the grass
						g2.setColor(stageArt.getcGrass()[stageArt.getcGrass().length-2]);
						int grass = stageArt.getGrass(j+drawded.origin.getY());
						g2.drawLine(pX3, pY,  pX3, (pY+grass));

						//The outline at the bottom
						g2.setColor(stageArt.getOutline());
						g2.drawLine(pX3,(pY+grass),  pX3, (pY+grass));

						//If this is the first or last pixel, draw an outline next to it 
						if(j == 0)
						{
							g2.drawLine(pX3-1,  (pY+1),  pX3-1, (pY+grass-1));
						}
						if(j == drawded.length)
						{
							g2.drawLine(pX3+1,  (pY+1),  pX3+1, (pY+grass-1));
						}
					}
				}
				//Draw the outline at the top
				g2.setColor(stageArt.getOutline());
				if(!detail)
				{
					g2.setColor(Color.BLUE);
				}
				g2.drawLine(pX,  pY,  pX2,  pY);
			}
		}
	}

	private void drawActors(Graphics2D g2)
	{

		Collections.sort(actors, actorHeightSort);

		for(int i = 0; i < actors.size(); i++)
		{




			g2.setColor(Color.BLUE);
			int aX = (actors.get(i).getOrigin().getX()-camera.getXInt());
			int aY = drawHeight - 1 - (actors.get(i).getOrigin().getY()-camera.getYInt());
			BufferedImage sprite =actors.get(i).getSprite(); 
			if(sprite!=null)
			{
				g2.drawImage(sprite,null, aX-(sprite.getWidth()/2),aY-sprite.getHeight());
			}
			else
			{

				g2.drawLine(aX, drawHeight-aY, aX, drawHeight - (aY+32));

				g2.drawLine(aX+(rng.nextInt(5)+1), drawHeight - (aY+18+(rng.nextInt(10)-5)), aX, drawHeight - (aY+18));
				g2.drawLine(aX-(rng.nextInt(5)+1), drawHeight - (aY+18+(rng.nextInt(10)-5)), aX, drawHeight - (aY+18));


			}
			if(actors.get(i).isAir())
			{
				g2.setColor(Color.RED);
				//g2.drawLine(aX, drawHeight - aY, aX + (int)(actors.get(i).getAirVel().getX()*3), drawHeight - (aY + (int)(actors.get(i).getAirVel().getY()*3)));
			}
		}
	}

	private void drawHitboxes(Graphics2D g2)
	{
		for(int i = 0; i < actors.size(); i++)
		{

			g2.setColor(Color.GREEN);
			Rectangle hBox = actors.get(i).getHitBox();
			int x1 = hBox.x - camera.getXInt();
			int y1 = drawHeight  - 1 - (hBox.y - camera.getYInt() );
			int x2 = x1 + hBox.width;
			int y2 = y1 + hBox.height;
			g2.drawLine(x1, y1, x2, y1);
			g2.drawLine(x1, y1, x1, y2);
			g2.drawLine(x2, y1, x2, y2);
			g2.drawLine(x1, y2, x2, y2);

			int aX = (actors.get(i).getOrigin().getX()-camera.getXInt());
			int aY = drawHeight - 1 - (actors.get(i).getOrigin().getY()-camera.getYInt());
			g2.setColor(Color.RED);
			g2.drawLine(aX-2, aY, aX+2, aY);
			g2.drawLine(aX, (aY-2), aX, (aY+2));
		}
	}

	private void drawBase(Graphics2D g2, boolean grassDraw)
	{
		//BufferedImage animSpr = anim.getSprite();
		//g2.drawImage(animSpr, null,0,drawHeight-(stage.getBase(0)+animSpr.getHeight()));

		for(int i = 0; i < drawWidth; i++)
		{
			int x = i + camera.getXInt();
			int base = stage.getBase(x) - camera.getYInt();
			if(grassDraw )
			{
				int grass = stageArt.getGrass(x) +  base;
				int grass2 = grass - stageArt.getGrass2(x);

				Color[] cGrass = stageArt.getcGrass();

				//The dark-green base
				g2.setColor(cGrass[cGrass.length-3]);
				g2.drawLine(i,drawHeight - 0,i,drawHeight - (int)grass2);

				//The light-green top
				g2.setColor(cGrass[cGrass.length-1]);
				g2.drawLine(i,drawHeight - grass,i,drawHeight - (int)(grass2));

				//The outline
				g2.setColor(stageArt.getOutline());
				int grass3 = grass;

				if(i> 0 && i < drawWidth-1)
				{
					//Checking for cliffs
					int baseBe = stage.getBase(x-1) - camera.getYInt() + stageArt.getGrass(x-1) -1;
					int baseAf = stage.getBase(x+1) - camera.getYInt() + stageArt.getGrass(x+1) -1;
					if(baseBe > grass3)
					{
						grass3 = baseBe; 
					}
					if(baseAf > grass3)
					{
						grass3 = baseAf; 
					}
				}
				g2.drawLine(i,drawHeight - grass,i,drawHeight - grass3);

				//The occasional mid-green dot
				if(stageArt.getGrass2Bool(x))
				{
					g2.setColor(cGrass[cGrass.length-2]);
					g2.drawLine(i,drawHeight - (int)grass2,i,drawHeight - (int)grass2);
				}
			}
			else
			{
				g2.setColor(new Color(128,128,128));
				g2.drawLine(i,drawHeight - 0,i,drawHeight - base);
			}
		}

	}

	private BufferedImage drawTitles()
	{
		BufferedImage text = new BufferedImage(drawWidth, (titles.length + 2) * 15,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2d = text.createGraphics();

		for(int i = 0;i<titles.length;i++)
		{
			BufferedImage colorGet = actors.get(i).getSprite(AnimationSkeleton.ANIMATION_WALK,0); 
			g2d.setColor(new Color(colorGet.getRGB(colorGet.getWidth()/2,colorGet.getHeight()/2)));
			g2d.drawString(names[i]+", "+titles[i].toString(true), 3 + i*10, 15*(i+1));
		}
		g2d.dispose();
		text = ColourTools.outline(text, Color.BLACK, true);
		return text;
	}

	private void saveSpriteSheet()
	{
		int spriteCount = AnimationSet.animationFrameCount;

		int maxPerRow = 10;
		int rows = spriteCount / maxPerRow + 1;

		System.out.println("Frames: " + spriteCount + ", rows: " + rows);

		BufferedImage saveFile = new BufferedImage(maxPerRow*32, rows*32, BufferedImage.TYPE_INT_RGB);
		Graphics2D saveScreen = saveFile.createGraphics();


		saveScreen.setColor(ColourSets.ENVIRONMENT_SKY_SPOOKY[0]);
		saveScreen.fillRect(0, 0, saveFile.getWidth(), saveFile.getHeight());

		for(int i = 0;i<spriteCount;i++)
		{
			int row = i/maxPerRow;
			int column = i % maxPerRow;
			saveScreen.drawImage(actors.get(0).getSprite(i),null,32*column,32*row);
		}
		saveScreen.dispose();

		File outputfile = new File("imgs\\sprite.bmp");
		try {
			ImageIO.write(saveFile, "bmp", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();


		}

	}

	private String Name()
	{
		String returnString = new String();
		int charCount = rng.nextInt(5) + 2;
		if(rng.nextBoolean())
		{
			charCount++;
		}
		boolean vowel = rng.nextBoolean();
		boolean cap = true;

		for(int i = 0; i < charCount; i++)
		{
			char addChar;

			if(vowel)
			{
				addChar = Title.Vowel(rng.nextInt(5), cap);
				cap = false;
				if(rng.nextInt(3)==0)
				{
					returnString+=addChar;
					addChar = Title.Vowel(rng.nextInt(5), cap);
				}
			}
			else
			{
				addChar = Title.Constanant(rng.nextInt(21), cap);
				cap = false;
				if((addChar=='t'||addChar=='T'||addChar=='c'||addChar=='C')&&(rng.nextInt(4)==0))
				{

					returnString+=addChar;
					addChar = 'h';
				}
				else if(addChar=='q')
				{
					if(rng.nextInt(2)==0)
					{
						addChar = 'c';
					}
					else
					{
						returnString+=addChar;
						addChar='u';
					}
				}
				else if(rng.nextInt(10)==0&&i!=0)
				{
					returnString+=addChar;
				}
			}


			returnString+=addChar;

			vowel=!vowel;
		}

		return returnString;
	}

	public void toggleDrawForeground()
	{
		drawForeground = !drawForeground;
	}

	public void toggleDrawBackground()
	{
		if(drawBackground)
		{
			drawBackground = false;
		}
		else if(drawBackBackground)
		{
			drawBackBackground = false;
		}
		else
		{
			drawBackground = true;
			drawBackBackground = true;
		}
		
	}

	public void toggleDrawData()
	{
		drawData = !drawData;
	}
	
	public void toggleEnd()
	{
		end = !end;
	}
	
	public boolean isEnd()
	{
		return end;
	}
	
}
