package sample;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.Socket;
import java.util.Base64;
import javax.imageio.ImageIO;

public class ScreenCapSender{

    public void start(){
        try {
            Thread t = new Thread(new ScreencapTaker(), "screencapTakingThread");
            t.run();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

class ScreencapTaker implements Runnable{
    
    public static String encodeToString(BufferedImage image, String type) {  
        String imageString = null;  
        ByteArrayOutputStream bos = new ByteArrayOutputStream();  
  
        try {  
            ImageIO.write(image, type, bos);  
            byte[] imageBytes = bos.toByteArray();  
  
            Base64.Encoder encoder = Base64.getEncoder();  
            imageString = encoder.encodeToString(imageBytes);  
            bos.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return imageString;  
    }  

    @Override
    public void run() {

        String hostName = "192.168.43.34";
        int portNumber = 5010;
        try {
            Socket clientSocket = new Socket(hostName, portNumber);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            clientSocket.setKeepAlive(true);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            while(true){
                BufferedImage image = new Robot().createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
                String b64 = encodeToString(image, "JPEG");
                out.println(b64);
                Thread.sleep(100);
//                System.out.println("Success!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}