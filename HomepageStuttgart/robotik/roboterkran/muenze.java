import java.awt.*;
import java.applet.Applet;
//import java.applet.AudioClip;
import java.awt.image.*;
import java.lang.Math;
//import java.net.*;
//import java.io.*;


public class muenze extends Applet implements Runnable
{
	cGrau_Bild g_m_Bild, g_o_Bild;
	cBinaer_Bild binaer_Bild;



	//AudioClip onceClip;

	private Thread	 m_thread = null;
	
	//Image grauBild, binaerBild, resultBild;

	Math m;
	String bildName;
	int zustand=1;
	long zeit=1000;
	Image ohne_Muenze, mit_Muenze, anfahr_bild, aufnehm_bild, abfahr_bild, skala;
	int omPunkte[],mmPunkte[];
	int breite=320, hoehe=240;
	boolean bildGueltig=false;
	int aus_x,aus_y,aus_b,aus_h;
	TextField text=new TextField("190");
	int schwelle=0;
	int flaeche=0, xs=0, ys=0;

	cSensor sensor;
	cPunkt punkt;

	Color grh[]=new Color[255];
	int grauwerte[]=new int[255];

	int yh=0,xh=0;
	//URL url;
	boolean url_gueltig=false;
	boolean init=false;
	boolean verfahren=false;
	boolean falscher_Schwellwert=false;


