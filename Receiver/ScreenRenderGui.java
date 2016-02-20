package tester;

import java.awt.Dimension;
import java.awt.Toolkit;
import javafx.scene.image.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author Abc
 */
public class ScreenRenderGui extends Application {
    
    ImageView view = new ImageView();
    boolean clickStatus = false;
    
    @Override
    public void start(Stage primaryStage) throws IOException {
                
        
        final StackPane root = new StackPane();
        primaryStage.setFullScreen(true);
        primaryStage.setMaximized(true);
        Scene scene = new Scene(root, 1366, 760);
        scene.setOnKeyPressed(new EventHandler<KeyEvent>(){

            @Override
            public void handle(KeyEvent event) {
                System.out.println(event.getCode());
            }
            
        });
        final ImageView iv = new ImageView();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        iv.setFitHeight(screenSize.height);
        iv.setFitWidth(screenSize.width);
        iv.setOnMousePressed((e)->{
        switch(e.getClickCount()){
            case 1:
                System.out.println("Testx");      
                clickStatus = true;
                break;
            case 2:
                System.out.println("Test");                
                clickStatus = true;
                break;
            case 3:
                break;
        }
        });
        
                
        Thread t;
        t = new Thread(new Runnable() {
            @Override
            public void run() {
                int portNumber = 5010;
                try (
                    ServerSocket serverSocket = new ServerSocket(portNumber);
                    Socket echoSocket = serverSocket.accept();
                    PrintWriter out = new PrintWriter(echoSocket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(echoSocket.getInputStream()));
                    
                    ) {
                    echoSocket.setKeepAlive(true);
                    
                    while(true){
                        String line;
                        if((line = in.readLine()) != null){
                            //System.out.println(line);
                            byte[] data = Base64.getDecoder().decode(line);
                            ByteArrayInputStream bais = new ByteArrayInputStream(data);
                            BufferedImage bi = ImageIO.read(bais);
                            Image im = SwingFXUtils.toFXImage(bi, null);
                            iv.setImage(im);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            
            public ByteArrayInputStream decodeToImage(String imageString) {
                
                BufferedImage image = null;
                byte[] imageByte;
                ByteArrayInputStream bis = null;
                try {
                    Base64.Decoder decoder = Base64.getDecoder();  
                    imageByte = decoder.decode(imageString);
                    bis = new ByteArrayInputStream(imageByte); 
                    Image img = new Image(bis);
                    image = ImageIO.read(bis);
                    bis.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }  
                return bis;
            }

        });
        root.getChildren().add(iv);
//        iv.setImage(new Image(new File("E:/abc7.bmp").toURI().toString()));
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
        


        t.start();


    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public void setImageView(ImageView iv, ByteArrayInputStream img){
        Image image = new Image(img);
        iv.setImage(image);
    }
    
    
}


//class MouseEvents implements MouseListener{
//
//     
//    }
//}
