import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

public class Conta {
    private float saldo;
    private ArrayList<Movimento> movimentos;
    private final ReentrantLock lock;

    public Conta(float saldo){
        this.saldo = saldo;
        this.movimentos = new ArrayList<>();
        this.lock = new ReentrantLock();
    }

    public void levantar(double montante) throws NotEnoughFunds {
        if(montante > this.saldo)
            throw new NotEnoughFunds(montante);
        this.saldo -= montante;
    }

    public void depositar(double montante){
        this.saldo += montante;
    }

    public float getSaldo(){
        return this.saldo;
    }

    public ReentrantLock getLock(){
        return this.lock;
    }

    public ArrayList<Movimento> getMovimentos(){
        return this.movimentos;
    }

    public void addMovimento(Movimento m){
        this.movimentos.add(m);
    }
}
