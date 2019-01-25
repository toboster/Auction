package Bank;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Bank {

    private Map<Integer, BankAgentClient> bankClientMap;

    private static int account_ID_val = 1;

    private Scanner sc;

    private int bankPortNumber;

    private String ACentralIp;
    private int ACentralPort;

    private ServerSocket serverSocket;


    public Bank(int portNumber){
        bankClientMap = Collections.synchronizedMap(new HashMap<>());
        sc = new Scanner(System.in);
        this.bankPortNumber = portNumber;
        try{
            serverSocket = new ServerSocket(bankPortNumber);
        } catch(Exception e){
            e.printStackTrace();
        }

        System.out.println("Assuming that Auction central is waiting for connection.");


        scanCentralPort();
        scanCentralIp();

        System.out.println("Creating Central socket with port: " + ACentralPort
                + " ip: " + ACentralIp);

        createConnection();

    }

    public void createConnection(){
        Thread bankCentralThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    System.out.println("Waiting for Central connection...");
                    BankCentralClient centralClient =
                            new BankCentralClient(new Socket(ACentralIp,ACentralPort),Bank.this);
                    System.out.println("Connected with Central.");

                    while(true){
                        System.out.println("Waiting for Agent connection...");
                        Socket clientSocket = serverSocket.accept();
                        System.out.println("Connected with an Agent.");
                        // will start thread itself.
                        new BankAgentClient(clientSocket,Bank.this, account_ID_val);

                    }
                } catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
        bankCentralThread.start();
    }

    /**
     *
     * @param bankClient
     * @return the key that the bankClient is associated with.
     */
    public int register(BankAgentClient bankClient){
        int bankKey = account_ID_val;
        bankClientMap.put(new Integer(account_ID_val),bankClient);
        account_ID_val++;
        System.out.println("registered bankkey" + bankKey);
        return bankKey;
    }

    /**
     * Assumes isValidKey was used to check.
     * @param key
     * @return
     */
    public BankAgentClient getBankAgentClient(int key){
        return bankClientMap.get(new Integer(key));
    }

    public boolean isValidKey(int key){
        return bankClientMap.containsKey(new Integer(key));
    }

    private void scanCentralPort() {
        while (true) {
            System.out.println("Please input the port number for Central.");
            if (sc.hasNextInt()) {
                ACentralPort = sc.nextInt();
                break;
            }
            System.out.println("Invalid port number for Central.");
        }
    }

    private void scanCentralIp(){
        while(true){
            System.out.println("Please input IP for Central.");
            if(sc.hasNext()){
                ACentralIp = sc.next();
                break;
            }
            System.out.println("Invalid ip for Central.");
        }
    }



    public static void main(String[] args){
        int portNumber = Integer.parseInt(args[0]);
        new Bank(portNumber);

    }


}

