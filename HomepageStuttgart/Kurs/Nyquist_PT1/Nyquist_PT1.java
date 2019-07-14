import java.lang.*;
import java.awt.*;
import java.applet.*;


public class Nyquist_PT1 extends Applet implements Runnable
{	
	private Thread	 m_thread = null;
	int zeit;
	Image ausgabe;
	Graphics g;
	int sizex,sizey;
	
	int i;
	int zustand = 0;
	int px[]=new int[3];
	int py[]=new int[3];
	// Sinus
	double sinus [] = new double[361];
	int sinus_i [] = new int [361];
	int periode[]=new int[361];
	int pos_sin_x, pos_sin_y;
	// Kreis
	int pos_kreis_x, pos_kreis_y;
	int sinus_k [] = new int [361];
	int cosinus_k [] = new int [361];

	double sinusp [] = new double[361];
	int sinusp_i [] = new int [361];
	int sinusp_k [] = new int [361];
	int cosinusp_k [] = new int [361];
	Polygon sinusp_poly,kreisp_poly;
	Polygon sinus_poly,kreis_poly;
	int sin_p1x[] = new int [3];
	int sin_p1y[] = new int [3];
	int sin_p2x[] = new int [3];
	int sin_p2y[] = new int [3];
	int kreis_p1x[] = new int [3];
	int kreis_p1y[] = new int [3];
	int kreis_p2x[] = new int [3];
	int kreis_p2y[] = new int [3];
	Math m;
	Choice darst_c;
	double w[];
	int logw[];
	double A[];
	int logA[];
	double phi[];
	double omega;
	int anw = 25;
	
	public void init()
	{
		ausgabe=null;
		g=null;
		resize(700, 600);
		int sx=size().width;
		int sy=size().height;
		if(sx!=sizex || sy!=sizey){
			sizex=sx;
			sizey=sy;
			ausgabe=createImage(sx,sy);
			g=ausgabe.getGraphics();
		}
		setLayout(null);
		darst_c=new Choice();
		darst_c.add("Nyquist-Diagramm");
		darst_c.add("Frequenzgang");	
		darst_c.setBounds(150,330,130,20);
		add(darst_c);
		A=new double[301];
		w=new double[301];
		phi = new double[301];
		logw=new int[301];
		logA =new int [301];
		
		pos_sin_x = 300;
		pos_sin_y = 160;
		pos_kreis_x = 130;
		pos_kreis_y = 160;
		zeit = 0;

		for (int i=0;i<361;i++)
		{
			// Sinus

			sinus[i]= m.sin(2*m.PI*i/360);
			sinus_i[i] = (int) m.round((float)(pos_sin_y - 100 *sinus[i]));
			periode[i] = pos_sin_x + i;
			sinus_poly = new Polygon (periode, sinus_i, 361);

			// Kreis
			sinus_k[i] = (int) m.round((float)(pos_kreis_y - 100 *sinus[i]));
			cosinus_k[i] = (int) m.round((float)(pos_kreis_x + 100  * m.cos(2*m.PI*i/360)));
			kreis_poly = new Polygon(cosinus_k, sinus_k, 361);

			sinusp[i]= m.sin(2*m.PI*i/360.0-m.PI/4);
			sinusp_i[i] = (int) m.round((float)(pos_sin_y - 70 *sinusp[i]));
			sinusp_poly = new Polygon (periode, sinusp_i, 361);

			// Kreis
			sinusp_k[i] = (int) m.round((float)(pos_kreis_y - 70 *sinusp[i]));
			cosinusp_k[i] = (int) m.round((float)(pos_kreis_x + 70  * m.cos(2*m.PI*i/360.0-m.PI/4)));
			kreisp_poly = new Polygon(cosinusp_k, sinusp_k, 361);
		}
		for (i=0;i<301;i++)
		{
			if (i==0){
				w[i]=1000;
			}
			else
			{
				w[i]=2*Math.PI/i;
			}
			logw[i]=(int)(100*(1/Math.log(10))*Math.log(w[i]));
			
			phi[i] = -Math.atan(w[i]);
			
			
			A[i]=(1/Math.sqrt((1+w[i]*w[i])));
			logA[i]=(int)(-20*(1/Math.log(10))*Math.log(A[i]));
		}
		
	}

		public void start()
	{
		if (m_thread == null)
		{
			m_thread = new Thread(this);
			m_thread.start();
		}
	}
	
	public void stop()
	{
		if (m_thread != null)
		{
			m_thread.stop();
			m_thread = null;
		}
	}



