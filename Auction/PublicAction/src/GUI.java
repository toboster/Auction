import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import Agent.*;


public class GUI extends Application {

    private Agent agent;
    private Scene auction;
    private Label balance = new Label();
    private Label item = new Label();
    private Label currBid = new Label();
    private Label bidStatus = new Label();
    ComboBox houses = new ComboBox();

    /**
     * starts GUI
     * @param stage of the gui
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Public Auction");
        Pane menuLayout = new Pane();
        Pane auctionLayout = new Pane();

        setUpMenu(menuLayout,stage);
        setUpAuction(auctionLayout);

        Scene menu = new Scene(menuLayout,500,240);
        auction = new Scene(auctionLayout,700,190);
        stage.setScene(menu);
        stage.setResizable(false);
        stage.show();
        stage.setOnCloseRequest(event -> {
            if(agent != null) agent.stopAll();
        });
    }

    /**
     * sets up the auction scene of the gui
     * @param layout that things are to be added to
     */
    private void setUpAuction(Pane layout){
        Font size = new Font(20);
        //background
        ImageView iv = new ImageView();
        iv.setViewport(new Rectangle2D(0,0,700,190));
        iv.setImage(new Image(getClass().getClassLoader().getResourceAsStream("GuiBackGround/GUIBackGround.jpg")
                ,700,190,false,false));
        //labels
        balance.setTranslateX(20);
        balance.setTranslateY(10);
        balance.setFont(size);

        Label auctionHouses = new Label("Auction Houses:");
        auctionHouses.setTranslateY(40);
        auctionHouses.setTranslateX(20);
        auctionHouses.setFont(size);

        item.setText("Item being bid on: <Insert Item Name Here>");
        item.setTranslateX(20);
        item.setTranslateY(70);
        item.setFont(size);

        currBid.setText("Current Bid: $0");
        currBid.setTranslateY(100);
        currBid.setTranslateX(20);
        currBid.setFont(size);

        Label b = new Label("Your Bid:");
        b.setTranslateY(130);
        b.setTranslateX(20);
        b.setFont(size);

        Label result = new Label("");
        result.setTranslateX(20);
        result.setTranslateY(160);
        result.setFont(size);

        bidStatus.setTranslateY(160);
        bidStatus.setTranslateX(20);
        bidStatus.setFont(size);
        //combo box with auction houses
        //houses.getItems().addAll("Auction House 1", "Auction House 2", "Auction House 3");
        houses.setTranslateX(190);
        houses.setTranslateY(40);


        //text field for bid
        TextField bid = new TextField();
        bid.setTranslateY(130);
        bid.setTranslateX(115);

        //buttons
        Button change = new Button("Change Auction House");
        change.setTranslateY(40);
        change.setTranslateX(350);
        change.setOnAction(e->{
            if(houses.getValue() != null) {
                agent.changeAuctionHouse(Integer.parseInt(houses.getValue().toString()));
            }
        });

        Button placeBid = new Button("Place Bid");
        placeBid.setTranslateY(130);
        placeBid.setTranslateX(290);
        placeBid.setOnAction(event -> {
            result.setText("");
            if(bid.getText().equals("")){
                result.setText("Bid is empty.");
            }else {
                try {
                    Float.parseFloat(bid.getText());
                } catch (Exception e) {
                    result.setText("Bid must be a float ex: \"128.43\"");
                }
                if(result.getText().equals("")){
                    //result.setText("Sending Bid");
                    agent.submitBid(Float.parseFloat(bid.getText()));
                }
            }
        });

        //adds everything from above to gui layout
        layout.getChildren().addAll(iv,balance,auctionHouses,houses,change,item,currBid,b,bid,placeBid,result,bidStatus);
    }

    /**
     * sets up menu scene
     * @param layout for things to be added to
     * @param stage of the gui
     */
    private void setUpMenu(Pane layout,Stage stage){
        Font size = new Font(20);

        //background
        ImageView iv = new ImageView();
        iv.setViewport(new Rectangle2D(0,0,500,240));
        iv.setImage(new Image(getClass().getClassLoader().getResourceAsStream("GuiBackGround/GUIBackGround.jpg")
                ,500,240,false,false));

        //labels
        Label error = new Label("");
        error.setTranslateY(160);
        error.setTranslateX(20);
        error.setFont(size);

        Label nameLabel = new Label("Name:");
        nameLabel.setTranslateY(10);
        nameLabel.setTranslateX(20);
        nameLabel.setFont(size);

        Label bankIpLabel = new Label("Bank IP:");
        bankIpLabel.setTranslateY(40);
        bankIpLabel.setTranslateX(20);
        bankIpLabel.setFont(size);

        Label bankPortLabel = new Label("Bank Port:");
        bankPortLabel.setTranslateY(70);
        bankPortLabel.setTranslateX(20);
        bankPortLabel.setFont(size);

        Label auctionIpLabel = new Label("Auction Central IP:");
        auctionIpLabel.setTranslateY(100);
        auctionIpLabel.setTranslateX(20);
        auctionIpLabel.setFont(size);

        Label auctionPortLabel = new Label("Auction Central Port:");
        auctionPortLabel.setTranslateY(130);
        auctionPortLabel.setTranslateX(20);
        auctionPortLabel.setFont(size);

        //text fields
        TextField nameText = new TextField();
        nameText.setTranslateY(10);
        nameText.setTranslateX(230);

        TextField bankIpText = new TextField();
        bankIpText.setTranslateY(40);
        bankIpText.setTranslateX(230);

        TextField bankPortText = new TextField();
        bankPortText.setTranslateY(70);
        bankPortText.setTranslateX(230);

        TextField auctionIpText = new TextField();
        auctionIpText.setTranslateY(100);
        auctionIpText.setTranslateX(230);

        TextField auctionPortText = new TextField();
        auctionPortText.setTranslateY(130);
        auctionPortText.setTranslateX(230);

        nameText.setText("steven");
        bankIpText.setText("64.106.20.210");
        bankPortText.setText("2222");
        auctionIpText.setText("64.106.20.212");
        auctionPortText.setText("2233");

        //submit button
        Button b = new Button("Submit");
        b.setTranslateX(335);
        b.setTranslateY(190);
        b.setOnAction(event -> {
            if(!nameText.getText().equals("") && !bankIpText.getText().equals("") && !bankPortText.getText().equals("")
                    && !auctionIpText.getText().equals("") && !auctionPortText.getText().equals("")) {
                error.setText("");
                try{
                    Integer.parseInt(bankPortText.getText());
                }catch(Exception e){
                    error.setText("Bank Port must be an Integer.");
                    System.out.println("bad");
                }
                try{
                    Integer.parseInt(auctionPortText.getText());
                }catch(Exception e){
                    error.setText("Auction Central Port must be an Integer.");
                }
                if(error.getText().equals("")){
                    agent = new Agent(nameText.getText(),Integer.parseInt(bankPortText.getText()),bankIpText.getText()
                            ,Integer.parseInt(auctionPortText.getText()),auctionIpText.getText(),balance,item,currBid,bidStatus,houses);
                    stage.setScene(auction);
                    stage.setTitle(agent.getName() + "'s Agent");
                }

            }else{
                error.setText("Not all inputs have been filled.");
            }
        });

        //adds everything to layout
        layout.getChildren().addAll(iv,nameLabel,nameText,bankIpLabel,bankIpText,bankPortText,bankPortLabel
                ,auctionPortLabel,auctionPortText,auctionIpLabel,auctionIpText,b,error);
    }

    public static void main(String[] args){
        //new testServer(2222);

        launch(args);
    }
}
