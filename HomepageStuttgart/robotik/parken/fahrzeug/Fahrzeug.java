/*
 * Created on 07.10.2007
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package fahrzeug;

import java.awt.Color;
import java.awt.Graphics;

/**
 * @author Harald
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class Fahrzeug {
	double x; // x-Position
	double y; // y-Position
	double psi; // Gierwinkel
	double delta; // Lenkwinkel
	double v; // Geschwindigkeit
	
	double skal = 40;
	double kr; //Rückführung des Abstandes zur Sollbahn
	double kpsi; // Rückführung des Winkels zur Sollbahn
	
	public Fahrzeug(double x_startpos,double y_startpos,double psi_startpos){
		double a12;
		double b2 ;
		double p=1;
		double k12,k22,k11;
		double q11=1;
		double q22=1;
		double radabstand = 1.8;
		a12 = 2;
		b2 = a12/radabstand;
		k12=Math.sqrt(b2*p*q11)/(b2*b2*p);
		k22=Math.sqrt((2*a12*k12+q22)/(b2*b2*p));
		k11=(b2*b2*k12*k22*p)/a12;
		kr = p*k12*b2;
		kpsi = p*k22*b2;
		
		x=x_startpos;
		y=y_startpos;
		psi=psi_startpos;
		
		
	}
	
	public void fahre(double t){
		double l=1.8;
		double psi0;
 		if ((delta < 0.001)&&(delta > -0.001)){	// Gerade fahren		
			x = x + v * Math.cos(psi)*t;			
			y = y - v * Math.sin(psi)*t;
		}
		else{ //Kreisfahrt
			psi0=psi;
			psi = v*t/l*Math.tan(delta)+psi0;
			x = (l/Math.tan(delta))*(Math.sin(psi)-Math.sin(psi0))+x;
			y = (l/Math.tan(delta))*(Math.cos(psi)-Math.cos(psi0))+y;
		}
	}

	public void regle(double abstandsfehler, double winkelfehler){
		delta = -(kr*abstandsfehler+kpsi * winkelfehler);
		if(delta>(60.0/180.0*Math.PI)){
			delta = 60.0/180.0*Math.PI;
		}
		if(delta<(-60.0/180.0*Math.PI)){
			delta = -60.0/180.0*Math.PI;
		}
	}
	public void zeichne(Graphics g){
		double reifenbreite = 0.1;
		double reifenhoehe = 0.3;
		double reifenentfernung = 0.6;
		int fzgex[]=new int[4];
		int fzgey[]=new int[4];
		double radabstand = 1.8;
		double uev = 0.6;
		double ueh = 0.6;
		double bhalbe = 0.8;
		g.setColor(Color.black);
		
		fzgex[0]=(int) (x*skal+(radabstand +uev)*Math.cos(psi)*skal+bhalbe * Math.sin(psi)*skal);
		fzgey[0]=(int) (y*skal-(radabstand +uev)*Math.sin(psi)*skal+bhalbe * Math.cos(psi)*skal);
		fzgex[1]=(int) (x*skal+(radabstand +uev)*Math.cos(psi)*skal-bhalbe * Math.sin(psi)*skal);
		fzgey[1]=(int) (y*skal-(radabstand +uev)*Math.sin(psi)*skal-bhalbe * Math.cos(psi)*skal);
		fzgex[2]=(int) (x*skal+(-ueh)*Math.cos(psi)*skal-bhalbe * Math.sin(psi)*skal);
		fzgey[2]=(int) (y*skal-(-ueh)*Math.sin(psi)*skal-bhalbe * Math.cos(psi)*skal);
		fzgex[3]=(int) (x*skal+(-ueh)*Math.cos(psi)*skal+bhalbe * Math.sin(psi)*skal);
		fzgey[3]=(int) (y*skal-(-ueh)*Math.sin(psi)*skal+bhalbe * Math.cos(psi)*skal);
		g.drawPolygon(fzgex,fzgey,4);
		fzgex[0]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+(reifenhoehe)*Math.cos(psi)*skal+reifenbreite * Math.sin(psi)*skal);
		fzgey[0]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-(reifenhoehe)*Math.sin(psi)*skal+reifenbreite * Math.cos(psi)*skal);
		fzgex[1]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+(reifenhoehe)*Math.cos(psi)*skal-reifenbreite * Math.sin(psi)*skal);
		fzgey[1]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-(reifenhoehe)*Math.sin(psi)*skal-reifenbreite * Math.cos(psi)*skal);
		fzgex[2]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+(-reifenhoehe)*Math.cos(psi)*skal-reifenbreite * Math.sin(psi)*skal);
		fzgey[2]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-(-reifenhoehe)*Math.sin(psi)*skal-reifenbreite * Math.cos(psi)*skal);
		fzgex[3]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+(-reifenhoehe)*Math.cos(psi)*skal+reifenbreite * Math.sin(psi)*skal);
		fzgey[3]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-(-reifenhoehe)*Math.sin(psi)*skal+reifenbreite * Math.cos(psi)*skal);
		g.fillPolygon(fzgex,fzgey,4);
		fzgex[0]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+(reifenhoehe)*Math.cos(psi)*skal+reifenbreite * Math.sin(psi)*skal);
		fzgey[0]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-(reifenhoehe)*Math.sin(psi)*skal+reifenbreite * Math.cos(psi)*skal);
		fzgex[1]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+(reifenhoehe)*Math.cos(psi)*skal-reifenbreite * Math.sin(psi)*skal);
		fzgey[1]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-(reifenhoehe)*Math.sin(psi)*skal-reifenbreite * Math.cos(psi)*skal);
		fzgex[2]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+(-reifenhoehe)*Math.cos(psi)*skal-reifenbreite * Math.sin(psi)*skal);
		fzgey[2]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-(-reifenhoehe)*Math.sin(psi)*skal-reifenbreite * Math.cos(psi)*skal);
		fzgex[3]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+(-reifenhoehe)*Math.cos(psi)*skal+reifenbreite * Math.sin(psi)*skal);
		fzgey[3]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-(-reifenhoehe)*Math.sin(psi)*skal+reifenbreite * Math.cos(psi)*skal);
		g.fillPolygon(fzgex,fzgey,4);
		fzgex[0]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(reifenhoehe)*Math.cos(psi+delta)*skal+reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[0]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(reifenhoehe)*Math.sin(psi+delta)*skal+reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[1]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(reifenhoehe)*Math.cos(psi+delta)*skal-reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[1]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(reifenhoehe)*Math.sin(psi+delta)*skal-reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[2]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(-reifenhoehe)*Math.cos(psi+delta)*skal-reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[2]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(-reifenhoehe)*Math.sin(psi+delta)*skal-reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[3]=(int) (x*skal-reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(-reifenhoehe)*Math.cos(psi+delta)*skal+reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[3]=(int) (y*skal-reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(-reifenhoehe)*Math.sin(psi+delta)*skal+reifenbreite * Math.cos(psi+delta)*skal);
		g.fillPolygon(fzgex,fzgey,4);
		fzgex[0]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(reifenhoehe)*Math.cos(psi+delta)*skal+reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[0]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(reifenhoehe)*Math.sin(psi+delta)*skal+reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[1]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(reifenhoehe)*Math.cos(psi+delta)*skal-reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[1]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(reifenhoehe)*Math.sin(psi+delta)*skal-reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[2]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(-reifenhoehe)*Math.cos(psi+delta)*skal-reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[2]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(-reifenhoehe)*Math.sin(psi+delta)*skal-reifenbreite * Math.cos(psi+delta)*skal);
		fzgex[3]=(int) (x*skal+reifenentfernung * Math.sin(psi)*skal+radabstand* Math.cos(psi)*skal+(-reifenhoehe)*Math.cos(psi+delta)*skal+reifenbreite * Math.sin(psi+delta)*skal);
		fzgey[3]=(int) (y*skal+reifenentfernung * Math.cos(psi)*skal-radabstand* Math.sin(psi)*skal-(-reifenhoehe)*Math.sin(psi+delta)*skal+reifenbreite * Math.cos(psi+delta)*skal);
		g.fillPolygon(fzgex,fzgey,4);
	}
	/**
	 * @return Returns the psi.
	 */
	public double getPsi() {
		return psi;
	}
	/**
	 * @param psi The psi to set.
	 */
	public void setPsi(double psi) {
		this.psi = psi;
	}
	/**
	 * @return Returns the v.
	 */
	public double getV() {
		return v;
	}
	/**
	 * @param v The v to set.
	 */
	public void setV(double v) {
		this.v = v;
	}
	/**
	 * @return Returns the x.
	 */
	public double getX() {
		return x;
	}
	/**
	 * @param x The x to set.
	 */
	public void setX(double x) {
		this.x = x;
	}
	/**
	 * @return Returns the y.
	 */
	public double getY() {
		return y;
	}
	/**
	 * @param y The y to set.
	 */
	public void setY(double y) {
		this.y = y;
	}
	/**
	 * @return Returns the delta.
	 */
	public double getDelta() {
		return delta;
	}
	/**
	 * @param delta The delta to set.
	 */
	public void setDelta(double delta) {
		this.delta = delta;
	}
	/**
	 * @return Returns the kpsi.
	 */
	public double getKpsi() {
		return kpsi;
	}
	/**
	 * @param kpsi The kpsi to set.
	 */
	public void setKpsi(double kpsi) {
		this.kpsi = kpsi;
	}
	/**
	 * @return Returns the kr.
	 */
	public double getKr() {
		return kr;
	}
	/**
	 * @param kr The kr to set.
	 */
	public void setKr(double kr) {
		this.kr = kr;
	}
	/**
	 * @return Returns the skal.
	 */
	public double getSkal() {
		return skal;
	}
	/**
	 * @param skal The skal to set.
	 */
	public void setSkal(double skal) {
		this.skal = skal;
	}
}
