package instance;
import instance.Stage.Platform;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import tools.Point2DD;
import tools.Point2DInt;

public class Actor
{

	public static final double DEFAULT_MOVE_SPEED = 1.2,
			DEFAULT_JUMP_SPEED = 4.5,
			DEFAULT_GRAVITY = 0.2,
			TRACTION = 0.1;

	private AnimationSet sprite;



	private Point2DInt origin = new Point2DInt(0,0);

	private boolean air;
	private Point2DD vel;
	private Point2DD velLeft;

	private int orders;
	final static int ORDER_IDLE = 0,
			ORDER_MOVE_RIGHT = 1,
			ORDER_MOVE_LEFT = 2;

	private final int hitPointsMax = 100,
			moveSpeed = 1,
			evasion = 1,
			resistance = 1,
			coolDownRed = 1;

	private int hitPoints = 100,
			hitPointsMaxMod = 0,
			moveSpeedMod = 0,
			evasionMod = 0,
			resistanceMod = 0,
			coolDownRedMod = 0,

			actionSpeedMod = 0,
			reachMod = 0,
			powerMod = 0,
			weightMod = 0;

	private ActorArm mainHand, offHand;
	private boolean twoHand;

	private class ActorArm
	{

		public final int actionSpeed,
		attackType,
		reach,
		power,
		weight;
		public final boolean catalyst;

		public final static int ARM_COOLDOWN_MAX = 100;
		public int cooldown;

		private ActorArm(int actionSpeed, int attackType, int reach, int power, int weight, boolean catalyst)
		{
			this.actionSpeed = actionSpeed;
			this.attackType = attackType;
			this.reach = reach;
			this.power = power;
			this.weight = weight;
			this.catalyst = catalyst;
			this.cooldown = 0;
		}



		private void setCooldown()
		{
			cooldown = ARM_COOLDOWN_MAX;
		}

		private void updateCooldown()
		{
			if(cooldown > 0)
			{
				cooldown-= (actionSpeed + actionSpeedMod);
			}
		}

		private boolean isCooldown()
		{
			return (cooldown <= 0);
		}

	}

	public Actor(int x, int y, Color outline)
	{
		this.origin = new Point2DInt(x,y);
		this.air = true;
		this.vel = new Point2DD(DEFAULT_MOVE_SPEED,0);
		this.velLeft = new Point2DD(0,0);
		
		 sprite = new AnimationSet(outline);

		/*File inputFile = new File("Untitled.png");
		try {
			sprite = ImageIO.read(inputFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			sprite = null;
		}
		 */
	}

	public void updateCooldowns()
	{
		if(!mainHand.isCooldown())
		{
			mainHand.updateCooldown();
		}
		if(twoHand)
		{
			if(offHand.isCooldown())
			{
				offHand.updateCooldown();
			}
		}
	}

	public void doMovement(Stage stage)
	{
		doMoveHoriz(stage);

		if(air)
		{
			doMoveVert(stage);
		}
		else
		{
			vel.setY(0);
			velLeft.setY(0);
			friction();
		}

		checkAir(stage);
		sprite.offsetIdleFrame(this.getMoveSpeed());
	}

	private void applyGravity()
	{
		vel.offsetY(-DEFAULT_GRAVITY);
	}

	private void friction()
	{
		vel.setX(vel.getX()*(1-TRACTION));
	}

	public void jump(int dir)
	{
		if(dir == 0)
		{
			vel.setX(0);
			vel.setY(DEFAULT_JUMP_SPEED*4/3);
		}
		else
		{
			if(Math.abs(dir)>1)
			{
				dir = dir/Math.abs(dir);
			}
			vel.setY(DEFAULT_JUMP_SPEED);
			vel.setX((this.getMoveSpeed())*4/3*dir);
		}


		//origin.offsetY(1);

		air = true;
	}

	private void checkAir(Stage stage)
	{
		boolean newAir = true;
		if(origin.getY() <= stage.getBase(origin.getX()))
		{
			newAir = false;
		}
		else
		{
			ArrayList<Platform>platforms = Stage.getPlatformsSpecific(origin.getX(),stage.getPlatforms());
			for(int i = 0; i < platforms.size(); i++)
			{
				double platY = platforms.get(i).origin.getY();
				if (platY == origin.getY())
				{
					newAir = false;;
					break;
				}
			}
			if(newAir)
			{
				if(!air)
				{
					this.setYVel(0);
				}
			}
		}
		if((air && vel.getY() > 0))
		{
			newAir = true;
		}

		this.setAir(newAir);
	}

