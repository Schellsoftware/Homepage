

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
public class pglied extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	
	int periode=0;
	double p=0.5;
	double pd=100;
	Choice cTestfunk;
	Button uebernehmeButton;
	int x[]=new int[150];
	int y[]=new int[150];
	TextField pant = new TextField("0.5");
	TextField FPd = new TextField("100");
	String str;
	int px[] = new int[3];
	int py[] = new int[3];
	int zeit=0;

	public void init()
	{
		setLayout(null); // Freie Positionierung
		uebernehmeButton = new Button("Übernehmen");
		uebernehmeButton.setBounds(300,20,100,20);
		add(uebernehmeButton);
		cTestfunk = new Choice();
		cTestfunk.setBounds(20,20,100,20);
		cTestfunk.addItem("Rechteck");
		cTestfunk.addItem("Sinus");
		cTestfunk.addItem("Dreieck");
		add(cTestfunk);
		pant.setBounds(130,20,30,20);
		add(pant);
		FPd.setBounds(130,40,30,20);
		add(FPd);
		
		for(int i=0;i<150;i++) x[i]=0;
		
		ausgabe=null;
		g=null;
		resize(500, 300);
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
				//grafik_vorbereiten();
				
				Thread.sleep(200);
		        repaint();
		        periode ++;
				zeit++;
				if(zeit >= 150){
					zeit=0;
					for(int i=149;i>=0;i--)
					{
						x[i]=0;
						y[i]=0;
					}
				}
				
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
			if((periode%(int)pd)<(int)(pd/2)){
				x[zeit]=100;
			}
			else{
				x[zeit]=-100;
			}
		}
		if(cTestfunk.getSelectedIndex()==1){
			int h;
			h= periode % (int) pd;
			x[zeit]=(int)(100*Math.sin(2*3.14/pd*h));
		}
		if(cTestfunk.getSelectedIndex()==2){
			if((periode%(int)pd)<(int)(pd/2)){
				x[zeit]=-100+400*(periode%(int)pd)/(int)pd;
			}
			else{
				x[zeit]=300-400*(periode%(int)pd)/(int)pd;
			}
		}
		for(int i=149;i>0;i--)
		{
			//x[i]=x[i-1];
		}
		y[zeit]=(int)(p*x[zeit]);
		for(int i=149;i>0;i--)
		{
		//	y[i]=y[i-1];
		}	
	}
	
	public void zeichneZyklus()
	{
		// Funktionsgenerator
		g.setColor(Color.blue);
		g.drawLine(30,150,180,150);
		g.drawLine(30,30,30,270);
		g.drawString("t",170,160);
		g.drawString("u",20,50);
		px[0]=27;
		py[0]=50;
		px[1]=33;
		py[1]=50;
		px[2]=30;
		py[2]=40;
		g.fillPolygon(px,py,3);
		px[0]=180;
		py[0]=147;
		px[1]=180;
		py[1]=153;
		px[2]=190;
		py[2]=150;
		g.fillPolygon(px,py,3);
		g.setColor(Color.red);
		g.drawOval(30+zeit-5,150-5-x[zeit],10,10);
		g.setColor(Color.black);
		for(int i=0;i<149;i++){
			g.drawLine(30+i,150-x[i],30+i+1,150-x[i+1]);
		}
		// P Glied
		g.drawLine(190,150,210,150);
		px[0]=200;
		py[0]=147;
		px[1]=200;
		py[1]=153;
		px[2]=210;
		py[2]=150;
		g.fillPolygon(px,py,3);
		g.drawRect(210,100,100,100);
		g.drawLine(220,110,220,190);
		g.drawLine(220,190,300,190);
		g.drawLine(220,140,300,140);
		g.drawLine(310,150,330,150);
		px[0]=330;
		py[0]=147;
		px[1]=330;
		py[1]=153;
		px[2]=340;
		py[2]=150;
		g.fillPolygon(px,py,3);
		// Ausgang
		g.setColor(Color.blue);
		g.drawLine(340,150,490,150);
		g.drawLine(340,40,340,270);
		g.drawString("t",470,160);
		g.drawString("y",330,50);
		px[0]=337;
		py[0]=50;
		px[1]=343;
		py[1]=50;
		px[2]=340;
		py[2]=40;
		g.fillPolygon(px,py,3);
		px[0]=480;
		py[0]=147;
		px[1]=480;
		py[1]=153;
		px[2]=490;
		py[2]=150;
		g.fillPolygon(px,py,3);
		g.setColor(Color.red);
		g.drawOval(340+zeit-5,150-5-y[zeit],10,10);
		g.setColor(Color.black);
		for(int i=0;i<149;i++){
			g.drawLine(340+i,150-y[i],340+i+1,150-y[i+1]);
		}
		g.setColor(Color.black);
		g.drawString("Proportionalverstärkung",165,35);
		g.drawString("Periodendauer",165,55);
		g.drawString("Harald Schellinger",350,280);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == uebernehmeButton)){
			str = pant.getText();
			if(str.length()!=0)	p = (Double.valueOf(pant.getText())).doubleValue();
			str = FPd.getText();
			if(str.length()!=0)	pd = (Double.valueOf(FPd.getText())).doubleValue();
			
			return true;
		}
		else return false;
	}
}