	public void init()
	{
		setLayout(new FlowLayout());
		add(text);

		xs=0;
		ys=0;

		Integer x,y,b,h;
		x=new Integer(getParameter("Ausschnitt_x"));
		y=new Integer(getParameter("Ausschnitt_y"));
		b=new Integer(getParameter("Ausschnitt_b"));
		h=new Integer(getParameter("Ausschnitt_h"));
		aus_x=x.intValue();
		aus_y=y.intValue();
		aus_b=b.intValue();
		aus_h=h.intValue();
		laden();
		if(aus_b>breite) aus_b=breite;
		if(aus_h>hoehe) aus_h=hoehe;

		g_o_Bild=new cGrau_Bild(aus_b,aus_h);
		g_m_Bild=new cGrau_Bild(aus_b,aus_h);
		binaer_Bild=new cBinaer_Bild(aus_b,aus_h);

		sensor=new cSensor(29,0,39,266);
		
		Integer wert=new Integer(text.getText());
		schwelle=wert.intValue();


		for (int i=0;i<255 ;i++ )
		{
			grh[i]=new Color(i,i,i);
			grauwerte[i]=grh[i].getRGB();
		}
		skala=createImage(new MemoryImageSource(255,1,grauwerte,0,255));

		laden();
		//if(bildGueltig){
			erzeugeBild();
			grauBild();
		//}
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
			//laden();
			//erzeugeBild();
			//grauBild();
			//binaerBild();

			//berechne();
			if(bildGueltig)
			{
				switch(zustand)
				{
					case 1:
						binaerBild();
						berechne();
						zustand=2;
						zeit=2500;
						break;
					case 2:
						binaerBild();
						berechne();
						if((flaeche!=0)&&(!falscher_Schwellwert)) zustand=3;
						else zustand=1;
						zeit=500;
						break;
					case 3:
						zustand=4;
						break;
					case 4:
						zustand=5;
						break;
					case 5:
						zustand=1;
						break;
				}
			}

			repaint();

			try
			{	
				Thread.sleep(zeit); 
			}
			catch (InterruptedException e)
			{
				stop();
			}
			
		}
	}



	public void paint(Graphics g)
	{
			if(bildGueltig)
			{
				g.setColor(Color.black);
				g.drawString("Schwellwert: ",90,20);
				g.drawString("Histogramm: ",40,320);
				
				

				switch(zustand)
				{
					case 1:
						g.drawImage(ohne_Muenze,0,30,346,259,this);
						g.setColor(Color.black);
						for (int i=0;i<255 ;i++ )
						{
							g.drawLine(10+i,400,10+i,400-g_o_Bild.histogramm[i]);
						}
						g.drawImage(skala,10,410,255,1,this);
						g.setColor(Color.red);
						g.drawLine(schwelle+10,410,schwelle+10,400);
						if (falscher_Schwellwert)
						{
							g.setColor(Color.black);
							g.drawString("Der Schwellwert ist zu Hoch",200,20);
						}
						break;
					case 2:
						g.drawImage(mit_Muenze,0,30,346,259,this);
						g.setColor(Color.black);
						for (int i=0;i<255 ;i++ )
						{
							g.drawLine(10+i,400,10+i,400-g_m_Bild.histogramm[i]);
						}
						g.drawImage(skala,10,410,255,1,this);
						g.setColor(Color.red);
						g.drawLine(schwelle+10,410,schwelle+10,400);
						if(((xs!=0)||(ys!=0))&&(!falscher_Schwellwert))
						{
							g.setColor(Color.black);
							g.drawString("Position: ",200,20);
							Integer xs_h=new Integer ((int)punkt.x);//(aus_x+xs);
							g.drawString("Xs: "+xs_h.toString(),245,20);
							Integer ys_h=new Integer ((int)punkt.y);//(aus_y+ys);
							g.drawString("Ys: "+ys_h.toString(),290,20);
							g.setColor(Color.red);
							g.drawOval(aus_x+xs-5,aus_y+ys-5+30,10,10);
						}
						if (falscher_Schwellwert)
						{
							g.setColor(Color.black);
							g.drawString("Der Schwellwert ist zu Hoch",200,20);
						}
						break;
					case 3:
						g.drawImage(anfahr_bild,0,30,346,259,this);
						if(((xs!=0)||(ys!=0))&&(!falscher_Schwellwert))
						{
							g.setColor(Color.black);
							g.drawString("Position: ",200,20);
							Integer xs_h=new Integer ((int)punkt.x);//(aus_x+xs);
							g.drawString("Xs: "+xs_h.toString(),245,20);
							Integer ys_h=new Integer ((int)punkt.y);//(aus_y+ys);
							g.drawString("Ys: "+ys_h.toString(),290,20);
							g.setColor(Color.red);
							g.drawOval(aus_x+xs-5,aus_y+ys-5+30,10,10);
						}
						break;
					case 4:
						g.drawImage(aufnehm_bild,0,30,346,259,this);
						if(((xs!=0)||(ys!=0))&&(!falscher_Schwellwert))
						{
							g.setColor(Color.black);
							g.drawString("Position: ",200,20);
							Integer xs_h=new Integer ((int)punkt.x);//(aus_x+xs);
							g.drawString("Xs: "+xs_h.toString(),245,20);
							Integer ys_h=new Integer ((int)punkt.y);//(aus_y+ys);
							g.drawString("Ys: "+ys_h.toString(),290,20);
							g.setColor(Color.red);
							g.drawOval(aus_x+xs-5,aus_y+ys-5+30,10,10);
						}
						break;
					case 5:
						g.drawImage(abfahr_bild,0,30,346,259,this);
						break;
				}
				g.setColor(Color.blue);
				g.drawRect(aus_x,aus_y+30,aus_b,aus_h);

			//	Integer fl=new Integer (flaeche);
			//		g.setColor(Color.black);
			//		g.drawString("Fläche: "+fl.toString(),40,340);

			}
			else
			{
				g.drawString("Konnte bild nicht laden",0,10);
			}
	}
	
	
	private void laden()
	{

			MediaTracker tracker = new MediaTracker(this);//Überwacht das Laden des Bildes
			//ohne_Muenze, mit_Muenze, anfahr_bild, aufnehm_bild, abfahr_bild
			ohne_Muenze = getImage(getDocumentBase(),"Bild1.jpg");//Producer für das Bild
			mit_Muenze = getImage(getDocumentBase(),"Bild2.jpg");
			anfahr_bild = getImage(getDocumentBase(),"Bild3.jpg");
			aufnehm_bild = getImage(getDocumentBase(),"Bild4.jpg");
			abfahr_bild = getImage(getDocumentBase(),"Bild5.jpg");
			tracker.addImage(ohne_Muenze,0);
			tracker.addImage(mit_Muenze,1);
			tracker.addImage(anfahr_bild,2);
			tracker.addImage(aufnehm_bild,3);
			tracker.addImage(abfahr_bild,4);
			try//Versuche das Bild zu laden
			{
				tracker.waitForID(0);
				tracker.waitForID(1);
				tracker.waitForID(2);
				tracker.waitForID(3);
				tracker.waitForID(4);
				bildGueltig=true;
			}
			catch (Exception e)
			{
				bildGueltig=false;
			}
			//if (bildGueltig)
			//{
			breite=346;//ohne_Muenze.getWidth(this);
			hoehe=259;//ohne_Muenze.getHeight(this);
			//}
	}
	
	private void erzeugeBild()
	{
		omPunkte=new int[breite*hoehe];//Die Bildpunkte werden in dieses Array geladen 

		PixelGrabber grabber = new PixelGrabber(ohne_Muenze.getSource(),aus_x,aus_y,aus_b,aus_h,omPunkte,0,aus_b);//Konsumer für das Bild
		try
		{
			grabber.grabPixels();
		}
		catch (Exception e)
		{
			return;
		}
		if ((grabber.status() & ImageObserver.ABORT) != 0)
		{
			return;
		}

		mmPunkte=new int[breite*hoehe];
		PixelGrabber grabber2 = new PixelGrabber(mit_Muenze.getSource(),aus_x,aus_y,aus_b,aus_h,mmPunkte,0,aus_b);//Konsumer für das Bild
		try
		{
			grabber2.grabPixels();
		}
		catch (Exception e)
		{
			return;
		}
		if ((grabber.status() & ImageObserver.ABORT) != 0)
		{
			return;
		}

	}

	void grauBild()
	{
		int zeile,spalte;

		for(int i=0;i<(aus_b*aus_h);i++)
		{
			zeile=i/aus_b;
			spalte=i%aus_b;
			g_o_Bild.matrix[spalte][zeile]=omPunkte[i]&0xFF;
			g_m_Bild.matrix[spalte][zeile]=mmPunkte[i]&0xFF;
		}			
		g_m_Bild.init=true;
		g_o_Bild.init=true;
		g_o_Bild.ermittle_Histogramm();
		g_m_Bild.ermittle_Histogramm();
	}

	void binaerBild()
	{
		int zeile,spalte;

		Integer wert=new Integer(text.getText());
		schwelle=wert.intValue();

		if (g_m_Bild.init==true)
		{
				for(int i=0;i<(aus_b*aus_h);i++)
				{
					zeile=i/aus_b;
					spalte=i%aus_b;
					if (g_m_Bild.matrix[spalte][zeile]>=schwelle)
					{
						binaer_Bild.matrix[spalte][zeile]=0;
					}
					else
					{
						binaer_Bild.matrix[spalte][zeile]=1;
					}
				}
				binaer_Bild.init=true;			
		}
	}



	void berechne()
	{			
		int zeile,spalte;
		//Fläche berechnen
		flaeche=0;
		xs=0;
		ys=0;

		if (binaer_Bild.init==true)
		{
		
			for(int i=0;i<(aus_b*aus_h);i++)
			{
				zeile=i/aus_b;
				spalte=i%aus_b;
				if (binaer_Bild.matrix[spalte][zeile]==1)
				{
					flaeche++;
					xs+=spalte;
					ys+=zeile;
				}
			}
			if (flaeche!=0)
			{
				xs=xs/flaeche;
				ys=ys/flaeche;
				//repaint();
				xh=aus_x+xs-160;
				yh=120-aus_y-ys;
				if (flaeche<149)
				{
					punkt=sensor.erm_punkt(xh,yh);
				}
				else 
				{
					xs=0;
					ys=0;
				}
			}
			if (flaeche<149) falscher_Schwellwert=false;
			else falscher_Schwellwert=true;
		}
	}
}

