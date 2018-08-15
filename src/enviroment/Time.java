package enviroment;

import java.util.Random;

public class Time {

	private int day, hour;
	private float minute;
	
	public Time(int day, int hour, int minute)
	{		
		this.day = Math.abs(day)%366 + 1;
		this.hour = Math.abs(hour) % 24;
		this.minute = (float)(Math.abs(minute) % 60)/60;		
	}
	
	public Time(int rnd)
	{
		Random rng = new Random(rnd);
		this.day = rng.nextInt(366) + 1;
		this.hour = rng.nextInt(24);
		this.minute = rng.nextFloat()%1;
		
	}
	
	public void addMinutes(int mins)
	{
		this.minute += ((float)mins)/60;
		
		while(minute >= 1)
		{
			minute-=1;
			hour+=1;
		}
		while(hour >= 24)
		{
			hour-=24;
			day+=1;
		}
		
	}
	
	public String toString()
	{
		String text = new String();

		if(hour==0||hour==12)
		{
			text+=12;
		}
		else if(this.isMorning())
		{
			text+=hour;
		}
		else
		{
			text+=hour-12;
		}
		text+=":";
		int minuteTxt = (int)(minute*60);
		if(minuteTxt<10)
		{
			text+=0;
		}
		text+=minuteTxt;
		if(this.isMorning())
		{
			text+="am";
		}
		else
		{
			text+="pm";
		}
		
		return text;
	}
	

	
	public float getLight()
	{
		float L = (12-Math.abs(hour + minute - 12))/12;
		
		if(L<0.5)
		{
			L = (float)Math.pow((L*2),2)/2;
			//L = Math.pow((L*2),2)/2;
		}
		else
		{
			L = 1-(float)Math.pow(((1-L)*2),2)/2;
			//L = 1-Math.pow(((1-L)*2),2)/2;
		}
		
		return L;
	}
	
	public int getLightAngle()
	{
		int theta = (int)( ((hour + minute - 6) % 24)*15 );

		return theta;
		
	}
	
	public boolean isMorning()
	{
		return hour<12;
	}
}
