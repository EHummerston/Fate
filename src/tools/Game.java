package tools;
import instance.Instance;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;

import enviroment.Time;


public class Game {

	Random rng;
	Instance instance;
	Time time;
	
	boolean running = false;
	boolean paused = false;

	public final static int SCREEN_WIDTH = 480, SCREEN_HEIGHT = 270;

	//final int GAME_SCALE = 1;
	public final static double PIC_SCALE = 2;
	public final boolean fullScreen;

	float currentFPS;

	public final static boolean SPOOKY = false;

	
	
	int saveLoop = 0;
	final int SAVE_LOOP_MAX = 600;
	boolean save = false;

	public final static double TARGET_FPS = 30;
	final double TARGET_TIME_BETWEEN_UPDATES = 1000000000 / TARGET_FPS;
	int frameCounter = 0;

	public static void main(String[] args) {


		new Game();


	}

	public Game()
	{

		rng = new Random();

		


		int screenW = 1920;
		int screenH = 1080;
		fullScreen = ((SCREEN_WIDTH*PIC_SCALE>=screenW)||(SCREEN_HEIGHT*PIC_SCALE>=screenH));

		Drawing ps = new Drawing(SCREEN_WIDTH,SCREEN_HEIGHT);
		ps.validate();


		if(fullScreen)
		{
			GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().setFullScreenWindow(ps);
		}
		time = new Time(1,rng.nextInt(19)+5,rng.nextInt(60));
		instance = new Instance(time,rng.nextInt());
		
		running = true;

		double now = System.nanoTime();
		double lastUpdateTime = System.nanoTime();

		boolean loop = true;

		while(loop)
		{
			if(!paused)
			{
				instance.update();
				if(instance.isEnd())
				{
					running = false;
					ps.repaint();
					
					time.addMinutes(rng.nextInt(121)+30);
					instance = new Instance(time,rng.nextInt());
					running = true;
				}
			}

			ps.repaint();
			currentFPS = (float) ( TARGET_TIME_BETWEEN_UPDATES / (now-lastUpdateTime)  * TARGET_FPS );
			if(frameCounter==TARGET_FPS)
			{

				//System.out.println("FPS: " + currentFPS);
				frameCounter = 0;
			}
			frameCounter++;

			lastUpdateTime += TARGET_TIME_BETWEEN_UPDATES;

			while ( now - lastUpdateTime < TARGET_TIME_BETWEEN_UPDATES)
			{
				Thread.yield();

				//This stops the app from consuming all your CPU. It makes this slightly less accurate, but is worth it.
				//You can remove this line and it will still work (better), your CPU just climbs on certain OSes.
				//FYI on some OS's this can cause pretty bad stuttering. Scroll down and have a look at different peoples' solutions to this.
				try {Thread.sleep(1);} catch(Exception e) {}

				loop = running;

				now = System.nanoTime();
			}

		}
		ps.dispose();

	}

	private class Drawing extends JFrame implements KeyListener {


		/**
		 * 
		 */
		private static final long serialVersionUID = -3358805991593520296L;

		public Drawing(int x, int y) {
			super("Project \"Fate\"");
			initUI(x,y);
		}

		private void initUI(int x, int y) {
			setLocationRelativeTo(null);

			add(new Surface());

			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			if(fullScreen)
			{
				this.setUndecorated(true);
			}
			else
			{
				this.setLocation(10, 10);
				
				x*=PIC_SCALE;
				y*=PIC_SCALE;
				x+=6;
				y+=28;
				setSize(x, y);
			}

			this.setResizable(false);
			this.setFocusable(true);

			this.setVisible(true);

			this.addKeyListener(this);


		}

		private class Surface extends JPanel {

			int drawWidth, drawHeight;

			/**
			 * 
			 */
			private static final long serialVersionUID = 670446242401454707L;

			public Surface()
			{
				setBackground(Color.BLACK);
			}

			@SuppressWarnings("unused")
			private void doDrawing(Graphics g) {

				drawWidth = SCREEN_WIDTH;
				drawHeight = SCREEN_HEIGHT;

				if(running)
				{
					Graphics2D g2d = (Graphics2D) g;

					Instance instance2 = instance;

					if((PIC_SCALE%1) != 0)
					{


						BufferedImage drawDat = new BufferedImage(drawWidth,drawHeight,BufferedImage.TYPE_3BYTE_BGR);
						Graphics2D img = drawDat.createGraphics();
						int drawWidthScaled = (int)(drawWidth * PIC_SCALE);
						int drawHeightScaled = (int)(drawHeight * PIC_SCALE);

						instance2.draw(img,drawWidth,drawHeight, currentFPS);

						
						
						Image scaledImage = drawDat.getScaledInstance(drawWidthScaled,drawHeightScaled, Image.SCALE_FAST);
						BufferedImage imageBuff = new BufferedImage(drawWidthScaled,drawHeightScaled, BufferedImage.TYPE_INT_RGB);
						Graphics g3 = imageBuff.createGraphics();
						g3.drawImage(scaledImage, 0, 0, new Color(0,0,0), null);
						g3.dispose();


						g2d.drawImage(imageBuff, null, 0, 0);
					}
					else
					{

						g2d.scale(PIC_SCALE,PIC_SCALE);
						instance2.draw(g2d,drawWidth,drawHeight, currentFPS);	
					}
					if(save)
					{
						BufferedImage saveFile = new BufferedImage(drawWidth, drawHeight, BufferedImage.TYPE_INT_RGB);
						Graphics2D saveScreen = saveFile.createGraphics();


						saveScreen.setColor((new Color(140,190,214)));
						saveScreen.fillRect(0, 0, drawWidth, drawHeight);
						instance2.draw(g2d,drawWidth,drawHeight, currentFPS);

						if(saveLoop >= SAVE_LOOP_MAX)
						{
							save = false;
							System.out.println("final frame: "+saveLoop);
						}
						else
						{
							saveLoop++;
						}
						if(save)
						{
							File outputfile = new File("imgs\\"+saveLoop+".bmp");
							try {
								ImageIO.write(saveFile, "bmp", outputfile);
							} catch (IOException e) {

								e.printStackTrace();
							}
						}
					}
				}
			}



			@Override
			public void paintComponent(Graphics g) {

				super.paintComponent(g);

				doDrawing(g);
			}
		}

		@Override
		public void keyPressed(KeyEvent e) {
				
				switch( e.getKeyCode())
				{
				case KeyEvent.VK_ESCAPE:
					running=false;
					break;
				case KeyEvent.VK_SPACE:
					paused = !paused;
					break;
				case KeyEvent.VK_1:
					instance.toggleDrawForeground();
					break;
				case KeyEvent.VK_2:
					instance.toggleDrawBackground();
					break;
				case KeyEvent.VK_3:
					instance.toggleDrawData();
					break;
				case KeyEvent.VK_R:
					instance.toggleEnd();
					break;
				}
				
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}


	}

}