	public void run()
	{
		while (true)
		{
			grafik_vorbereiten();
			repaint();
			zeit ++;
			if (zeit > 360)
			{
				zeit =0;
				zustand++;
				if(zustand>4) zustand = 0;
			}
			switch(zustand){
				case 0: anw = 90;
					break;
				case 1: anw = 20;
					break;
				case 2: anw = 6;
					break;
				case 3: anw = 3;
					break;
				case 4: anw = 1;
			}
			try
			{	
				Thread.sleep(30); 
			}
			catch (InterruptedException e)
			{
				stop();
			}
		}
	}
	public void update (Graphics g1)
	{
		g1.drawImage(ausgabe, 0, 0, this);
	}
	
	public void grafik_vorbereiten()
	//public void update (Graphics g) 
	//public void paint (Graphics g)
	{
		for (int i=0;i<361;i++)
		{
			sinusp[i]= m.sin(2*m.PI*i/360.0+phi[anw]);
			sinusp_i[i] = (int) m.round((float)(pos_sin_y - 100*A[anw] *sinusp[i]));
			sinusp_poly = new Polygon (periode, sinusp_i, 361);

			// Kreis
			sinusp_k[i] = (int) m.round((float)(pos_kreis_y - 100*A[anw] *sinusp[i]));
			cosinusp_k[i] = (int) m.round((float)(pos_kreis_x + 100*A[anw] * m.cos(2*m.PI*i/360.0+phi[anw])));
			kreisp_poly = new Polygon(cosinusp_k, sinusp_k, 361);
		}
		int wgefunden = 0;

		if (zeit != 0)
		{
			//g.clearRect(cosinus_k[zeit-1]-4, sinus_k[zeit-1]-4, 8,8);
			g.clearRect(periode[zeit-1]-4, sinus_i[zeit-1]-4, 8,8);
			g.clearRect(periode[zeit-1]-4, sinusp_i[zeit-1]-4, 8,8);
		}
		else
		{
			//g.clearRect(cosinus_k[360]-4, sinus_k[360]-4, 8,8);
			g.clearRect(periode[360]-4, sinus_i[360]-4, 8,8);
			g.clearRect(periode[360]-4, sinusp_i[360]-4, 8,8);
		}
		g.clearRect(pos_kreis_x-120, pos_kreis_y-120, 240,240);
		g.clearRect(0,0,700,800);
		g.setColor(Color.black);
		g.drawPolygon(sinus_poly);
		g.setColor(Color.green);
		g.drawPolygon(kreisp_poly);
		for (int i=0;i<360 ;i++ )
				{
					g.drawLine(periode[i], sinusp_i[i], periode[i+1], sinusp_i[i+1]);
				}
		g.setColor(Color.black);
		g.drawPolygon(kreis_poly);
		g.drawLine(pos_kreis_x,pos_kreis_y,cosinus_k[zeit],sinus_k[zeit]);
		g.setColor(Color.green);
		g.drawLine(pos_kreis_x,pos_kreis_y,cosinusp_k[zeit],sinusp_k[zeit]);
		g.setColor(Color.red);
		g.fillOval(cosinus_k[zeit]-4, sinus_k[zeit]-4, 8,8);
		g.fillOval(cosinusp_k[zeit]-4, sinusp_k[zeit]-4, 8,8);
		g.fillOval(periode[zeit]-4, sinus_i[zeit]-4, 8,8);
		g.fillOval(periode[zeit]-4, sinusp_i[zeit]-4, 8,8);
		//g.setColor(Color.green);
		//g.fillPolygon(sinus_poly);
		
		g.setColor(Color.blue);
		g.drawLine(pos_sin_x,pos_sin_y,pos_sin_x+380,pos_sin_y);
		g.drawLine(pos_sin_x,pos_sin_y-110,pos_sin_x,pos_sin_y+110);

		sin_p1x[0] = pos_sin_x+390;
		sin_p1y[0] = pos_sin_y;
		sin_p1x[1] = pos_sin_x+380;
		sin_p1y[1] = pos_sin_y-3;
		sin_p1x[2] = pos_sin_x+380;
		sin_p1y[2] = pos_sin_y+3;
		g.fillPolygon(sin_p1x, sin_p1y, 3);
		sin_p2x[0] = pos_sin_x;
		sin_p2y[0] = pos_sin_y-120;
		sin_p2x[1] = pos_sin_x+3;
		sin_p2y[1] = pos_sin_y-110;
		sin_p2x[2] = pos_sin_x-3;
		sin_p2y[2] = pos_sin_y-110;
		g.fillPolygon(sin_p2x, sin_p2y, 3);
		for (int i=-100;i<=100 ;i=i+25)
		{

			g.drawLine(pos_sin_x+2,pos_sin_y+i,pos_sin_x-2,pos_sin_y+i);
			Float d1 = new Float (-(float)i/100);
			g.drawString(d1.toString(),pos_sin_x-30,pos_sin_y+i+5);

		}
		for (int i=45;i<=360 ;i=i+45)
		{
			g.drawLine(pos_sin_x+i,pos_sin_y+2,pos_sin_x+i,pos_sin_y-2);
			Integer d2 = new Integer (i);
			g.drawString(d2.toString(),pos_sin_x+i-10,pos_sin_y+15);
		}
		g.drawString("u",pos_sin_x-10, pos_sin_y-110);
		g.drawString("t",pos_sin_x+380, pos_sin_y+20);
		g.setColor(Color.blue);
		//g.fillPolygon();
		//g.fillPolygon();
		g.drawLine(pos_kreis_x-110,pos_kreis_y,pos_kreis_x+110,pos_kreis_y);
		g.drawLine(pos_kreis_x,pos_kreis_y+110,pos_kreis_x,pos_kreis_y-110);
		kreis_p1x[0] = pos_kreis_x;
		kreis_p1y[0] = pos_kreis_y-120;
		kreis_p1x[1] = pos_kreis_x-3;
		kreis_p1y[1] = pos_kreis_y-110;
		kreis_p1x[2] = pos_kreis_x+3;
		kreis_p1y[2] = pos_kreis_y-110;
		g.fillPolygon(kreis_p1x, kreis_p1y, 3);
		kreis_p2x[0] = pos_kreis_x+120;
		kreis_p2y[0] = pos_kreis_y;
		kreis_p2x[1] = pos_kreis_x+110;
		kreis_p2y[1] = pos_kreis_y+3;
		kreis_p2x[2] = pos_kreis_x+110;
		kreis_p2y[2] = pos_kreis_y-3;
		g.fillPolygon(kreis_p2x, kreis_p2y, 3);
		for (int i=-100;i<=100 ;i=i+50)
		{
			g.drawLine(pos_kreis_x+i,pos_kreis_y+2,pos_kreis_x+i,pos_kreis_y-2);
			Float d3 = new Float ((float)i/100);
			g.drawString(d3.toString(),pos_kreis_x+i-10,pos_kreis_y+15);
		}
		for (int i=-100;i<=100 ;i=i+50)
		{

			g.drawLine(pos_kreis_x+2,pos_kreis_y+i,pos_kreis_x-2,pos_kreis_y+i);
			Float d4 = new Float (-(float)i/100);
			g.drawString(d4.toString(),pos_kreis_x-30,pos_kreis_y+i+5);
		}
		
		g.drawString("u",pos_kreis_x-10, pos_kreis_y-110);
		g.drawString("x",pos_kreis_x+110, pos_kreis_y+20);
		if(darst_c.getSelectedIndex()==1)
		{
			g.setColor(Color.gray);
			g.drawLine(250+logw[1],400,250+logw[300],400);
			g.drawLine(250+logw[1],420,250+logw[300],420);
			g.drawLine(250+logw[1],440,250+logw[300],440);
			g.drawLine(250+logw[1],500,250+logw[300],500);
			g.drawLine(250+logw[1],500+(int)(57.3*Math.PI/2),250+logw[300],500+(int)(57.3*Math.PI/2));
			//g.drawLine(250+logw[1],500+(int)(57.3*Math.PI),250+logw[300],500+(int)(57.3*Math.PI));
			g.drawString("0 dB",250+logw[300],400);
			g.drawString("20 dB",250+logw[300],420);
			g.drawString("40 dB",250+logw[300],440);
			g.drawString("0°",250+logw[300],500);
			g.drawString("90°",250+logw[300],500+(int)(57.3*Math.PI/2));
			//g.drawString("180°",250+logw[300],500+(int)(57.3*Math.PI));
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
				g.drawLine(250+logw[i],350,250+logw[i],600);
			}
			for (i=10;i<100;i+=10)
			{
				g.drawLine(250+logw[i],350,250+logw[i],600);
			}
			for (i=100;i<299;i+=100)
			{
				g.drawLine(250+logw[i],350,250+logw[i],600);
			}
			g.drawString("Omega",200+logw[1],710);

					g.setColor(Color.blue);
					g.drawOval(245+logw[anw],395+logA[anw],10,10);
					g.drawOval(245+logw[anw],495+(int)(-57.3*phi[anw]),10,10);

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
			//for(i=300;i>=0;i--)
			//{
				//if((wgefunden==0)&&(w[i]>omega))
				//{
				//	wgefunden=1;
					g.setColor(Color.blue);
					g.drawOval((int)(195+50*A[anw]*Math.cos(phi[anw])),(int)(445-50*A[anw]*Math.sin(phi[anw])),10,10);
				//}
			//}
		}
	}
}


