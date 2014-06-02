import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.*;
import java.math.*;
import java.awt.image.*;
import java.applet.*;
import javax.imageio.ImageIO;

public class Target{

	public static final int RADIUS = 50;

	private int health = 5;

	int x,y,xC;
	Color c;
	Point center;
	Image img;
	static Image[] images;
	boolean fromImage = false;
	boolean dead = false;


	public Target(int x, int y , Color c){
		this.xC = x;
		this.y = y;
		this.c = c;
		center = new Point(x+RADIUS, y+RADIUS);
	}

	public Target(int x, int y , Color c, boolean fromImage){
			this.xC = x;
			this.x = x;
			this.y = y;
			this.c = c;
			center = new Point(x+RADIUS, y+RADIUS);

			if(fromImage){
				try{
							img = ImageIO.read(new File("images/Target1.png"));
							images = new Image[4];
							images[3]= ImageIO.read(new File("images/Target2.png"));
							images[2]= ImageIO.read(new File("images/Target3.png"));
							images[1] = ImageIO.read(new File("images/Target4.png"));
							images[0] = ImageIO.read(new File("images/Target5.png"));
			}catch(IOException e){}
			}
	}

	public static void setImages(){
//System.out.println(here);


		//	img = images[4];
	}

	public void decHealth(){
		if(health>1){
		health--;
		img = images[health-1];
		}

		if(health == 1){
			dead = true;
		}
	}

	public void move(int v){
		x = xC-v;
		center = new Point(x+RADIUS,y+RADIUS);
		//System.out.println(xC+" "+x);
	}

	public void draw(Graphics g){
		if(img != null){


			g.drawImage(img,x,y,(2*RADIUS),(2*RADIUS),null);

			g.setFont(new Font("Arial",Font.PLAIN,12));
			g.setColor(Color.BLACK);
			g.drawString(health+"",x+30,y+30);
		}else{
			g.setColor(c);
			g.fillOval(x,y,(2*RADIUS),(2*RADIUS));

			g.setColor(Color.BLACK);
			g.drawString(health+"",x+30,y+30);
		}
	}

	public String toString(){
		return x+" "+y+" "+xC;
	}

}