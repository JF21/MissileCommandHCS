import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.applet.*;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class MissileCommand extends JFrame implements Runnable
{
public static final int UP = 0;
public static final int DOWN = 1;
public static final int LEFT = 2;
public static final int RIGHT = 3;

public static final int X = 0;
public static final int Y = 1;

public static final int WIDTH = 1000;
public static final int HEIGHT = 600;
public static final double FPS = 50.0;
public static final int FRAMERATE = (int)(1000*(1/FPS));

/*CIRCLE DATA*/

public static final int cX = 400;
public static final int cY = 300;
public static final int rInit = 1;

Integer dR = 2;

/***********/

/*DATA*/

Point origin = new Point(WIDTH/2,HEIGHT-75);

ArrayList<Target> targets = new ArrayList<Target>();

ArrayList<Missile> missilesE = new ArrayList<Missile>();
ArrayList<Missile> missilesM = new ArrayList<Missile>();


/*************/

int mLeft = 0;
int r = rInit;
boolean go = true;
int score = 0;
Color color = Color.BLACK;//new Color(255,255,255);

String worldString= "";
Graphics bufgfx;
Image bufimg, back;
BufferedImage grid;
Thread mythread;
Graphics2D gg;
int xLoc=450, yLoc=300;
int m=0;
int count=0;
int level=1; int at=1;
int[] missileMax = {10,15,20,20};
int missileCount = 0;


public int dirX = -1;
public int dirY = -1;

double[] probL = {0.5,0.7,0.9 ,1.1};
double[] probS = {0.2,0.3,0.4,0.5};

int vleft = 0;

public MissileCommand()
{
	super("Moving Exclamation Point ");
	addMouseListener(new mouseHandler());
	addKeyListener(new keyL());
	setSize(WIDTH,HEIGHT);
	setVisible(true);
	Thread animator = new Thread(this);
	animator.start();
}

public void worldSetup(Graphics g)
{
	m++;
	int w = this.getWidth();
	int h = this.getHeight();
	grid = (BufferedImage)createImage(w, h);
	bufimg = createImage(w,h);
	bufgfx = bufimg.getGraphics();

	bufgfx.setColor(Color.GREEN);
	bufgfx.fillRect(1,1,w,h);
	bufgfx.setColor(Color.BLACK);

	targets.add(new Target(50,550,Color.ORANGE,true));
	targets.add(new Target(200,550,Color.ORANGE,true));
	targets.add(new Target(350,550,Color.ORANGE,true));
	targets.add(new Target(550,550,Color.ORANGE,true));
	targets.add(new Target(700,550,Color.ORANGE,true));
	targets.add(new Target(850,550,Color.ORANGE,true));
				try{
							back = ImageIO.read(new File("images/back.png"));

			}catch(IOException e){}
}


public void update(Graphics g){
	paint(g);
}

public void run()
{
	while (true)
	{
		try
		{
			count++;
			if(missilesE.size()>0){
				for(int i = 0; i<missilesE.size(); i++){
					if(missilesE.get(i).dead){
						missilesE.remove(i);
						i--;
					}else{
						missilesE.get(i).move();

						if(Math.random()*100 < probS[at-1]){
							if(missilesE.get(i).type == Missile.ENEMY && !missilesE.get(i).breaked){
								missilesE.add(Missile.newMissile(missilesE.get(i).current,Missile.endPoint(),Missile.CHILD));
								missilesE.get(i).breaked = true;
							}
						}

					}
				}
			}

			if(missilesM.size()>0){
				for(int i = 0; i<missilesM.size(); i++){
					if(missilesM.get(i).dead){
						missilesM.remove(i);
						i--;
					}else{
						missilesM.get(i).move();
					}
				}
			}

			if(missilesE.size()>0){

				for(int i = 0; i<missilesE.size(); i++){

					if(missilesM.size()>0){
						for(int j = 0; j<missilesM.size(); j++){
							if(missilesE.get(i).current.distance(missilesM.get(j).current) < 20){
								missilesM.get(j).dead = true;
								missilesE.get(i).dead = true;
								missileCount++;
							}

						}
					}

				if(m!=0){
			//		System.out.println(targets.size());
						for(int j = 0; j<targets.size(); j++){
						if(missilesE.get(i).current.distance(targets.get(j).center) < Target.RADIUS){
								targets.get(j).decHealth();
								missilesE.get(i).dead = true;
							}

							if(targets.get(j).dead){
								targets.remove(j);
								j--;
							}else{

							targets.get(j).move(vleft);
							}
					//	System.out.println(targets.get(j));
						}
				}

				}

			}


			if(Math.random()*100 < probL[level-1]){
				missilesE.add(Missile.newMissile(Missile.ENEMY));
			}

			if(missileCount > missileMax[at-1]){
				level++;
				missileCount = 0;
				if(at < missileMax.length-1){
					at++;
				}
			}

			if(m!=0){
				vleft+=(mLeft*15);
				if(vleft < 0)
					vleft = 0;
				if(vleft > WIDTH)
					vleft = WIDTH;
			}

			repaint();
			Thread.sleep(FRAMERATE);

		}
		catch(InterruptedException e) {}
	}
}

private class mouseHandler extends MouseAdapter
{
	public void mouseReleased(MouseEvent e){
		//go = false;
	}

	public void mousePressed(MouseEvent e)
	{

		if(e.getPoint().getY() < origin.getY())
		missilesM.add(new Missile(origin,e.getPoint(),Missile.MINE));
		//go = true;
	}


}

private class keyL extends KeyAdapter{


	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();

		switch(key){
			case KeyEvent.VK_F: mLeft = 0;
				break;
			case KeyEvent.VK_A: mLeft = 0;
				break;
		}
	}

	public void keyPressed(KeyEvent e){
		int key = e.getKeyCode();


		switch(key){
			case KeyEvent.VK_F: mLeft=1;
								break;

			case KeyEvent.VK_A: mLeft=-1;
								break;
		}
	}

}

public void paint(Graphics g)
{
	if(m==0)
		worldSetup(bufgfx);

	bufgfx.setColor(color);

	int cut = vleft>>1;

	bufgfx.drawImage(back,-100-cut,0,WIDTH,HEIGHT,null);
	bufgfx.drawImage(back,850-cut,0,WIDTH,HEIGHT,null);

	bufgfx.setColor(Color.BLACK);


	if(targets.size()>0){
		for(Target t : targets){
			t.draw(bufgfx);
		}
	}

	if(missilesE.size()>0){
		for(Missile m : missilesE){
			m.draw(bufgfx);
		}
	}

	if(missilesM.size()>0){
		for(Missile m : missilesM){
			m.draw(bufgfx);
		}
	}

	bufgfx.setColor(Color.WHITE);
	bufgfx.setFont(new Font("Times New Roman",Font.BOLD,40));
	bufgfx.drawString("Level "+level+" "+missileCount,50,100);

	g.drawImage(bufimg,0,0,this);

}

public static void main(String args[])
{
MissileCommand app = new MissileCommand();
app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
}
}



