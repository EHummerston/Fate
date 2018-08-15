package instance;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.Random;

import tools.ColourTools;
import tools.Game;

public class AnimationSet {

	BufferedImage[] spritesWalk,
	spritesAirNeutral,
	spritesAirUp,
	spritesAirDown,
	spritesStand;

	//pleeeeaaaase keep this as a power of 2:
	final static int animationFrameCount = 8;

	//How far they walk in one loop (two steps)
	final int pixPerLoop = 28;

	double moveFrameNo = 0;
	double idleFrameNo = 0;
	
	int r;
	int g;
	int b;

	Color outline;
	
	public AnimationSet(Color outline)
	{
		this.outline = outline;

		initSprites();
	}

	private void initSprites()
	{

		final int height = 28, width = 28;

		Random rng = new Random();
		r = 128 + rng.nextInt(128);
		g = 128 + rng.nextInt(128);
		b = 128 + rng.nextInt(128);

		if(Game.SPOOKY)
		{
			g = r;
			b = r;
		}
		
		spritesWalk = new BufferedImage[animationFrameCount];
		spritesAirNeutral = new BufferedImage[animationFrameCount];
		spritesAirUp = new BufferedImage[animationFrameCount];
		spritesAirDown = new BufferedImage[animationFrameCount];
		spritesStand = new BufferedImage[animationFrameCount];

		for(int i = 0; i < animationFrameCount; i++)
		{
			AnimationSkeleton bonesy;

			bonesy = new AnimationSkeleton(AnimationSkeleton.ANIMATION_WALK,height, width, i, animationFrameCount,0,0);
			spritesWalk[i] = drawAnimtest(bonesy);
			bonesy = new AnimationSkeleton(AnimationSkeleton.ANIMATION_AIR_NEUTRAL,height, width, i, animationFrameCount,0,0);
			spritesAirNeutral[i] = drawAnimtest(bonesy);
			bonesy = new AnimationSkeleton(AnimationSkeleton.ANIMATION_AIR_UP,height, width, i, animationFrameCount,0,0);
			spritesAirUp[i] = drawAnimtest(bonesy);
			bonesy = new AnimationSkeleton(AnimationSkeleton.ANIMATION_AIR_DOWN,height, width, i, animationFrameCount,0,0);
			spritesAirDown[i] = drawAnimtest(bonesy);
			bonesy = new AnimationSkeleton(AnimationSkeleton.ANIMATION_STAND,height, width, i, animationFrameCount,0,0);
			spritesStand[i] = drawAnimtest(bonesy);
		}

	}

