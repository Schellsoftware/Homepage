import java.applet.Applet;
import java.awt.Button;
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
public class Buegel extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	private Image offen,zu;
	int periode=0;
	double p=0.5;
	double pd=100;
	double yk;
	double ykm1=0;
	double xkm1=0;
	double y1;
	int ein = 1;
	int zeit=0;
	int periodendauer=200;
	Choice CAuto;
	Button EinButton,AusButton;
	int x[]=new int[250];
	int y[]=new int[250];
	String str;
	int px[] = new int[3];
	int py[] = new int[3];
	TextField obere_grenze = new TextField("350");
	TextField untere_grenze = new TextField("200");
	double ug,og;
	public void init()
	{
		setLayout(null); // Freie Positionierung
		EinButton = new Button("Ein");
		EinButton.setBounds(130,20,50,20);
		add(EinButton);
		AusButton = new Button("Aus");
		AusButton.setBounds(190,20,50,20);
		add(AusButton);
		CAuto = new Choice();
		CAuto.setBounds(20,20,100,20);
		CAuto.addItem("Automatik");
		CAuto.addItem("Manuell");
		untere_grenze.setBounds(250,20,40,20);
		obere_grenze.setBounds(390,20,40,20);
		add(untere_grenze);
		add(obere_grenze);
		add(CAuto);
		
		//offen = getToolkit().getImage("buegeleisen_offen.jpg");
		offen = getImage(getCodeBase(),"buegeleisen_offen.jpg");
		//zu = getToolkit().getImage("buegeleisen_geschlossen.jpg");
		zu = getImage(getCodeBase(),"buegeleisen_geschlossen.jpg");
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(offen,0);
		mt.addImage(zu,1);
		try {
			mt.waitForAll();

		} catch (InterruptedException e){
			;
		}
		for(int i=0;i<250;i++) x[i]=0;
		for(int i=0;i<250;i++) y[i]=0;
		
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
		g1.drawImage(zu, 0, 0, this);

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
				
				
				if(CAuto.getSelectedIndex()==0){
					str = untere_grenze.getText();
					if(str.length()!=0)	ug = Double.parseDouble(untere_grenze.getText())/4;
					str = obere_grenze.getText();
					if(str.length()!=0)	og = Double.parseDouble(obere_grenze.getText())/4;
					if (y[zeit] < ug){
						ein = 1;
					}
					if (y[zeit]>og)	{
						ein=0;
					}
				}
				//if(periode == 150) periode=0;
				Thread.sleep(200);
		        repaint();
		        zeit ++;
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
		if(ein==1){
			x[zeit]=100;
			y1=(x[zeit]-ykm1)*0.05+ykm1; 
			y[zeit]=(int)y1;
			//xkm1 = x[zeit];
			ykm1 = y1;
		}
		else{
			x[zeit]=0;
			y1=(0.95 * ykm1);
			y[zeit]=(int)y1;// - 0.9 * xkm1);
			xkm1 = x[zeit];
			ykm1 = y1;
		}
		
	}
	
	public void zeichneZyklus()
	{
		g.setColor(Color.black);
		for(int i=0;i<249;i++){
			g.setColor(Color.red);
			g.drawLine(300+i,200-x[i],301+i,200-x[i+1]);
			g.setColor(Color.black);
			g.drawLine(300+i,200-y[i],301+i,200-y[i+1]);
		}
		g.drawLine(300,200,560,200);
		g.drawString("t",560,215);
		px[0]=570;
		py[0]=200;
		px[1]=560;
		py[1]=197;
		px[2]=560;
		py[2]=203;
		g.fillPolygon(px,py,3);
		g.drawLine(300,200,300,90);
		g.drawString("T",290,90);
		px[0]=300;
		py[0]=80;
		px[1]=297;
		py[1]=90;
		px[2]=303;
		py[2]=90;
		g.fillPolygon(px,py,3);
		if (ein==1){
			g.drawImage(zu,40,80,this);
		}
		else{
			g.drawImage(offen,40,80,this);
		}
		if(y[zeit]<50)g.setColor(new Color((int)(5*y[zeit]),(int)(5*y[zeit]),255));
		else g.setColor(new Color(255,255-(int)(5*(y[zeit]-50)),255-(int)(5*(y[zeit]-50))));
		g.fillRect(55,175,205,10);
		g.fillRect(100,140,10,40);
		g.fillRect(100,140,80,10);
		g.setColor(Color.black);
		g.drawString("Untere Grenze",295,35);
		g.drawString("Obere Grenze",435,35);
		g.drawString("Harald Schellinger",400,230);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == EinButton)){
			ein = 1;
			
			return true;
		}
		if ((event.target == AusButton)){
			ein = 0;
			
			return true;
		}
		else return false;
	}
}