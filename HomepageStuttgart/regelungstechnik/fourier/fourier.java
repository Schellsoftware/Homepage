

import java.applet.Applet;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;




/*
 * Created on 18.02.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
 
/**
 * @author hars
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class fourier extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	
	int frequenz=0;
	double p=0.5;
	double pd=100;
	Choice cTestfunk;
	Button uebernehmeButton;
	int x[]=new int[150];
	int y[]=new int[150];
	int s[]=new int[150];

	String str;
	int px[] = new int[3];
	int py[] = new int[3];

	public void init()
	{
		setLayout(null); // Freie Positionierung
		uebernehmeButton = new Button("Neustart");
		uebernehmeButton.setBounds(150,20,100,20);
		add(uebernehmeButton);
		cTestfunk = new Choice();
		cTestfunk.setBounds(20,20,100,20);
		cTestfunk.addItem("Rechteck");
		cTestfunk.addItem("Sinus");
		cTestfunk.addItem("Dreieck");
		add(cTestfunk);

		
		for(int i=0;i<150;i++) x[i]=0;
		
		ausgabe=null;
		g=null;
		resize(500, 600);
		int sx=size().width;
		int sy=size().height;
		if(sx!=sizex || sy!=sizey){
			sizex=sx;
			sizey=sy;
			ausgabe=createImage(sx,sy);
			g=ausgabe.getGraphics();
		} // if
	}

	public void grafik_vorbereiten()
	{
		g.clearRect(0,0,sizex,sizey);
		zeichneZyklus();
	}

	public void paint (Graphics g1)
	{
		grafik_vorbereiten();
		g1.drawImage(ausgabe, 0, 0, this);
	}

	public void repaint (Graphics g1)
	{
		grafik_vorbereiten();
		g1.drawImage(ausgabe, 0, 0, this);
	}
	public void start()
	{
		if (m_thread == null)
		{
			m_thread = new Thread(this);
			m_thread.start();
		}
		// TODO: Place additional applet start code here
	}
	
	//		The stop() method is called when the page containing the applet is
	// no longer on the screen. The AppletWizard's initial implementation of
	// this method stops execution of the applet's thread.
	//--------------------------------------------------------------------------
	public void stop()
	{
		if (m_thread != null)
		{
			m_thread.stop();
			m_thread = null;
		}

		// TODO: Place additional applet stop code here
	}

	// THREAD SUPPORT
	//		The run() method is called when the applet's thread is started. If
	// your applet performs any ongoing activities without waiting for user
	// input, the code for implementing that behavior typically goes here. For
	// example, for an applet that performs animation, the run() method controls
	// the display of images.
	//--------------------------------------------------------------------------
	public void run()
	{

		// If re-entering the page, then the images have already been loaded.
		// m_fAllLoaded == TRUE.
		//----------------------------------------------------------------------
				

	     while (true){
			try
			{
				// Draw next image in animation
				//--------------------------------------------------------------
				
				rechneZyklus();
				grafik_vorbereiten();
				frequenz ++;
				if(frequenz > 12) frequenz=0;
				repaint();
				Thread.sleep(2000);
		        
		        
				
			}
			catch (InterruptedException e)
			{
				// TODO: Place exception-handling code here in case an
				//       InterruptedException is thrown by Thread.sleep(),
				//		 meaning that another thread has interrupted this one
				stop();
			}
	
	     }
	}
	
	public void rechneZyklus()
	{
		if(cTestfunk.getSelectedIndex()==0){
			for(int i=0;i<50;i++){
				x[i]=100;
			}
			for(int i=50;i<100;i++){
				x[i]=-100;
			}
			for(int i=100;i<150;i++){
				x[i]=100;
			}
			switch(frequenz){
				case 0:
					for(int i=0;i<150;i++){
						y[i]=0;
						s[i]=0;
					}
					break;
				case 1:
					for(int i=0;i<150;i++){
						y[i]=(int)((4*100/Math.PI)*Math.sin(2*3.14/pd*i));
						s[i]=(int)((4*100/Math.PI)*Math.sin(2*3.14/pd*i));
					}
					break;
				case 2:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((4.0*100.0/Math.PI)*(1.0/3.0)*Math.sin(3*2*3.14/pd*i));
						s[i]=(int)((4.0*100.0/Math.PI)*(1.0/3.0)*Math.sin(3*2*3.14/pd*i));
					}
					break;
				case 3:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((4.0*100.0/Math.PI)*(1.0/5.0)*Math.sin(5*2*3.14/pd*i));
						s[i]=(int)((4.0*100.0/Math.PI)*(1.0/5.0)*Math.sin(5*2*3.14/pd*i));
					}
					break;
				case 4:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((4.0*100.0/Math.PI)*(1.0/7.0)*Math.sin(7*2*3.14/pd*i));
						s[i]=(int)((4.0*100.0/Math.PI)*(1.0/7.0)*Math.sin(7*2*3.14/pd*i));
					}
					break;
				case 5:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((4.0*100.0/Math.PI)*(1.0/9.0)*Math.sin(9*2*3.14/pd*i));
						s[i]=(int)((4.0*100.0/Math.PI)*(1.0/9.0)*Math.sin(9*2*3.14/pd*i));
					}
					break;
				case 6:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((4.0*100.0/Math.PI)*(1.0/11.0)*Math.sin(11*2*3.14/pd*i));
						s[i]=(int)((4.0*100.0/Math.PI)*(1.0/11.0)*Math.sin(11*2*3.14/pd*i));
					}
					break;
				case 7:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((4.0*100.0/Math.PI)*(1.0/13.0)*Math.sin(13*2*3.14/pd*i));
						s[i]=(int)((4.0*100.0/Math.PI)*(1.0/13.0)*Math.sin(13*2*3.14/pd*i));
					}
					break;
				case 8:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((4.0*100.0/Math.PI)*(1.0/15.0)*Math.sin(15*2*3.14/pd*i));
						s[i]=(int)((4.0*100.0/Math.PI)*(1.0/15.0)*Math.sin(15*2*3.14/pd*i));
					}
					break;
				case 9:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((4.0*100.0/Math.PI)*(1.0/17.0)*Math.sin(17*2*3.14/pd*i));
						s[i]=(int)((4.0*100.0/Math.PI)*(1.0/17.0)*Math.sin(17*2*3.14/pd*i));
					}
					break;
				case 10:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((4.0*100.0/Math.PI)*(1.0/19.0)*Math.sin(19*2*3.14/pd*i));
						s[i]=(int)((4.0*100.0/Math.PI)*(1.0/19.0)*Math.sin(19*2*3.14/pd*i));
					}
					break;
				case 11:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((4.0*100.0/Math.PI)*(1.0/21.0)*Math.sin(21*2*3.14/pd*i));
						s[i]=(int)((4.0*100.0/Math.PI)*(1.0/21.0)*Math.sin(21*2*3.14/pd*i));
					}
					break;
					
				default:
					break;
			}

		}
		if(cTestfunk.getSelectedIndex()==1){
			int h;
			for(int i=0;i<150;i++){
				x[i]=(int)(100*Math.sin(2*3.14/pd*i));
				if(frequenz>=1){
					y[i]=(int)(100*Math.sin(2*3.14/pd*i));
					s[i]=(int)(100*Math.sin(2*3.14/pd*i));
				}
				else{
					y[i]=0;
					s[i]=0;
				}
			}
		}
		if(cTestfunk.getSelectedIndex()==2){
			for(int i=0;i<150;i++){
				if(((i+25)%(int)pd)<(int)(pd/2)){
					x[i]=-100+400*((i+25)%(int)pd)/(int)pd;
				}
				else{
					x[i]=300-400*((i+25)%(int)pd)/(int)pd;
				}
			}
			switch(frequenz){
				case 0:
					for(int i=0;i<150;i++){
						y[i]=0;
						s[i]=0;
					}
					break;
				case 1:
					for(int i=0;i<150;i++){
						y[i]=(int)((8*100/(Math.PI*Math.PI))*Math.sin(2*3.14/pd*i));
						s[i]=(int)((8*100/(Math.PI*Math.PI))*Math.sin(2*3.14/pd*i));
					}
					break;
				case 2:
					for(int i=0;i<150;i++){
						y[i]=y[i]-(int)((8.0*100.0/(Math.PI*Math.PI))*(1.0/9.0)*Math.sin(3*2*3.14/pd*i));
						s[i]=(int)((8.0*100.0/(Math.PI*Math.PI))*(1.0/9.0)*Math.sin(3*2*3.14/pd*i));
					}
					break;
				case 3:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((8.0*100/(Math.PI*Math.PI))*(1.0/25.0)*Math.sin(5*2*3.14/pd*i));
						s[i]=(int)((8.0*100/(Math.PI*Math.PI))*(1.0/25.0)*Math.sin(5*2*3.14/pd*i));
					}
					break;
				case 4:
					for(int i=0;i<150;i++){
						y[i]=y[i]-(int)((8.0*100/(Math.PI*Math.PI))*(1.0/49.0)*Math.sin(7*2*3.14/pd*i));
						s[i]=(int)((8.0*100/(Math.PI*Math.PI))*(1.0/49.0)*Math.sin(7*2*3.14/pd*i));
					}
					break;
				case 5:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((8.0*100/(Math.PI*Math.PI))*(1.0/81.0)*Math.sin(9*2*3.14/pd*i));
						s[i]=(int)((8.0*100/(Math.PI*Math.PI))*(1.0/81.0)*Math.sin(9*2*3.14/pd*i));
					}
					break;
				case 6:
					for(int i=0;i<150;i++){
						y[i]=y[i]-(int)((8.0*100.0/(Math.PI*Math.PI))*(1.0/121.0)*Math.sin(11*2*3.14/pd*i));
						s[i]=(int)((8.0*100/(Math.PI*Math.PI))*(1.0/121.0)*Math.sin(11*2*3.14/pd*i));
					}
					break;
				case 7:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((8.0*100/(Math.PI*Math.PI))*(1.0/169.0)*Math.sin(13*2*3.14/pd*i));
						s[i]=(int)((8.0*100/(Math.PI*Math.PI))*(1.0/169.0)*Math.sin(13*2*3.14/pd*i));
					}
					break;
				case 8:
					for(int i=0;i<150;i++){
						y[i]=y[i]-(int)((8.0*100/(Math.PI*Math.PI))*(1.0/225.0)*Math.sin(15*2*3.14/pd*i));
						s[i]=(int)((8.0*100/(Math.PI*Math.PI))*(1.0/225.0)*Math.sin(15*2*3.14/pd*i));
					}
					break;
				case 9:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((8.0*100/(Math.PI*Math.PI))*(1.0/289.0)*Math.sin(17*2*3.14/pd*i));
						s[i]=(int)((8.0*100/(Math.PI*Math.PI))*(1.0/289.0)*Math.sin(17*2*3.14/pd*i));
					}
					break;
				case 10:
					for(int i=0;i<150;i++){
						y[i]=y[i]-(int)((8.0*100/(Math.PI*Math.PI))*(1.0/361.0)*Math.sin(19*2*3.14/pd*i));
						s[i]=(int)((8.0*100/(Math.PI*Math.PI))*(1.0/361.0)*Math.sin(19*2*3.14/pd*i));
					}
					break;
				case 11:
					for(int i=0;i<150;i++){
						y[i]=y[i]+(int)((8.0*100/(Math.PI*Math.PI))*(1.0/441.0)*Math.sin(21*2*3.14/pd*i));
						s[i]=(int)((8.0*100.0/(Math.PI*Math.PI))*(1.0/441.0)*Math.sin(21*2*3.14/pd*i));
					}
					break;
					
				default:
					break;
			}
		}
	
	}
	
	public void zeichneZyklus()
	{
		// Funktionsgenerator
		
		g.setColor(Color.blue);
		g.drawLine(30,200,180,200);
		g.drawLine(30,80,30,320);
		g.drawString("t",170,210);
		g.drawString("A",20,90);
		px[0]=27;
		py[0]=80;
		px[1]=33;
		py[1]=80;
		px[2]=30;
		py[2]=70;
		g.fillPolygon(px,py,3);
		px[0]=180;
		py[0]=197;
		px[1]=180;
		py[1]=203;
		px[2]=190;
		py[2]=200;
		g.fillPolygon(px,py,3);
		g.setColor(Color.black);
		for(int i=0;i<149;i++){
			g.drawLine(30+i,200-x[i],30+i+1,200-x[i+1]);
		}
		g.setColor(Color.red);
		for(int i=0;i<149;i++){
			g.drawLine(30+i,200-y[i],30+i+1,200-y[i+1]);
		}
		g.setColor(Color.black);
		g.setColor(Color.blue);
		g.drawLine(30,450,180,450);
		g.drawLine(30,330,30,570);
		g.drawString("t",170,460);
		g.drawString("A",20,340);
		px[0]=27;
		py[0]=330;
		px[1]=33;
		py[1]=330;
		px[2]=30;
		py[2]=320;
		g.fillPolygon(px,py,3);
		px[0]=180;
		py[0]=447;
		px[1]=180;
		py[1]=453;
		px[2]=190;
		py[2]=450;
		g.fillPolygon(px,py,3);
		g.setColor(Color.red);
		for(int i=0;i<149;i++){
			g.drawLine(30+i,450-s[i],30+i+1,450-s[i+1]);
		}
		
		// Ausgang
		g.setColor(Color.blue);
		g.drawLine(240,200,390,200);
		g.drawLine(240,70,240,320);
		g.drawString("f",370,215);
		g.drawString("A",230,90);
		px[0]=237;
		py[0]=80;
		px[1]=243;
		py[1]=80;
		px[2]=240;
		py[2]=70;
		g.fillPolygon(px,py,3);
		px[0]=380;
		py[0]=197;
		px[1]=380;
		py[1]=203;
		px[2]=390;
		py[2]=200;
		g.fillPolygon(px,py,3);
		g.setColor(Color.black);
		if(cTestfunk.getSelectedIndex()==0){
			for(int i=0;i<12;i++){
				g.drawLine(240+10*i,200,240+10*i,200-(int)((4.0*100.0/Math.PI)*(1.0/(2.0*i-1))));
			}
			g.setColor(Color.red);
			for(int i=1;i<frequenz;i++){
				g.drawLine(240+10*i,200,240+10*i,200-(int)((4.0*100.0/Math.PI)*(1.0/(2.0*i-1))));
			}
		}
		if(cTestfunk.getSelectedIndex()==1){
			g.setColor(Color.black);
			g.drawLine(240+10,200,240+10,100);
			g.setColor(Color.red);
			if(frequenz>=1){
				g.drawLine(240+10,200,240+10,100);
			}
		}
		if(cTestfunk.getSelectedIndex()==2){
			for(int i=0;i<12;i++){
				g.drawLine(240+10*i,200,240+10*i,200-(int)((8.0*100.0/(Math.PI*Math.PI))*(1.0/((2.0*i-1)*(2.0*i-1)))));
			}
			g.setColor(Color.red);
			for(int i=1;i<frequenz;i++){
				g.drawLine(240+10*i,200,240+10*i,200-(int)((8.0*100.0/(Math.PI*Math.PI))*(1.0/((2.0*i-1)*(2.0*i-1)))));
			}
		}
		g.setColor(Color.black);
		g.drawString("Frequenzspektrum",280,80);
		g.drawString("Testfunktion und Näherungskurve",40,80);
		g.drawString("Hinzukommende Frequenz",40,340);
		g.drawString("Harald Schellinger",300,340);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == uebernehmeButton)){
			for(int i=0;i<150;i++){
				y[i]=0;
				x[i]=0;
				s[i]=0;
			}
			frequenz=0;
			return true;
		}
		else return false;
	}
}
