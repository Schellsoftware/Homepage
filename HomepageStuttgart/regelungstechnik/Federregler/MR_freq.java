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
public class MR_freq extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	int federabgriff;
	int federanfang;
	int kasten;
	int xe[]=new int[200];
	int periode=0;
	double pd=200;
	double x1=0;
	double x2=0;
	double x1kp, x2kp;
	double omega0=0.1;
	double D=0.5;
	double delta_t=0.001;
	int schwerpunkt[];
	TextField K = new TextField("0.1");
	Button uebernehmeButton;
	
	public void init()
	{
		setLayout(null);
		K.setBounds(20,20,30,20);
		add(K);
		uebernehmeButton = new Button("Übernehmen");
		uebernehmeButton.setBounds(60,20,100,20);
		add(uebernehmeButton);
		schwerpunkt = new int [200];
		for(int i=0;i<200;i++)
		{
			schwerpunkt[i]=150;
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

	public void paint (Graphics g1)
	{
		g1.drawImage(ausgabe, 0, 0, this);
	}

	public void repaint (Graphics g1)
	{
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

	public void run()
	{				

	     while (true){
			try
			{
				berechne();
		     	zeichne();
		        repaint();
				Thread.sleep(50);
				
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
	public void berechne()
	{	
		periode ++;
		if((periode%(int)pd)<(int)(pd/2)){
			xe[199]=20;
		}
		else{
			xe[199]=-20;
		}
		for(int i=0;i<199;i++)
		{
			xe[i]=xe[i+1];
		}
		federabgriff = xe[150];
		federanfang = federabgriff + 30;
		for(int i=0;i<1000;i++){
			x1kp=x1+x2*delta_t;
			x2kp=x2-2*D*omega0*x2*delta_t-omega0*omega0*x1*delta_t+omega0*omega0*federabgriff*delta_t;
			x1=x1kp;
			x2=x2kp;
		}
		for (int i=0;i<150;i++)
		{
			schwerpunkt[i]=schwerpunkt[i+1];
		}
		schwerpunkt[150] = (int) x1+150; // kasten;
		kasten =(int) x1+150;
	}
	
	public void zeichne(){
		
	int abstandRandx = 50;
	int abstandRandy = 100;
	int abstand;
	int abstandh;
	int federhoehe = 150;
	int federbreite = 10;
	
	g.clearRect(0,0,sizex,sizey);
	
	g.setColor(Color.blue);
	for (int i=0; i < 198; i++)
	{
		g.drawLine(abstandRandx +xe[i],i,abstandRandx +xe[i+1],1+i);
	}
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
	g.setColor(Color.red);
	for (int i=0; i <150; i++)
	{
		g.drawLine(abstandRandx+schwerpunkt[i]+75,i,abstandRandx+schwerpunkt[i+1]+75,i+1);
	}	
	
	}
	public boolean action(Event event, Object eventObject){
		String str=new String();

		if ((event.target == uebernehmeButton)){
			str = K.getText();
			if(str.length()!=0)	omega0 = (Double.valueOf(K.getText())).doubleValue();
			
			return true;
		}
		else return false;
	}
}
