//******************************************************************************
//mar11.java:	Applet
//
//******************************************************************************
import java.applet.*;
import java.awt.*;

//==============================================================================
//Main Class for applet mar11
//
//==============================================================================
public class mar11 extends Applet implements Runnable
{
	// THREAD SUPPORT:
	//		m_mar11	is the Thread object for the applet
	//--------------------------------------------------------------------------
	private Thread	 m_mar11 = null;

	// ANIMATION SUPPORT:
	//		m_Graphics		used for storing the applet's Graphics context
	//		m_Images[]		the array of Image objects for the animation
	//		m_nCurrImage	the index of the next image to be displayed
	//		m_ImgWidth		width of each image
	//		m_ImgHeight		height of each image
	//		m_fAllLoaded	indicates whether all images have been loaded
	//		NUM_IMAGES 		number of images used in the animation
	//--------------------------------------------------------------------------
	private Graphics m_Graphics;

	Integer zahl;
	Image ausgabe;
	Button berechneButton, automatikButton;
	Graphics g;
	int hx,hy,fx,fy;
	int sx,sy;
	int dachpx[]=new int [3];
	int dachpy[]=new int [3];
	int fpx[]=new int [12];
	int fpy[]=new int [12];
	int p1gx,p1gy;
	int p1x[]=new int[7];
	int p1y[]=new int[7];
	int p2gx,p2gy;
	int p2x[]=new int[7];
	int p2y[]=new int[7];
	int kreisx, kreisy;
	int prodPolyx[]=new int[360];
	int prodPolyy[]=new int[360];
	int absatzPolyx[]=new int[360];
	int absatzPolyy[]=new int[360];
	int zu=0;
	int zustt=0;
	
