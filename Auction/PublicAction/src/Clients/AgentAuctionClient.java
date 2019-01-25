package Clients;

import Message.*;
import Agent.*;

import java.net.Socket;
import AuctionHouse.*;

public class AgentAuctionClient extends Client{

    private Agent agent;
    private float currentBid;
    private Item currentItem;
    private int id;

    public AgentAuctionClient(Socket socket,Agent agent,int id){
            super(socket);
            this.agent = agent;
            this.id = id;
    }

    @Override
    public void processMessage(Message msg) {
        if(msg.getType().equals(MessageType.AuctionUpdate)){
            currentItem = msg.getCurrentItem();
            currentBid = msg.getCurrentBid();
            if((agent.getCurrentHouse() != null) && agent.getCurrentHouse().equals(this)) {
                agent.updateGuiAuction(currentItem, currentBid);
            }
        }else if(msg.getType().equals(MessageType.AuctionClosing)){
            agent.removeAuctionHouse(this);
        }else if(msg.getType().equals(MessageType.BidAccept)){
            agent.changeBidStatus(msg.getType(),id);
        }else if(msg.getType().equals(MessageType.BidDecline)){
            agent.changeBidStatus(msg.getType(),id);
        }else if(msg.getType().equals(MessageType.OutBid)){
            agent.changeBidStatus(msg.getType(),id);
        }else if(msg.getType().equals(MessageType.ItemWon)){
            agent.changeBidStatus(msg.getType(),id);
        }else{
            System.err.println("Unknown Message :(");
        }
    }

    public Item getCurrentItem() {
        return currentItem;
    }

    public float getCurrentBid() {
        return currentBid;
    }

    public int getHouseId(){
        return id;
    }
    public void sendBid(float bid){
        Message msg = new Message(MessageType.Bid,bid,agent.getBankKey());
        try {
            getOut().writeObject(msg);
        }catch(Exception e){

        }
        System.out.println("sent bid");
    }
}
