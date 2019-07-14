

import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;




/*
 * Created on 18.02.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
 

public class Klo_Steuerung extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	boolean zu;
	int zeitR=0;
	int i;
	int zufluss, abfluss;
	int zuflusszaehler=30;
	int behaelter;
	int abflusszaehler;
	int schwimmer_hoehe;
	int differenz;
	Button spueleButton;
	String str;
	int px[] = new int[3];
	int py[] = new int[3];
	//AudioClip sound;

	public void init()
	{
		setLayout(null); // Freie Positionierung
		spueleButton = new Button("Spülen");
		spueleButton.setBounds(20,20,80,20);
		add(spueleButton);
		
		behaelter = 0;
		abfluss = 0;
		abflusszaehler = 50;
		zufluss = 600;
		ausgabe=null;
		g=null;
		resize(340, 370);
		//sound = getAudioClip(getDocumentBase(),"spuelen.wav");
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
		        repaint();
				Thread.sleep(1000);
				//if(abflusszaehler>12){
					zuflusszaehler--;
					if(zuflusszaehler>0){
						zu=true;
					}
					else {
						zuflusszaehler=0;
						zu=false;					
					}
				//}
				


		        abflusszaehler ++;
		        if (abflusszaehler < 13){
		        	if(behaelter < abfluss){
		        		abfluss =behaelter;
		        	}
		        }
		        if (abflusszaehler > 13){
		        	abfluss =0;
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
		schwimmer_hoehe = (behaelter/150-100);
		if (schwimmer_hoehe <0) schwimmer_hoehe = 0;
		differenz = schwimmer_hoehe;
		if(zu){
			zufluss = 600;
		}
		else{
			zufluss=0;
		}
		behaelter=behaelter + zufluss - abfluss;
		if((behaelter/150)>130) behaelter=130*150;
		
		
	}
	
	public void zeichneZyklus()
	{
		g.setColor(Color.blue);
		if((behaelter/150)<125){
			for (int i=0; i<(behaelter/150);i++)
			{
				g.drawLine(100,250-i,250,250-i);
			}
			
		}
		else{
			for (int i=0; i<130;i++)
			{
				g.drawLine(100,250-i,250,250-i);
			}
			g.fillRect(98,121,2,5);
			g.fillRect(88,121,10,250);
		}
		g.fillRect(20,88,30,12);
		for (int i=0;i<=(zufluss/50);i++)
		{
			if(zufluss != 0){
				g.drawLine(20,100-i,130,100-i);
				g.drawLine(130+i,100-(zufluss/50),130+i,250);
			}
		}

		g.setColor(Color.black);
		g.fillRect(20,86,124,2);//Zuflussrohr
		g.fillRect(20,100,110,2);
		g.fillRect(142,86,2,50);
		g.fillRect(128,101,2,36);
		g.fillRect(98,130,2,122);//Behaelter
		g.fillRect(251,120,2,132);
		g.fillRect(98,251,154,2);
		g.fillRect(251,252,2,100);
		g.fillRect(209,252,2,100);
		g.setColor(Color.blue);
		for(int i=0;i<(abfluss/50);i++){
			g.drawLine(250-i,250,250-i,350);
		}
		g.setColor(Color.red);
		//g.fillOval(200,150-schwimmer_hoehe-10,40,20);
		if(zu){
			g.fillRect(50,68,10,20);
		}
		else{
			g.fillRect(50,80,10,20);
		}
		//g.drawLine(50,68+schwimmer_hoehe,220,68-schwimmer_hoehe);
		//g.drawLine(220,150-schwimmer_hoehe-10,220,68-schwimmer_hoehe);
		g.setColor(Color.black);
		g.drawString("Füllzeit: "+zuflusszaehler,110,35);
		g.drawString("Harald Schellinger",20,280);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == spueleButton)){
			abfluss = 2000;
			if(abflusszaehler>12){
				abflusszaehler = 0;
			}
			zuflusszaehler +=30;
			//zeitR=zeitR+30;
			//sound.play();
			//for (i=0; i<200; i++) produktion[i]=(int)(40+20*Math.sin(0.1*i)-10*Math.random());
			return true;
		}

		else return false;
	}
}
