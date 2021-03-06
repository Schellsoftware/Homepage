
import java.applet.Applet;
import java.awt.*;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextArea;
import java.awt.TextField;


/*
 * Created on 28.05.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Harald Schellinger
 *
 * 
 *
 */
public class satellit extends Applet{
	TextField lambdaF,lambdaB,betaF;
	Button berechne;
	boolean berechnet = false;
	boolean fehler;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	int px[] = new int[3];
	int py[] = new int[3];
	final double RE = 6378388;
	final double RS = 42164124.5;
	final double K = RE/RS;
	double alpha;
	double [] alphaA = new double[38];
	double delta;
	double [] deltaA = new double[38];
	double psi;
	double [] psiA = new double[38];
	double beta;
	double lambda;
	double [] lambdaA = new double[38];
	int i,j;

	/** Initialisierung der Variablen.
	 * Die Eingabefelder und Buttons werden erzeugt. 
	 * Die Gr��e des Ausgabebildes und Applet werden festgelegt.
	 */
	public void init()
	{
		setLayout(null); // Freie Positionierung
		lambdaF = new TextField ("7.8569", 5);// Eingabefeld f�r Lambda
		lambdaF.setBounds( 220, 20 , 50, 20 );
		add(lambdaF);
		lambdaB = new TextField ("19.2", 5);// Eingabefeld f�r Lambda
		lambdaB.setBounds( 220, 40 , 50, 20 );
		add(lambdaB);
		betaF = new TextField ("48.8", 5); // Eingabefeld f�r Beta
		betaF.setBounds( 220, 60 , 50, 20 );
		add(betaF);
		berechne = new Button ("Berechnen");
		berechne.setBounds (80,80,100,20);
		add(berechne);
		// Gr�sse des Applet festlegen
		resize(450, 800);
		int sx=size().width;
		int sy=size().height;
		if(sx!=sizex || sy!=sizey){
			sizex=sx;
			sizey=sy;
			ausgabe=createImage(sx,sy);// Grafik Buffer
			g=ausgabe.getGraphics();// Grafikreferenz erzeugen
		} 
		
	}
	/**
	 * Image wird gezeichnet.
	 * Die Grafik wird erzeugt. Die Ausgabetabelle wird erzeugt und einer Grafik 
	 * zur Veranschaulichung der Elevationskurve wird erzeugt.
	 */
	public void grafik_vorbereiten()
	{
		g.clearRect(0,0,sizex,sizey);
		// Beschriftung Eingabefelder
		g.drawString("L�ngengarad eigene Position:",10,35);
		g.drawString("L�ngengrad des Satelliten:",10,55);
		g.drawString("Breitengrad ihrer Position Beta:",10,75);
		// Fehlerbehandlung bei falscher Eingabe
		if(fehler)
		{
			g.drawString("Fehler bei der Eingabe!", 20,100);
		}
		// Falls die Berechnungen durchgef�hrt wurden
		if(berechnet){
			// Die Winkel f�r die Ausgabe
			String aPsi = new String( Double.toString(psi) );
			String aAlpha = new String( Double.toString(alpha) );
			String aDelta = new String( Double.toString(delta) );
			// L�nge der Ausgabestrings begrenzen
			if ( aPsi.length() > 7 ) {
				aPsi = aPsi.substring(0,6);
			}
			if ( aAlpha.length() > 7 ) {
				aAlpha = aAlpha.substring(0,6);
			}
			if ( aDelta.length() > 7 ) {
				aDelta = aDelta.substring(0,6);
			}
			g.drawString("Azimutwinkel Psi: "+aPsi,10,115);
			g.drawString("Elevationswinkel Alpha: "+aAlpha,10,135);
			g.drawString("Korrekturwinkel Delta: "+aDelta,10,155);
			// Aufbau der Elevationskurve
			g.drawString("Elevationskurve",300,30);
			g.drawString("Psi",300,50);
			g.drawString("Alpha",350,50);
			g.drawString("Delta",400,50);
			// Tabelle ausf�llen
			for(j=0;j<37;j++)
			{
				aPsi = new String( Double.toString(psiA[j]) );
				aAlpha = new String( Double.toString(alphaA[j]) );
				aDelta = new String( Double.toString(deltaA[j]) );

				if ( aPsi.length() > 7 ) {
					aPsi = aPsi.substring(0,6);
				}
				if ( aAlpha.length() > 7 ) {
					aAlpha = aAlpha.substring(0,6);
				}
				if ( aDelta.length() > 7 ) {
					aDelta = aDelta.substring(0,6);
				}
				g.drawString(aPsi,300,70+j*20);
				g.drawString(aAlpha,350,70+j*20);
				g.drawString(aDelta,400,70+j*20);
			}
			// Grafik erzeugen
			g.setColor(Color.blue);
			g.drawLine(10,300,220,300);// Abszisse
			px[0] = 230;
			py[0] = 300;
			px[1] = 220;
			py[1] = 297;
			px[2] = 220;
			py[2] = 303;
			g.fillPolygon(px, py, 3);//Pfeil
			g.drawLine(20,305,20,295);//Beschriftung Abszisse
			if (beta > 0)
			{
				g.drawString("90",20,315);
				g.drawLine(50,305,50,295);
				g.drawString("120",50,315);
				g.drawLine(80,305,80,295);
				g.drawString("150",80,315);
				g.drawLine(110,305,110,295);
				g.drawString("180",110,315);
				g.drawLine(150,305,150,295);
				g.drawString("210",150,315);
				g.drawLine(180,305,180,295);
				g.drawString("240",180,315);
				g.drawLine(210,305,210,295);
				g.drawString("270",210,315);
				g.drawString("Psi",210,330);
			}
			else
			{
				g.drawString("270",20,315);
				g.drawLine(50,305,50,295);
				g.drawString("300",50,315);
				g.drawLine(80,305,80,295);
				g.drawString("330",80,315);
				g.drawLine(110,305,110,295);
				g.drawString("0",110,315);
				g.drawLine(150,305,150,295);
				g.drawString("30",150,315);
				g.drawLine(180,305,180,295);
				g.drawString("60",180,315);
				g.drawLine(210,305,210,295);
				g.drawString("90",210,315);
				g.drawString("Psi",210,330);
			}
			
			g.drawLine(110,340,110,200);// Ordinate
			px[0] = 110;
			py[0] = 190;
			px[1] = 107;
			py[1] = 200;
			px[2] = 113;
			py[2] = 200;
			g.fillPolygon(px, py, 3);//Pfeil
			g.drawLine(105,330,115,330);// Beschriftung
			g.drawString("-30",120,335);
			g.drawLine(105,300,115,300);
			g.drawString("0",120,305);
			g.drawLine(105,270,115,270);
			g.drawString("30",120,275);
			g.drawLine(105,240,115,240);
			g.drawString("60",120,245);
			g.drawLine(105,210,115,210);
			g.drawString("90",120,215);
			g.drawString("Alpha",120,195);
			g.setColor(Color.black);
			if (beta > 0)
			{
				for(j=0;j<36;j++)
				{
					// Linie der Grafik
					g.drawLine((int)psiA[j]-70,(int)-alphaA[j]+300,(int)psiA[j+1]-70,(int)-alphaA[j+1]+300);
				}
			}
			else
			{
				for(j=0;j<18;j++)
				{
					// Linie der Grafik
					g.drawLine((int)psiA[j]+110,(int)-alphaA[j]+300,(int)psiA[j+1]+110,(int)-alphaA[j+1]+300);
				}
				for(j=19;j<36;j++)
				{
					// Linie der Grafik
					g.drawLine((int)psiA[j]-250,(int)-alphaA[j]+300,(int)psiA[j+1]-250,(int)-alphaA[j+1]+300);
				}
			}
			
		}
	}
	/**
	 * Grafik beim ersten Zeichnen.
	 */
	public void paint (Graphics g1)
	{
		grafik_vorbereiten();
		g1.drawImage(ausgabe, 0, 0, this);
	}
	/**
	 * Neuzeichnen des Applet
	 * @param g1
	 */
	public void repaint (Graphics g1)
	{
		grafik_vorbereiten();
		g1.drawImage(ausgabe, 0, 0, this);
	}
	/**
	 * Aktion beim Dr�cken des Berechnenbuttons.
	 * Wenn der Berechnenbutton ged�ckt wird, wird die Satellitenkurve neu berechnet.
	 * Danach wird die Grafik mit der Tabelle und der Elevationskurve neu gezeichnet.
	 */
	public boolean action(Event event, Object eventObject){
		if ((event.target == berechne)){
			berechneSat();
			repaint();
			return true;
		}
		else return false;
	}
	/**
	 * Berechnung der Ausrichtung der Satellitensch�ssel.
	 * Es werden die Daten aus den Eingabefelder eingelesen. 
	 * Die Winkel Psi, Alpha und Delta werden in Abh�ngigkeit der Eingabedaten berechnet.
	 * Die Elevationskurve wird in Abh�ngigkeit von Psi berechnet.
	 */
	public void berechneSat()
	{
		fehler=false;
		try
		{
			beta = Double.parseDouble(betaF.getText());// Felder auslesen
			beta = Math.toRadians(beta);
			lambda = Double.parseDouble(lambdaF.getText())-Double.parseDouble(lambdaB.getText());
			lambda = Math.toRadians(lambda);
		}
		catch (NumberFormatException e)
		{
			fehler = true;// Bei falscher Eingabe
			berechnet = false;
		}
		if((beta<-Math.PI/2)||(beta>Math.PI/2)||(lambda<-Math.PI)||(lambda>Math.PI))
		{
			fehler = true; // Winkel auf Plausibilit�t pr�fen
			berechnet = false;
		}
		if(!fehler)
		{
			psi = berechne_psi(lambda,beta);
			//alpha = Math.atan((Math.cos(beta)*Math.cos(lambda)-K)/Math.sqrt(1-Math.cos(beta)*Math.cos(beta)*Math.cos(lambda)*Math.cos(lambda)));
			//alpha = Math.toDegrees(alpha);
			alpha = berechne_alpha(lambda,beta);
			//delta = Math.atan((K*Math.sin(beta))/Math.sqrt(1+K*K*Math.cos(beta)*Math.cos(beta)-2*K*Math.cos(beta)*Math.cos(lambda)));
			//delta = Math.toDegrees(delta);
			delta = berechne_delta(lambda,beta);
			berechnet = true;
			j=0;
			if (beta >0)
			{
				for(i=90;i<=270;i+=5)// Arrays f�r Tabelle erzeugen
				{
					psiA[j]=i;	
					lambdaA[j]=berechne_lambda(Math.toRadians(psiA[j]),beta);
					alphaA[j]=berechne_alpha(lambdaA[j],beta);
					deltaA[j]=berechne_delta(lambdaA[j],beta);
					j++;
				}
			}
			else 
			{
				for(i=90;i>=0;i-=5)// Arrays f�r Tabelle erzeugen
				{
					psiA[j]=i;	
					lambdaA[j]=berechne_lambda(Math.toRadians(psiA[j]),beta);
					alphaA[j]=berechne_alpha(lambdaA[j],beta);
					deltaA[j]=berechne_delta(lambdaA[j],beta);
					j++;
				}
				for(i=355;i>=270;i-=5)// Arrays f�r Tabelle erzeugen
				{
					psiA[j]=i;	
					lambdaA[j]=berechne_lambda(Math.toRadians(psiA[j]),beta);
					alphaA[j]=berechne_alpha(lambdaA[j],beta);
					deltaA[j]=berechne_delta(lambdaA[j],beta);
					j++;
				}
			}
				
		}
	}
	/** Berechnung Azimutwinkel.
	 * Der Azimutwinkel wird unter Ber�cksichtigung des Sonderfalls �quator berechhnet.
	 * @param lambda Relative Satellitenposition.
	 * @param beta Breitengrad.
	 * @return Azimutwinekel in Grad. 
	 */
	public double berechne_psi(double lambda, double beta)
	{
		double psih;
		if ((beta==0)&&(lambda <=0))
			return 90.0;
		if ((beta==0)&&(lambda >0))
			return 270.0;
		if (beta >0) // N�rdliche Halbkugel
		{
			psih=Math.atan(Math.tan(lambda)/Math.sin(beta));
			psih = Math.toDegrees(psih);
			return psih +180;
		}
		else // S�dliche Halbkugel
		{
			psih=Math.atan(Math.tan(lambda)/Math.sin(beta));
			psih= Math.toDegrees(psih);
			if (psih <0) 
				psih = 360+psih;
			return psih;
		}
	}
	/** Berechnung Elevationswinkel.
	 * Der Elevationswinkel alpha wird berechnet.
	 * @param lambda Relative Satellitenposition.
	 * @param beta Breitengrad.
	 * @return Elevationswinkel in Grad.
	 */
	public double berechne_alpha(double lambda, double beta)
	{
		double alphah;
		alphah = Math.atan((Math.cos(beta)*Math.cos(lambda)-K)/Math.sqrt(1-Math.cos(beta)*Math.cos(beta)*Math.cos(lambda)*Math.cos(lambda)));
		return Math.toDegrees(alphah);
	}
	/** Berechnung Korrekturwinkel.
	 * Der Korrekturwinkel Delta wird berechnet.
	 * @param lambda Relative Satellitenposition.
	 * @param beta Breitengrad.
	 * @return Korrekturwinkel in Grad.
	 */
	public double berechne_delta(double lambda, double beta)
	{
		double deltah;
		deltah = Math.atan((K*Math.sin(beta))/Math.sqrt(1+K*K*Math.cos(beta)*Math.cos(beta)-2*K*Math.cos(beta)*Math.cos(lambda)));
		if (beta < 0) deltah =-deltah;
		return Math.toDegrees(deltah);
	}
	/** Berechnung von der relativen Satellitenposition f�r Elevationskurve.
	 * Die relative Satellitenposition Lambda wird f�r die Elevationskurve berechnet.
	 * @param psi Azimutwinkel.
	 * @param beta Breitengrad.
	 * @return Relative Satellitenposition in Rad.
	 */
	public double berechne_lambda(double psi, double beta)
	{
		return Math.atan(Math.tan(psi)*Math.sin(beta));
	}
    
}
