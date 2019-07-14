import java.applet.Applet;
import java.applet.AudioClip;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


/*
 * Created on 11.03.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author Harald
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class zwurscht extends Applet {
	Image wurschtImage;
	int mouseX=100,mouseY=100;
	AudioClip schuss,aua;
	public void init(){
		schuss = getAudioClip(getDocumentBase(),"schuss.wav");
		aua = getAudioClip(getDocumentBase(),"aua.wav");
		wurschtImage= getImage(getCodeBase(),"zwurscht.jpg");
		MediaTracker mt = new MediaTracker(this);
		mt.addImage(wurschtImage,0);
		try {
			mt.waitForAll();
		} catch (InterruptedException e){
			;
		}
		resize(328,250);

		//
		setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
		addMouseListener(new MouseAdapter()
				{
					public void mousePressed(MouseEvent e){
						mouseX=e.getX();
						mouseY=e.getY();
						Graphics g = getGraphics();
						g.fillOval(mouseX-10,mouseY-10,20,20);
						g.dispose();
						schuss.play();
						if((mouseX>125)&&(mouseX<215)&&(mouseY>30)&&(mouseY<225)){
							aua.play();
						}
					}
				}
				);
		
	}
	public void paint (Graphics g1)
	{
		g1.drawImage(wurschtImage, 0, 0, this);
		//g1.setColor(Color.red);
		//g1.drawRect(125,30,90,195);
		//g1.drawImage(zu, 0, 0, this);
	}

}
