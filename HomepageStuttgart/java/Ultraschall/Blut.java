

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

import Elemente.Kurve;




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
public class Blut extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	

	int ein = 1;
	double zeit=0;
	double wasser;
	double blut;
	int t=0;
	boolean stop=true;
	Image bild;
	Button StartButton;
	Kurve verlauf;
	String str;
	int px[] = new int[3];
	int py[] = new int[3];


	public void init()
	{
		setLayout(null); // Freie Positionierung
		StartButton = new Button("Start");
		StartButton.setBounds(20,20,50,20);
		//add(StartButton);
		verlauf=new Kurve(320,250,"t","A");
		MediaTracker mt = new MediaTracker(this);//Ladeassistent erzeugen
		for(int i=1;i<13;i++){
		  bild = getImage(getCodeBase(),"Erythrozyt.png");//Bilder laden
		  mt.addImage(bild,1);//Bilder zum Ladeassistent hinzufügen
		}
		try {
			mt.waitForAll();//Auf die Bilder warten
			
		} catch (InterruptedException e){
			;
		}
		
		ausgabe=null;
		g=null;
		resize(600, 450);
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


				//if(periode == 150) periode=0;
				Thread.sleep(200);
		        repaint();
		        zeit +=0.1;
		        t++;
		        if(t>200){
		        	t=0;
		        	for(int i=0;i<250;i++) verlauf.x[i]=0;
					for(int i=0;i<250;i++) verlauf.y[i]=0;
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
		wasser=100*Math.sin(2*Math.PI*t/200);
		blut=80*Math.sin(2*Math.PI*(t)/200);
		verlauf.x[t]= (int)wasser;
		verlauf.y[t]= (int)blut;
	}
	
	public void zeichneZyklus()
	{
		verlauf.zeichne(g);
		g.setColor(Color.blue);
		g.fillRect(20,150-(int)wasser,200,200);
		g.drawLine(220,250-(int)wasser,320+t,250-(int)wasser);
		g.setColor(Color.red);
		//g.fillRect(95,225-(int)blut,50,50);
		g.drawImage(bild,95,225-(int)blut,this);//Bilder anzeigen
		g.drawLine(145,250-(int)blut,320+t,250-(int)blut);
		g.setColor(Color.black);
		//g.drawString("Harald Schellinger",400,180);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == StartButton)){
			stop = false;
			ein=1;
			zeit=0;
			t=0;
			for(int i=0;i<250;i++) verlauf.x[i]=0;
			for(int i=0;i<250;i++) verlauf.y[i]=0;
			return true;
		}
		else return false;
	}
}
