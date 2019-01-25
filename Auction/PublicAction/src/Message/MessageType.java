package Message;

public enum MessageType {
    OpenBankAccount, BankAccountOpened, OpenAuctionAccount, AuctionAccountOpened, RegisterAuctionHouse,
    RequestBankBalance, SendBankBalance, AuctionUpdate, AuctionClosing, AgentClosing, CheckFunds, StopClient,
    BidAccept, BidDecline, OutBid, Bid, SendBankBalanceFail, DeductFunds, ItemWon
}
