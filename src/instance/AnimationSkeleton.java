package instance;

import tools.Point2DD;

public class AnimationSkeleton {

	public static final int ANIMATION_WALK = 0,
			ANIMATION_AIR_NEUTRAL = 1,
			ANIMATION_AIR_UP = 2,
			ANIMATION_AIR_DOWN = 3,
			ANIMATION_STAND = 4;
	
	public static final int JOINT_REFLECT = 0,
			JOINT_ALTERNATE = 1,
			JOINT_IDENTICAL = 2;

	Point2DD waist, neck, head,

	shoulderBack, elbowBack, wristBack, fingerBack,
	shoulderFront, elbowFront, wristFront, fingerFront,

	hipBack, kneeBack, ankleBack, toeBack,
	hipFront, kneeFront, ankleFront, toeFront;	

	private int 
	headAngle = 0,
	neckAngle = 0,
	shoulderAngle = 90,
	hipAngle = 90,

	elbowFrontAngle = 90,
	wristFrontAngle = 45,
	fingerFrontAngle = 90,

	elbowBackAngle = 215,
	wristBackAngle = 45,
	fingerBackAngle = 90,

	kneeFrontAngle = 180,
	ankleFrontAngle = 315,
	toeFrontAngle = 115,

	kneeBackAngle = 225,
	ankleBackAngle = 315,
	toeBackAngle = 90;

	private final double  headDis,
	neckDis,
	shoulderDis,
	elbowDis,
	wristDis,
	fingerDis,
	hipDis,
	kneeDis,
	ankleDis,
	toeDis;

	public AnimationSkeleton (int animationID, int width, int height, int i, int iMax, int frontArm, int backArm)
	{
		//double bodyHeight = height * 0.9;
		double bodyHeight = height;
		headDis = (bodyHeight/8)+1;
		neckDis = ((bodyHeight*3)/8);

		shoulderDis = (bodyHeight/16);
		elbowDis = (bodyHeight/6);
		wristDis = (bodyHeight/6);
		fingerDis = (bodyHeight/16);

		hipDis = (bodyHeight/16);
		kneeDis = (bodyHeight/4);
		ankleDis = (bodyHeight/4);
		toeDis = (bodyHeight/16);

		waist = new Point2DD(width/2,height/2);
		boolean natFrontArm = (frontArm==0);
		boolean natBackArm = (backArm==0);
		boolean importArms;
		boolean ground;
		switch(animationID)
		{
		
		case ANIMATION_WALK:
			this.animWalk(i,iMax);
			importArms = true;
			ground = true;
			break;
		case ANIMATION_AIR_NEUTRAL:
			this.animAirNeutral(i,iMax);
			importArms = true;
			ground = true;
			break;
		case ANIMATION_AIR_UP:
			this.animAirUp(i,iMax);
			importArms = true;
			ground = true;
			break;
		case ANIMATION_AIR_DOWN:
			this.animAirDown(i,iMax);
			importArms = true;
			ground = true;
			break;
		default:
		case ANIMATION_STAND:
			this.animDance(i,iMax);
			importArms = true;
			ground = true;
			break;
		}

		if(importArms)
		{
			if(!natFrontArm)
			{
				mainArm(frontArm);
			}
			if(!natBackArm)
			{
				offArm(backArm);
			}
		}
		setPoints();
		if(ground)
		{
			ground();
		}
	}

	private void animAirNeutral(int i, int iMax) {


		int[] kneeAngles = {160,170};
		int[] ankleAngles = {-50,-40};
		int[] toeAngles = {45,45};

		angleArray(i,iMax,2,kneeAngles,ankleAngles,toeAngles,false,JOINT_REFLECT,JOINT_ALTERNATE,JOINT_ALTERNATE);

		int[] elbowAngles = {125,105};
		int[] wristAngles = {0,0};
		int[] fingerAngles = {0,0};

		angleArray(i,iMax,2,elbowAngles,wristAngles,fingerAngles,true,JOINT_REFLECT,JOINT_ALTERNATE,JOINT_ALTERNATE);
	}
	
	private void animAirUp(int i, int iMax) {

		neckAngle = 0;

		int[] kneeAngles = {170,170};
		int[] ankleAngles = {-40,-50};
		int[] toeAngles = {20,20};

		angleArray(i,iMax,2,kneeAngles,ankleAngles,toeAngles,false,JOINT_REFLECT,JOINT_ALTERNATE,JOINT_ALTERNATE);
		
		kneeFrontAngle+=30;
		kneeBackAngle+=30;
		ankleBackAngle-=20;

		int[] elbowAngles = {150,130};
		int[] wristAngles = {20,20};
		int[] fingerAngles = {0,0};

		angleArray(i,iMax,2,elbowAngles,wristAngles,fingerAngles,true,JOINT_REFLECT,JOINT_ALTERNATE,JOINT_ALTERNATE);
		
		//elbowFrontAngle -= 20;
		//elbowBackAngle -= 20;
	}
	
