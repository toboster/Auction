package AuctionCentral;

import Message.Message;

import java.awt.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Scanner;

import Bank.*;
import Clients.*;
import Message.Message;
import Message.MessageType;

/**
 * Created by Fred on 4/15/2018.
 * Class CS 351
 * Lab4: Auction
 */
public class AuctionCentral {
    private LinkedList<AuctionCentralAgentClient> agents;
    private LinkedList<AuctionCentralAuctionHouseClient> houses;
    private LinkedList<AuctionHouseInfo> houseInfos;
    private AuctionCentralBankClient bank;
    private ServerSocket server;
    private int currentKey = 1;
    private int currentHouseId = 1;
    public AuctionCentral(int port){
        agents = new LinkedList<>();
        houses = new LinkedList<>();
        houseInfos = new LinkedList<>();
        try {
            server = new ServerSocket(port);
        }catch(IOException e){}
        System.out.println("Waiting for bank connection...");
        try {
            bank = new AuctionCentralBankClient(server.accept(), this);
        }catch (Exception e){}
        bank.start();
        System.out.println("Connected to bank");
        listeningThread();

    }
    public void listeningThread(){
        AuctionCentral myAuct = this;
        Runnable listen = new Runnable() {
            @Override
            public void run() {
                while(true){
                    try {
                        Socket s = server.accept();
                        System.out.println("got");
                        ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
                        ObjectInputStream in = new ObjectInputStream(s.getInputStream());
                        System.out.println("waiting for msg");
                        Message msg = (Message) in.readObject();
                        if(msg.getType() == MessageType.OpenAuctionAccount){
                            AuctionCentralAgentClient a = new AuctionCentralAgentClient(s,out,in,currentKey);
                            a.start();
                            agents.push(a);
                            a.sendMessage(new Message(MessageType.AuctionAccountOpened,currentKey));
                            for(AuctionHouseInfo info: houseInfos){
                                a.sendMessage(new Message(MessageType.AuctionUpdate, info));
                            }
                            currentKey++;
                            System.out.println("Agent Connected...");
                        }else if(msg.getType() == MessageType.RegisterAuctionHouse){
                            AuctionCentralAuctionHouseClient h = new AuctionCentralAuctionHouseClient(s,out, in, currentHouseId,myAuct);
                            houses.push(h);
                            houseInfos.push(new AuctionHouseInfo(msg.getAHIP(),msg.getAHport(),currentHouseId));
                            currentHouseId++;
                            h.start();
                            updateAgents();
                            System.out.println("Auction House Connected...");
                        }else{
                            System.out.println("Stop...");
                        }
                        System.out.println("connected client");
                    }catch (Exception e){}
                }
            }
        };
        Thread t = new Thread(listen);
        t.start();
    }
    private void updateAgents(){

        System.out.println("Sending Auction Info to Agents...");
        System.out.println(houseInfos.size());
        for(AuctionCentralAgentClient a : agents){
            for(AuctionHouseInfo info: houseInfos){
                a.sendMessage(new Message(MessageType.AuctionUpdate, info));
            }
        }
    }

    public void sendToBank(Message msg, int id){
        bank.sendMessage(new Message(msg.getType(), msg.getAmountToCheck(), msg.getBankKey(), id));
    }

    public void removeAutHouse(int id){
        for(AuctionCentralAuctionHouseClient aut: houses){
            if(id == aut.getID()){
                aut.sendStop();
                houses.remove(aut);
            }
        }
    }

    public void tellAuctionHouse(Message msg){
        int id = msg.getAccountNum();
        System.out.println(id);
        for(AuctionCentralAuctionHouseClient aut: houses){
            System.out.println(aut.getID());
            if(id == aut.getID()){
                if(msg.getValidBid()){
                    System.out.println("Found Auction House: Accept");
                    aut.sendMessage(new Message(MessageType.BidAccept));
                }else{
                    System.out.println("Found Auction House: Decline");
                    aut.sendMessage(new Message(MessageType.BidDecline));
                }

            }
        }
    }
    public static void main(String[] args){
        new AuctionCentral(Integer.parseInt(args[0]));
    }
}

