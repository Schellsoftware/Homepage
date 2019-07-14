/*
 * Created on 24.11.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package Elemente;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Harald
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Kurve {
	int x_pos;
	int y_pos;
	public int x[]=new int[250];
	public int y[]=new int[250];
	int px[] = new int[3];
	int py[] = new int[3];
	String x_s,y_s;
	
	public Kurve(int k_x, int k_y, String xs, String ys){
		x_pos=k_x;
		y_pos=k_y;
		for(int i=0;i<250;i++) x[i]=0;
		for(int i=0;i<250;i++) y[i]=0;
		x_s=xs;
		y_s=ys;
	}
	public void zeichne(Graphics g) {
		g.setColor(Color.black);
		for(int i=0;i<249;i++){
			g.setColor(Color.blue);
			g.drawLine(x_pos+i,y_pos-x[i],x_pos+1+i,y_pos-x[i+1]);
			g.setColor(Color.black);
			g.drawLine(x_pos+i,y_pos-y[i],x_pos+1+i,y_pos-y[i+1]);
		}
		g.drawLine(x_pos,y_pos,x_pos+260,y_pos);
		g.drawString(x_s,x_pos+260,y_pos+15);
		px[0]=x_pos+270;
		py[0]=y_pos;
		px[1]=x_pos+260;
		py[1]=y_pos-3;
		px[2]=x_pos+260;
		py[2]=y_pos+3;
		g.fillPolygon(px,py,3);
		g.drawLine(x_pos,y_pos,x_pos,y_pos-110);
		g.drawString(y_s,x_pos-10,y_pos-110);
		px[0]=x_pos;
		py[0]=y_pos-120;
		px[1]=x_pos-3;
		py[1]=y_pos-110;
		px[2]=x_pos+3;
		py[2]=y_pos-110;
		g.fillPolygon(px,py,3);
	}
}
