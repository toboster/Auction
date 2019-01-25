
package Clients;

import Message.Message;
import AuctionHouse.*;
import Message.MessageType;

import java.net.Socket;

public class AHAgentClient extends Client {
    private static int AGENTID = 0;
    private int ID;
    private AuctionHouse house;
    private int bankKey;

    public AHAgentClient(Socket socket, AuctionHouse house){
        super(socket);
        this.house = house;
        ID = AGENTID;
        AGENTID++;
    }

    public void updateAuction(Item item, Float bid){
        try{
            getOut().writeObject(new Message(MessageType.AuctionUpdate, item, bid));
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getID() { return ID; }

    public void closeAgent(){
        house.removeAgent(ID);
        try{
            socket.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void outBid(){
        try{
            getOut().writeObject(new Message(MessageType.OutBid));
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void bidAccepted(){
        try{
            getOut().writeObject(new Message(MessageType.BidAccept));
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void bidDeclined(){
        try{
            getOut().writeObject(new Message(MessageType.BidDecline));
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public int getBankKey(){
        return bankKey;
    }

    public void itemWon(){
        try{
            getOut().writeObject(new Message(MessageType.ItemWon));
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void processMessage(Message msg) {
        switch (msg.getType()) {
            case StopClient:
                closeAgent();
                break;
            case Bid:
                this.bankKey = msg.getBankKey();
                house.processBid(this, msg.getAmountToCheck(), msg.getBankKey());
                break;
            default:
                System.out.println("AHClient unable to process message");

        }
    }
}

