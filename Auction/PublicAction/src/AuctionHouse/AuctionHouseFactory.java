package AuctionHouse;

import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;

public class AuctionHouseFactory {
    private String centralIP;
    private int centralPort;
    private LinkedList<Item> items;

    public AuctionHouseFactory(String centralIP, int centralPort){
        this.centralIP = centralIP;
        this.centralPort = centralPort;
    }

    public AuctionHouse getHouse(){
        items = new LinkedList<>();
        items.add(Item.Bread);
        items.add(Item.Iron);
        Random rn = new Random();
        int port = rn.nextInt(1000)+4000;
        AuctionHouse house = new AuctionHouse(centralIP, centralPort, items, port);
        return house;
    }

    public static void main(String args[]){
        Scanner sc = new Scanner(System.in);

        System.out.println("Enter the central ip address:");
        String ip = sc.nextLine();
        System.out.println("Enter the central port:");
        int port = sc.nextInt();

        Random rn = new Random();

        AuctionHouseFactory factroy = new AuctionHouseFactory(ip, port);
        LinkedList<AuctionHouse> houses = new LinkedList<>();

        while(true){
            AuctionHouse house = factroy.getHouse();
            houses.add(house);
            Long time = Long.valueOf(rn.nextInt(300000));
            try{
                Thread.sleep(time);
            }catch(Exception e){
                e.printStackTrace();
            }

        }
    }
}
