package Message;

import AuctionCentral.AuctionHouseInfo;
import AuctionHouse.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Message implements Serializable{
    private int AHport;
    private String AHIP;
    private MessageType type;
    private int bankKey;
    private int auctionKey;
    private int accountNum;
    private float balance;
    private float pendingBalance;
    private float currentBid;
    private Item currentItem;
    private boolean validBid;
    private float amountToCheck;
    private int auctionHouseID;
    private AuctionHouseInfo autHouseInfos;

    /**
     * constructor
     * @param type of msg
     */
    public Message(MessageType type){
        this.type = type;
    }

    /**
     * constructor
     * @param type of msg
     * @param value int value
     */
    public Message(MessageType type, int value){
        this.type = type;
        if(type.equals(MessageType.AuctionAccountOpened)) {
            this.auctionKey = value;
        }else if(type.equals(MessageType.SendBankBalance)){
            this.balance = value;
        }else if(type.equals(MessageType.RequestBankBalance)){
            this.bankKey = value;
        }
    }

    /**
     * constructor
     * @param type of msg
     * @param bankKey to set/lookup
     * @param accountNum to set/lookup
     * @param balance to give
     */
    public Message(MessageType type, int bankKey, int accountNum, float balance){
        this.type = type;
        this.bankKey = bankKey;
        this.accountNum = accountNum;
        this.balance = balance;
    }
    public Message(MessageType type, int bankKey, int accountNum){
        this.type = type;
        this.bankKey = bankKey;
        this.accountNum = accountNum;
    }
    public Message(MessageType type, String AHIP, int AHport){
        this.type = type;
        this.AHIP = AHIP;
        this.AHport = AHport;
    }

    public Message(MessageType type, Item item, Float bid){
        this.type = type;
        this.currentItem = item;
        this.currentBid = bid;
    }

    public Message(MessageType type, float amount, int bankKey){
        this.type = type;
        this.amountToCheck = amount;
        this.bankKey = bankKey;
    }

    public Message(MessageType type, AuctionHouseInfo info){
        this.type = type;
        autHouseInfos = info;
    }

    public Message(MessageType type, float amountToCheck, int accountNum, boolean validBid, int auctionHouseID){
        this.type = type;
        this.amountToCheck = amountToCheck;
        this.accountNum = accountNum;
        this.validBid = validBid;
        this.auctionHouseID = auctionHouseID;
    }

    public Message(MessageType type, float amount, int bankKey, int auctionHouseID){
        this.type = type;
        this.amountToCheck = amount;
        this.bankKey = bankKey;
        this.auctionHouseID = auctionHouseID;
    }

    /**
     * Used when sending bank balance to agent.
     * @param type
     * @param balance
     * @param pendingBalance
     */
    public Message(MessageType type, float balance, float pendingBalance){
        this.type = type;
        this.balance = balance;
        this.pendingBalance = pendingBalance;
    }

    public void setAuctionHouseID(int auctionHouseID){
        this.auctionHouseID = auctionHouseID;
    }

    public int getAuctionHouseID(){
        return auctionHouseID;
    }

    /**
     *
     * @return bankKey of msg
     */
    public int getBankKey(){
        return bankKey;
    }

    /**
     *
     * @return auction key of msg
     */
    public int getAuctionKey(){
        return auctionKey;
    }

    /**
     *
     * @return account num of msg
     */
    public  int getAccountNum(){
        return accountNum;
    }

    /**
     *
     * @return type of msg
     */
    public MessageType getType(){
        return type;
    }

    /**
     *
     * @return balance of msg
     */
    public float getBalance(){
        return balance;
    }

    public float getPendingBalance(){
        return pendingBalance;
    }

    public int getAHport() {
        return AHport;
    }


    public String getAHIP() {
        return AHIP;
    }

    public float getCurrentBid() { return currentBid; }

    public Item getCurrentItem(){ return currentItem; }

    public AuctionHouseInfo getAutHouseInfos(){return autHouseInfos;}

    public float getAmountToCheck(){
        return amountToCheck;
    }

    public boolean getValidBid(){
        return validBid;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", bankKey=" + bankKey +
                ", auctionKey=" + auctionKey +
                ", accountNum=" + accountNum +
                ", balance=" + balance +
                ", pending=" + pendingBalance +
                '}';
    }
}
