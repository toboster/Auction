package Bank;

import Clients.Client;
import Message.Message;
import Message.MessageType;

import java.net.Socket;

public class BankCentralClient extends Client {

    private Bank bank;

    private Message message;

    public BankCentralClient(Socket socket, Bank bank) {
        super(socket);
        this.bank = bank;
        start();
    }

    @Override
    public void processMessage(Message msg) {
        this.message = msg;
        switch (msg.getType()) {
            case CheckFunds:
                requestHold();
                break;
            case DeductFunds:
                successBidProcess();
                break;
            case OutBid:
                failBidProcess();
                break;
            default:
                System.out.println("Error, invalid msg type within Bank Central Client.");
        }
    }

    public void requestHold() {
        int key = message.getBankKey();
        int intHouseID = message.getAuctionHouseID();
        BankAgentClient bankAccount = null;
        boolean validKey = bank.isValidKey(key);
        float askBid = message.getAmountToCheck();
        boolean validBid = false;

        if(validKey){
            bankAccount = bank.getBankAgentClient(key);
        }
        else {
            System.out.println("Error not valid key when Requesting Hold.");
        }
        // is there enough money depending on available balance.
        if (validKey && bankAccount.getAvailableBalance() >= askBid) {
            // we can hold money on account.
            bankAccount.processHold(askBid);
            validBid = true;
        }
        message = new Message(MessageType.CheckFunds, askBid, message.getBankKey(), validBid, intHouseID);
        System.out.println("auction house ID" + message.getAuctionHouseID());
        sendMessage(message);
    }

    /**
     * Will throw null if incorrect key was given.
     */
    private void failBidProcess(){
        int key = message.getBankKey();
        BankAgentClient bankAccount = bank.getBankAgentClient(key);

        bankAccount.adjustBalanceFail(message.getAmountToCheck());
    }

    /**
     * Will throw null if incorrect key was given.
     */
    private void successBidProcess(){
        int key = message.getBankKey();
        BankAgentClient bankAccount = bank.getBankAgentClient(key);

        bankAccount.adjustBalanceSuccess(message.getAmountToCheck());
    }

    public void sendMessage(Message msg) {
        try {
            getOut().writeObject(msg);
            getOut().flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

