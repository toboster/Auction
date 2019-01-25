package AuctionCentral;

import java.io.Serializable;

/**
 * Created by bucky on 4/30/2018.
 * Class CS 351
 * Lab4: Public Auction
 */
public class AuctionHouseInfo implements Serializable {
    private String ip;
    private int port;
    private int auctionHouseID;

    AuctionHouseInfo(String ip, int port, int auctionHouseID){
        this.ip = ip;
        this.port = port;
        this.auctionHouseID = auctionHouseID;
    }

    public int getAuctionHouseID() {
        return auctionHouseID;
    }

    public int getPort() {
        return port;
    }

    public String getIp() {return ip;}
}
