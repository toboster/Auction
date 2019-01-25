package Clients;

import AuctionHouse.AuctionHouse;
import Message.*;
import java.net.Socket;

public class AHCentralClient extends Client{
    private int port;
    private String ip;
    private AuctionHouse house;
    private boolean checkFunds;
    private boolean pending;

    public AHCentralClient(Socket socket, String ip, int port, AuctionHouse house){
        super(socket);
        this.ip = ip;
        this.port = port;
        this.house = house;
        registerAH();
    }

    public void registerAH(){
        Message msg = new Message(MessageType.RegisterAuctionHouse, ip, port);
        try{
            getOut().writeObject(msg);
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public boolean checkFunds(float amount, int bankKey){
        //send message to central to check funds
        checkFunds = false;
        pending = true;
        try{
            getOut().writeObject(new Message(MessageType.CheckFunds, amount, bankKey));
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Waiting for response from Central.");

        return checkFunds;

    }

    public void deduct(float amount, int bankKey){
        try{
            getOut().writeObject(new Message(MessageType.DeductFunds, amount, bankKey));
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void outBid(float amount, int key){
        try{
            getOut().writeObject(new Message(MessageType.OutBid, amount, key));
            getOut().flush();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void processMessage(Message msg){
        System.out.println("");
        switch(msg.getType()){
            case BidAccept:
                System.out.println("BA");
                house.vaildBid(true);
                break;
            case BidDecline:
                System.out.println("BD");
                house.vaildBid(false);
                break;
            case StopClient:
                try{
                    socket.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
                break;
            default:
                System.out.println("CentralClient unable to process message");
        }
    }
}
