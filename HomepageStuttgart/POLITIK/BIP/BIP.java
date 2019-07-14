/*
 * Created on 15.08.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;


/**
 * @author hars
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BIP extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	double kapital=500;
	double konsum =500;
	double bipwachs=3;
	double zins =3.5;
	int zeit;
	Button uebernehmeButton;
	Button neustartButton;
	TextField bipText, zinsText;
	
	public void init()
	{	
		setLayout(null); // Freie Positionierung
		uebernehmeButton = new Button("Übernehme");
		uebernehmeButton.setBounds(150,20,80,20);
		add(uebernehmeButton);
		neustartButton = new Button("Neustart");
		neustartButton.setBounds(150,40,80,20);
		add(neustartButton);
		bipText = new TextField ("3.0", 5);
		bipText.setBounds(100, 20 , 40, 20 );
		add(bipText);
		zinsText = new TextField ("3.5", 5);
		zinsText.setBounds( 100, 40 , 40, 20 );
		add(zinsText);
		resize(400,400);
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
				zeit++;
				
				grafik_vorbereiten();
				
				Thread.sleep(200);
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
		double gesamt;
		gesamt = konsum+kapital;
		gesamt = gesamt*(1+bipwachs/100);
		kapital = kapital*(1+zins/100);
		if (kapital > gesamt) kapital = gesamt;
		konsum = gesamt - kapital;
	}
	
	public void zeichneZyklus()
	{
		int xvers = 200;
		int yvers = 200;
		int laenge;
		double gesamt;
		g.setColor(Color.green);
		g.drawString("Nettozahler",10,95);
		gesamt = konsum+kapital;
		laenge = (int)Math.sqrt(gesamt);
		g.fillArc(xvers-(laenge/2),yvers-(laenge/2),laenge,laenge,90,(int)(konsum/gesamt*360));
		g.setColor(Color.red);
		g.drawString("Zinsempfänger",10,115);
		g.fillArc(xvers-(laenge/2),yvers-(laenge/2),laenge,laenge,90,(int)(-kapital/gesamt*360));
		g.setColor(Color.black);
		g.drawString("BIP Wachstum: ",10,35);
		g.drawString("Kapitalzins: ",10,55);
		g.drawString("Jahre: "+zeit,10,75);
		//g.drawString("Bakterien: "+(int)bakterien,10,10);
		g.drawString("Harald Schellinger",280,380);
	}
	
	public boolean action(Event event, Object eventObject){
		String str;
		if ((event.target == uebernehmeButton)){
			str = zinsText.getText();
			if(str.length()!=0)	zins = (Double.valueOf(zinsText.getText())).doubleValue();
			str = bipText.getText();
			if(str.length()!=0)	bipwachs = (Double.valueOf(bipText.getText())).doubleValue();
			return true;
		}
		if ((event.target == neustartButton)){
			konsum = 500;
			kapital = 500;
			zeit = 0;
			return true;
		}
		else return false;
	}

}