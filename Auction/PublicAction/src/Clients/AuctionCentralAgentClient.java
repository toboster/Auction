package Clients;
import AuctionCentral.*;
import Message.*;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * Created by bucky on 4/26/2018.
 * Class CS 351
 * Lab4: Auction
 */
public class AuctionCentralAgentClient extends Client{
    private int key;
    public AuctionCentralAgentClient(Socket socket, ObjectOutputStream out, ObjectInputStream in,int key){
        super(socket,out,in);
        this.key = key;
    }

    @Override
    public void processMessage(Message msg){
        System.out.println("Why are you sending me something...");
    }

    public void sendMessage(Message msg){
        try{
            getOut().writeObject(msg);
            getOut().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}