	private void doMoveHoriz(Stage stage)
	{
		double horizToMove = vel.getX() + velLeft.getX();
		if(horizToMove!=0)
		{
			int dir = (int)(horizToMove/Math.abs(horizToMove));

			boolean horizMove = true;

			int dest = origin.getX() + (int)horizToMove;

			for(int i = origin.getX(); i*dir <= dest*dir; i+=dir)
			{

				//Ramp detection
				int diff = stage.getBase(i) - origin.getY();

				if(diff > 1)
				{
					this.offsetX((i-dir)-origin.getX());
					horizMove = false;
					break;
				}
				else if(diff == 1 || diff == -1)
				{
					offsetY(diff);
				}

			}

			if(horizMove)
			{
				this.offsetX(horizToMove);
			}
			else if(!air)
			{
				vel.setX(0);
				velLeft.setX(0);
			}
		}

	}

	private void doMoveVert(Stage stage)
	{
		this.applyGravity();

		boolean vertMove = true;
		double vertToMove = vel.getY() + velLeft.getY();
		int baseSpef = stage.getBase(origin.getX());
		ArrayList<Platform> plats = Stage.getPlatformsSpecific(origin.getX(),stage.getPlatforms());

		if(vertToMove<0)
		{
			//Moving downward
			if(origin.getY()+vertToMove < baseSpef)
			{	//Collision with bottom of stage.
				vertMove=false;
				offsetY(-(origin.getY() - (baseSpef) ));
			}
			else
			{
				for(int i = 0; i < plats.size(); i++)
				{
					double platY = plats.get(i).origin.getY();
					if (((platY <= origin.getY()) &&
							(platY >= (origin.getY() + vertToMove))))
					{
						vertMove = false;

						this.offsetY((int)-(origin.getY() - platY));
						break;
					}
				}
			}
		}

		if(vertMove)
		{
			this.offsetY(vertToMove);
		}
	}

	public Point2DInt getOrigin()
	{
		return origin;
	}


	public void offsetX(double offset)
	{
		origin.offsetX((int)offset);
		velLeft.setX( offset - (int)offset );
		sprite.offsetMoveFrame((int)offset);
	}

	public void offsetY(double offset)
	{
		origin.offsetY((int)offset);
		velLeft.setY( offset - (int)offset );
		//sprite.offset(offset);
	}

	public void setAir(boolean air) {
		this.air = air;
	}

	public boolean isAir() {

		return air;
	}

	public Point2DD getAirVel()
	{
		return vel;
	}

	public void setYVel(double yVel)
	{
		vel.setY(yVel);
	}

	public void setXVel(double xVel)
	{
		vel.setX(xVel);
	}

	public int getOrders() {
		return orders;
	}

	public void setOrders(int orders) {
		this.orders = orders;
	}

	final double airThreshold  = 1.5;

	public BufferedImage getSprite()
	{
		if(air)		//In the air
		{
			if(vel.getY()>airThreshold || vel.getY()<-airThreshold)
			{
				if(vel.getY()<0)
				{
					return sprite.getSprite(AnimationSkeleton.ANIMATION_AIR_DOWN);
				}
				else
				{
					return sprite.getSprite(AnimationSkeleton.ANIMATION_AIR_UP);
				}
			}
			else
			{
				return sprite.getSprite(AnimationSkeleton.ANIMATION_AIR_NEUTRAL);
			}
		}
		else		//On the ground
		{
			if(vel.getX()<-0.5)	//Moving left
			{
				return sprite.getSprite(AnimationSkeleton.ANIMATION_WALK);
			}
			else if(vel.getX()>0.5)	//Moving right
			{
				return sprite.getSprite(AnimationSkeleton.ANIMATION_WALK);
			}
			else				//Stationary
			{
				return sprite.getSprite(AnimationSkeleton.ANIMATION_STAND);
			}
		}
	}


	public BufferedImage getSprite(int AnimID)
	{
		return sprite.getSprite(AnimID);
	}

	public BufferedImage getSprite(int AnimID, int i)
	{
		return sprite.getSprite(AnimID,i);
	}

	public Rectangle getHitBox()
	{
		final int width = 21;
		return new Rectangle(origin.getX()-width/2,origin.getY() + 32,width,32);

	}

	public double getMoveSpeed()
	{
		return DEFAULT_MOVE_SPEED*(moveSpeed+moveSpeedMod);
	}
}
