import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import java.util.Enumeration;


public class SerialDataSender {
	private static final String PORT = "COM3";
	private static final int TIME_OUT = 2000;
	private static final int DATA_RATE = 9600;
	 
	private SerialPort serialPort;
	private OutputStream output;
	private ByteArrayOutputStream bOut;
	private OutputStreamWriter outputWriter;
	
	public SerialDataSender(){
		bOut = new ByteArrayOutputStream();
		outputWriter = new OutputStreamWriter(bOut);
		CommPortIdentifier portId = findPortId();
		try {
		serialPort = (SerialPort) portId.open(this.getClass().getName(), TIME_OUT);
		serialPort.setSerialPortParams(DATA_RATE,
                SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1,
                SerialPort.PARITY_NONE );
		output = serialPort.getOutputStream();
		serialPort.notifyOnDataAvailable(true);
		} catch (Exception e) {
			System.out.println(e.getStackTrace());
		}
		
	}
	String sData;
	public void SendDataToArduino(int x, int y){
		 String sX, sY;

		 if( x > 99){
			 sX = Integer.toString(x);
		 } else if( x > 9){
			 sX = "0" + Integer.toString(x);
		 }else{
			 sX = "00"+ Integer.toString(x);
		 }
		 
		 if( y > 99){
			 sY = Integer.toString(y);
		 } else if( y > 9){
			 sY = "0" + Integer.toString(y);
		 }else{
			 sY = "00"+ Integer.toString(y);
		 }
		 
		 try {
			 sData= sX + "-" + sY + "@";
			 outputWriter.write(sData);	
			 outputWriter.flush();
			 bOut.writeTo(output);
			 bOut.reset();
		 } catch (Exception e) {
			e.printStackTrace();
		}

		 
	}
	
	
	 private CommPortIdentifier findPortId() {
	        CommPortIdentifier portId = null;
	        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
	        while (portEnum.hasMoreElements()) {
	            CommPortIdentifier currPortId = (CommPortIdentifier)portEnum.nextElement();
	            
	                if (currPortId.getName().equals(PORT)) {
	                    portId = currPortId;
	                    break;
	                }
	            
	        }
	        return portId;
	    }
	

}