	private void animAirDown(int i, int iMax) {

		neckAngle = -10;
		
		int[] kneeAngles = {140,170};
		int[] ankleAngles = {-40,-40};
		int[] toeAngles = {90,90};

		angleArray(i,iMax,2,kneeAngles,ankleAngles,toeAngles,false,JOINT_REFLECT,JOINT_ALTERNATE,JOINT_ALTERNATE);

		int[] elbowAngles = {60,40};
		int[] wristAngles = {45,45};
		int[] fingerAngles = {0,0};

		angleArray(i,iMax,2,elbowAngles,wristAngles,fingerAngles,true,JOINT_REFLECT,JOINT_REFLECT,JOINT_ALTERNATE);
	}

	private void animWalk(int i, int iMax)
	{

		int[] kneeAngles = {150,220,240,165};
		int[] ankleAngles = {-10,-120,-40,-30};
		int[] toeAngles = {10,10,90,135};
	
		angleArray(i,iMax,4,kneeAngles,ankleAngles,toeAngles,false,JOINT_ALTERNATE,JOINT_ALTERNATE,JOINT_ALTERNATE);

		int[] elbowAngles = {210,175,110,185};
		int[] wristAngles = {20,0,45,0};
		int[] fingerAngles = {0,0,0,0};

		angleArray(i,iMax,4,elbowAngles,wristAngles,fingerAngles,true,JOINT_ALTERNATE,JOINT_ALTERNATE,JOINT_ALTERNATE);

	}
	
	private void animDance(int i, int iMax)
	{

		int[] kneeAngles = {110,70};
		int[] ankleAngles = {70,110};
		int[] toeAngles = {-90,-90};
	
		angleArray(i,iMax,2,kneeAngles,ankleAngles,toeAngles,false,JOINT_REFLECT,JOINT_REFLECT,JOINT_REFLECT);

		int[] elbowAngles = {80,220};
		int[] wristAngles = {60,40};
		int[] fingerAngles = {90,90};

		angleArray(i,iMax,2,elbowAngles,wristAngles,fingerAngles,true,JOINT_REFLECT,JOINT_REFLECT,JOINT_REFLECT);

	}		

	private void mainArm(int animID)
	{

	}

	private void offArm(int animID)
	{

	}

	private void setPoints()
	{
		neck = determineApp(waist,neckAngle,neckDis);

		headAngle+=neckAngle;
		head = determineApp(neck,headAngle,headDis);

		shoulderAngle+=neckAngle;
		shoulderFront = determineApp(neck,shoulderAngle,shoulderDis);
		shoulderBack = determineApp(neck,shoulderAngle+180,shoulderDis);

		elbowFront = determineApp(shoulderFront,elbowFrontAngle,elbowDis);
		wristFrontAngle+=elbowFrontAngle;
		wristFront = determineApp(elbowFront,wristFrontAngle,wristDis);
		fingerFrontAngle+=wristFrontAngle;
		fingerFront = determineApp(wristFront,fingerFrontAngle,fingerDis);

		elbowBack = determineApp(shoulderBack,elbowBackAngle,elbowDis);
		wristBackAngle+=elbowBackAngle;
		wristBack = determineApp(elbowBack,wristBackAngle,wristDis);
		fingerBackAngle+=wristBackAngle;
		fingerBack = determineApp(wristBack,fingerBackAngle,fingerDis);

		hipFront = determineApp(waist,hipAngle,hipDis);
		hipBack = determineApp(waist,hipAngle+180,hipDis);


		kneeBack = determineApp(hipBack,kneeBackAngle,kneeDis);
		ankleBackAngle+=kneeBackAngle;
		ankleBack = determineApp(kneeBack,ankleBackAngle,ankleDis);
		toeBackAngle+=ankleBackAngle;
		toeBack = determineApp(ankleBack,toeBackAngle,toeDis);

		kneeFront = determineApp(hipFront,kneeFrontAngle,kneeDis);
		ankleFrontAngle+=kneeFrontAngle;
		ankleFront = determineApp(kneeFront,ankleFrontAngle,ankleDis);
		toeFrontAngle+=ankleFrontAngle;
		toeFront = determineApp(ankleFront,toeFrontAngle,toeDis);
	}

