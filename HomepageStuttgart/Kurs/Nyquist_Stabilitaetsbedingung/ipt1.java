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
public class ipt1 extends Applet implements Runnable{
	private Thread	 m_thread = null;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	int federabgriff;
	int federanfang;
	int kasten;
	int xe[]=new int[200];
	int istwerte[]=new int[100];
	int mittelwert;
	int periode=0;
	int zeit;
	int px[] = new int[3];
	int py[]=new int [3];
	double pd=100;
	double prop=0.8;
	double x1=0;
	double x2=0;
	double x3=0;
	double u;
	double x1kp, x2kp, x3kp;
	double omega0=0.06;
	double D=0.5;
	double delta_t=0.001;
	int schwerpunkt[];
	TextField K = new TextField("0.8");
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
		for(int i=0;i<100;i++){
			xe[i]=(int)(20*Math.sin(2*3.14/100*i));
		}
		ausgabe=null;
		g=null;
		resize(600,420);
		
		
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
				zeit++;
				if(zeit>=100) zeit=0;
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
		//periode ++;
		//int h;
		//h= periode % (int) pd;
		//xe[199]=(int)(20*Math.sin(2*3.14/pd*h));
		//for(int i=0;i<199;i++)
		//{
		//	xe[i]=xe[i+1];
		//}

		u=prop*xe[zeit]/15.0;
		//u=(prop/15.0)*(xe[zeit]-x1);
		for(int i=0;i<1000;i++){
			x1kp=x1+x2*delta_t;
			x2kp=x2-2*D*omega0*x2*delta_t-omega0*omega0*x1*delta_t+omega0*omega0*x3*delta_t;
			x3kp=x3+u*delta_t;
			x1=x1kp;
			x2=x2kp;
			x3=x3kp;
		}
		federabgriff = (int)x3;
		federanfang = federabgriff + 30;
		for (int i=0;i<150;i++)
		{
			schwerpunkt[i]=schwerpunkt[i+1];
		}
		schwerpunkt[100] = (int) x1+150; // kasten;
		kasten =(int) x1+150;
		istwerte[zeit]=(int)x1;
		//if(istwerte[zeit]>=30)istwerte[zeit]=30;
	}
	
	public void zeichne(){
		
	int abstandRandx = 250;
	int abstandRandy = 50;
	int abstand;
	int abstandh;
	int federhoehe = 100;
	int federbreite = 10;
	
	g.clearRect(0,0,sizex,sizey);
	
	g.setColor(Color.blue);
	for (int i=0; i < 99; i++)
	{
		g.drawLine(30 +i,100-xe[i],30 +i+1,100-xe[1+i]);
	}
	g.drawRect(25, 70, 110, 60);
	g.setColor(Color.black);
	g.drawLine(30, 100, 130, 100);
	g.setColor(Color.green);
	g.drawLine(30, 80, 130, 80);
	g.drawLine(30, 120, 130, 120);
	g.setColor(Color.red);
	g.drawOval(30+zeit-5,100-xe[zeit]-5,10,10);
	for(int i=0;i<99;i++){
		mittelwert+=istwerte[i];
	}
	mittelwert=mittelwert/100;
	for(int i=0;i<99;i++){
		g.drawLine(150+i,200+istwerte[i]-mittelwert,150+i+1,200+istwerte[i+1]-mittelwert);
	}
	g.drawOval(150+zeit-5,200+istwerte[zeit]-5-mittelwert,10,10);
	g.setColor(Color.blue);
	g.drawRect(140,170,120,60);
	g.setColor(Color.black);
	g.drawLine(150, 200,250, 200);
	g.setColor(Color.green);
	g.drawLine(150, 180, 250, 180);
	g.drawLine(150, 220, 250, 220);
	g.setColor(Color.red);
	for(int i=0;i<99;i++){
		g.drawLine(150+i,320-istwerte[i]+mittelwert,150+i+1,320-istwerte[i+1]+mittelwert);
	}
	g.drawOval(150+zeit-5,320-istwerte[zeit]+mittelwert-5,10,10);
	g.setColor(Color.blue);
	g.drawRect(140,290,120,60);
	g.setColor(Color.black);
	g.drawLine(150, 320,250, 320);
	g.setColor(Color.green);
	g.drawLine(150, 300, 250, 300);
	g.drawLine(150, 340, 250, 340);
	g.setColor(Color.black);
	g.drawLine(480,150,480,370);
	g.drawLine(480, 370, 200, 370);
	g.drawLine(200, 350, 200, 370);
	g.drawLine(200, 290, 200, 260);
	px[0]=197;
	py[0]=270;
	px[1]=203;
	py[1]=270;
	px[2]=200;
	py[2]=260;
	g.fillPolygon(px,py,3);
	g.drawOval(195, 250, 10, 10);
	g.drawString("-",210,270);
	g.drawLine(200, 230, 200, 250);
	g.drawLine(200, 170, 200, 150);
	px[0]=197;
	py[0]=150;
	px[1]=203;
	py[1]=150;
	px[2]=200;
	py[2]=140;
	g.fillPolygon(px,py,3);
	g.drawLine(140, 100, 195, 100);
	px[0]=185;
	py[0]=103;
	px[1]=185;
	py[1]=97;
	px[2]=195;
	py[2]=100;
	g.fillPolygon(px,py,3);
	g.drawOval(195, 95, 10, 10);
	g.drawLine(205, 100, 230, 100);
	px[0]=230;
	py[0]=97;
	px[1]=230;
	py[1]=103;
	px[2]=240;
	py[2]=100;
	g.fillPolygon(px,py,3);
	g.setColor(Color.blue);
	g.drawRect(abstandRandx+federanfang-20, federhoehe-20, 20, 70);
	g.drawRect(250,federhoehe+30, 300, 10);
	//g.drawLine(abstandRandx+federabgriff,federhoehe,abstandRandx+federanfang, federhoehe);
	//g.setColor(Color.red);
	//g.drawOval( abstandRandx-5+federabgriff, federhoehe-5,10,10);
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
	for (int i=0; i <100; i++)
	{
		g.drawLine(abstandRandx+schwerpunkt[i]+75,i,abstandRandx+schwerpunkt[i+1]+75,i+1);
	}	
	
	}
	public boolean action(Event event, Object eventObject){
		String str=new String();

		if ((event.target == uebernehmeButton)){
			str = K.getText();
			if(str.length()!=0)	prop = (Double.valueOf(K.getText())).doubleValue();
			x1=0;
			x2=0;
			x3=0;
			zeit=0;
			return true;
		}
		else return false;
	}
}
