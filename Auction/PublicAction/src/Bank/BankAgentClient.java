package Bank;

import Clients.Client;
import Message.*;

import java.net.Socket;

/**
 * Contains account information regarding an Agents bank account,
 * and the socket connection.
 *
 */
public class BankAgentClient extends Client {

    private Bank bank;
    private float balance = 1000.00f;
    private float pendingBalance = 0;
    private int accountNumber;

    public BankAgentClient(Socket socket, Bank bank, int accountNumber) {
        super(socket);
        this.bank = bank;
        this.accountNumber = accountNumber;
        start();
    }

    @Override
    public void processMessage(Message msg) {

        switch (msg.getType()) {
            case OpenBankAccount:
                openAccount(msg);
                break;
            case RequestBankBalance:
                getBalance(msg);
                break;
            default:
                System.out.println("Error not a valid Message type :" + msg.getType());

        }

    }

    private void openAccount(Message msg) {
        int bankKey = bank.register(this);
        Message sucessMessage = new Message(MessageType.BankAccountOpened, bankKey, accountNumber);
        sendMessage(sucessMessage);
    }

    private void getBalance(Message msg) {
        MessageType status;
        float resultBalance = 0;
        float pendBalance = 0;
        // is the given key in message valid, not really needed since unique connection.
        status = bank.isValidKey(msg.getBankKey()) ?
               MessageType.SendBankBalance : MessageType.SendBankBalanceFail;
        // gets balance only if key was valid.
        if(status.equals(MessageType.SendBankBalance)){
            pendBalance = pendingBalance;
          resultBalance = balance;
        }
        // order of balance is important.
        sendMessage(new Message(status, resultBalance, pendBalance));
    }

    public int getAccountNumber(){
        return accountNumber;
    }

    /**
     * @return balance that depends on the current pending balance.
     */
    public float getAvailableBalance(){
        if(balance < pendingBalance){
            System.out.println("Invalid pending Balance Error : balance," +
                    balance + " pending, " + pendingBalance);
        }
        return balance - pendingBalance;
    }

    /**
     * Assumes that the method getAvailableBalance was used to check.
     * @param bid
     */
    public void processHold(float bid){
        pendingBalance += bid;
    }

    /**
     * Transaction has failed, release hold on given bid and deduct balance.
     * @param bid
     */
    public void adjustBalanceSuccess(float bid){
        pendingBalance -= bid;
        balance -= bid;
    }

    /**
     * Transaction has failed, release hold on given the amount of bid.
     * @param bid
     */
    public void adjustBalanceFail(float bid){
        pendingBalance -= bid;
    }

    public void sendMessage(Message msg) {
        try {
            getOut().flush();
            getOut().writeObject(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}



