import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

class FaceDetector {  
     private CascadeClassifier face_cascade;
     CommPortIdentifier portIdentifier;
	 CommPort commPort;
	 SerialPort serialPort;
	 SerialDataSender sds ;
     public FaceDetector(){  
    	 sds = new SerialDataSender();
         try{
        	 portIdentifier = CommPortIdentifier.getPortIdentifier("COM3");
        	 commPort = portIdentifier.open(this.getClass().getName(),2000);
        	 if ( commPort instanceof SerialPort )
             {
                 serialPort = (SerialPort) commPort;
                 serialPort.setSerialPortParams(9600,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);
             }
         }catch(Exception ex){
        	 System.out.println(ex.getStackTrace());
         }
        face_cascade=new CascadeClassifier("./cascades/haarcascade_frontalface_alt.xml"); 
          if(face_cascade.empty())  
          {  
               System.out.println("--(!)Cassade Classifier could not load!\n");  
                return;  
          }  
          else  
          {  
                     System.out.println("Classifier loaded!");  
          }  
     }  
     int preAvgX, preAvgY;
     public Mat detect(Mat inputframe){  
         
    	 Mat mRgba=new Mat();  
          Mat mGrey=new Mat();  
          MatOfRect faces = new MatOfRect();  
          inputframe.copyTo(mRgba);  
          inputframe.copyTo(mGrey);  
          Imgproc.cvtColor( mRgba, mGrey, Imgproc.COLOR_BGR2GRAY);  
          Imgproc.equalizeHist( mGrey, mGrey );  
          face_cascade.detectMultiScale(mGrey, faces);  
          int facesCount = faces.toArray().length;
          System.out.println(String.format("There are %s faces on video.", facesCount)); 
          int currentAvgX = 0, currentAvgY =0;
          preAvgX = currentAvgX;
          preAvgY = currentAvgY;
          for(Rect rect:faces.toArray())  
          {  
        	  currentAvgX  += rect.x + rect.width/2;
        	  currentAvgX = currentAvgX /facesCount;
        	  
        	  currentAvgY += rect.y + rect.height/2;
        	  currentAvgY = currentAvgY / facesCount;
               Imgproc.rectangle(mRgba, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y+rect.height),
            		   			new Scalar(0,0,255));
               System.out.println("x: " + rect.x+ " y: " + rect.y );
               
          }  
          //This control decreases the amount of sending near values from pc to Arduino
          if(preAvgX < currentAvgX -10 || preAvgX > currentAvgX +10 || preAvgY < currentAvgY -10 || preAvgY > currentAvgY +10){
        	  sds.SendDataToArduino(currentAvgX, currentAvgY);
        	  preAvgX = currentAvgX;
        	  preAvgY = currentAvgY;
          }
          return mRgba;  
     } 
    
}  