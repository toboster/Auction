package Agent;

import Message.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class testServer {
    Socket socket;
    ObjectInputStream in;
    ObjectOutputStream out;
    int balance = 20000;
    public testServer(int port){
        try {
            System.out.println(port);
            ServerSocket server = new ServerSocket(port);
            Runnable run = new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("server is made. waiting for connection....");
                        socket = server.accept();
                        out = new ObjectOutputStream(socket.getOutputStream());
                        in = new ObjectInputStream(socket.getInputStream());
                        System.out.println("connected client");

                        while (true) {

                            Message x = (Message) in.readObject();
                            if (x.getType().equals(MessageType.OpenBankAccount)) {

                                //Message msg = new Message(MessageType.BankAccountOpened, 123, 12345,balance);
                                //out.writeObject(msg);
                                out.flush();
                                System.out.println("sent bank account");
                                Timeline timeline = new Timeline(new KeyFrame(
                                        Duration.millis(500),
                                        ae -> {
                                           balance += 10;

                                        }));
                                timeline.setCycleCount(Animation.INDEFINITE);
                                timeline.play();


                            } else if (x.getType().equals(MessageType.OpenAuctionAccount)) {
                                Message msg = new Message(MessageType.AuctionAccountOpened, 33564);
                                out.writeObject(msg);
                                out.flush();

                            }else if(x.getType().equals(MessageType.RequestBankBalance)){
                                Message msg = new Message(MessageType.SendBankBalance,balance);
                                out.writeObject(msg);
                                out.flush();
                            }else if(x.getType().equals(MessageType.StopClient)){
                                break;
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            };

            Thread thread = new Thread(run);
            thread.start();
        }catch(Exception e){
            e.printStackTrace();
        }



    }
    public static void main(String[] args){
        new testServer(2225);
    }
}
