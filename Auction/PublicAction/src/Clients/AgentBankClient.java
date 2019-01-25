package Clients;

import Message.*;
import Agent.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

import java.net.Socket;

public class AgentBankClient extends Client{

    private Agent agent;

    /**
     * constructor takes socket and agent
     * @param socket to be set with parent
     * @param agent to be set
     */
    public AgentBankClient(Socket socket,Agent agent){
        super(socket);
        this.agent = agent;
    }

    /**
     * send bank message that this agent need a bank account
     */
    public void setUpAccountWithBank(){
        Message msg = new Message(MessageType.OpenBankAccount);
        try {
            getOut().writeObject(msg);
            getOut().flush();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * thread that continuously request an update of the balance
     */
    public void startUpdateThread(){
        Message msg = new Message(MessageType.RequestBankBalance,agent.getBankKey());
        Runnable run = new Runnable() {
            Timeline timeline;
            @Override
            public void run() {

                timeline = new Timeline(new KeyFrame(
                        Duration.millis(1000),
                        ae -> {
                            if(isRunning()) {
                                try {
                                    getOut().writeObject(msg);
                                    getOut().flush();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else{
                                timeline.stop();
                            }
                        }));
                    timeline.setCycleCount(Animation.INDEFINITE);
                    timeline.play();
            }
        };
        Thread t = new Thread(run);
        t.start();
    }

    /**
     * processes msg based on what message it is
     * @param msg to process
     */
    @Override
    public void processMessage(Message msg) {
        if(msg.getType().equals(MessageType.BankAccountOpened)){
            agent.setBankAccountNumber(msg.getAccountNum());
            agent.setBankKey(msg.getBankKey());
            //agent.updateBalance(msg.getBalance());
            System.out.println("made account with key: " + agent.getBankKey() );
            startUpdateThread();
        }
        else if(msg.getType().equals(MessageType.SendBankBalance)){
            agent.updateBalance(msg.getBalance(), msg.getPendingBalance());
        }else{
            System.out.println(msg.getType());
            System.err.println("Unknown Message :(");
        }
    }

}
