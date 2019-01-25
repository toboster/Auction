package Clients;

import Message.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Client extends Thread{

    protected Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private boolean running = true;

    /**
     * parent constructor.
     * This make in input/output streams given a socket
     * @param socket used to make streams
     */
    public Client(Socket socket){
        System.out.println("made");
        this.socket = socket;
        try {
            out = new ObjectOutputStream(this.socket.getOutputStream());
            System.out.println("made out");
            in = new ObjectInputStream(this.socket.getInputStream());
            System.out.println("made in");
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public Client(Socket socket, ObjectOutputStream out, ObjectInputStream in){
        this.socket = socket;
        this.out = out;
        this.in = in;
    }

    /**
     * Waits for a message and processes it
     */
    public void run() {
        while(running){
            try{
                Message msg = (Message)in.readObject();
                if(msg.getType().equals(MessageType.StopClient)){

                    running = false;
                    break;

                }else if(msg.getType() != null){
                    processMessage(msg);
                }
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("Connection broke");
                running = false;
                break;
                //e.printStackTrace();
                //System.err.println("Error reading object from input stream");
            }

        }
        try {
            socket.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public boolean isRunning() {
        return running;
    }

    /**
     *
     * @return the output stream
     */
    public ObjectOutputStream getOut() {
        return out;
    }
    /**
     * method ever child need to make
     * @param msg to process
     */
    public abstract void processMessage(Message msg);
    public void sendStop(){
        try {
            System.out.println("pie");
            running = false;
            getOut().writeObject(new Message(MessageType.StopClient));
            socket.close();
        }catch (Exception e){

        }
    }
}
