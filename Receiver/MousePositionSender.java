package sample;

import java.awt.MouseInfo;
import java.awt.Point;
import java.io.*;
import java.net.*;
import java.util.*;

public class MousePositionSender {
    public void start(){
        Thread t = new Thread(new MousePositionThread(), "mousePositionTappingThread");
        t.start();
    }
}

class MousePositionThread implements Runnable{

    @Override
    public void run() {
        final int portNumber = 2000;
        try {
            ServerSocket socketServer = new ServerSocket(portNumber);
            Socket echoSocket = socketServer.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
            echoSocket.setKeepAlive(true);
            PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
            ObjectOutputStream outToServer = new ObjectOutputStream(echoSocket.getOutputStream());
            while(true){
                Point p = MouseInfo.getPointerInfo().getLocation();
                out.flush();
                out.println(p.x + "," + p.y );
//                System.out.println(p.x + " " + p.y);
                Thread.sleep(15);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}