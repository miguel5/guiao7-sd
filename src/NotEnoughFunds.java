public class NotEnoughFunds extends Exception {

    public NotEnoughFunds(double montante){
        super("Not enough funds: " + montante);
    }
}