	private BufferedImage drawAnimtest(AnimationSkeleton bonesy)
	{
		BufferedImage img = new BufferedImage(32,40,BufferedImage.TYPE_4BYTE_ABGR);
		Graphics2D g2 = img.createGraphics();
		//g2.setColor(new Color(r,g,b));
		
		g2.setColor(new Color(r*4/5,g*4/5,b));

		g2.drawLine(bonesy.shoulderBack.getXInt(), img.getHeight() - bonesy.shoulderBack.getYInt(), bonesy.neck.getXInt(), img.getHeight() - bonesy.neck.getYInt());
		g2.drawLine(bonesy.shoulderBack.getXInt(), img.getHeight() - bonesy.shoulderBack.getYInt(), bonesy.elbowBack.getXInt(), img.getHeight() - bonesy.elbowBack.getYInt());
		g2.drawLine(bonesy.wristBack.getXInt(), img.getHeight() - bonesy.wristBack.getYInt(), bonesy.elbowBack.getXInt(), img.getHeight() - bonesy.elbowBack.getYInt());
		g2.drawLine(bonesy.wristBack.getXInt(), img.getHeight() - bonesy.wristBack.getYInt(), bonesy.fingerBack.getXInt(), img.getHeight() - bonesy.fingerBack.getYInt());

		g2.drawLine(bonesy.waist.getXInt(), img.getHeight() - bonesy.waist.getYInt(), bonesy.hipBack.getXInt(), img.getHeight() - bonesy.hipBack.getYInt());
		g2.drawLine(bonesy.kneeBack.getXInt(), img.getHeight() - bonesy.kneeBack.getYInt(), bonesy.hipBack.getXInt(), img.getHeight() - bonesy.hipBack.getYInt());
		g2.drawLine(bonesy.kneeBack.getXInt(), img.getHeight() - bonesy.kneeBack.getYInt(), bonesy.ankleBack.getXInt(), img.getHeight() - bonesy.ankleBack.getYInt());
		g2.drawLine(bonesy.toeBack.getXInt(), img.getHeight() - bonesy.toeBack.getYInt(), bonesy.ankleBack.getXInt(), img.getHeight() - bonesy.ankleBack.getYInt());

		g2.setColor(new Color(r,g,b));

		g2.drawLine(bonesy.head.getXInt(), img.getHeight() - bonesy.head.getYInt(), bonesy.neck.getXInt(), img.getHeight() - bonesy.neck.getYInt());
		g2.drawLine(bonesy.head.getXInt()-1, img.getHeight() - (bonesy.head.getYInt()), bonesy.head.getXInt()+1, img.getHeight()-(bonesy.head.getYInt()));
		g2.drawLine(bonesy.head.getXInt()-1, img.getHeight() - (bonesy.head.getYInt()-2), bonesy.head.getXInt()+1, img.getHeight()-(bonesy.head.getYInt()-2));
		g2.drawLine(bonesy.head.getXInt()+1, img.getHeight() - (bonesy.head.getYInt()), bonesy.head.getXInt()+1, img.getHeight()-(bonesy.head.getYInt()-2));
		g2.drawLine(bonesy.head.getXInt()-1, img.getHeight() - (bonesy.head.getYInt()), bonesy.head.getXInt()-1, img.getHeight()-(bonesy.head.getYInt()-2));

		g2.drawLine(bonesy.waist.getXInt(), img.getHeight() - bonesy.waist.getYInt(), bonesy.neck.getXInt(), img.getHeight() - bonesy.neck.getYInt());


		if(Game.SPOOKY)
		{
			boolean rib = false;

			int x = bonesy.neck.getXInt();
			int start = bonesy.waist.getYInt();
			int end = bonesy.neck.getYInt();
			int h = img.getHeight();
			for(int y = end-start-1; y > 0;y--)
			{
				int w = y/6 + 1;
				if(rib)
				{
					g2.drawLine(x-w, h-(start+y), x+w, h- (start+y));
				}
				rib = !rib;


			}
			g2.setColor(new Color(255-(r-128)*2,0,0));
			g2.drawLine(bonesy.head.getXInt()-1, img.getHeight() - (bonesy.head.getYInt()-1), bonesy.head.getXInt()-1, img.getHeight()-(bonesy.head.getYInt()-1));
			g2.drawLine(bonesy.head.getXInt()+1, img.getHeight() - (bonesy.head.getYInt()-1), bonesy.head.getXInt()+1, img.getHeight()-(bonesy.head.getYInt()-1));
		}
		g2.setColor(new Color(r,g,b));
		g2.drawLine(bonesy.waist.getXInt(), img.getHeight() - bonesy.waist.getYInt(), bonesy.hipFront.getXInt(), img.getHeight() - bonesy.hipFront.getYInt());
		g2.drawLine(bonesy.kneeFront.getXInt(), img.getHeight() - bonesy.kneeFront.getYInt(), bonesy.hipFront.getXInt(), img.getHeight() - bonesy.hipFront.getYInt());
		g2.drawLine(bonesy.kneeFront.getXInt(), img.getHeight() - bonesy.kneeFront.getYInt(), bonesy.ankleFront.getXInt(), img.getHeight() - bonesy.ankleFront.getYInt());
		g2.drawLine(bonesy.toeFront.getXInt(), img.getHeight() - bonesy.toeFront.getYInt(), bonesy.ankleFront.getXInt(), img.getHeight() - bonesy.ankleFront.getYInt());

		g2.drawLine(bonesy.shoulderFront.getXInt(), img.getHeight() - bonesy.shoulderFront.getYInt(), bonesy.neck.getXInt(), img.getHeight() - bonesy.neck.getYInt());
		g2.drawLine(bonesy.shoulderFront.getXInt(), img.getHeight() - bonesy.shoulderFront.getYInt(), bonesy.elbowFront.getXInt(), img.getHeight() - bonesy.elbowFront.getYInt());
		g2.drawLine(bonesy.wristFront.getXInt(), img.getHeight() - bonesy.wristFront.getYInt(), bonesy.elbowFront.getXInt(), img.getHeight() - bonesy.elbowFront.getYInt());
		g2.drawLine(bonesy.wristFront.getXInt(), img.getHeight() - bonesy.wristFront.getYInt(), bonesy.fingerFront.getXInt(), img.getHeight() - bonesy.fingerFront.getYInt());


		img = ColourTools.outline(img,outline, true);
		g2.dispose();
		return img;

	}

	public void offsetMoveFrame(int offset)
	{
		double offset2 = Math.abs((double)offset)*animationFrameCount/pixPerLoop;
		moveFrameNo=(moveFrameNo + offset2);
		while(moveFrameNo >= animationFrameCount)
		{
			moveFrameNo-=animationFrameCount;
		}
	}

	public void offsetIdleFrame(double d)
	{
		double offset2 = Math.abs(d*animationFrameCount/Game.TARGET_FPS/2);
		idleFrameNo=(idleFrameNo + offset2);
		while(idleFrameNo >= animationFrameCount)
		{
			idleFrameNo-=animationFrameCount;
		}
	}
	
	public BufferedImage getSprite(int animID)
	{
		switch(animID)
		{
		case AnimationSkeleton.ANIMATION_WALK:
			return spritesWalk[(int)moveFrameNo];

		case AnimationSkeleton.ANIMATION_AIR_NEUTRAL:
			return spritesAirNeutral[(int)moveFrameNo];
			
		case AnimationSkeleton.ANIMATION_AIR_UP:
			return spritesAirUp[(int)moveFrameNo];
			
		case AnimationSkeleton.ANIMATION_AIR_DOWN:
			return spritesAirDown[(int)moveFrameNo];

		default:
		case AnimationSkeleton.ANIMATION_STAND:
			return spritesStand[(int)idleFrameNo];
		}
	}

	public BufferedImage getSprite(int animID, int i)
	{
		return spritesWalk[i];
	}
	

}
