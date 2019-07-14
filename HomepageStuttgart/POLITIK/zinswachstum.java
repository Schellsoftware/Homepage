/*
 * Created on 13.08.2006
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
import java.applet.Applet;
import java.awt.*;


/**
 * @author hars
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class zinswachstum extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	double zinssatz=3.7;
	double kapital=100;
	int zeit=0;
	String str;
	int px[] = new int[3];
	int py[] = new int[3];
	TextField zinssatzField;
	Button uebernehmeButton;
	double kapFeld[] = new double[150];
	double zinsFeld[] = new double[150];
	
	public void init()
	{
		setLayout(null); // Freie Positionierung
		uebernehmeButton = new Button("Übernehme");
		uebernehmeButton.setBounds(120,20,80,20);
		add(uebernehmeButton);
		zinssatzField = new TextField ("3.7", 5);
		zinssatzField.setBounds( 70, 20 , 40, 20 );
		add(zinssatzField);
		ausgabe=null;
		g=null;
		resize(320, 460);
		kapFeld[0]=100;
		zinsFeld[0]=0;
		for(int i=1;i<150;i++){
			kapFeld[i]=kapFeld[i-1]*(1+zinssatz/100);
			zinsFeld[i]=kapFeld[i]*zinssatz/100;
		}
		
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
				
				Thread.sleep(100);
		        repaint();
		        zeit++;
		        if(zeit==100)
		        {
		        	zeit = 0;
		        	kapital = 100;
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
		kapital = kapital * (1+zinssatz/100);
	}
	
	public void zeichneZyklus()
	{
		int xvers = 40;
		g.setColor(Color.green);
		g.setColor(Color.black);

		g.drawString("Geld",90,55);
		for(int i=0;i<340;i=i+10)
		{
			g.drawString(i*10+"",90,400-i);
			g.drawLine(125,400-i,135,400-i);
		}
		g.drawString("Zeit",280,415);
		for (int i=0;i<150;i+=10){
			//g.drawString(i+"",90+i,410);
			g.drawLine(130+i,405,130+i,395);
		}
		g.drawString("0",130,415);
		g.drawString("50",180,415);
		g.drawString("100",230,415);
		//g.drawString("150",280,415);
		g.drawLine(130,400,290,400);
		px[0] = 300;
		py[0] = 400;
		px[1] = 290;
		py[1] = 397;
		px[2] = 290;
		py[2] = 403;
		g.fillPolygon(px, py, 3);
		g.drawLine(xvers+90,400,xvers+90,40);
		px[0] = xvers+90;
		py[0] = 40;
		px[1] = xvers+93;
		py[1] = 50;
		px[2] = xvers+87;
		py[2] = 50;
		g.fillPolygon(px, py, 3);
		g.setColor(Color.blue);
		g.fillRect(30,390,30,10);
		g.drawString("Grundkapital",10,415);
		g.drawLine(130,400,130,390);
		g.setColor(Color.red);
		g.drawOval(125+zeit,395-(int)(kapFeld[zeit]/10),10,10);
		g.drawString("Zins",10,445);
		g.fillRect(30,400-(int)(kapFeld[zeit]/10),30,(int)(zinsFeld[zeit]/10));
		for (int i=1;i<120;i++){
			g.drawLine(129+i,400-(int)(zinsFeld[i-1]/10),130+i,400-(int)(zinsFeld[i]/10));
		}
		g.setColor(Color.black);
		g.drawString("Kapital",10,430);
		g.drawRect(30,400-(int)(kapFeld[zeit]/10),30,(int)(kapFeld[zeit]/10));
		for (int i=1;i<120;i++){
			g.drawLine(129+i,400-(int)(kapFeld[i-1]/10),130+i,400-(int)(kapFeld[i]/10));
		}
		g.drawString("Zinssatz",10,35);
		g.drawString("Harald Schellinger",190,445);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == uebernehmeButton)){
			str = zinssatzField.getText();
			if(str.length()!=0)	zinssatz = (Double.valueOf(zinssatzField.getText())).doubleValue();
			kapFeld[0]=100;
			zinsFeld[0]=0;
			for(int i=1;i<150;i++){
				kapFeld[i]=kapFeld[i-1]*(1+zinssatz/100);
				zinsFeld[i]=kapFeld[i]*zinssatz/100;
			}
			return true;
		}
		else return false;
	}
}