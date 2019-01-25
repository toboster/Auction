package Bank;

import Message.MessageType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import Message.*;

public class TestAgentServer {

    public static void main(String[] args) throws IOException {
        int portNumber = Integer.parseInt(args[0]);
        String hostName = args[1];
        try(
                Socket socket = new Socket(hostName,portNumber);
                ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

        ){
            out.writeObject(new Message(MessageType.OpenBankAccount));
            while(true){
                try{
                    System.out.println("Success, reading in message.");
                    Message message = (Message) in.readObject();
                    System.out.println(message);
                    Thread.sleep(5000);
                    out.writeObject(new Message(MessageType.RequestBankBalance,1,1));
                } catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
    }
}
