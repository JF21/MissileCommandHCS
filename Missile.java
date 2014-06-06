import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.applet.*;
import java.util.ArrayList;

public class Missile{

	int dR = 10;
	int dC = 5;
	public static final int ENEMY = 0;
	public static final int CHILD = 2;
	public static final int MINE = 1;

	public static final int ALIVE = 0;
	public static final int BURST = 1;
	public static final int DEAD = 2;

	Point start,end,current;
	int r = 0;
	int type;
	double d;
	Color c = Color.ORANGE;
	int state = -1;
	boolean breaked=false;


	public Missile(Point start, Point end, Color c, int type){

		state = ALIVE;

		current = start;
		this.start = start;
		this.end = end;
		this.c=c;
		this.type = type;
		d = start.distance(end);

		if(type == ENEMY || type == CHILD){
			dR = 2;
		}

	}

		public Missile(Point start, Point end, int type){

			state = ALIVE;
			current = start;
			this.start = start;
			this.end = end;
			this.type = type;
			d = start.distance(end);

			if(type == ENEMY || type == CHILD){
				dR = 2;
			}

	}

	public static Point endPoint(){
		return new Point((int)(Math.random()*MissileCommand.WIDTH),
			(int)(Math.random()*(60)+(MissileCommand.HEIGHT-60)));
	}

	public static Missile newMissile(int side){
		return new Missile(new Point((int)(Math.random()*MissileCommand.WIDTH),0),endPoint(),side);
	}

	public static Missile newMissile(Point start, Point end, int side){
		return new Missile(start,end,side);
	}

	public void resetR(){
		r = 0;
	}

	public void move(){

		if(current.getX()>MissileCommand.WIDTH || current.getX()<0 || current.getY()<0 || current.getY()>MissileCommand.HEIGHT){
			state = DEAD;
		}
		else{
			if(state == ALIVE){
				r += dR;
			}else if (state == BURST){
				r += dC;

				if(r > 20){
					dC*=-1;
				}

				if(r < 0 ){
					state = DEAD;
				}
			}

			if(state == ALIVE){


				int x2 = (int)(start.getX()+((end.getX()-start.getX())*r)/d);
				int y2 = (int)(start.getY()+((end.getY()-start.getY())*r)/d);
				current = new Point(x2,y2);

				if(y2>MissileCommand.HEIGHT/2)
					breaked=true;
			}
		}



	}

	public void draw(Graphics g){
		g.setColor(c);

		if(state == ALIVE)
			g.drawLine((int)start.getX(),(int)start.getY(),(int)current.getX(),(int)current.getY());
		else if (state == BURST)
			g.drawOval((int)current.getX()-r,(int)current.getY()-r,2*r,2*r);
	}

}