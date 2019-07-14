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
public class pt2_super extends Applet implements Runnable{
	private Thread	 m_thread = null;
	int x1[]=new int[200];
	int x2[]=new int[200];
	int x3[]=new int[200];
	int periode=0;
	double p=0.5;
	double pd=200;
	int zeitabschnitt =0;
	int zeit,zeitabstand;
	Image ausgabe;
	Checkbox automatik_c, manuell_c;
	TextField omega_f;
	Choice darst_c;
	String str;
	int federabgriff;
	int federanfang;
	int kasten;
	int schwerpunkt[];
	int schwerpunkt1[];
	int schwerpunkt2[];
	int schwerpunkt3[];
	int zustand;
	Graphics g;
	int sizex,sizey;
	int sinus[];
	int samples;
	int px[] = new int[3];
	int py[]=new int [3];
	double w0,dw0;
	double omega0,daempfomega;
	double w[];
	int logw[];
	double A[];
	int logA[];
	double phi[];
	double x1kp1, x1k;
	double x2kp1, x2k;
	double omega;
	double maximum[]=new double[7];
	double maxOmega[]=new double[7];
	int abschnitt[] = new int[7];
	Math m;
	Choice cTestfunk;
	TextField FPd = new TextField("200");
	Button uebernehmeButton;
	public void init()
	{
		setLayout(null);
		cTestfunk = new Choice();
		cTestfunk.setBounds(20,20,100,20);
		cTestfunk.addItem("Rechteck");
		cTestfunk.addItem("Sinus");
		cTestfunk.addItem("Dreieck");
		//add(cTestfunk);
		FPd.setBounds(130,20,30,20);
		//add(FPd);
		uebernehmeButton = new Button("Übernehmen");
		uebernehmeButton.setBounds(180,20,100,20);
		//add(uebernehmeButton);
		
		//automatik_c = new Checkbox("Automatik",true);
		//manuell_c = new Checkbox("Manuell",false);
		//automatik_c.setBounds( 100, 40, 100, 20 );
		//manuell_c.setBounds(100,60,100,20);
		//add(automatik_c);
		//add(manuell_c);
		//darst_c=new Choice();
		//darst_c.add("Bode-Diagramm");
		//darst_c.add("Nyquist-Diagramm");
		//darst_c.setBounds(150,330,130,20);
		//add(darst_c);
		//omega_f=new TextField("0.1");
		//omega_f.setBounds(100,80,30,20);
		//add(omega_f);
		int i;
		double w0q,w2w;
		double nennerPhi;
		for (i=0;i<7;i++)
		{
			abschnitt[i]=0;
			maximum [i]=0.000000001;
			maxOmega[i]=0.000000001;
		}
		zeit = 0;
		x1k = 0;
		x2k =0;
		w0=0.32;
		omega0=0.1;
		dw0=0.07;
		daempfomega = 0.2;
		samples =	200;
		zeitabstand = 500;
		sinus = new int [samples];
		schwerpunkt = new int [samples];
		schwerpunkt1 = new int [samples];
		schwerpunkt2 = new int [samples];
		schwerpunkt3 = new int [samples];
		for(i=0;i<samples;i++)
		{
			schwerpunkt[i]=150;
			schwerpunkt1[i]=150;
			schwerpunkt2[i]=150;
			schwerpunkt3[i]=150;
		}
		omega = 2*Math.PI/200;
		
		A=new double[301];
		w=new double[301];
		phi = new double[301];
		logw=new int[301];
		logA =new int [301];
		w0q=w0*w0;
		w2w=w[i]*w[i];
		for (i=0;i<301;i++)
		{
			if (i==0){
				w[i]=1000;
			}
			else
			{
				w[i]=2*Math.PI/i;
			}
			//logw[i]=(int)(100*(1/Math.log(10))Math.log(w[i]));
			w2w = w[i]*w[i];
			nennerPhi = w0q-w2w;
			if (nennerPhi>0)
			{
				phi[i] = Math.atan((-2*dw0*w[i])/nennerPhi);
			}
			else
			{
				phi[i] = Math.atan((-2*dw0*w[i])/nennerPhi)-Math.PI;
			}
			A[i]=((w0q/((w0q-w2w)*(w0q-w2w)+2*dw0*dw0*w2w))*Math.sqrt(((w0q-w2w)*(w0q-w2w)+2*dw0*dw0*w2w)));
			logA[i]=(int)(-20*Math.log(A[i]));
		}
		
		ausgabe=null;
		g=null;
		resize(400,220);
		
		
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
		zeichneSinus();
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
				berechneSinus();
		     	grafik_vorbereiten();
		        repaint();
				Thread.sleep(50);
				zeit ++;
				if(zeit>200){
					zeit=0;
					zeitabschnitt++;
					if(zeitabschnitt>4){
						zeitabschnitt=0;
					}
				}
				periode ++;
				
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
	public void berechneSinus()
	{
		int i;
		if(zeitabschnitt==0){
			if((periode%(int)pd)<(int)(pd/2)){
				x1[199]=20;
			}
			else{
				x1[199]=-20;
			}
		}
		if(zeitabschnitt==1){
			if((periode%(int)pd)<(int)(pd/2)){
				x2[199]=-20+80*(periode%(int)pd)/(int)pd;
			}
			else{
				x2[199]=60-80*(periode%(int)pd)/(int)pd;
			}
		}
		if(zeitabschnitt==2){
			if((periode%(int)pd)<(int)(pd/2)){
				x3[199]=20+-20+80*(periode%(int)pd)/(int)pd;
			}
			else{
				x3[199]=-20+60-80*(periode%(int)pd)/(int)pd;
			}
		}
		for( i=0;i<199;i++)
		{
			if(zeitabschnitt==0) x1[i]=x1[i+1];
			if(zeitabschnitt==1) x2[i]=x2[i+1];
			if(zeitabschnitt==2) x3[i]=x3[i+1];
		}
		for (i=0; i<200; i++)
		{
			if(zeitabschnitt==0) sinus[i]=x1[i];
			if(zeitabschnitt==1) sinus[i]=x2[i];
			if(zeitabschnitt==2) sinus[i]=x3[i];
		}
		federabgriff = sinus[150];
		federanfang = federabgriff + 30;
		kasten = federabgriff + 150;
		x1kp1 = x1k + x2k;
		//x2kp1 = x2k -0.4 * x2k  + 0.3*(federabgriff- x1k);
		x2kp1 = -omega0*x1k +(1-daempfomega)*x2k + omega0* federabgriff;
		x1k = x1kp1;
		x2k = x2kp1;
		for (i=0;i<150;i++)
		{
			schwerpunkt[i]=schwerpunkt[i+1];
		}
		schwerpunkt[150] = (int) x1k+150; // kasten;
		kasten =(int) x1k+150;

		
	}
	public void zeichneSinus()
	{
		int i;
		int abstandRandx = 50;
		int abstandRandy = 100;
		int abstand;
		int abstandh;
		int federhoehe = 150;
		int federbreite = 10;
		int wgefunden = 0;
		g.setColor(Color.blue);
		if(zeitabschnitt>=0){
			g.setColor(Color.blue);
			for (i=0; i < samples-1; i++)
			{
				g.drawLine(abstandRandx +x1[i],i,abstandRandx +x1[i+1],1+i);
			}
		}
		if(zeitabschnitt>=1){
			g.setColor(Color.green);
			for (i=0; i < samples-1; i++)
			{
				g.drawLine(abstandRandx +x2[i],i,abstandRandx +x2[i+1],1+i);
			}
		}
		if(zeitabschnitt>=2){
			g.setColor(Color.red);
			for (i=0; i < samples-1; i++)
			{
				g.drawLine(abstandRandx +x3[i],i,abstandRandx +x3[i+1],1+i);
			}
		}
		
		for (i=0; i <150; i++)
		{
			if(zeitabschnitt==0) schwerpunkt1[i]=schwerpunkt[i];
			if(zeitabschnitt==1) schwerpunkt2[i]=schwerpunkt[i];
			if(zeitabschnitt==2) schwerpunkt3[i]=schwerpunkt[i];
			if(zeitabschnitt>=0){
				g.setColor(Color.blue);
				g.drawLine(abstandRandx+schwerpunkt1[i]+75,i,abstandRandx+schwerpunkt1[i+1]+75,i+1);
			}
			if(zeitabschnitt>=1){
				g.setColor(Color.green);
				g.drawLine(abstandRandx+schwerpunkt2[i]+75,i,abstandRandx+schwerpunkt2[i+1]+75,i+1);
			}
			if(zeitabschnitt>=2){
				g.setColor(Color.red);
				g.drawLine(abstandRandx+schwerpunkt3[i]+75,i,abstandRandx+schwerpunkt3[i+1]+75,i+1);
			}
			
			if(zeitabschnitt>=2){
				g.setColor(Color.orange);
				g.drawLine(abstandRandx-150+schwerpunkt1[i]+schwerpunkt2[i]+75,i,abstandRandx-150+schwerpunkt1[i+1]+schwerpunkt2[i+1]+75,i+1);
			}
		}
		g.setColor(Color.blue);
		for (i=0; i < 150; i++)
		{
		//	g.drawLine(20 +2* i,2*sinus[i]+300,20+1+2*i ,2*sinus[i+1]+300);
		}
		g.setColor(Color.red);
		for (i=0; i <150; i++)
		{
		//	g.drawLine(20+2*i,2*schwerpunkt[i],21+2*i,2*schwerpunkt[i+1]);
		}
		g.setColor(Color.black);
		g.drawString("Superposition am Beispiel eines Feder-Masse-Schwingers",20,20);
		//g.drawString("Automatik",110,30);
		//g.drawString("Manuell",110,40);
		//g.drawString("Omega",100,55);
		g.drawLine(abstandRandx+federabgriff,federhoehe,abstandRandx+federanfang, federhoehe);
		g.setColor(Color.red);
		g.drawOval( abstandRandx-5+federabgriff, federhoehe-5,10,10);
		g.setColor(Color.black);
		g.drawRect(abstandRandx+kasten,abstandRandy+20,150,60);
		abstand= (int) (kasten-federanfang)/4;
		abstandh = (int)abstand/2;
		g.drawLine( abstandRandx+federanfang,federhoehe, abstandRandx+federanfang,federhoehe+federbreite);
		g.drawLine( abstandRandx+federanfang,federhoehe+federbreite, abstandRandx+federanfang+abstandh,federhoehe-federbreite);
		g.drawLine( abstandRandx+federanfang+abstandh,federhoehe-federbreite, abstandRandx+federanfang+abstand,federhoehe+federbreite);
		g.drawLine( abstandRandx+federanfang+abstand,federhoehe+federbreite, abstandRandx+federanfang+abstand+abstandh,federhoehe-federbreite);
		g.drawLine( abstandRandx+federanfang+abstand+abstandh,federhoehe-federbreite, abstandRandx+federanfang+2*abstand,federhoehe+federbreite);
		g.drawLine( abstandRandx+federanfang+2*abstand,federhoehe+federbreite, abstandRandx+federanfang+2*abstand+abstandh,federhoehe-federbreite);
		g.drawLine( abstandRandx+federanfang+2*abstand+abstandh,federhoehe-federbreite, abstandRandx+federanfang+3*abstand,federhoehe+federbreite);
		g.drawLine( abstandRandx+federanfang+3*abstand,federhoehe+federbreite, abstandRandx+federanfang+3*abstand+abstandh,federhoehe-federbreite);
		g.drawLine( abstandRandx+federanfang+3*abstand+abstandh,federhoehe-federbreite, abstandRandx+federanfang+4*abstand,federhoehe);
		
		
		/*for (i=0;i<300;i+=50)
		{
			g.drawString(Integer.toString(logw[i]),250+logw[i],400);
		}*/
		for(i=0;i<5;i++)
		{
			//g.drawLine(350+(int)(50*Math.log(maxOmega[i])),400+(int)(-10*Math.log(maximum[i]/10)),350+(int)(50*Math.log(maxOmega[i+1])),400+(int)(-10*Math.log(maximum[i+1]/10)));
			
		}
		//for (i=0;i<6;i++) g.drawString(" A:"+Math.log(maximum[i]/10)+"Omega:"+(Math.log(maxOmega[i])),100,(i+1)*15);
		
		g.drawString("Harald Schellinger",260,200);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == uebernehmeButton)){
			str = FPd.getText();
			if(str.length()!=0)	pd = (Double.valueOf(FPd.getText())).doubleValue();
			
			return true;
		}
		else return false;
	}
}
