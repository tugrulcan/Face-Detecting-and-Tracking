#include <Servo.h>
  Servo servoTilt;
  Servo servoPan;
  int angleOfPan = 90;
  int angleOfTilt = 90;
  byte bSx[4], bSy[4];//Buffers
  char sX[4], sY[4];
  int x, y;
  bool isDataRecieved = false;
  int amountOfIncrease = 1;

void setup() {
        Serial.begin(9600);
        servoTilt.attach(4);
        servoPan.attach(3);
        servoTilt.write(angleOfTilt);
        servoPan.write(angleOfPan);
}

void loop() {
isDataRecieved = false;
        // send data only when you receive data:
        if (Serial.available() > 0)
        {
                //Gets value of x from XXX-YYY@ 
               Serial.readBytesUntil('-', bSx, 3);
                for(int i= 0; i<3; i++)
                  sX[i] = (char)bSx[i];  
                
                sX[3] = '\0';
                x = atoi(sX);

                Serial.readBytes(bSx, 1);// skip the character skip '-' 
               
               
               Serial.readBytesUntil('@', bSy, 3);
                for(int i= 0; i<3; i++)
                  sY[i] = (char)bSy[i];  
                sY[3] = '\0';
                y = atoi(sY);
                Serial.readBytes(bSx, 1); // skip the character '@' 
                
                // say what you got:
                Serial.print("x: ");
                Serial.println(x);
                Serial.print("y: ");
                Serial.println(y);
                isDataRecieved = true;
        }

        if(isDataRecieved)
        {
           if( x < 220)
           {
              if(angleOfPan > amountOfIncrease)
                angleOfPan += amountOfIncrease;
              else
                angleOfPan = 0;
           }
           if( x > 420)
           {
            if(angleOfPan < 180-amountOfIncrease)
              angleOfPan -=amountOfIncrease;
            else
              angleOfPan = 180;
           }
           if( y < 160)
           {
              if( angleOfTilt > amountOfIncrease)
                  angleOfTilt -= amountOfIncrease;
              else
                  angleOfTilt = 0;
            }
           if( y > 320)
           {
            if(angleOfTilt < 180-amountOfIncrease)
              angleOfTilt +=amountOfIncrease;
            else
              angleOfTilt = 180;
           }
           
          Serial.print("Angles of servos \nservoPan: ");
          Serial.print(angleOfPan);
          Serial.print("\nservoTilt: ");
          Serial.print(angleOfTilt);
          Serial.println("");
          servoPan.write(angleOfPan);
          servoTilt.write(angleOfTilt);       
        }
        



        
}
 