class cBild
{
	int anz_Spalten, anz_Zeilen;
	boolean init;
	int matrix[][]; 
};

class cGrau_Bild extends cBild
{
	int histogramm[];
	
	cGrau_Bild(int breite, int hoehe)
	{
		anz_Spalten=breite;
		anz_Zeilen=hoehe;
		init=false;
		histogramm= new int[255];
		matrix=new int [breite][hoehe];
	}

	void ermittle_Histogramm()
	{
		int zeile,spalte;

		for (int i=0;i<255 ;i++ )
		{
			histogramm[i]=0;
		}
		for(int i=0;i<(anz_Spalten*anz_Zeilen);i++)
		{
			zeile=i/anz_Spalten;
			spalte=i%anz_Spalten;
			if((matrix[spalte][zeile]>=0)&&(matrix[spalte][zeile]<255))	histogramm[matrix[spalte][zeile]]++;
		}

		for(int i=0;i<255;i++)
		{
			if (histogramm[i]>100)
			{
				histogramm[i]=100;
			}
		}
	}
};

class cBinaer_Bild extends cBild
{
	cBinaer_Bild(int breite, int hoehe)
	{
		anz_Spalten=breite;
		anz_Zeilen=hoehe;
		init=false;
		matrix=new int [breite][hoehe];
		
	}
};

class cSensor
{
	Math m;
	double pos_x, pos_y, pos_z, beta, brennweite;

	cSensor(double x,double y,double z,double br)
	{
		pos_x=x;
		pos_y=y;
		pos_z=z;
		brennweite=br;
		beta=m.atan(pos_x/pos_z);
	}
	cPunkt erm_punkt(int x, int y)
	{
		cPunkt p=new cPunkt();
		p.y=pos_z*m.tan(m.atan((double)y/brennweite)+beta)-pos_x;	
		p.x=m.sqrt((pos_x+p.y)*(pos_x+p.y)+pos_z*pos_z)*(double)x/brennweite;
		p.z=0;
		return p;
	}

};

class cPunkt
{
	public double x,y,z;
	cPunkt(double xh, double yh,double zh)
	{
		x=xh;
		y=yh;
		z=zh;
	}
	cPunkt()
	{
		x=0;
		y=0;
		z=0;
	}
};






