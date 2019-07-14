/*
 * Created on 24.06.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.applet.Applet;
import java.awt.*;
/**
 * @author Harald
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

public class Cobweb extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;

	int sizex,sizey;
	double m;
	int i;
	int angebot, nachfrage;
	int preis, menge;
	double anreiz;
	int produktion[] = new int [200];
	TextField anField;
	Button berechneButton, uebernehmeButton;
	String str;
	int zeit=4;
	int px[] = new int[3];
	int py[] = new int[3];
	int preisA[] = new int[120];
	int mengeA[] = new int[120];
	int webZaehler =0;
	public void init()
	{
		setLayout(null); // Freie Positionierung
		anField = new TextField ("130", 5);
		anField.setBounds( 120, 20 , 50, 20 );
		add(anField);
		uebernehmeButton = new Button("�bernehmen");
		uebernehmeButton.setBounds(190,20,80,20);
		add(uebernehmeButton);
		berechneButton = new Button("Neustart");
		berechneButton.setBounds(280,20,80,20);
		add(berechneButton);
		//for (i=0; i<200; i++) produktion[i]=(int)Math.abs(50-30*Math.sin(0.1*i));
		//for (i=0; i<200; i++) produktion[i]=20;
		//for (i=0; i<200; i++) produktion[i]=(int)Math.abs(50-30*Math.random());
		//for (i=0; i<200; i++) produktion[i]=(int)(40+20*Math.sin(0.1*i)-10*Math.random());
		angebot = 100;
		nachfrage = 100;
		anreiz = 130;
		ausgabe=null;
		g=null;
		resize(500, 280);
		
		m = anreiz /100;
		preisA[0]=30;
		mengeA[0]=(int)(preisA[0]/m);
		webZaehler = 1;
		preisA[1]=100-mengeA[0];
		mengeA[1]=mengeA[0];
		webZaehler = 2;
		preisA[2]=preisA[1];
		mengeA[2]=(int)(preisA[1]/m);
		webZaehler = 3;
		for (i=0; i<179; i++) produktion[i]= 0;
		for (i=179; i<200; i++) produktion[i]= mengeA[0];
		
		int sx=size().width;
		int sy=size().height;
		if(sx!=sizex || sy!=sizey){
			sizex=sx;
			sizey=sy;
			ausgabe=createImage(sx,sy);
			g= ausgabe.getGraphics();
		} // if
	}

	public void grafik_vorbereiten()
	{
		g.clearRect(0,0,sizex,sizey);
		zeichneZyklus();
	}

	public void paint (Graphics g1)
	{
		//grafik_vorbereiten();
		
		g1.drawImage(ausgabe, 0, 0, this);
	}

	public void repaint (Graphics g1)
	{
		//grafik_vorbereiten();
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
				zeit+=1;
				rechneZyklus();
				grafik_vorbereiten();
				
				Thread.sleep(100);
		        repaint();
		        Thread.sleep(200);
		        if(zeit>200) {
		        	zeit=0;
		        }
		        if(zeit==199){
		        	
		        	if((webZaehler%2)==0)
		    		{
		    			preisA[webZaehler]=preisA[webZaehler-1];
		    			mengeA[webZaehler]=(int)(preisA[webZaehler-1]/m);
		    		}
		    		if((webZaehler%2)==1)
		    		{
		    			preisA[webZaehler]=100-mengeA[webZaehler-1];
		    			mengeA[webZaehler]=mengeA[webZaehler-1];
		    		}
		    		if (webZaehler<119) webZaehler++;
		        }
		        if(zeit==0){
		        	
		        	if((webZaehler%2)==0)
		    		{
		    			preisA[webZaehler]=preisA[webZaehler-1];
		    			mengeA[webZaehler]=(int)(preisA[webZaehler-1]/m);
		    		}
		    		if((webZaehler%2)==1)
		    		{
		    			preisA[webZaehler]=100-mengeA[webZaehler-1];
		    			mengeA[webZaehler]=mengeA[webZaehler-1];
		    		}
		    		if (webZaehler<119) webZaehler++;
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
		m = anreiz /100;
		menge = produktion[199];
		if (menge < 0) menge =0;
		if(menge>100) menge = 100;
		preis = angebot - menge;
		for (i=199;i>0;i--)
		{
			produktion[i]=produktion[i-1];
		}

		

		
		
		if(menge!=0){
			produktion[0]=(int) (preis/m);
		}
		else produktion[0]=0;
	}
	
	public void zeichneZyklus()
	{
		double m = anreiz /100;
		g.setColor(Color.blue);
		g.drawLine(50,150-nachfrage,150,150);
		g.drawString("Nachfrage: blau",55,180);
		g.setColor(Color.green);
		g.drawLine(50,150,150,150-(int) anreiz);
		g.drawString("Angebot: gr�n",55,200);
		g.drawLine(420,180,420,240);
		g.drawLine(420,240,50,240);
		g.drawLine(50,240,50,150);
		//px[0] = 50;
		//py[0] = 150;
		//px[1] = 47;
		//py[1] = 160;
		//px[2] = 53;
		//py[2] = 160;
		//g.fillPolygon(px, py, 3);
		//(g.setColor(Color.red);
		//g.drawOval(50+(int)(preis/m)-5,150-(int)preis-5,10,10);
		//g.drawOval(50+menge-5,150-(int)(m*menge)-5,10,10);
		//g.drawOval(50 +menge-5,150-angebot+menge-5,10,10);
		g.setColor(Color.green);
		for(i=0;i<200;i++)
		{
			g.drawLine(220+i,150,220+i,150-produktion[i]);
		}
		g.setColor(Color.black);
		g.drawString("Produktionsanreiz",10,35);
		g.drawString("Preis",10,55);
		g.drawString("Menge",150,165);
		//g.drawString("Menge",210,55);
		//g.drawString("Zeit",400,165);
		g.drawLine(50,150,160,150);
		px[0] = 170;
		py[0] = 150;
		px[1] = 160;
		py[1] = 147;
		px[2] = 160;
		py[2] = 153;
		g.fillPolygon(px, py, 3);
		g.drawLine(50,150,50,40);
		px[0] = 50;
		py[0] = 40;
		px[1] = 53;
		py[1] = 50;
		px[2] = 47;
		py[2] = 50;
		g.fillPolygon(px, py, 3);
		
		//g.drawLine(200,150,410,150);

		g.setColor(Color.red);
		for(i=1;i<webZaehler;i++)
		{
			g.drawLine(50+mengeA[i-1],150-preisA[i-1],50+mengeA[i],150-preisA[i]);
			//g.drawString("Z"+webZaehler,150,100);
			//g.drawString("M"+mengeA[webZaehler-1],150,120);
			//g.drawString("P"+preisA[webZaehler-1],150,140);
		}
		
		g.setColor(Color.black);
		g.drawLine(220,150,420,150);
		g.drawLine(220,180,420,180);
		g.fillOval(205,150,30,30);
		g.fillOval(405,150,30,30);
		g.setColor(Color.yellow);
		g.fillArc(205,150,30,30,(int)(-360*4*zeit/200),30);
		g.fillArc(405,150,30,30,(int)(-360*4*zeit/200),30);
		g.setColor(Color.black);
		g.drawString("Harald Schellinger",300,220);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == berechneButton)){
			m = anreiz /100;
			preisA[0]=30;
			mengeA[0]=(int)(preisA[0]/m);
			webZaehler = 1;
			preisA[0]=30;
			mengeA[0]=(int)(preisA[0]/m);
			webZaehler = 1;
			preisA[1]=100-mengeA[0];
			mengeA[1]=mengeA[0];
			webZaehler = 2;
			preisA[2]=preisA[1];
			mengeA[2]=(int)(preisA[1]/m);
			webZaehler = 3;
			zeit=1;
			for (i=0; i<179; i++) produktion[i]= 0;
			for (i=179; i<200; i++) produktion[i]= mengeA[0];
			return true;
		}
		if ((event.target == uebernehmeButton)){
			str = anField.getText();
			if(str.length()!=0)	anreiz = (Double.valueOf(anField.getText())).doubleValue();
			return true;
		}
		else return false;
	}
}