	int arbeiter, arbeitszeit, produktionszeit, lohn, produktionsmenge; 
	int gewinn, preis, absatz, herstellungskosten, schulden, investitionen;
	TextField f_arbeiter, f_arbeitszeit, f_produktionszeit, f_produktionsmenge, f_skalierung;
	TextField f_gewinn, f_preis, f_absatz, f_herstellungskosten, f_schulden, f_investitionen;
	Math m;
	TextField f_lohn;
	double skalierung;
	int sin_p1x[] = new int [3];
	int sin_p1y[] = new int [3];
	int t=0;
	int mx,my,m2x,m2y;
	// mar11 Class Constructor
	//--------------------------------------------------------------------------
	public mar11()
	{
		// TODO: Add constructor code here
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
	// APPLET INFO SUPPORT:
	//		The getAppletInfo() method returns a string describing the applet's
	// author, copyright date, or miscellaneous information.
 //--------------------------------------------------------------------------
	public String getAppletInfo()
	{
		return "Name: mar11\r\n" +
		       "Author: Harald Schellinger\r\n";
	}


	// The init() method is called by the AWT when an applet is first loaded or
	// reloaded.  Override this method to perform whatever initialization your
	// applet needs, such as initializing data structures, loading images or
	// fonts, creating frame windows, setting the layout manager, or adding UI
	// components.
 //--------------------------------------------------------------------------
	public void init()
	{
     // If you use a ResourceWizard-generated "control creator" class to
     // arrange controls in your applet, you may want to call its
     // CreateControls() method from within this method. Remove the following
     // call to resize() before adding the call to CreateControls();
     // CreateControls() does its own resizing.
     //----------------------------------------------------------------------
 	resize(1000, 800);

		// TODO: Place additional initialization code here
 		// Haus
 		hx=30;
		hy=370;
		// Fabrik
		fx=530;
		fy=100;
		// Kreis
		kreisx=300;
		kreisy=200;
		// Männchen
		mx = 240;
		my = 370;
		m2x = 540;
		m2y = 440;
		// Eingabe
		setLayout(null); // Freie Positionierung
		f_arbeiter = new TextField ("100", 5);
		f_arbeiter.setBounds( fx+240, fy+330, 50, 20 );
		add(f_arbeiter);
		f_arbeitszeit = new TextField ("40", 5);
		f_arbeitszeit.setBounds( 300, 400, 50, 20 );
		add(f_arbeitszeit);
		f_produktionsmenge = new TextField ("35", 5);
		f_produktionsmenge.setBounds(10, 70, 50, 20 );
		f_produktionsmenge.setEditable(false);
		add(f_produktionsmenge);
		f_lohn = new TextField ("2000", 5);
		f_lohn.setBounds( 300, 470, 50, 20 );
		add(f_lohn);
		f_gewinn = new TextField ("100", 5);
		f_gewinn.setBounds( fx+40, fy+360, 50, 20 );
		add(f_gewinn);		
		f_preis = new TextField ("40", 5);
		f_preis.setBounds( fx+40, fy+390, 50, 20 );
		f_preis.setEditable(false);
		add(f_preis);
		f_absatz = new TextField ("350", 5);
		f_absatz.setBounds( 10, 100, 50, 20 );
		f_absatz.setEditable(false);
		add(f_absatz);
		f_skalierung = new TextField ("4", 5);
		f_skalierung.setBounds( 10, 130, 50, 20 );
		add(f_skalierung);
		f_produktionszeit = new TextField ("30", 5);
		f_produktionszeit.setBounds( fx+240, fy+360, 50, 20 );
		add(f_produktionszeit);
		
		f_herstellungskosten = new TextField("30",5);
		f_herstellungskosten.setBounds(fx+40,fy+330,50,20);
		f_herstellungskosten.setEditable(false);
		add (f_herstellungskosten);		
		f_schulden = new TextField("0",5);
		f_schulden.setBounds(hx+40,hy+40,50,20);
		add (f_schulden);
		f_investitionen = new TextField("0",5);
		f_investitionen.setBounds(fx+240,fy+390,50,20);
		add (f_investitionen);
		
		
		zahl = new Integer(10);
		
		berechneButton = new Button ("Berechnen");
		berechneButton.setBounds(10,30,100,30);
		add(berechneButton);
		//automatikButton
		ausgabe=null;
		g=null;
		
		sx=size().width;
		sy=size().height;
		ausgabe=createImage(sx,sy);
		g=ausgabe.getGraphics();
	}	

	// Place additional applet clean up code here.  destroy() is called when
	// when you applet is terminating and being unloaded.
	//-------------------------------------------------------------------------
	public void destroy()
	{
		// TODO: Place applet cleanup code here
	}

 // ANIMATION SUPPORT:
 //		Draws the next image, if all images are currently loaded
 //--------------------------------------------------------------------------
	private void grafik_vorbereiten()
	{
		
		// Löschen
		g.clearRect(0,0,sx,sy);
		
		// Produktion, Absatz
		g.setColor(Color.blue);
		g.fillPolygon (prodPolyx,prodPolyy,360);
		g.setColor(Color.green);
		g.fillPolygon (absatzPolyx,absatzPolyy,360);
		
		// Männchen
		g.setColor(Color.black);
		if (t<300){
			if ((t%16)==0){
				g.drawOval(mx+t,my,6,6);
				g.drawLine(mx+3+t,my+6,mx+t+3,my+20);
				g.drawLine(mx+3+t,my+12,mx+t+3,my+18);
				g.drawLine(mx+3+t,my+12,mx+t+3,my+18);
				g.drawLine(mx+3+t,my+20,mx+t+3,my+29);
				g.drawLine(mx+3+t,my+20,mx+t+3,my+29);
			}
			else if ((t%16)==4){
				g.drawOval(mx+t,my,6,6);
				g.drawLine(mx+3+t,my+6,mx+t+3,my+20);
				g.drawLine(mx+3+t,my+12,mx+t+13,my+18);
				g.drawLine(mx+3+t,my+12,mx+t+3,my+18);
				g.drawLine(mx+3+t,my+20,mx+t+13,my+26);
				g.drawLine(mx+3+t,my+20,mx+t+3,my+29);
			}
			else if ((t%16)==8){
				g.drawOval(mx+t,my,6,6);
				g.drawLine(mx+3+t,my+6,mx+t+3,my+20);
				g.drawLine(mx+3+t,my+12,mx+t+13,my+18);
				g.drawLine(mx+3+t,my+12,mx+t-7,my+18);
				g.drawLine(mx+3+t,my+20,mx+t+13,my+26);
				g.drawLine(mx+3+t,my+20,mx+t-7,my+26);
			}
			else if ((t%16)==12){
				g.drawOval(mx+t,my,6,6);
				g.drawLine(mx+3+t,my+6,mx+t+3,my+20);
				g.drawLine(mx+3+t,my+12,mx+t+3,my+18);
				g.drawLine(mx+3+t,my+12,mx+t-7,my+18);
				g.drawLine(mx+3+t,my+20,mx+t+3,my+29);
				g.drawLine(mx+3+t,my+20,mx+t-7,my+26);
			}
		}
		if (t>300){
			if ((t%16)==0){
				g.drawOval(m2x+300-t,m2y,6,6);
				g.drawLine(m2x+3+300-t,m2y+6,m2x+300-t+3,m2y+20);
				g.drawLine(m2x+3+300-t,m2y+12,m2x+300-t+3,m2y+18);
				g.drawLine(m2x+3+300-t,m2y+12,m2x+300-t+3,m2y+18);
				g.drawLine(m2x+3+300-t,m2y+20,m2x+300-t+3,m2y+29);
				g.drawLine(m2x+3+300-t,m2y+20,m2x+300-t+3,m2y+29);
			}
			else if ((t%16)==4){
				g.drawOval(m2x+300-t,m2y,6,6);
				g.drawLine(m2x+3+300-t,m2y+6,m2x+300-t+3,m2y+20);
				g.drawLine(m2x+3+300-t,m2y+12,m2x+300-t-7,m2y+18);
				g.drawLine(m2x+3+300-t,m2y+12,m2x+300-t+3,m2y+18);
				g.drawLine(m2x+3+300-t,m2y+20,m2x+300-t-7,m2y+26);
				g.drawLine(m2x+3+300-t,m2y+20,m2x+300-t+3,m2y+29);
			}
			else if ((t%16)==8){
				g.drawOval(m2x+300-t,m2y,6,6);
				g.drawLine(m2x+3+300-t,m2y+6,m2x+300-t+3,m2y+20);
				g.drawLine(m2x+3+300-t,m2y+12,m2x+300-t+13,m2y+18);
				g.drawLine(m2x+3+300-t,m2y+12,m2x+300-t-7,m2y+18);
				g.drawLine(m2x+3+300-t,m2y+20,m2x+300-t+13,m2y+26);
				g.drawLine(m2x+3+300-t,m2y+20,m2x+300-t-7,m2y+26);
			}
			else if ((t%16)==12){

				g.drawOval(m2x+300-t,m2y,6,6);
				g.drawLine(m2x+3+300-t,m2y+6,m2x+300-t+3,m2y+20);
				g.drawLine(m2x+3+300-t,m2y+12,m2x+300-t+3,m2y+18);
				g.drawLine(m2x+3+300-t,m2y+12,m2x+300-t+13,m2y+18);
				g.drawLine(m2x+3+300-t,m2y+20,m2x+300-t+3,m2y+29);
				g.drawLine(m2x+3+300-t,m2y+20,m2x+300-t+13,m2y+26);
			}
		}
		// Pfeil
		
		g.setColor(Color.yellow);
		p1gx=230;
		p1gy=400;
		p1x[0]=p1gx;
		p1y[0]=p1gy;
		p1x[1]=p1gx+260;
		p1y[1]=p1gy;
		p1x[2]=p1gx+260;
		p1y[2]=p1gy-20;
		p1x[3]=p1gx+300;
		p1y[3]=p1gy+10;
		p1x[4]=p1gx+260;
		p1y[4]=p1gy+40;
		p1x[5]=p1gx+260;
		p1y[5]=p1gy+20;
		p1x[6]=p1gx;
		p1y[6]=p1gy+20;
		g.fillPolygon(p1x,p1y,7);
		
		p2gx=230;
		p2gy=480;
		p2x[0]=p2gx;
		p2y[0]=p2gy;
		p2x[1]=p2gx+40;
		p2y[1]=p2gy-30;
		p2x[2]=p2gx+40;
		p2y[2]=p2gy-10;
		p2x[3]=p2gx+300;
		p2y[3]=p2gy-10;
		p2x[4]=p2gx+300;
		p2y[4]=p2gy+10;
		p2x[5]=p2gx+40;
		p2y[5]=p2gy+10;
		p2x[6]=p2gx+40;
		p2y[6]=p2gy+30;
		g.fillPolygon(p2x,p2y,7);
		// Haus

		g.setColor(Color.black);
		dachpx[0]= hx;
		dachpy[0]= hy;
		dachpx[1]= hx+100;
		dachpy[1]= hy-100;
		dachpx[2]= hx+200;
		dachpy[2]= hy;
		g.setColor(Color.red);
		g.fillPolygon(dachpx,dachpy,3);
		g.fillRect(hx+150,hy-70,20,50);
		g.setColor(Color.black);
		g.drawRect(hx,hy,200,160);
		
		// Fabrik

		fpx[0]=fx;
		fpy[0]=fy;
		fpx[1]=fx+40;
		fpy[1]=fy;
		fpx[2]=fx+40;
		fpy[2]=fy+300;
		fpx[3]=fx+140;
		fpy[3]=fy+200;
		fpx[4]=fx+140;
		fpy[4]=fy+300;
		fpx[5]=fx+240;
		fpy[5]=fy+200;
		fpx[6]=fx+240;
		fpy[6]=fy+300;
		fpx[7]=fx+340;
		fpy[7]=fy+200;
		fpx[8]=fx+340;
		fpy[8]=fy+300;
		fpx[9]=fx+440;
		fpy[9]=fy+200;
		fpx[10]=fx+440;
		fpy[10]=fy+430;
		fpx[11]=fx;
		fpy[11]=fy+430;
		g.setColor(Color.red);
		g.fillPolygon(fpx,fpy,12);
		
		// Legende
		g.setColor(Color.blue);
		g.drawRect(60,70,130,20);
		g.setColor(Color.green);
		g.fillRect(60,100,130,20);
		
		// Blinken
		switch(zu){
			case 0:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+290,fy+330,100,20);
				break;
			case 1: 
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+290,fy+330,100,20);
				break;
			case 2:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+290,fy+390,100,20);
				break;
			case 3:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+290,fy+390,100,20);	
				break;
			case 4:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+290,fy+360,100,20);
				if ((t%8) == 4) g.fillRect(fx+90,fy+360,100,20);
				break;
			case 5:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+290,fy+360,100,20);
				if ((t%8) == 4) g.fillRect(fx+90,fy+360,100,20);
				break;
			case 6:
				g.setColor(Color.red);
				if ((t%8) == 4) g.fillRect(350,400,100,20);
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+90,fy+360,100,20);
				 break;
			case 7:
				g.setColor(Color.red);
				if ((t%8) == 4) g.fillRect(350,400,100,20);
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+90,fy+360,100,20);
				 break;
			case 8:
				g.setColor(Color.red);
				if ((t%8) == 4) g.fillRect(350,470,100,20);
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+90,fy+360,100,20);
				 break;
			case 9:
				g.setColor(Color.red);
				if ((t%8) == 4) g.fillRect(350,470,100,20);
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+90,fy+360,100,20);
				 break;
			case 10:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+290,fy+390,100,20);
				break;
			case 11:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+290,fy+390,100,20);
				break;
			case 12:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(hx+90,hy+40,100,20);
				break;
			case 13:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(hx+90,hy+40,100,20);
				break;
			case 14:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+90,fy+360,100,20);
				break;
			case 15:
				g.setColor(Color.yellow);
				if ((t%8) == 4) g.fillRect(fx+90,fy+360,100,20);
				break;
			default: 
				break;
		}
		
		// Beschriftungen
		g.setColor(Color.black);
		g.drawString("Arbeiter", fx+300, fy+345);
		g.drawString("Arbeitszeit", 360, 415);
		g.drawString("Lohn", 360, 485);
		g.setColor(Color.blue);
		g.drawString("Produktionsmenge", 70, 85);
		g.setColor(Color.black);
		g.drawString("Gewinn", fx+100, fy+375);
		g.drawString("Preis", fx+100, fy+405);
		//g.setColor(Color.green);
		g.drawString("Absatz", 70, 115);
		g.setColor(Color.black);
		g.drawString("Produktionszeit",fx+300,fy+375);
		g.drawString("Herstellungskosten",fx+100,fy+345);
		g.drawString("Schulden",hx+100,hy+55);
		g.drawString("Investitionen",fx+300,fy+405);
		g.drawString("Skalierung",70,145);
		
		
	}

	//		The start() method is called when the page containing the applet
	// first appears on the screen. The AppletWizard's initial implementation
	// of this method starts execution of the applet's thread.
	//--------------------------------------------------------------------------
	public void start()
	{
		if (m_mar11 == null)
		{
			m_mar11 = new Thread(this);
			m_mar11.start();
		}
		// TODO: Place additional applet start code here
	}
	
	//		The stop() method is called when the page containing the applet is
	// no longer on the screen. The AppletWizard's initial implementation of
	// this method stops execution of the applet's thread.
	//--------------------------------------------------------------------------
	public void stop()
	{
		if (m_mar11 != null)
		{
			m_mar11.stop();
			m_mar11 = null;
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

		berechnen();
		// If re-entering the page, then the images have already been loaded.
		// m_fAllLoaded == TRUE.
		//----------------------------------------------------------------------
		m_Graphics = getGraphics();
		

     while (true){
		try
		{
			// Draw next image in animation
			//--------------------------------------------------------------
			
	     	berechnen();
	     	grafik_vorbereiten();
	        repaint();
			Thread.sleep(1000);
			t+=4;
			if (t == 600) t = 0;
			zustt++;
			
			if ((zustt>0)&&(zustt<3)) zu = 0;
			if ((zustt>3)&&(zustt<30)) zu = 1;
			if ((zustt>33)&&(zustt<60)) zu = 2;
			if ((zustt>63)&&(zustt<90)) zu = 3;
			if ((zustt>93)&&(zustt<120)) zu = 4;
			if ((zustt>123)&&(zustt<150)) zu = 5;
			if ((zustt>153)&&(zustt<180)) zu = 6;
			if ((zustt>183)&&(zustt<210)) zu = 7;
			if ((zustt>213)&&(zustt<240)) zu = 8;
			if ((zustt>243)&&(zustt<270)) zu = 9;
			if ((zustt>273)&&(zustt<300)) zu = 10;
			if ((zustt>303)&&(zustt<330)) zu = 11;
			if ((zustt>333)&&(zustt<360)) zu = 12;
			if ((zustt>363)&&(zustt<390)) zu = 13;
			if ((zustt>393)&&(zustt<420)) zu = 14;
			if ((zustt>423)&&(zustt<450)) zu = 15;
			// if (zustt> 200) zustt = 0;
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

	// MOUSE SUPPORT:
	//		The mouseDown() method is called if the mouse button is pressed
	// while the mouse cursor is over the applet's portion of the screen.
	//--------------------------------------------------------------------------
	public boolean mouseDown(Event evt, int x, int y)
	{
		// TODO: Place applet mouseDown code here
		return true;
	}

	// MOUSE SUPPORT:
	//		The mouseUp() method is called if the mouse button is released
	// while the mouse cursor is over the applet's portion of the screen.
	//--------------------------------------------------------------------------
	public boolean mouseUp(Event evt, int x, int y)
	{
		// TODO: Place applet mouseUp code here
		return true;
	}


	// TODO: Place additional applet code here
	public void berechnen(){
		//Integer arbeiter, arbeitszeit, produktionszeit, lohn, produktionsmenge; 
		//Integer gewinn, preis, absatz;
		switch (zu){
		case 0:
			break;
		case 1:
			f_arbeiter.setText("80");
			break;
		case 2:
			break;
		case 3:
			f_investitionen.setText("40000");
			break;
		case 4:
			break;
		case 5:
			f_investitionen.setText("0");
			f_produktionszeit.setText("20");
			f_gewinn.setText("225");
			break;
		case 6:
			break;
		case 7:
			f_arbeitszeit.setText("35");
			f_gewinn.setText("190");
			break;
		case 8:	
			break;
		case 9:
			f_lohn.setText("3000");
			f_gewinn.setText("47");
		case 10:
			break;
		case 11:
			f_gewinn.setText("100");
			f_investitionen.setText("40000");
			break;
		case 12:
			break;
		case 13:	
			f_investitionen.setText("0");
			f_schulden.setText("1000");
		case 14:
			break;
		case 15:
			f_gewinn.setText("0");
		default:	
				
		}
		
		
		
		arbeiter = (zahl.valueOf(f_arbeiter.getText())).intValue();
		arbeitszeit = (zahl.valueOf(f_arbeitszeit.getText())).intValue();
		produktionszeit = (zahl.valueOf(f_produktionszeit.getText())).intValue();
		lohn = (zahl.valueOf(f_lohn.getText())).intValue();
		gewinn = (zahl.valueOf(f_gewinn.getText())).intValue();
		produktionsmenge = (int) (arbeiter*arbeitszeit * 4 / produktionszeit);
		f_produktionsmenge.setText(zahl.toString(produktionsmenge));
		herstellungskosten = (int)(arbeiter * lohn / produktionsmenge );
		f_herstellungskosten.setText(zahl.toString(herstellungskosten));
		preis = (int) (herstellungskosten+gewinn);
		f_preis.setText(zahl.toString(preis));
		schulden  = (zahl.valueOf(f_schulden.getText())).intValue();
		investitionen = (zahl.valueOf(f_investitionen.getText())).intValue();
		absatz = (int) ((arbeiter*(lohn+schulden)+investitionen) /preis);
		if (absatz > produktionsmenge) absatz = produktionsmenge;
		f_absatz.setText(zahl.toString(absatz));
		skalierung = (zahl.valueOf(f_skalierung.getText())).doubleValue();
		
		for (int i=0;i<360; i++ ){
			prodPolyx[i]=kreisx + (int) m.round(produktionsmenge /skalierung * m.sin(2*m.PI*i/360));
			prodPolyy[i]=kreisy + (int)m.round(produktionsmenge/skalierung * m.cos(2*m.PI*i/360));
			absatzPolyx[i]=kreisx + (int)m.round(absatz/skalierung * m.sin(2*m.PI*i/360));
			absatzPolyy[i]=kreisy + (int)m.round(absatz/skalierung * m.cos(2*m.PI*i/360));
		}
		

	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == berechneButton)){
			berechnen();
			return true;
		}
		else return false;
	}
	

}

