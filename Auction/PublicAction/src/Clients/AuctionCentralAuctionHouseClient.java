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
public class AuctionCentralAuctionHouseClient extends Client{
    private AuctionCentral autCent;
    private int id;
    public AuctionCentralAuctionHouseClient(Socket socket, ObjectOutputStream out, ObjectInputStream in, int id, AuctionCentral autCent){
        super(socket, out, in);
        this.id = id;
        this.autCent = autCent;
    }
    @Override
    public void processMessage(Message msg) {
       if(msg.getType() == MessageType.CheckFunds){
           System.out.println("Got Bid...");
            autCent.sendToBank(msg, id);
        }else if(msg.getType() == MessageType.OutBid){
            autCent.sendToBank(msg, id);
        }else if(msg.getType() == MessageType.AuctionClosing){
            autCent.removeAutHouse(id);
        }else if(msg.getType() == MessageType.DeductFunds){
           autCent.sendToBank(msg, id);
       }else{
            System.out.println("Unexpected message type from Auction House:" + msg.getType());
        }
    }

    public void sendMessage(Message msg){
        try{
            getOut().writeObject(msg);
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Sent to Auction House");
    }

    public int getID(){return id;}
}
