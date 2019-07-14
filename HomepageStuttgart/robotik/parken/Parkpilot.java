import java.applet.Applet;
import java.awt.Button;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;

import fahrzeug.Fahrzeug;




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
public class Parkpilot extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	int trajz=0;
	int trajx[]=new int[100000];
	int trajy[]=new int[100000];
	int xi,yi,psii;
	Fahrzeug fzg=new Fahrzeug(1,5,0);
	double leitlinie = 4;
	Button setzeButton;
	String str;
	int px[] = new int[3];
	int py[] = new int[3];
	TextField xF = new TextField("1");
	TextField yF = new TextField("5");
	TextField psiF = new TextField("0");
	TextField q11F; 
	TextField q22F; 
	double a12;
	double b2 ;
	double p=1;
	double k12,k22,k11;
	double q11=1;
	double q22=1;
	double kr,kpsi;

	public void init()
	{
		setLayout(null); // Freie Positionierung
		setzeButton = new Button("Setzen");
		setzeButton.setBounds(10,70,60,20);
		add(setzeButton);
		xF.setBounds(10,10,30,20);
		add(xF);
		yF.setBounds(10,30,30,20);
		add(yF);
		psiF.setBounds(10,50,30,20);
		add(psiF);
		fzg.setV(1);
		ausgabe=null;
		g=null;
		resize(600, 350);

		int sx=size().width;
		int sy=size().height;
		if(sx!=sizex || sy!=sizey){
			sizex=sx;
			sizey=sy;
			ausgabe=createImage(sx,sy);
			g=ausgabe.getGraphics();
		} // if

		String krS=Double.toString(fzg.getKr());
		System.out.println(krS);
		String krS2 = krS.substring(0,5);
		String kpsiS=Double.toString(fzg.getKpsi());
		String kpsiS2=kpsiS.substring(0,5);
		q11F= new TextField(krS2);
		q22F= new TextField(kpsiS2);
		q11F.setBounds(330,10,50,20);
		add(q11F);
		q22F.setBounds(330,30,50,20);
		add(q22F);
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
				fzg.regle(leitlinie-fzg.getY(),fzg.getPsi());
				fzg.fahre(0.1);
				if(trajz<100000){
					trajx[trajz]=(int)(fzg.getX()*fzg.getSkal());
					trajy[trajz]=(int)(fzg.getY()*fzg.getSkal());
				}
				trajz++;
				grafik_vorbereiten();
				
				Thread.sleep(200);
		        repaint();
		        if (fzg.getX()>(600/fzg.getSkal())){
					str = xF.getText();
					if(str.length()!=0)	fzg.setX(Double.parseDouble(xF.getText()));
					str = yF.getText();
					if(str.length()!=0)	fzg.setY(Double.parseDouble(yF.getText()));
					str = psiF.getText();
					if(str.length()!=0)	fzg.setPsi(Double.parseDouble(psiF.getText())/180*Math.PI);
					trajz =0;
					fzg.setDelta(0);
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
	

	
	public void zeichneZyklus()
	{
		
		double reifenbreite = 0.1;
		double reifenhoehe = 0.3;
		double reifenentfernung = 0.6;
		
		g.setColor(Color.black);
		
		fzg.zeichne(g);
		g.setColor(Color.blue);
		g.drawLine(0,(int)(leitlinie*fzg.getSkal()),600,(int)(leitlinie*fzg.getSkal()));
		g.setColor(Color.red);
		g.drawLine((int)(fzg.getX()*fzg.getSkal()),(int)(leitlinie*fzg.getSkal()),(int)(fzg.getX()*fzg.getSkal()),(int)(fzg.getY()*fzg.getSkal()));
		g.fillArc((int)(fzg.getX()*fzg.getSkal())-30,(int)(fzg.getY()*fzg.getSkal())-30,60,60,0,(int)(180*fzg.getPsi()/Math.PI));
		g.setColor(Color.green);
		if(trajz>1){
			for(int i=0;i<trajz-1;i++){
				g.drawLine(trajx[i],trajy[i],trajx[i+1],trajy[i+1]);
			}
		}
		g.setColor(Color.black);
		String aPsi = new String( Double.toString(fzg.getPsi()) );
		String ax = new String( Double.toString(fzg.getX()) );
		String ay = new String( Double.toString(fzg.getY()) );

		if ( aPsi.length() > 7 ) {
			aPsi = aPsi.substring(0,6);
		}
		if ( ax.length() > 7 ) {
			ax = ax.substring(0,6);
		}
		if ( ay.length() > 7 ) {
			ay = ay.substring(0,6);
		}
		g.drawString("x Startposition",45,25);
		g.drawString("y Startposition",45,45);
		g.drawString("psi Startposition",45,65);
		g.drawString("x aktuell:   "+ax,200,25);
		g.drawString("y aktuell:   "+ay,200,45);
		g.drawString("Psi aktuell: "+aPsi,200,65);
		g.drawString("Rückführung Abstandsdifferenz",385,25);
		g.drawString("Rückführung Winkeldifferenz",385,45);
		g.drawString("Harald Schellinger",450,330);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == setzeButton)){
			str = xF.getText();
			if(str.length()!=0)	fzg.setX(Double.parseDouble(xF.getText()));
			str = yF.getText();
			if(str.length()!=0)	fzg.setY(Double.parseDouble(yF.getText()));
			str = psiF.getText();
			if(str.length()!=0)	fzg.setPsi(Double.parseDouble(psiF.getText())/180*Math.PI);
			//for (i=0; i<200; i++) produktion[i]=(int)(40+20*Math.sin(0.1*i)-10*Math.random());
			str = q11F.getText();
			if(str.length()!=0)	kr = Double.parseDouble(q11F.getText());
			str = q22F.getText();
			if(str.length()!=0)	kpsi = Double.parseDouble(q22F.getText());
			trajz =0;
			fzg.setDelta(0);
			return true;
		}

		else return false;
	}
}
