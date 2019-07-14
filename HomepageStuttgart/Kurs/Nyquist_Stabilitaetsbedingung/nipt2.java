
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
public class nipt2 extends Applet implements Runnable{
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
	double prop=0.5;
	double x1=0;
	double x2=0;
	double x3=0;
	double u;
	double x1kp, x2kp, x3kp;
	double omega0=0.06;
	double D=0.5;
	double delta_t=0.001;
	int schwerpunkt[];
	TextField K = new TextField("0.5");
	Button uebernehmeButton;
	Choice darst_c;
	double w0[];
	int logw[];
	double A[];
	int logA[];
	double phi[];
	public void init()
	{
		setLayout(null);
		K.setBounds(20,20,30,20);
		add(K);
		uebernehmeButton = new Button("Übernehmen");
		uebernehmeButton.setBounds(60,20,100,20);
		add(uebernehmeButton);
		darst_c=new Choice();
		darst_c.add("Nyquist-Diagramm");
		darst_c.add("Bode-Diagramm");
		darst_c.setBounds(30,150,130,20);
		add(darst_c);
		schwerpunkt = new int [200];
		for(int i=0;i<200;i++)
		{
			schwerpunkt[i]=150;
		}
		for(int i=0;i<100;i++){
			xe[i]=(int)(20*Math.sin(2*3.14/100*i));
		}
		A=new double[301];
		w0=new double[301];
		phi = new double[301];
		logw=new int[301];
		logA =new int [301];
		for (int i=0;i<11;i++){
			if (i==0){
				w0[i]=0.001;
			}
			else
			{
				w0[i]=0.001*i;
			}
			logw[i]=(int)(100*Math.log10(w0[i]));
		}
		for (int i=10;i<21;i++){
			if (i==10){
				w0[i]=0.01;
			}
			else
			{
				w0[i]=0.01*(i-10);
			}
			logw[i]=(int)(100*(1/Math.log(10))*Math.log10(w0[i]));
		}
		for (int i=30;i<41;i++){
			if (i==30){
				w0[i]=0.1;
			}
			else
			{
				w0[i]=0.1*(i-30);
			}
			logw[i]=(int)(100*(1/Math.log(10))*Math.log10(w0[i]));
		}
		ausgabe=null;
		g=null;
		resize(600,620);
		
		
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

		//u=prop*xe[zeit]/15.0;
		u=(prop/15.0)*(xe[zeit]-x1);
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
	int amp_gef=0;
	int phase_gef=0;
	double w=0;
	int im[]=new int [1000];
	int re[]=new int [1000];
	double imd[]=new double[1000];
	double red[]=new double[1000];
	int amplitude[]=new int[1000];
	double phase[]=new double[1000];
	int wgefunden = 0;
	double f[]=new double[1000];
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
	
	
	for(int i=1;i<=10;i++){
		w=0.001*i;
		red[i]=(100*omega0*omega0*prop/15.0*(-1)*(2*D*omega0*w*w)/((2*D*omega0*w*w)*(2*D*omega0*w*w)+w*w*(omega0*omega0-w*w)*(omega0*omega0-w*w)));
		imd[i]=(100*omega0*omega0*prop/15.0*(-w)*(omega0*omega0-w*w)/((2*D*omega0*w*w)*(2*D*omega0*w*w)+w*w*(omega0*omega0-w*w)*(omega0*omega0-w*w)));
		re[i]=(int)red[i];
		im[i]=(int)imd[i];
		f[i]=100*Math.log10(w);
		amplitude[i]=(int)(20*Math.log10(0.01*Math.sqrt(red[i]*red[i]+imd[i]*imd[i])));
		phase[i]=(int)(57.3*Math.atan(imd[i]/red[i]));

		
	}
	
	for(int i=11;i<=20;i++){
		w=0.01*(i-10);
		red[i]=(100*omega0*omega0*prop/15.0*(-1)*(2*D*omega0*w*w)/((2*D*omega0*w*w)*(2*D*omega0*w*w)+w*w*(omega0*omega0-w*w)*(omega0*omega0-w*w)));
		imd[i]=(100*omega0*omega0*prop/15.0*(-w)*(omega0*omega0-w*w)/((2*D*omega0*w*w)*(2*D*omega0*w*w)+w*w*(omega0*omega0-w*w)*(omega0*omega0-w*w)));
		re[i]=(int)red[i];
		im[i]=(int)imd[i];
		f[i]=100*Math.log10(w);
		amplitude[i]=(int)(20*Math.log10(0.01*Math.sqrt(red[i]*red[i]+imd[i]*imd[i])));
		phase[i]=(int)(57.3*Math.atan(imd[i]/red[i]));

	}
	
	for(int i=21;i<=30;i++){
		w=0.1*(i-20);
		red[i]=(100*omega0*omega0*prop/15.0*(-1)*(2*D*omega0*w*w)/((2*D*omega0*w*w)*(2*D*omega0*w*w)+w*w*(omega0*omega0-w*w)*(omega0*omega0-w*w)));
		imd[i]=(100*omega0*omega0*prop/15.0*(-w)*(omega0*omega0-w*w)/((2*D*omega0*w*w)*(2*D*omega0*w*w)+w*w*(omega0*omega0-w*w)*(omega0*omega0-w*w)));
		re[i]=(int)red[i];
		im[i]=(int)imd[i];
		f[i]=100*Math.log10(w);
		amplitude[i]=(int)(20*Math.log10(0.01*Math.sqrt(red[i]*red[i]+imd[i]*imd[i])));
		double zeiger=(imd[i]/red[i]);
		//if(zeiger>2*Math.PI) zeiger=zeiger-2*Math.PI;
		phase[i]=(int)(57.3*Math.atan(zeiger));
	}
	for(int i=1;i<=30;i++){
		if (amplitude[i]<0){
			phase_gef=i;
			break;
		}
	}
	for(int i=1;i<=30;i++){
		if (phase[i]<0){
			amp_gef=i;
			break;
		}
	}

	w=0.001;
	
	for(int i=0;i<1000;i++){
		w+=0.001;
		red[i]=(100*omega0*omega0*prop/15.0*(-1)*(2*D*omega0*w*w)/((2*D*omega0*w*w)*(2*D*omega0*w*w)+w*w*(omega0*omega0-w*w)*(omega0*omega0-w*w)));
		imd[i]=(100*omega0*omega0*prop/15.0*(-w)*(omega0*omega0-w*w)/((2*D*omega0*w*w)*(2*D*omega0*w*w)+w*w*(omega0*omega0-w*w)*(omega0*omega0-w*w)));
		re[i]=(int)red[i];
		im[i]=(int)imd[i];
		//f[i]=100*Math.log10(w);
		//amplitude[i]=(int)(20*Math.log10(Math.sqrt(red[i]*red[i]+imd[i]*imd[i])));
		//phase[i]=(int)Math.atan(imd[i]/red[i]);
	}
	if(darst_c.getSelectedIndex()==0)
	{
	g.setColor(Color.black);
	for(int i=0;i<999;i++){
		g.drawLine(300+re[i], 300-im[i], 300+re[i+1], 300-im[i+1]);
	}

	g.setColor(Color.blue);
	g.drawLine(150,300,450,300);
	px[0]=450;
	py[0]=303;
	px[1]=450;
	py[1]=297;
	px[2]=460;
	py[2]=300;
	g.fillPolygon(px,py,3);
	g.drawLine(295, 200, 305, 200);
	g.drawString("1", 287, 203);
	g.drawString("Realteil", 450, 315);
	g.drawLine(300, 150, 300, 550);
	px[0]=303;
	py[0]=160;
	px[1]=297;
	py[1]=160;
	px[2]=300;
	py[2]=150;
	g.fillPolygon(px,py,3);
	g.drawString("Imaginaerteil", 308, 160);
	g.drawLine(400, 295, 400, 305);
	g.drawString("1", 397, 290);
	g.setColor(Color.red);
	g.drawLine(190, 300, 210, 300);
	g.drawLine(200, 290, 200, 310);
	g.drawString("-1", 190,290);

	}
	if(darst_c.getSelectedIndex()==1)
	{
		g.setColor(Color.gray);
		g.drawLine(550+(int)f[1],190,550+(int)f[30],190);
		g.drawLine(550+(int)f[1],210,550+(int)f[30],210);
		g.drawLine(550+(int)f[1],230,550+(int)f[30],230);
		g.drawLine(550+(int)f[1],250,550+(int)f[30],250);
		g.drawLine(550+(int)f[1],270,550+(int)f[30],270);
		g.drawLine(550+(int)f[1],290,550+(int)f[30],290);

		g.drawString("20 dB",200+logw[300],190);
		g.drawString("10 dB",200+logw[300],210);
		g.drawString("0 dB",200+logw[300],230);
		g.drawString("-20 dB",200+logw[300],250);
		g.drawString("-40 dB",200+logw[300],270);
		g.drawString("-60 dB",200+logw[300],290);
		g.drawString("0°",230+logw[300],320);
		g.drawString("90°",230+logw[300],320+(int)(57.3*Math.PI/2));
		g.drawString("180°",230+logw[300],320+(int)(57.3*Math.PI));
		g.setColor(Color.blue);
		g.drawString("Amplitudengang",100,200);
		g.drawString("Phasengang",100,410);
		g.setColor(Color.gray);
		g.drawLine(550,320,550+(int)f[1],320);
		g.drawLine(550,500,550+(int)f[1],500);
		g.drawLine(550,590,550+(int)f[1],590);
		g.drawLine(550,410,550+(int)f[1],410);
		g.setColor(Color.red);
		for(int i=1;i<30;i++)
		{	
			g.drawLine(550+(int)f[i],230-amplitude[i],551+(int)f[i+1],230-amplitude[i+1]);
			
			g.drawLine(550+(int)f[i],500+(int)(-phase[i]),551+(int)f[i+1],500+(int)(-phase[i+1]));

			//g.drawLine(150+(int)(30*Math.log(w[i])),500+(int)(-10*A[i]),151+(int)(30*Math.log(w[i+1])),500+(int)(-10*A[i+1]));
			//g.drawLine(150+(int)(10*(w[i])),500+(int)(-10*A[i]),151+(int)(10*(w[i+1])),500+(int)(-10*A[i+1]));
			//g.drawString("Log:"+(int)(10*Math.log(w[50])),50,50);
			//g.drawString("A" +
			//		"Phi:"+(int)(phi[50]),50,70);
			
		}

		g.setColor(Color.gray);
		for (int i=1;i<31;i+=1)g.drawLine(550+(int)f[i],170,550+(int)f[i],600);
		g.drawString("1", 550, 610);
		g.drawString("0.1", 450, 610);
		g.drawString("0.01", 350, 610);
		g.drawString("0.001", 250, 610);
		/*for (int i=1;i<41;i+=1)
		{
			g.drawLine(550+logw[i],170,550+logw[i],500);
			
		}
		for (int i=10;i<100;i+=10)
		{
			g.drawLine(250+logw[i],170,250+logw[i],500);
		}
		for (int i=100;i<299;i+=100)
		{
			g.drawLine(250+logw[i],170,250+logw[i],500);
			
		}*/
		g.drawString("Omega",200+logw[1],510);
		g.setColor(Color.red);
		g.drawLine(550+(int)f[amp_gef], 230-amplitude[amp_gef], 550+(int)f[amp_gef],230);
		g.drawLine(550+(int)f[phase_gef],(int)(500- phase[phase_gef]), 550+(int)f[phase_gef],500);

	}
	g.setColor(Color.black);
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