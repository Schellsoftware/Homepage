#include <Servo.h> 
 
Servo meinservo;  //Der Servo wird angesprochen
 
#define INLENGTH 16//Länge des Strings
#define INTERMINATOR 13//Zeilenumbruch
char empfString[INLENGTH+1];//String der empfangen wird
char empfDataString[INLENGTH+1];//Empfangener String ohne Befehlszeichen
int inCount;

int analogPin = 5;     //Der Distanzsensor liegt an PIN 5
int val = 0;           //Wert des Distanzsensors
int taster2, taster3; //Tasten

void setup() 
{ 
  meinservo.attach(9);  // attaches the servo on pin 9 to the servo object 
  Serial.begin(9600);//Die Bautrate 9600 wird gesetzt
  pinMode(2,INPUT);//PIN2 als Input
  digitalWrite(2,HIGH);//An PIN2 wird der Pullup Widerstand aktviert
  pinMode(3,INPUT);//PIN3 als Input
  digitalWrite(3,HIGH);//An PIN3 wird der Pullup Widerstand aktviert
  pinMode(5,OUTPUT);//Die LED wird an PIN5 angeschlossen
} 
 
// int incomingByte = 0;	//Anzahl der eingehenden Zeichen

void loop() 
 {                    
   inCount = 0;//Anzahl der eingehenden Zeichen ist 0
  do {
    while (!Serial.available());             //Warte bis ein Zeichen anliegt 
    empfString[inCount] = Serial.read();       //Lese ein Zeichen ein und speichere es in empfString
    if (empfString [inCount] == INTERMINATOR) break;//Wenn ein Zeilenumbruch kommt beende die Schleife
  } while(++inCount < INLENGTH);//Solange der String kleiner als das Maximum ist
  empfString[inCount] = 0;                     //Mit Null wird der String beendet	
  for(int i=0;i<inCount-1;i++){//Das erste Zeichen ist der Befehl
    empfDataString[i]=empfString[i+1];//der neue String ist ohne das erste Zeichen (Befehl)
  }
  switch(empfString[0]){//Zeichen für das Kommando
    case 'A': Serial.print("A");//Mit A (Analogwert) wird der Distanzsensor abgefragt
              val = analogRead(analogPin);    // val ist der eingelesene Analogwert
              Serial.println(val);//Die Distanz wird zurückgesendet
              break;
    case 'D': //Die digital Ein- und Ausgänge werden abgefragt
              if(empfDataString[0]=='2'){//Taster2 an PIN 2 wird abgefragt
                taster2 = digitalRead(2);
                Serial.print("D");//Kommando wird zurückgegeben
                Serial.println(taster2);//gedrückt ist 0 und ungedrückt ist 1
              }  
              if(empfDataString[0]=='3'){
                taster3 = digitalRead(3);//Taster3 an PIN 3 wird abgefragt
                Serial.print("D");//Kommando wird zurückgegeben
                Serial.println(taster3);//gedrückt ist 0 und ungedrückt ist 1
              }
              if(empfDataString[0]=='5'){
                digitalWrite(5,HIGH);//Led wird angeschalten
              }  
              if(empfDataString[0]=='6'){
                digitalWrite(5,LOW);//Led wird ausgeschalten
              }  
              break;
   case'S': Serial.print("S");//Sollwert für Servo
      int mein_integer_data = atoi(empfDataString);//Sollwertgenerierung
      meinservo.write(mein_integer_data);//Servo
      break;           
  }
}
