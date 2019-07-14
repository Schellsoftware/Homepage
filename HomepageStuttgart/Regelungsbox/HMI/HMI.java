


import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import app.Com;
import app.Parameters;
import core.SerialPort;

public class HMI extends Frame {
	
	Com com; //Kommunikationsklasse
	BufferedWriter logfile;
	
	TextField servoField,distanzField,connectField;                             //Editierbare Felder in der Applikation
	Button servoButton = new Button("Servo");                           //Knöpfe in der Applikation, Instanz mit Beschriftung
	Button distanzButton = new Button("Distanz");
	Button ledButton = new Button("Led");
	Button tasterButton = new Button("Taster");
	Button connectButton=new Button("Connect");
	Checkbox taster1,taster2,led;                                        //Checkbox
	
	StringBuilder serialString;
	

	
	void init(){
		
		setLayout(null);
    	setBackground(new Color(230,230,230));
        setSize(500,200);
        setVisible(true);
        connectField=new TextField("COM4");
        connectField.setBounds(20,40,60,20);
        add(connectField);
        connectButton.setBounds(70,40,100,20);
        add(connectButton);
        
        servoField=new TextField("0");
        servoField.setBounds(20,70,60,20);
        add(servoField);
		servoButton.setBounds(80,70,80,20);
		add(servoButton);
		distanzField = new TextField("0");
		distanzField.setBounds(20,100,60,20);
		add(distanzField);
		distanzButton.setBounds(80,	100,80,20);
		add(distanzButton);
		ledButton.setBounds(230,70,80,20);
		add(ledButton);
		tasterButton.setBounds(350, 100, 80, 20);
		add(tasterButton);
		
		led=new Checkbox();
		led.setBounds(170, 70, 20, 20);
		add (led);
		taster1=new Checkbox();
		taster1.setBounds(170, 100, 20, 20);
		add (taster1);
		taster2=new Checkbox();
		taster2.setBounds(270, 100, 20, 20);
		add (taster2);
		
		serialString=new StringBuilder();
		
		addWindowListener( new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
            	try{
            		logfile.close();
            	} 
            	catch (Exception exc){
    				try{
    					logfile.write(e.toString());
    					logfile.newLine();
    				}catch(Exception exp){
    					
    				}
            	}
                System.exit(0);
            }
        } );
		
	    // Schreibe alle Elemente als Textzeilen in die Datei:
	    try {
	      logfile = new BufferedWriter(
	                           new OutputStreamWriter(
	                           new FileOutputStream( "Logfile.txt" ) ) );
	        logfile.write("logfile.txt");
	        logfile.newLine();
	       
	    } catch( IOException ex ) {
	      System.out.println( ex );
			try{
				logfile.write(ex.toString());
				logfile.newLine();
			}catch(Exception exp){		
			}
	    }

		try{		
		SerialPort serialPort = new SerialPort();
		Parameters parameters = new Parameters();
		parameters.setPort("COM4");
		logfile.write("Serielle Schnittstelle: COM4");
		logfile.newLine();
		parameters.setBaudRate("9600");
		logfile.write("Bautrate: 9600");
		logfile.newLine();
		
		com = new Com(parameters);
		} catch (Exception e) {
			e.printStackTrace();
			try{
				logfile.write(e.toString());
				logfile.newLine();
			}catch(Exception exp){
			}
		}
	}

	
	public void paint(Graphics g)
	{
		 g.drawString("Led", 195, 85);
		 g.drawString("Taster 1", 195, 115);
		 g.drawString("Taster 2", 295, 115);

	}
	
	public boolean action(Event event, Object eventObject){
		if ((event.target == servoButton)){
			String servoString=new String();
			servoString="S"+servoField.getText();
			sendData(servoString);
			return true;
		}
		else if ((event.target == connectButton)){
			try{		
				Parameters parameters = new Parameters();
				parameters.setPort(connectField.getText());
				logfile.write("Port: "+connectField.getText());
				logfile.newLine();
				System.out.println(connectField.getText());
				parameters.setBaudRate("9600");
				logfile.write("Baurate: 9600");
				logfile.newLine();
				com = new Com(parameters);
			} catch (Exception e) {
				e.printStackTrace();
				try{
					logfile.write(e.toString());
					logfile.newLine();
				}catch(Exception exp){
					
				}
				
			}
			return true;
		}
		else if ((event.target == distanzButton)){
			String serielEingang=new String();
			String input;
			sendData("A");
			serielEingang=receiveData('A');
			input=serielEingang.substring(1);
			distanzField.setText(input);
			return true;
		}
		else if ((event.target == ledButton)){//Bei Drücken des ledButton
			if(led.getState()==true)sendData("D5");//Schalte Led an
			else sendData("D6");//Schalte Led aus
			return true;
		}
		else if ((event.target == tasterButton)){
			sendData("D2");//Taster 2 soll ausgelesen werden
			String tasterString1=receiveData('D');//Zustand des Tasters 2
			if(tasterString1.charAt(1)=='0') taster1.setState(true);//Checkbox wird markiert
			else taster1.setState(false);//Keine Markierung des Checkbox
			sendData("D3");//Taster 3 soll ausgelesen werden
			String tasterString2=receiveData('D');//Zustand des Tasters 3
			if(tasterString2.charAt(1)=='0') taster2.setState(true);//Checkbox wird markiert
			else taster2.setState(false);//Keine Markierung des Checkbox
			return true;
		}
		else return false;
	}
	
public void sendData(String data){	
	try{
		  for (int i = 0; i < data.toCharArray().length; i++) {//Sende den Sting
			  com.sendSingleData(data.toCharArray()[i]);//Buchstabe für Buchstabe wird gesendet
		  }
		  com.sendSingleData(13);//Der String endet mit Zeilenumbruch
		  logfile.write("Sende:"+data);//Im logfile wird gespeichert
		  logfile.newLine();
		  
	}
	catch(Exception e){//Wenn nicht gesendet werden kann
			e.printStackTrace();//Bei Consolenanwendungen wird auf die Konsole gescrieben
			try{
				logfile.write(e.toString());//Fehler wird ins Logfile geschrieben
				logfile.newLine();
			}catch(Exception exp){			
			}
	}
}
public String receiveData(char kommand){
	String buchstabe=new String();
	try{
		serialString.delete(0, serialString.length());//Der String wird gelöscht
		while(!buchstabe.equalsIgnoreCase("\n")){//Solange kein Zeilenumbruch kommt
			buchstabe=com.receiveSingleString();//Neuer Buchstabe wird empfangen
			if(buchstabe.length()>0) {//Wenn der String Buchstabe nicht leer ist
				serialString.append(buchstabe);//Buchstabe wird angehängt
			}
		}
		logfile.write("Empfangen:"+serialString.toString());//Empfangener String
	}	
	catch(Exception e){
		e.printStackTrace();//Fehler wird auf Console geschrieben
		try{
			logfile.write(e.toString());//In die logfile Datei wird geschrieben
			logfile.newLine();
		}catch(Exception exp){			
		}
	}
	return serialString.toString();	
}
		
	
	public static void main(String[] args) {				

		HMI hmi=new HMI();
		hmi.init();
	}
}
