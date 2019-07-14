import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.TextField;

import javax.swing.JApplet;
import javax.swing.JSlider;

public class Licht_hand extends JApplet implements Runnable{
	
	JSlider regler;
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	int px[] = new int[3];
	int py[] = new int[3];
	float m_ist;
	float m_soll=(float)(180.0/255.0);
	int m_stell;
	float m_sonne=(float)(60.0/255.0);
	int m_wetter;
	int wetter=0;
	TextField proportional = new TextField ("0.6");
	//JPanel jPanel1 = new JPanel();
	//FlowLayout flowLayout1 = new FlowLayout();
	
	public void init()
	{
		setSize(new Dimension(600,500));
		getContentPane().setLayout(null); // Freie Positionierung
		//setLayout(null);
		proportional.setBounds(100,300,40,20);
		//getContentPane().add(proportional);
		regler = new JSlider();
		regler.setBounds(170,300,150,20);
		regler.setMaximum(195);
		regler.setMinimum(0);
		regler.setVisible(true);
		regler.setValue(30);
		//jPanel1.setVisible(true);
		getContentPane().add(regler);
		//jPanel1.setLayout(flowLayout1);
		//jPanel1.add(regler);
		ausgabe=null;
		g=null;
		//setSize(new Dimension(800,600));

		
		
		
		/*
		resize(800, 600);
		//sound = getAudioClip(getDocumentBase(),"spuelen.wav");
		int sx=size().width;
		int sy=size().height;
		if(sx!=sizex || sy!=sizey){
			sizex=sx;
			sizey=sy;
			ausgabe=createImage(sx,sy);
			g=ausgabe.getGraphics();
		}*/
	}
	public void grafik_vorbereiten()
	{
		//g.clearRect(0,0,sizex,sizey);
		zeichneZyklus();
	}

/*	public void paint (Graphics g1)
	{
		//grafik_vorbereiten();
		//g1.drawImage(ausgabe, 0, 0, this);
	}

	public void repaint (Graphics g1)
	{
		//grafik_vorbereiten();
		//g1.drawImage(ausgabe, 0, 0, this);
	}*/
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
		double zufall;		

	     while (true){
			try
			{
				grafik_vorbereiten();
				Thread.sleep(2000);
				wetter++;
				zufall=Math.random();
				//System.out.println(zufall);
				if(wetter>10){
					if(zufall<0.3){
						m_sonne=(float)(140.0/255.0);
						m_wetter=1;
					}
					if((zufall>0.3)&&(zufall<0.6)){
						m_sonne=(float)(100.0/255.0);
						m_wetter=2;
					}
					if(zufall>0.6){
						m_sonne=(float)(60.0/255.0);
						m_wetter=3;
					}
					wetter=0;
				}
				//repaint();
				
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
		g=getContentPane().getGraphics();
		getContentPane().setBackground(Color.white);
		g.setColor(Color.green);
		g.fillRect(0,450,600,50);
		g.setColor(Color.red);
		px[0]=350;
		py[0]=250;
		px[1]=450;
		py[1]=150;
		px[2]=550;
		py[2]=250;
		g.fillPolygon(px,py,3);
		g.setColor(Color.BLACK);
		g.fillRect(350,250,200,200);
		m_stell=m_stell+(int)(255.0*Float.parseFloat(proportional.getText())*(m_soll-m_ist));
		if(m_stell<0)m_stell=0;
		if(m_stell>195)m_stell=195;
		//regler.setValue(m_stell);
		m_ist =m_sonne + (float) (regler.getValue()/195.0);
		if(m_ist>1) m_ist=1;
		g.setColor(new Color(m_ist,m_ist,m_ist));
		g.fillRect(360,260,180,180);
		g.setColor(Color.black);
		g.drawLine(445,270,445,250);
		g.setColor(Color.yellow);
		g.fillOval(430,270,30,30);
		g.setColor(Color.blue);
		g.fillRect(460,300,50,50);
		g.fillRect(380,300,50,50);
		g.setColor(Color.black);
		g.drawLine(550,310,580,310);
		g.drawLine(580,310,580,480);
		g.drawLine(80,480,580,480);
		g.drawLine(80,480,80,315);//Istwert
		px[0]=75;
		py[0]=325;
		px[1]=80;
		py[1]=315;
		px[2]=85;
		py[2]=325;
		g.fillPolygon(px,py,3);
		g.setColor(new Color(m_ist,m_ist,m_ist));
		g.fillRect(90,335,40,20);
		g.setColor(Color.black);
		g.drawRect(90,335,40,20);
		g.drawString("Ist",140,350);
		g.drawLine(85,330,95,330);
		g.drawOval(75,305,10,10);//Vergleicher
		g.drawLine(10,310,75,310);//Sollwert
		px[0]=65;
		py[0]=305;
		px[1]=75;
		py[1]=310;
		px[2]=65;
		py[2]=315;
		g.fillPolygon(px,py,3);
		g.setColor(new Color(m_soll,m_soll,m_soll));
		g.fillRect(10,280,40,20);
		g.setColor(Color.black);
		g.drawRect(10,280,40,20);
		g.drawString("Soll",10,270);
		g.drawLine(60, 295, 70, 295);
		g.drawLine(65, 290, 65, 300);
		// Regler
		/*g.drawLine(85, 310, 100, 310);
		px[0]=90;
		py[0]=305;
		px[1]=100;
		py[1]=310;
		px[2]=90;
		py[2]=315;
		g.fillPolygon(px,py,3);
		g.drawLine(140, 310, 170, 310);
		px[0]=160;
		py[0]=305;
		px[1]=170;
		py[1]=310;
		px[2]=160;
		py[2]=315;
		g.fillPolygon(px,py,3);*/
		//Nach Steller
		g.drawLine(320, 310, 350, 310);
		px[0]=340;
		py[0]=305;
		px[1]=350;
		py[1]=310;
		px[2]=340;
		py[2]=315;
		g.fillPolygon(px,py,3);
		g.setColor(Color.yellow);
		g.fillOval(425,20, 50, 50);
		g.setColor(Color.white);
		switch(m_wetter){
		case 1:	g.setColor(Color.white);
				break;
		case 2: g.setColor(Color.gray);
				break;
		case 3:	g.setColor(Color.darkGray);
				break;
		}
		g.fillOval(425,80,50,50);
		g.fillOval(400,80,50,50);
		g.fillOval(450,80,50,50);
		
	}
}
