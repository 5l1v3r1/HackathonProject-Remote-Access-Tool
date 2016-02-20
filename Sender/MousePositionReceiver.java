package mainprogramhackathon;

import java.awt.Robot;
import java.awt.event.InputEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class MousePositionReceiver {
    public static void start() {
        int portNumber = 2000;
        String hostName = "192.168.43.34";
        try (
            Socket clientSocket = new Socket(hostName, portNumber);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            ObjectInputStream inFromServer = new ObjectInputStream(clientSocket.getInputStream());
        ) {
            int i = 0;
            while(true){
                String line;
                while((line = in.readLine()) != null){
                    String[] strings = line.split(",");
                    int x = Integer.parseInt(strings[0]);
                    int y = Integer.parseInt(strings[1]);
                    Thread.sleep(15);
                    float xRatio = ((float)1366)/1707;
                    float yRatio = ((float)768)/970;
                    new Robot().mouseMove((int)(x * xRatio),(int)(y*yRatio));
                    i++;
                    if(i>200){
                        Robot bot = new Robot();
                        int mask = InputEvent.BUTTON1_DOWN_MASK;
                        bot.mousePress(mask);
                        bot.mouseRelease(mask);
                        i = 0;
//                        System.out.println("left mouse button clicked");
                    }
                }
              
//            System.out.println("Test");
            }
        } catch(Exception er){
            er.printStackTrace();
        }
    }
    
}
