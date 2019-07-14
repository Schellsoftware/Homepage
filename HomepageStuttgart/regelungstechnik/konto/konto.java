import java.applet.Applet;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
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
public class konto extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	private Image saub;
	int periode=0;
	double pd=100;
	double yk;
	double ykm1=0;
	double xkm1=0;
	double ik,ikm1=0;
	double y1=0;
	double w=0;
	double wmx,wmxm1=0;
	double z=0;
	double u=0;
	int ein = 1;
	int zeit=0;
	int periodendauer=200;
	Choice CAuto;
	Button NeustartButton,AbbuchButton;
	int x[]=new int[250];
	int y[]=new int[250];
	String str;
	int px[] = new int[3];
	int py[] = new int[3];
	TextField standF = new TextField("0");
	TextField sollF = new TextField("100");
	TextField abbuchungF = new TextField("0");
	TextField verstaerkungF = new TextField("0.8");
	double stand,soll,abbuchung,verstaerkung;

	public void init()
	{
		setLayout(null); // Freie Positionierung
		NeustartButton = new Button("Neustart");
		NeustartButton.setBounds(20,20,70,20);
		add(NeustartButton);
		AbbuchButton = new Button("Abbuchung");
		AbbuchButton.setBounds(120,20,90,20);
		add(AbbuchButton);

		verstaerkungF.setBounds(110,100,40,20);
		standF.setBounds(210,100,40,20);
		sollF.setBounds(20,100,40,20);
		abbuchungF.setBounds(160,60,40,20);
		add(standF);
		add(sollF);
		add(abbuchungF);
		add(verstaerkungF);

		for(int i=0;i<250;i++) x[i]=0;
		for(int i=0;i<250;i++) y[i]=0;
		
		ausgabe=null;
		g=null;
		resize(600, 200);
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
		//g1.drawImage(zu, 0, 0, this);
	}

	public void repaint (Graphics g1)
	{
		grafik_vorbereiten();
		//g1.drawImage(ausgabe, 0, 0, this);

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
				periode ++;
				repaint();
				
				//if(periode == 150) periode=0;
				Thread.sleep(1000);
		        
		        //zeit ++;
		        if(zeit >=250){
		        	zeit = 0;
		        	for(int i=0;i<250;i++) x[i]=0;
		    		for(int i=0;i<250;i++) y[i]=0;
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
		double delta;
		str = sollF.getText();
		if(str.length()!=0)	soll = Double.parseDouble(sollF.getText());
		str = abbuchungF.getText();
		if(str.length()!=0)	abbuchung = Double.parseDouble(abbuchungF.getText());
		str = verstaerkungF.getText();
		if(str.length()!=0)	verstaerkung = Double.parseDouble(verstaerkungF.getText());
		standF.setText(Double.toString(stand));
		for(int l=0;l<10;l++){
			y[zeit]=(int)stand;
			x[zeit]=(int)soll;
			zeit ++;
			if (zeit>=250){
				zeit =0;
				for(int i=0;i<250;i++) x[i]=0;
				for(int i=0;i<250;i++) y[i]=0;
			}
		}
		delta = soll-stand;
		stand = stand + verstaerkung * delta - abbuchung;	
		
	}
	
	public void zeichneZyklus()
	{
		g.setColor(Color.black);
		g.setColor(Color.red);
		//g.drawLine(300,200-(int)w,560,150-(int)w);
		for(int i=0;i<249;i++){
			g.setColor(Color.red);
			g.drawLine(300+i,150-x[i],301+i,150-x[i+1]);
			g.setColor(Color.black);
			g.drawLine(300+i,150-y[i],301+i,150-y[i+1]);
		}
		g.drawLine(300,150,560,150);
		g.drawString("t",560,165);
		px[0]=570;
		py[0]=150;
		px[1]=560;
		py[1]=147;
		px[2]=560;
		py[2]=153;
		g.fillPolygon(px,py,3);
		g.drawLine(300,150,300,40);
		g.drawString("G",290,40);
		px[0]=300;
		py[0]=30;
		px[1]=297;
		py[1]=40;
		px[2]=303;
		py[2]=40;
		g.fillPolygon(px,py,3);
		

		if(y[zeit]<50)g.setColor(new Color((int)(5*y[zeit]),(int)(5*y[zeit]),255));
		else g.setColor(new Color(255,255-(int)(5*(y[zeit]-50)),255-(int)(5*(y[zeit]-50))));
		g.setColor(Color.black);
		g.drawString("Sollwert ",20,95);
		g.drawString("Verstärkung ",100,95);
		g.drawString("Kontostand ",210,95);
		g.drawString("Zahlungen ",150,55);
		g.drawOval(80,105,6,6);
		px[0]=80;
		py[0]=108;
		px[1]=70;
		py[1]=105;
		px[2]=70;
		py[2]=111;
		g.fillPolygon(px,py,3);
		g.drawString("+",70,105);
		px[0]=83;
		py[0]=111;
		px[1]=80;
		py[1]=121;
		px[2]=86;
		py[2]=121;
		g.fillPolygon(px,py,3);
		g.drawString("-",90,121);
		g.drawLine(60,108,80,108);
		g.drawLine(86,108,110,108);
		px[0]=110;
		py[0]=108;
		px[1]=100;
		py[1]=105;
		px[2]=100;
		py[2]=111;
		g.fillPolygon(px,py,3);
		g.drawLine(180,80,180,105);
		px[0]=180;
		py[0]=105;
		px[1]=183;
		py[1]=95;
		px[2]=177;
		py[2]=95;
		g.fillPolygon(px,py,3);
		g.drawOval(177,105,6,6);
		g.drawString("-",187,100);
		g.drawString("+",167,107);
		px[0]=177;
		py[0]=108;
		px[1]=167;
		py[1]=105;
		px[2]=167;
		py[2]=111;
		g.fillPolygon(px,py,3);
		g.drawLine(150,108,177,108);
		px[0]=210;
		py[0]=108;
		px[1]=200;
		py[1]=105;
		px[2]=200;
		py[2]=111;
		g.fillPolygon(px,py,3);
		g.drawLine(183,108,210,108);
		px[0]=290;
		py[0]=108;
		px[1]=280;
		py[1]=105;
		px[2]=280;
		py[2]=111;
		g.fillPolygon(px,py,3);
		g.drawLine(250,108,290,108);
		g.drawLine(270,108,270,138);
		g.drawLine(83,138,270,138);
		g.drawLine(83,111,83,138);
		//g.drawString("y1 "+y1,400,20);
		//g.drawString("t "+zeit,400,40);
		g.drawString("Harald Schellinger",400,180);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == NeustartButton)){
			for(int i=0;i<250;i++){
				x[i]=0;
				y[i]=0;
			}
			ykm1 = 0;
			zeit = 0;
			stand = 0; 
			return true;
		}
		if ((event.target == AbbuchButton)){

			abbuchungF.setText("10");
			return true;
		}
		else return false;
	}
}
