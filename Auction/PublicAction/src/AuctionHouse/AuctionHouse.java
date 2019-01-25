package AuctionHouse;

import Clients.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.LinkedList;


public class AuctionHouse{

    /*agent messages:
       sendto:
        auction  update
        auction closing
        outbid
        bid accepted/rejected
       recieve:
        agent closing
        bid


      central messages:
       sendto:
        registerAuctionHouse
        checkfunds
        deduct funds
        auction closing
       recieve:
        accepted/rejected
      */

    private static int ID = 0;
    private int houseID;
    private int port;
    private LinkedList<Item> items;
    private LinkedList<AHAgentClient> clients;
    private Item currentItem;
    private float currentBid;
    private AHCentralClient centralClient;
    private ServerSocket server;
    private boolean running;
    private AHAgentClient topBid;
    private AuctionHouse house;
    private Thread connectThread;
    private long startTime;
    private boolean started;
    private AHAgentClient pendingClient;
    private float pendingBid;



    public AuctionHouse(String auctionCentralIP, int auctionCentralPort, LinkedList<Item> items, int port){
        this.port = port;
        houseID = ID;
        ID++;
        this.items = items;
        running = true;
        currentBid = 0;
        currentItem = Item.None;
        house = this;
        clients = new LinkedList<>();
        started = false;
        try {
            server = new ServerSocket(port);
            centralClient = new AHCentralClient(new Socket(auctionCentralIP, auctionCentralPort),
                    Inet4Address.getLocalHost().getHostAddress(), port, this);
            centralClient.start();
        }catch(Exception e){
            e.printStackTrace();
        }
        Runnable clientHandler = new Runnable() {
            @Override
            public void run() {
                try {
                    while(running) {
                        System.out.println("Waiting for clients.");
                        Socket socket = server.accept();
                        System.out.println("Accepted client");
                        AHAgentClient client = new AHAgentClient(socket, house);
                        clients.push(client);
                        client.start();
                        System.out.println("Started client");
                        client.updateAuction(currentItem, currentBid);
                        System.out.println("Connected Client");
                        if(!started){
                            startAuction();
                            started = true;
                        }
                    }
                }catch(Exception e){
                    stopAll();
                    e.printStackTrace();
                }
            }
        };
        connectThread = new Thread(clientHandler);
        connectThread.start();

    }

    private void startAuction(){
        Runnable auction = new Runnable() {
            @Override
            public void run() {


                System.out.println("Auction " + houseID + " starting.");
                while (!(items.isEmpty())) {
                    currentItem = items.pop();
                    updateClients();
                    startTime = System.currentTimeMillis();
                    long elapsedTime = 0L;
                    while (elapsedTime < 30 * 1000 || currentBid == 0) {
                        elapsedTime = System.currentTimeMillis() - startTime;
                    }
                    System.out.println("won");
                    centralClient.deduct(currentBid, topBid.getBankKey());
                    topBid.itemWon();
                    topBid = null;
                    currentBid = 0;
                }
            }
        };
        Thread t = new Thread(auction);
        t.start();
    }

    private void updateClients(){
        for(int i = 0; i < clients.size(); i++){
            clients.get(i).updateAuction(currentItem, currentBid);
        }
    }

    private void stopAll(){
        connectThread.stop();
        for(int i = 0; i < clients.size(); i++){
            clients.get(i).sendStop();
        }
    }

    public void removeAgent(int ID){
        for(int i = 0; i < clients.size(); i++){
            if(clients.get(i).getID() == ID){
                clients.remove(i);
            }
        }
    }

    public void vaildBid(boolean valid){
        if (valid) {
            //need to tell central about outbid
            if(topBid != null){ topBid.outBid();}
            centralClient.outBid(pendingBid, pendingClient.getBankKey());
            topBid = pendingClient;
            topBid.bidAccepted();
            currentBid = pendingBid;
            startTime = System.currentTimeMillis();
            updateClients();
        } else {
            pendingClient.bidDeclined();
        }
    }
    public synchronized void processBid(AHAgentClient client, float bid, int bankKey){
        if(bid > currentBid) {
            pendingClient = client;
            pendingBid = bid;
            centralClient.checkFunds(bid, bankKey);
            System.out.println("Processed.");
        }else{
            client.bidDeclined();
        }
    }

    /*@Override
    public void run(){
        //wait for agent to connect
        System.out.print("Waiting for clients.");
        while(clients.size() == 0){ System.out.print("."); }
        startAuction();
    }*/

    public static void main(String args[]){
        /*
        LinkedList<Item> items = new LinkedList<>();
        items.add(Item.Bread);
        AuctionHouse house = new AuctionHouse("NONE", 0000,items, 4444);
        Thread t = new Thread(house);
        t.start();
        */
    }

}
