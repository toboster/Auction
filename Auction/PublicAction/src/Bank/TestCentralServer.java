package Bank;

import Message.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import Message.*;

public class TestCentralServer {
    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        String hostName = args[1];
        System.out.println("Waiting for bank connection...");
        try(
                ServerSocket serverSocket = new ServerSocket(portNumber);
                Socket socket = serverSocket.accept();
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        ){
            System.out.println("Connected, wrting message.");
                try{
                    Thread.sleep(6000);
                    for(int i = 0; i < 10; i++){
                        out.writeObject(new Message(MessageType.CheckFunds,10f,1));
                        Message message1 = (Message) in.readObject();
                        System.out.println("Check Funds status = " +  message1.getValidBid());
                    }
                    for(int i = 0; i < 10; i++){
                        Thread.sleep(5000);
                        out.writeObject(new Message(MessageType.BidDecline, 10f,1));
                        System.out.println("Sending message for success of bid, bank key 1");
                        System.out.println("---------------------------------------------------------");
                    }

                    while(true){
                        Thread.sleep(5000);
                        System.out.println("Standing by...");
                    }
                } catch(Exception e){
                    e.printStackTrace();
                }


        }
    }
}