	private void ground()
	{
		double lowPoint = waist.getY();
		if(kneeBack.getY() < lowPoint)
		{
			lowPoint = kneeBack.getY();
		}
		if(ankleBack.getY() < lowPoint)
		{
			lowPoint = ankleBack.getY();
		}
		if(toeBack.getY() < lowPoint)
		{
			lowPoint = toeBack.getY();
		}

		if(kneeFront.getY() < lowPoint)
		{
			lowPoint = kneeFront.getY();
		}
		if(ankleFront.getY() < lowPoint)
		{
			lowPoint = ankleFront.getY();
		}
		if(toeFront.getY() < lowPoint)
		{
			lowPoint = toeFront.getY();
		}



		lowPoint -=2;
		lowPoint *= -1;
		//System.out.print(lowPoint + " waist before: " + waist.getY());

		waist.offsetY(lowPoint);
		neck.offsetY(lowPoint);
		head.offsetY(lowPoint);

		//System.out.println(lowPoint + " waist after: " + waist.getY());

		shoulderBack.offsetY(lowPoint);
		elbowBack.offsetY(lowPoint);
		wristBack.offsetY(lowPoint);
		fingerBack.offsetY(lowPoint);

		shoulderFront.offsetY(lowPoint);
		elbowFront.offsetY(lowPoint);
		wristFront.offsetY(lowPoint);
		fingerFront.offsetY(lowPoint);

		hipBack.offsetY(lowPoint);
		kneeBack.offsetY(lowPoint);
		ankleBack.offsetY(lowPoint);
		toeBack.offsetY(lowPoint);

		hipFront.offsetY(lowPoint);
		kneeFront.offsetY(lowPoint);
		ankleFront.offsetY(lowPoint);
		toeFront.offsetY(lowPoint);
	}

	private static Point2DD determineApp(Point2DD base, int angle, double offset)
	{
		Point2DD p = new Point2DD(0,offset);

		p.rotate(Math.toRadians(angle));
		p.offsetX(base.getX());
		p.offsetY(base.getY());

		return p;
	}
	
	private void angleArray(int i, int iMax, int variationsTotal, int[] angles1, int[] angles2, int[] angles3, boolean arms, int reflect1, int reflect2, int reflect3)
	{
		int variationLength = iMax/variationsTotal;

		//Which variation we are in:
		int curVar = i/variationLength;

		//How far into the variation we are:
		int iFrame = i % variationLength;

		//		System.out.println("Frame " + i + " of " + iMax + ".");
		//		System.out.println("\tThere are " + variationsTotal + " variations, each is " + variationLength + " frames long.");
		//		System.out.println("\tThis frame is " + iFrame + " frame(s) into variation " + curVar + ".");

		

		int angleFront1 = angles1[curVar%variationsTotal] + ((angles1[(curVar+1)%variationsTotal]-angles1[curVar%variationsTotal])*iFrame/variationLength);
		int angleFront2 = angles2[curVar%variationsTotal] + ((angles2[(curVar+1)%variationsTotal]-angles2[curVar%variationsTotal])*iFrame/variationLength);
		int angleFront3 = angles3[curVar%variationsTotal] + ((angles3[(curVar+1)%variationsTotal]-angles3[curVar%variationsTotal])*iFrame/variationLength);

		int angleBack1;
		int angleBack2;
		int angleBack3;
		
		switch(reflect1)
		{
		case JOINT_REFLECT:
			angleBack1 = -angleFront1;
			break;
		case JOINT_ALTERNATE:
			angleBack1 = angles1[(curVar+variationsTotal/2)%variationsTotal] + ((angles1[((curVar+variationsTotal/2)+1)%variationsTotal]-angles1[(curVar+variationsTotal/2)%variationsTotal])*iFrame/variationLength);
			break;
		default:
		case JOINT_IDENTICAL:
			angleBack1 = angleFront1;
		}
		
		switch(reflect2)
		{
		case JOINT_REFLECT:
			angleBack2 = -angleFront2;
			break;
		case JOINT_ALTERNATE:
			angleBack2 = angles2[(curVar+variationsTotal/2)%variationsTotal] + ((angles2[((curVar+variationsTotal/2)+1)%variationsTotal]-angles2[(curVar+variationsTotal/2)%variationsTotal])*iFrame/variationLength);
			break;
		default:
		case JOINT_IDENTICAL:
			angleBack2 = angleFront2;
		}
		
		switch(reflect3)
		{
		case JOINT_REFLECT:
			angleBack3 = -angleFront3;
			break;
		case JOINT_ALTERNATE:
			angleBack3 = angles3[(curVar+variationsTotal/2)%variationsTotal] + ((angles3[((curVar+variationsTotal/2)+1)%variationsTotal]-angles3[(curVar+variationsTotal/2)%variationsTotal])*iFrame/variationLength);
			break;
		default:
		case JOINT_IDENTICAL:
			angleBack3 = angleFront3;
		}
	
		if(arms)
		{
			elbowFrontAngle = angleFront1;
			wristFrontAngle = angleFront2;
			fingerFrontAngle = angleFront3;
			
			elbowBackAngle = angleBack1;
			wristBackAngle = angleBack2;
			fingerBackAngle = angleBack3;
		}
		else
		{
			kneeFrontAngle = angleFront1;
			ankleFrontAngle = angleFront2;
			toeFrontAngle = angleFront3;
			
			kneeBackAngle = angleBack1;
			ankleBackAngle = angleBack2;
			toeBackAngle = angleBack3;
		}
		
		
	}
}
