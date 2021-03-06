

import java.applet.Applet;
import java.awt.*;




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
public class Marktzyklus extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	
	int i;
	int angebot, nachfrage;
	int preis, menge;
	double anreiz;
	int produktion[] = new int [200];
	TextField anField;
	Button berechneButton, uebernehmeButton;
	String str;
	int px[] = new int[3];
	int py[] = new int[3];
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
		for (i=0; i<200; i++) produktion[i]=(int)(40+20*Math.sin(0.1*i)-10*Math.random());
		angebot = 100;
		nachfrage = 100;
		anreiz = 130;
		ausgabe=null;
		g=null;
		resize(450, 250);
		
		
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
				
				Thread.sleep(100);
		        repaint();
				
				
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
		double m = anreiz /100;
		menge = produktion[199];
		if (menge < 0) menge =0;
		if(menge>100) menge = 100;
		preis = angebot - menge;
		for (i=199;i>0;i--)
		{
			produktion[i]=produktion[i-1];
		}
		
		produktion[0]=(int) (preis/m);
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
		g.drawLine(399,150,399,210);
		g.drawLine(399,210,50,210);
		g.drawLine(50,210,50,150);
		px[0] = 50;
		py[0] = 150;
		px[1] = 47;
		py[1] = 160;
		px[2] = 53;
		py[2] = 160;
		g.fillPolygon(px, py, 3);
		g.setColor(Color.red);
		g.drawOval(50+(int)(preis/m)-5,150-(int)preis-5,10,10);
		//g.drawOval(50+menge-5,150-(int)(m*menge)-5,10,10);
		g.drawOval(50 +menge-5,150-angebot+menge-5,10,10);
		g.setColor(Color.green);
		for(i=0;i<200;i++)
		{
			g.drawLine(200+i,150,200+i,150-produktion[i]);
		}
		g.setColor(Color.black);
		g.drawString("Produktionsanreiz",10,35);
		g.drawString("Preis",10,55);
		g.drawString("Menge",150,165);
		g.drawString("Menge",210,55);
		g.drawString("Zeit",400,165);
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
		g.drawLine(200,150,410,150);
		px[0] = 420;
		py[0] = 150;
		px[1] = 410;
		py[1] = 147;
		px[2] = 410;
		py[2] = 153;
		g.fillPolygon(px, py, 3);
		g.drawLine(200,150,200,40);
		px[0] = 200;
		py[0] = 40;
		px[1] = 203;
		py[1] = 50;
		px[2] = 197;
		py[2] = 50;
		g.fillPolygon(px, py, 3);
		g.drawString("Harald Schellinger",270,230);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == berechneButton)){
			for (i=0; i<200; i++) produktion[i]=(int)(40+20*Math.sin(0.1*i)-10*Math.random());
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