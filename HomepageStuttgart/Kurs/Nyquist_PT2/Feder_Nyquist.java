import java.applet.Applet;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
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
public class Feder_Nyquist extends Applet implements Runnable{
	private Thread	 m_thread = null;
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
	
	public void init()
	{
		setLayout(null);
		automatik_c = new Checkbox("Automatik",true);
		manuell_c = new Checkbox("Manuell",false);
		automatik_c.setBounds( 100, 40, 100, 20 );
		manuell_c.setBounds(100,60,100,20);
		add(automatik_c);
		add(manuell_c);
		darst_c=new Choice();
		darst_c.add("Nyquist-Diagramm");
		darst_c.add("Bode-Diagramm");
		darst_c.setBounds(150,330,130,20);
		add(darst_c);
		omega_f=new TextField("0.1");
		omega_f.setBounds(100,80,30,20);
		add(omega_f);
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
		for(i=0;i<samples;i++)
		{
			schwerpunkt[i]=150;
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
			logw[i]=(int)(100*(1/Math.log(10))*Math.log10(w[i]));
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
			logA[i]=(int)(-20*Math.log10(A[i]));
		}
		
		ausgabe=null;
		g=null;
		resize(400, 750);
		
		
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
				berechneSinus();
		     	grafik_vorbereiten();
		        repaint();
				Thread.sleep(100);
				zeit ++;
				if (automatik_c.getState())
				{
					if(zeit >zeitabstand) 
					{
						zustand = 0;
						omega = 2*Math.PI/100;
						//omega = 2*Math.PI/18;
					}
					if((zeit >(zeitabstand+100))&&(zeit < (zustand+2)*zeitabstand)) 
					{
						if(abschnitt[zustand]==0)
						{
							abschnitt[zustand]=1;
							maximum[zustand] = 0;
						}
						if (x1k>maximum[zustand])
						{	
							maximum[zustand]=x1k;
							maxOmega[zustand] = omega;
						}
					}
					if(zeit >2*zeitabstand) 
					{
						zustand = 1;
						omega = 2*Math.PI/30;
						//omega = 2*Math.PI/19;
					}
					if((zeit >(2*zeitabstand+100))&&(zeit < (zustand+2)*zeitabstand)) 
					{
						if(abschnitt[zustand]==0)
						{
							abschnitt[zustand]=1;
							maximum[zustand] = 0;
						}
						if (x1k>maximum[zustand])
						{	
							maximum[zustand]=x1k;
							maxOmega[zustand] = omega;
						}
					}
					if(zeit >3*zeitabstand) 
					{
						zustand = 2;
						omega = 2*Math.PI/20;
						//omega = 2*Math.PI/20;
					}
					if((zeit >(3*zeitabstand+100))&&(zeit < (zustand+2)*zeitabstand)) 
					{
						if(abschnitt[zustand]==0)
						{
							abschnitt[zustand]=1;
							maximum[zustand] = 0;
						}
						if (x1k>maximum[zustand])
						{	
							maximum[zustand]=x1k;
							maxOmega[zustand] = omega;
						}
					}
					if(zeit >4*zeitabstand) 
					{
						zustand = 3;
						omega = 2*Math.PI/15;
					}
					if(zeit >(4*zeitabstand+100)) 
					{
						if(abschnitt[zustand]==0)
						{
							abschnitt[zustand]=1;
							maximum[zustand] = 0;
						}
						if ((x1k>maximum[zustand])&&(zeit < (zustand+2)*zeitabstand))
						{	
							maximum[zustand]=x1k;
							maxOmega[zustand] = omega;
						}
					}
					if(zeit >5*zeitabstand) 
					{
						zustand = 4;
						omega = 2*Math.PI/10;
						//omega = 2*Math.PI/22;
					}
					if(zeit >(5*zeitabstand+100)) 
					{
						if(abschnitt[zustand]==0)
						{
							abschnitt[zustand]=1;
							maximum[zustand] = 0;
						}
						if ((x1k>maximum[zustand])&&(zeit < (zustand+2)*zeitabstand))
						{	
							maximum[zustand]=x1k;
							maxOmega[zustand] = omega;
						}
					}
					if(zeit >6*zeitabstand) 
					{
						zustand = 5;
						omega = 2*Math.PI/5;
					}
					if(zeit >(6*zeitabstand+100)) 
					{
						if(abschnitt[zustand]==0)
						{
							abschnitt[zustand]=1;
							maximum[zustand] = 0;
						}
						if ((x1k>maximum[zustand])&&(zeit < (zustand+2)*zeitabstand))
						{	
							maximum[zustand]=x1k;
							maxOmega[zustand] = omega;
						}
					}
					if(zeit >7*zeitabstand) 
					{  
						zeit=0;
					}
				}
			
			
				if (manuell_c.getState())
				{
					str = omega_f.getText();
					if(str.length()!=0)	omega = (Double.valueOf(omega_f.getText())).doubleValue();
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
	public void berechneSinus()
	{
		int i;
		for (i=0; i<samples; i++)
		{
			sinus[i]=(int) Math.round(10*Math.sin(omega*zeit+omega*i));
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
		for (i=0; i < samples-1; i++)
		{
			g.drawLine(abstandRandx +sinus[i],i,abstandRandx +sinus[i+1],1+i);
		}
		g.setColor(Color.red);
		for (i=0; i <150; i++)
		{
			g.drawLine(abstandRandx+schwerpunkt[i]+75,i,abstandRandx+schwerpunkt[i+1]+75,i+1);
		}
		g.setColor(Color.blue);
		for (i=0; i < 150; i++)
		{
			g.drawLine(20 +2* i,2*sinus[i]+300,20+1+2*i ,2*sinus[i+1]+300);
		}
		g.setColor(Color.red);
		for (i=0; i <150; i++)
		{
			g.drawLine(20+2*i,2*schwerpunkt[i],21+2*i,2*schwerpunkt[i+1]);
		}
		g.setColor(Color.black);
		g.drawString("Feder-Masse-Schwinger",100,20);
		//g.drawString("Automatik",110,30);
		//g.drawString("Manuell",110,40);
		g.drawString("Omega",100,55);
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
		if(darst_c.getSelectedIndex()==1)
		{
			g.setColor(Color.gray);
			g.drawLine(250+logw[1],400,250+logw[300],400);
			g.drawLine(250+logw[1],420,250+logw[300],420);
			g.drawLine(250+logw[1],440,250+logw[300],440);
			g.drawLine(250+logw[1],500,250+logw[300],500);
			g.drawLine(250+logw[1],500+(int)(57.3*Math.PI/2),250+logw[300],500+(int)(57.3*Math.PI/2));
			g.drawLine(250+logw[1],500+(int)(57.3*Math.PI),250+logw[300],500+(int)(57.3*Math.PI));
			g.drawString("0 dB",250+logw[300],400);
			g.drawString("20 dB",250+logw[300],420);
			g.drawString("40 dB",250+logw[300],440);
			g.drawString("0°",250+logw[300],500);
			g.drawString("90°",250+logw[300],500+(int)(57.3*Math.PI/2));
			g.drawString("180°",250+logw[300],500+(int)(57.3*Math.PI));
			g.setColor(Color.red);
			g.drawString("Amplituden-",10,400);
			g.drawString("gang",10,410);
			g.drawString("Phasengang",10,500);
			for(i=1;i<299;i++)
			{	
				g.drawLine(250+logw[i],400+logA[i],251+logw[i+1],400+logA[i+1]);
			
				g.drawLine(250+logw[i],500+(int)(-57.3*phi[i]),251+logw[i+1],500+(int)(-57.3*phi[i+1]));
				//g.drawLine(150+(int)(30*Math.log(w[i])),500+(int)(-10*A[i]),151+(int)(30*Math.log(w[i+1])),500+(int)(-10*A[i+1]));
				//g.drawLine(150+(int)(10*(w[i])),500+(int)(-10*A[i]),151+(int)(10*(w[i+1])),500+(int)(-10*A[i+1]));
				//g.drawString("Log:"+(int)(10*Math.log(w[50])),50,50);
				//g.drawString("A" +
				//		"Phi:"+(int)(phi[50]),50,70);
				
			}
			g.setColor(Color.gray);
			for (i=1;i<10;i+=1)
			{
				g.drawLine(250+logw[i],350,250+logw[i],700);
			}
			for (i=10;i<100;i+=10)
			{
				g.drawLine(250+logw[i],350,250+logw[i],700);
			}
			for (i=100;i<299;i+=100)
			{
				g.drawLine(250+logw[i],350,250+logw[i],700);
			}
			g.drawString("Omega",200+logw[1],710);
			for(i=300;i>=0;i--)
			{
				if((wgefunden==0)&&(w[i]>omega))
				{
					wgefunden=1;
					g.setColor(Color.blue);
					g.drawOval(245+logw[i],395+logA[i],10,10);
					g.drawOval(245+logw[i],495+(int)(-57.3*phi[i]),10,10);
				}
			}
		}
		
		/*for (i=0;i<300;i+=50)
		{
			g.drawString(Integer.toString(logw[i]),250+logw[i],400);
		}*/
		
		for(i=0;i<5;i++)
		{
			//g.drawLine(350+(int)(50*Math.log(maxOmega[i])),400+(int)(-10*Math.log(maximum[i]/10)),350+(int)(50*Math.log(maxOmega[i+1])),400+(int)(-10*Math.log(maximum[i+1]/10)));
			
		}
		//for (i=0;i<6;i++) g.drawString(" A:"+Math.log(maximum[i]/10)+"Omega:"+(Math.log(maxOmega[i])),100,(i+1)*15);
		if(darst_c.getSelectedIndex()==0)
		{
			g.setColor(Color.red);
			for(i=1;i<299;i++)
			{
				g.drawLine((int)(200+50*A[i]*Math.cos(phi[i])),(int)(450-50*A[i]*Math.sin(phi[i])),(int)(200+50*A[i+1]*Math.cos(phi[i+1])),(int)(450-50*A[i+1]*Math.sin(phi[i+1])));
			}
			g.setColor(Color.gray);
			g.drawLine(100,450,300,450);
			g.drawString("Realteil",290,465);
			px[0]=300;
		    py[0]=450;	
			px[1]=290;
			py[1]=447;
			px[2]=290;
			py[2]=453;
			g.fillPolygon(px,py,3);
			g.drawLine(200,400,200,620);
			g.drawString("Imaginärteil",130,410);
			px[0]=200;
		    py[0]=400;	
			px[1]=203;
			py[1]=410;
			px[2]=197;
			py[2]=410;
			g.fillPolygon(px,py,3);
			for(i=300;i>=0;i--)
			{
				if((wgefunden==0)&&(w[i]>omega))
				{
					wgefunden=1;
					g.setColor(Color.blue);
					g.drawOval((int)(195+50*A[i]*Math.cos(phi[i])),(int)(445-50*A[i]*Math.sin(phi[i])),10,10);
				}
			}
		}
		g.setColor(Color.black);
		g.drawString("Harald Schellinger",260,730);
	}
}