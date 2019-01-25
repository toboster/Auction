package Agent;

import AuctionHouse.*;
import Clients.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import Message.*;
import AuctionCentral.*;

public class Agent {

    private Label l;
    private Label itemL;
    private Label bidL;
    private Label bidStatus;
    private float bankBalance;
    private float pending;
    private int bankKey;
    private int bankAccountNumber;
    private String name;
    private AgentBankClient bank;
    private AgentCentralClient auctionCentral;
    private LinkedList<AgentAuctionClient> auctionHouses;
    private AgentAuctionClient currentHouse;
    private ComboBox housesBox;

    /**
     * Constructor for agent
     * @param name name of the user
     * @param bankPortNum port of bank
     * @param bankIP ip of bank
     * @param auctionCentralPortNum port of auction central
     * @param auctionCentralIP ip of auction central
     * @param l label from gui used to update balance
     */
    public Agent(String name, int bankPortNum, String bankIP, int auctionCentralPortNum, String auctionCentralIP, Label l
            , Label itemL, Label bidL, Label bidStatus, ComboBox housesBox){
        this.name = name;
        this.l = l;
        this.itemL = itemL;
        this.bidL = bidL;
        this.bidStatus = bidStatus;
        this.housesBox = housesBox;
        auctionHouses = new LinkedList<>();
        try{
            System.out.println("connecting to bank");
        bank = new AgentBankClient(new Socket(bankIP,bankPortNum),this);
            System.out.println("connected to bank");
            System.out.println("connecting to auction");
        auctionCentral = new AgentCentralClient(new Socket(auctionCentralIP,auctionCentralPortNum),this);
        System.out.println("connected to auction");
        }catch (IOException e){
            e.printStackTrace();
        }
        bank.start();
        auctionCentral.start();
        System.out.println("done starting");
        bank.setUpAccountWithBank();
        auctionCentral.setUpWithAuctionCentral();
        System.out.println("set up accounts");
    }

    /**
     *
     * @return the name of the user
     */
    public String getName() {
        return name;
    }

    /**
     * updates the label stating the balance
     * @param newBalance new balance of user
     */
    public void updateBalance(float newBalance,float pending){
        bankBalance = newBalance;
        this.pending = pending;
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                l.setText("Balances: Total: $" + bankBalance +
                        "  Pending: $" + pending +
                        " Available: $" + (bankBalance - pending));
            }
        });
    }
    public void updateGuiAuction(Item item, float bid){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                itemL.setText("Item being bid on: " + item.toString());
                bidL.setText("Current Bid: " + bid);
            }
        });

    }
    /**
     *
     * @return users bank key
     */
    public int getBankKey(){
        return bankKey;
    }

    /**
     *
     * @return users account num
     */
    public int getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void changeBidStatus(MessageType x, int id){
        if(x.equals(MessageType.BidAccept)){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    bidL.setText("Bid Accepted for Auction House " + id);
                }
            });

        }else if(x.equals(MessageType.BidDecline)){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    bidL.setText("Bid Declined for Auction House " + id);
                }
            });

        }else if(x.equals(MessageType.OutBid)){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    bidL.setText("You've been out bid for Auction House " + id);
                }
            });

        }else if(x.equals(MessageType.ItemWon)){
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    bidL.setText("You've won your bid for Auction House " + id);
                }
            });


        }
    }
    /**
     * sets users bank key
     * @param bankKey to be set
     */
    public void setBankKey(int bankKey){
        this.bankKey = bankKey;
    }

    /**
     * sets bank account
     * @param bankAccountNumber to be set
     */
    public void setBankAccountNumber(int bankAccountNumber){
        this.bankAccountNumber = bankAccountNumber;
    }
    public void stopAll(){
        bank.sendStop();
        auctionCentral.sendStop();
        if(currentHouse != null)currentHouse.sendStop();
    }
    public void removeAuctionHouse(AgentAuctionClient r){
        for(int i = 0; i < auctionHouses.size(); i++){
            if(auctionHouses.get(i).equals(r)){
                auctionHouses.get(i).interrupt();
                auctionHouses.remove(i);
                if(currentHouse.equals(r)){
                    currentHouse = null;
                    bidStatus.setText("Current Auction House has disconnected");
                }
            }
        }

    }
    public void updateHouses(AuctionHouseInfo list){
        System.out.println("in update");
        System.out.println(auctionHouses.size());
        for(AgentAuctionClient c : auctionHouses){
                if(list.getAuctionHouseID() == c.getHouseId()){
                    return;
                }
            }

        try {
                System.out.println(list.getIp().toString() + ", " + list.getPort());
                AgentAuctionClient AH = new AgentAuctionClient(new Socket(list.getIp(), list.getPort()),this,list.getAuctionHouseID());
                AH.start();
                auctionHouses.push(AH);
                housesBox.getItems().add(list.getAuctionHouseID());
                System.out.println("done adding new");
            }catch(IOException e){}


    }
    public void changeAuctionHouse(int id){
        if(currentHouse == null){
            for (AgentAuctionClient a : auctionHouses) {
                if (a.getHouseId() == id) {
                    currentHouse = a;
                    updateGuiAuction(a.getCurrentItem(), a.getCurrentBid());
                    System.out.println("changed house to:" + a.getHouseId());
                    break;
                }
            }
        }else if(currentHouse.getHouseId() != id) {
            for (AgentAuctionClient a : auctionHouses) {
                if (a.getHouseId() == id) {
                    currentHouse = a;
                    updateGuiAuction(a.getCurrentItem(), a.getCurrentBid());
                    System.out.println("changed house to:" + a.getHouseId());
                    break;
                }
            }
        }
        System.out.println("did not change house");

    }

    public AgentAuctionClient getCurrentHouse() {
        return currentHouse;
    }

    public void submitBid(float bid){
        currentHouse.sendBid(bid);
    }
}
