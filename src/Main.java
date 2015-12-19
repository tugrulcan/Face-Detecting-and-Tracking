import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Main {  
    
	public static void main(String arg[]) throws InterruptedException{  
      // Load the native library.  
      System.loadLibrary(Core.NATIVE_LIBRARY_NAME); 
      //or ...     System.loadLibrary("opencv_java244");       

      //make the JFrame
      JFrame frame = new JFrame("Face detection interface");  
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
     
      FaceDetector faceDetector=new FaceDetector();  
      
      FacePanel facePanel = new FacePanel();  
      frame.setSize(400,400); //give the frame some arbitrary size 
      frame.setBackground(Color.BLUE);
      frame.add(facePanel,BorderLayout.CENTER);       
      frame.setVisible(true);       
      
      //Open and Read from the video stream  
       Mat webcam_image=new Mat();  
       VideoCapture webCam =new VideoCapture(1);
       //VideoCapture webcam2 = new VideoCapture(1);
  
    
        if( webCam.isOpened())  
          {  
           Thread.sleep(500); /// This one-time delay allows the Webcam to initialize itself  
           while( true )  
           {  
        	 webCam.read(webcam_image);  
             if( !webcam_image.empty() )  
              {   
            	  //Thread.sleep(200); /// This delay eases the computational load .. with little performance leakage
                   frame.setSize(webcam_image.width()+40,webcam_image.height()+60);
                   //Apply the classifier to the captured image  
                   webcam_image=faceDetector.detect(webcam_image);  
                  //Display the image  
                   facePanel.matToBufferedImage(webcam_image);  
                   facePanel.repaint();   
              }  
              else  
              {   
                   System.out.println(" --(!)Resim yakalanamadý!");   
                   break;   
              }  
             }  
            }
           webCam.release(); 
 
      } //end main 
	
}