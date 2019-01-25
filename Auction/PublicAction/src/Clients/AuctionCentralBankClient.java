package Clients;
import AuctionCentral.*;
import Message.*;

import java.net.Socket;
/**
 * Created by bucky on 4/26/2018.
 * Class CS 351
 * Lab4: Auction
 */
public class AuctionCentralBankClient extends Client{
    private AuctionCentral autCent;
    public AuctionCentralBankClient(Socket socket, AuctionCentral autcent){
        super(socket);
        this.autCent = autcent;
    }

    @Override
    public void processMessage(Message msg) {
        if(msg.getType() == MessageType.CheckFunds){
            System.out.println("Got Bank Message");
            autCent.tellAuctionHouse(msg);
        }
    }

    public void sendMessage(Message msg){
        try{
            getOut().writeObject(msg);
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Sent" + msg.getBankKey());
    }
}
