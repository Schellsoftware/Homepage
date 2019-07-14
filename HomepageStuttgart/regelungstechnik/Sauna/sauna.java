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
public class sauna extends Applet implements Runnable{
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
	Button EinButton,TuerButton,ZuButton;
	int x[]=new int[250];
	int y[]=new int[250];
	String str;
	int px[] = new int[3];
	int py[] = new int[3];
	int tx[] = new int[4];
	int ty[] = new int[4];
	TextField preg = new TextField("5");
	TextField ireg = new TextField("1");
	TextField dreg = new TextField("10");
	TextField soll = new TextField("80");
	double ug,og;
	Checkbox pan;
	Checkbox ian;
	Checkbox dan;
	double p,i,d;
	public void init()
	{
		setLayout(null); // Freie Positionierung
		EinButton = new Button("Einschalten");
		EinButton.setBounds(160,20,90,20);
		add(EinButton);
		TuerButton = new Button("Tür auf");
		TuerButton.setBounds(160,50,90,20);
		add(TuerButton);
		ZuButton = new Button("Tür zu");
		ZuButton.setBounds(160,80,90,20);
		add(ZuButton);
		
		CAuto = new Choice();
		CAuto.setBounds(20,20,100,20);
		CAuto.addItem("Automatik");
		CAuto.addItem("Manuell");
		soll.setBounds(280,20,40,20);
		preg.setBounds(110,20,40,20);
		ireg.setBounds(110,50,40,20);
		dreg.setBounds(110,80,40,20);
		add(soll);
		add(preg);
		add(ireg);
		add(dreg);
		pan = new Checkbox("Proportional",true);
		ian = new Checkbox("Integrierer",false);
		dan = new Checkbox("Differenzierer",false);
		pan.setBounds(20,20,100,20);
		ian.setBounds(20,50,100,20);
		dan.setBounds(20,80,100,20);
		add(pan);
		add(ian);
		add(dan);
		//add(CAuto);
		
		saub = getImage(getCodeBase(),"sauna.jpg");
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(saub,0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e){
			;
		}
		for(int i=0;i<250;i++) x[i]=0;
		for(int i=0;i<250;i++) y[i]=0;
		
		ausgabe=null;
		g=null;
		resize(600, 300);
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
				Thread.sleep(500);
		        
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
		u=0;
		str = preg.getText();
		if(str.length()!=0)	p = Double.parseDouble(preg.getText());
		str = ireg.getText();
		if(str.length()!=0)	i = Double.parseDouble(ireg.getText());
		str = dreg.getText();
		if(str.length()!=0)	d = Double.parseDouble(dreg.getText());
		str = soll.getText();
		if(str.length()!=0)	w = Double.parseDouble(soll.getText());
		wmx = w - y1;
		if (pan.getState()){
			u+=wmx*p;
		}
		
		if (ian.getState()){
			ik=ikm1+wmx;
			ikm1 = ik;
			u+=ik*i;
		}
		if (dan.getState()){
			if(zeit>1){
				u+=d*(wmx-wmxm1);
				wmxm1=wmx;
			}
		}
		if(u>z) u-=z;
		y1=(u -ykm1)*0.05+ykm1; 
		y[zeit]=(int)y1;
		if (y[zeit]>100) {
			y[zeit]=100;
			y1=100;
		}
		//xkm1 = x[zeit];
		ykm1 = y1;
	}
	
	public void zeichneZyklus()
	{
		g.setColor(Color.black);
		g.setColor(Color.red);
		g.drawLine(300,200-(int)w,560,200-(int)w);
		for(int i=0;i<249;i++){
			//g.setColor(Color.red);
			//g.drawLine(300+i,200-x[i],301+i,200-x[i+1]);
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
		g.drawImage(saub,40,110,this);

		if(y[zeit]<50)g.setColor(new Color((int)(5*y[zeit]),(int)(5*y[zeit]),255));
		else g.setColor(new Color(255,255-(int)(5*(y[zeit]-50)),255-(int)(5*(y[zeit]-50))));
		tx[0]=123;
		ty[0]=148;
		tx[1]=160;
		ty[1]=144;
		tx[2]=160;
		ty[2]=277;
		tx[3]=123;
		ty[3]=271;
		g.fillPolygon(tx,ty,4);
		g.setColor(Color.black);
		g.drawString("Sollwert ",325,35);
		//g.drawString("y1 "+y1,400,20);
		//g.drawString("t "+zeit,400,40);
		g.drawString("Harald Schellinger",400,230);
	}
	public boolean action(Event event, Object eventObject){
		if ((event.target == EinButton)){
			for(int i=0;i<250;i++) y[i]=0;
			ykm1 = 0;
			zeit = 0;
			return true;
		}
		if ((event.target == TuerButton)){
			z=40;
			
			return true;
		}
		if ((event.target == ZuButton)){
			z=0;
			return true;
		}
		else return false;
	}
}
