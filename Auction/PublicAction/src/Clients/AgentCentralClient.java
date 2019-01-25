package Clients;

import Message.*;
import Agent.*;
import java.net.Socket;

public class AgentCentralClient extends Client {
    Agent agent;

    /**
     * constructor just takes socket
     * @param socket to be set
     */
    public AgentCentralClient(Socket socket,Agent agent){
        super(socket);
        this.agent = agent;
    }

    /**
     * sends msg to auction central that this agent needs an auction account
     */
    public void setUpWithAuctionCentral(){
        Message msg = new Message(MessageType.OpenAuctionAccount);
        try {
            getOut().writeObject(msg);
            getOut().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * processes msg based on what message it is
     * @param msg to process
     */
    @Override
    public void processMessage(Message msg) {
        if(msg.getType().equals(MessageType.AuctionAccountOpened)){
            int biddingKey = msg.getAuctionKey();
            System.out.println("Auction key:" + biddingKey);
        }else if (msg.getType().equals(MessageType.AuctionUpdate)) {
            System.out.println("central sent update!!\n\n\n\n\n");
            agent.updateHouses(msg.getAutHouseInfos());
        }else{
            System.err.println("Unknown Message :(");
        }
    }
}

