import java.applet.Applet;
import java.awt.Button;
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
public class auto_super extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int zeit=0;
	double d1[]=new double[200];
	double d2[]=new double[200];
	double d3[]=new double[200];
	int zeitabschnitt=0;
	int sizex,sizey;
	double skal = 40;
	double x = 1;
	double y = 4;
	double psi=0.0/180.0*Math.PI;
	double xkm1 = x;
	double ykm1 = y;
	double psikm1 = psi;
	double xk,yk,psik;
	double v = 1;
	double delta =0.0/180.0*Math.PI;
	int trajz=0;
	int trajx[]=new int[100000];
	int trajy[]=new int[100000];
	int x1[]=new int[121];
	int x2[]=new int[121];
	int x3[]=new int[121];
	int y1[]=new int[121];
	int y2[]=new int[121];
	int y3[]=new int[121];
	int xi,yi,psii;
	int fzgex[]=new int[4];
	int fzgey[]=new int[4];
	double radabstand = 1.8;
	double uev = 0.6;
	double ueh = 0.6;
	double bhalbe = 0.8;
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
		//add(setzeButton);
		xF.setBounds(10,10,30,20);
		//add(xF);
		yF.setBounds(10,30,30,20);
		//add(yF);
		psiF.setBounds(10,50,30,20);
		//add(psiF);
		for(int i=0;i<200;i++){
			d1[i]=0;
		}
		ausgabe=null;
		g=null;
		resize(600, 250);

		int sx=size().width;
		int sy=size().height;
		if(sx!=sizex || sy!=sizey){
			sizex=sx;
			sizey=sy;
			ausgabe=createImage(sx,sy);
			g=ausgabe.getGraphics();
		} // if
		a12 = 2;
		b2 = a12/radabstand;
		k12=Math.sqrt(b2*p*q11)/(b2*b2*p);
		k22=Math.sqrt((2*a12*k12+q22)/(b2*b2*p));
		k11=(b2*b2*k12*k22*p)/a12;
		kr = p*k12*b2;
		kpsi = p*k22*b2;
		String krS=Double.toString(kr);
		String krS2 = krS.substring(0,5);
		String kpsiS=Double.toString(kpsi);
		String kpsiS2=kpsiS.substring(0,5);
		q11F= new TextField(krS2);
		q22F= new TextField(kpsiS2);
		q11F.setBounds(330,10,50,20);
		//add(q11F);
		q22F.setBounds(330,30,50,20);
		//add(q22F);
		
		for(int i=0;i<20;i++){
			d1[i]=0.3;
		}
		for(int i=20;i<40;i++){
			d1[i]=-0.3;
		}
		for(int i=40;i<60;i++){
			d1[i]=0.3;
		}
		for(int i=60;i<80;i++){
			d1[i]=-0.3;
		}
		for(int i=80;i<100;i++){
			d1[i]=0.3;
		}
		for(int i=100;i<120;i++){
			d1[i]=-0.3;
		}
		for(int i=0;i<20;i++){
			d2[i]=-0.6+1.2*(i/20.0);
		}
		for(int i=20;i<40;i++){
			d2[i]=0.6-1.2*((i-20)/20.0);
		}
		for(int i=40;i<60;i++){
			d2[i]=-0.6+1.2*((i-40)/20.0);;
		}
		for(int i=60;i<80;i++){
			d2[i]=0.6-1.2*((i-60)/20.0);;
		}
		for(int i=80;i<100;i++){
			d2[i]=-0.6+1.2*((i-80)/20.0);;
		}
		for(int i=100;i<120;i++){
			d2[i]=0.6-1.2*((i-100)/20.0);
		}
		for(int i=0;i<120;i++){
			d3[i]=d1[i]+d2[i];
		}
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
				if(trajz<100000){
					trajx[trajz]=(int)(x*skal);
					trajy[trajz]=(int)(y*skal);
				}
				if(trajz<120){
					if(zeitabschnitt==0){
						x1[trajz]=trajx[trajz];
						y1[trajz]=trajy[trajz];
					}
					if(zeitabschnitt==1){
						x2[trajz]=trajx[trajz];
						y2[trajz]=trajy[trajz];
					}
					if(zeitabschnitt==2){
						x3[trajz]=trajx[trajz];
						y3[trajz]=trajy[trajz];
					}
				}
				trajz++;
				grafik_vorbereiten();
				zeit++;
				if(zeit>120){
					zeit=0;
					zeitabschnitt++;
					xkm1 = 1;
					ykm1 = 4;
					psikm1 = 0.0/180.0*Math.PI;
					trajz =0;
					delta = 0;
					trajz=0;
					if(zeitabschnitt>4)zeitabschnitt=0;
				}
				Thread.sleep(200);
		        repaint();
		        if (x>(600/skal)){
					str = xF.getText();
					if(str.length()!=0)	x = Double.parseDouble(xF.getText());
					str = yF.getText();
					if(str.length()!=0)	y = Double.parseDouble(yF.getText());
					str = psiF.getText();
					if(str.length()!=0)	psi = Double.parseDouble(psiF.getText())/180*Math.PI;
					//for (i=0; i<200; i++) produktion[i]=(int)(40+20*Math.sin(0.1*i)-10*Math.random());
					xkm1 = x;
					ykm1 = y;
					psikm1 = psi;
					trajz =0;
					delta = 0;
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
		double s= 0.1;
		if(zeitabschnitt==0){
			delta = d1[zeit];//-(kr*(leitlinie-y)+kpsi * psi);
		}
		if(zeitabschnitt==1){
			delta = d2[zeit];//-(kr*(leitlinie-y)+kpsi * psi);
		}
		if(zeitabschnitt>=2){
			delta = d3[zeit];//-(kr*(leitlinie-y)+kpsi * psi);
		}
		if(delta>(60.0/180.0*Math.PI)){
			delta = 60.0/180.0*Math.PI;
		}
		if(delta<(-60.0/180.0*Math.PI)){
			delta = -60.0/180.0*Math.PI;
		}
		if ((delta < 0.001)&&(delta > -0.001)){
			psi = psikm1;	
			x = xkm1 + s * Math.cos(psi);			
			y = ykm1 - s * Math.sin(psi);
			psikm1 = psi;
			ykm1 = y;
			xkm1 = x;
		}
		else{
			psi = s/radabstand*Math.tan(delta)+psikm1;
			x = (radabstand/Math.tan(delta))*(Math.sin(psi)-Math.sin(psikm1))+xkm1;
			y = (radabstand/Math.tan(delta))*(Math.cos(psi)-Math.cos(psikm1))+ykm1;
			xkm1 = x;
			ykm1 = y;
			psikm1 = psi;	
		}
		
		//delta = -(2*(leitlinie-y)+0.5 * psi);
	}
	
	public void zeichneZyklus()
	{
		
		double reifenbreite = 0.1;
		double reifenhoehe = 0.3;
		double reifenentfernung = 0.6;
		
		g.setColor(Color.black);
		
		fzgex[0]=(int) (x*skal+(radabstand +uev)*Math.cos(psi)*skal+bhalbe * Math.sin(psi)*skal);
		fzgey[0]=(int) (y*skal-(radabstand +uev)*Math.sin(psi)*skal+bhalbe * Math.cos(psi)*skal);
		fzgex[1]=(int) (x*skal+(radabstand +uev)*Math.cos(psi)*skal-bhalbe * Math.sin(psi)*skal);
		fzgey[1]=(int) (y*skal-(radabstand +uev)*Math.sin(psi)*skal-bhalbe * Math.cos(psi)*skal);
		fzgex[2]=(int) (x*skal+(-ueh)*Math.cos(psi)*skal-bhalbe * Math.sin(psi)*skal);
		fzgey[2]=(int) (y*skal-(-ueh)*Math.sin(psi)*skal-bhalbe * Math.cos(psi)*skal);
		fzgex[3]=(int) (x*skal+(-ueh)*Math.cos(psi)*skal+bhalbe * Math.sin(psi)*skal);
		fzgey[3]=(int) (y*skal-(-ueh)*Math.sin(psi)*skal+bhalbe * Math.cos(psi)*skal);
		g.drawPolygon(fzgex,fzgey,4);
		fzgex[0]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+(reifenhoehe)*Math.cos(psi)*skal+reifenbreite * Math.sin(psi)*skal);
		fzgey[0]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-(reifenhoehe)*Math.sin(psi)*skal+reifenbreite * Math.cos(psi)*skal);
		fzgex[1]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+(reifenhoehe)*Math.cos(psi)*skal-reifenbreite * Math.sin(psi)*skal);
		fzgey[1]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-(reifenhoehe)*Math.sin(psi)*skal-reifenbreite * Math.cos(psi)*skal);
		fzgex[2]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+(-reifenhoehe)*Math.cos(psi)*skal-reifenbreite * Math.sin(psi)*skal);
		fzgey[2]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-(-reifenhoehe)*Math.sin(psi)*skal-reifenbreite * Math.cos(psi)*skal);
		fzgex[3]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+(-reifenhoehe)*Math.cos(psi)*skal+reifenbreite * Math.sin(psi)*skal);
		fzgey[3]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-(-reifenhoehe)*Math.sin(psi)*skal+reifenbreite * Math.cos(psi)*skal);
		g.fillPolygon(fzgex,fzgey,4);
		fzgex[0]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+(reifenhoehe)*Math.cos(psi)*skal+reifenbreite * Math.sin(psi)*skal);
		fzgey[0]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-(reifenhoehe)*Math.sin(psi)*skal+reifenbreite * Math.cos(psi)*skal);
		fzgex[1]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+(reifenhoehe)*Math.cos(psi)*skal-reifenbreite * Math.sin(psi)*skal);
		fzgey[1]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-(reifenhoehe)*Math.sin(psi)*skal-reifenbreite * Math.cos(psi)*skal);
		fzgex[2]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+(-reifenhoehe)*Math.cos(psi)*skal-reifenbreite * Math.sin(psi)*skal);
		fzgey[2]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-(-reifenhoehe)*Math.sin(psi)*skal-reifenbreite * Math.cos(psi)*skal);
		fzgex[3]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+(-reifenhoehe)*Math.cos(psi)*skal+reifenbreite * Math.sin(psi)*skal);
		fzgey[3]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-(-reifenhoehe)*Math.sin(psi)*skal+reifenbreite * Math.cos(psi)*skal);
		g.fillPolygon(fzgex,fzgey,4);
		fzgex[0]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(reifenhoehe)*Math.cos(psi+delta)*skal+reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[0]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(reifenhoehe)*Math.sin(psi+delta)*skal+reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[1]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(reifenhoehe)*Math.cos(psi+delta)*skal-reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[1]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(reifenhoehe)*Math.sin(psi+delta)*skal-reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[2]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(-reifenhoehe)*Math.cos(psi+delta)*skal-reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[2]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(-reifenhoehe)*Math.sin(psi+delta)*skal-reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[3]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(-reifenhoehe)*Math.cos(psi+delta)*skal+reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[3]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(-reifenhoehe)*Math.sin(psi+delta)*skal+reifenbreite * Math.cos(psi+delta)*skal);
		g.fillPolygon(fzgex,fzgey,4);
		fzgex[0]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(reifenhoehe)*Math.cos(psi+delta)*skal+reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[0]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(reifenhoehe)*Math.sin(psi+delta)*skal+reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[1]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(reifenhoehe)*Math.cos(psi+delta)*skal-reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[1]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(reifenhoehe)*Math.sin(psi+delta)*skal-reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[2]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(-reifenhoehe)*Math.cos(psi+delta)*skal-reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[2]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(-reifenhoehe)*Math.sin(psi+delta)*skal-reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[3]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(-reifenhoehe)*Math.cos(psi+delta)*skal+reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[3]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(-reifenhoehe)*Math.sin(psi+delta)*skal+reifenbreite * Math.cos(psi+delta)*skal);
		g.fillPolygon(fzgex,fzgey,4);
		g.setColor(Color.blue);
		//g.drawLine(0,(int)(leitlinie*skal),600,(int)(leitlinie*skal));
		//g.setColor(Color.red);
		//g.drawLine((int)(x*skal),(int)(leitlinie*skal),(int)(x*skal),(int)(y*skal));
		//g.fillArc((int)(x*skal)-30,(int)(y*skal)-30,60,60,0,(int)(180*psi/Math.PI));
		/*g.setColor(Color.green);
		if(trajz>1){
			for(int i=0;i<trajz-1;i++){
				g.drawLine(trajx[i],trajy[i],trajx[i+1],trajy[i+1]);
			}
		}*/
		
		if(zeitabschnitt==0){
			g.setColor(Color.blue);
			for(int i=0;i<trajz-1;i++){
				g.drawLine(x1[i],y1[i],x1[i+1],y1[i+1]);
			}
		}
		if(zeitabschnitt==1){
			g.setColor(Color.blue);
			for(int i=0;i<119;i++){
				g.drawLine(x1[i],y1[i],x1[i+1],y1[i+1]);
			}
			g.setColor(Color.green);
			for(int i=0;i<trajz-1;i++){
				g.drawLine(x2[i],y2[i],x2[i+1],y2[i+1]);
			}
		}
		if(zeitabschnitt>=2){
			g.setColor(Color.blue);
			for(int i=0;i<119;i++){
				g.drawLine(x1[i],y1[i],x1[i+1],y1[i+1]);
			}
			g.setColor(Color.green);
			for(int i=0;i<119;i++){
				g.drawLine(x2[i],y2[i],x2[i+1],y2[i+1]);
			}
			g.setColor(Color.orange);
			for(int i=0;i<119;i++){
				g.drawLine((x1[i]+x2[i]-(int)(1*skal)),(y1[i]+y2[i]-(int)(4*skal)),(x1[i+1]+x2[i+1]-(int)(1*skal)),(y1[i+1]+y2[i+1]-(int)(4*skal)));
			}
			g.setColor(Color.red);
			for(int i=0;i<trajz-1;i++){
				g.drawLine(x3[i],y3[i],x3[i+1],y3[i+1]);
			}
		}

		g.setColor(Color.black);
		String aPsi = new String( Double.toString(psi) );
		String ax = new String( Double.toString(x) );
		String ay = new String( Double.toString(y) );

		if ( aPsi.length() > 7 ) {
			aPsi = aPsi.substring(0,6);
		}
		if ( ax.length() > 7 ) {
			ax = ax.substring(0,6);
		}
		if ( ay.length() > 7 ) {
			ay = ay.substring(0,6);
		}
		if(zeitabschnitt>=0){
			g.setColor(Color.blue);
			for (int i=0; i < 120; i++)
			{
				g.drawLine(20+i, (int) (50+40*d1[i]),21+i,(int)(50+40*d1[i+1]));
			}
			
		}
		if(zeitabschnitt==0) {
			g.setColor(Color.red);
			g.drawOval(15+zeit,(int) (45+40*d1[zeit]),10,10);
		}
		if(zeitabschnitt>=1){
			g.setColor(Color.green);
			for (int i=0; i < 120; i++)
			{
				g.drawLine(20+i, (int) (50+40*d2[i]),21+i,(int)(50+40*d2[i+1]));
			}
		}
		if(zeitabschnitt==1) {
			g.setColor(Color.red);
			g.drawOval(15+zeit,(int) (45+40*d2[zeit]),10,10);
		}
		if(zeitabschnitt>=2){
			g.setColor(Color.red);
			for (int i=0; i < 120; i++)
			{
				g.drawLine(20+i, (int) (50+40*d3[i]),21+i,(int)(50+40*d3[i+1]));
			}
		}
		if(zeitabschnitt==2) {
			g.setColor(Color.red);
			g.drawOval(15+zeit,(int) (45+40*d3[zeit]),10,10);
		}
		g.setColor(Color.black);
		g.drawLine(20,50,150,50);
		px[0]=20;
		py[0]=10;
		px[1]=17;
		py[1]=20;
		px[2]=23;
		py[2]=20;
		g.fillPolygon(px,py,3);
		g.drawLine(20,80,20,20);
		px[0]=160;
		py[0]=50;
		px[1]=150;
		py[1]=47;
		px[2]=150;
		py[2]=53;
		g.fillPolygon(px,py,3);
		g.drawString("Lenkwinkel",30,20);
		g.drawString("t",145,65);
		/*g.drawString("x Startposition",45,25);
		g.drawString("y Startposition",45,45);
		g.drawString("psi Startposition",45,65);
		g.drawString("x aktuell:   "+ax,200,25);
		g.drawString("y aktuell:   "+ay,200,45);
		g.drawString("Psi aktuell: "+aPsi,200,65);
		g.drawString("Rückführung Abstandsdifferenz",385,25);
		g.drawString("Rückführung Winkeldifferenz",385,45);
		g.drawString("Harald Schellinger",450,330);*/
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == setzeButton)){
			str = xF.getText();
			if(str.length()!=0)	x = Double.parseDouble(xF.getText());
			str = yF.getText();
			if(str.length()!=0)	y = Double.parseDouble(yF.getText());
			str = psiF.getText();
			if(str.length()!=0)	psi = Double.parseDouble(psiF.getText())/180*Math.PI;
			//for (i=0; i<200; i++) produktion[i]=(int)(40+20*Math.sin(0.1*i)-10*Math.random());
			str = q11F.getText();
			if(str.length()!=0)	kr = Double.parseDouble(q11F.getText());
			str = q22F.getText();
			if(str.length()!=0)	kpsi = Double.parseDouble(q22F.getText());
			xkm1 = x;
			ykm1 = y;
			psikm1 = psi;
			trajz =0;
			delta = 0;
			return true;
		}

		else return false;
	}
}