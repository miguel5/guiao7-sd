import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Banco {
    private int idCounter;
    private Map<Integer, Conta> contas;
    private ReentrantLock lock;

    public Banco(){
        idCounter = 0;
        this.contas = new HashMap<>();
        this.lock = new ReentrantLock();
    }

    public int createAccount(float initialBalance){
        this.lock.lock();
        int id = idCounter;
        this.contas.put(id, new Conta(initialBalance));
        this.idCounter ++;
        this.lock.unlock();

        return id;
    }

    public float closeAccount(int id) throws InvalidAccount{
        this.lock.lock();
        if(contas.containsKey(id)){
            contas.get(id).getLock().lock();
            Conta c = contas.remove(id);
            c.getLock().unlock();
            this.lock.unlock();
            return c.getSaldo();
        }
        else{
            this.lock.unlock();
            throw new InvalidAccount(id);
        }
    }

    public void transfer(int from, int to, float amount) throws InvalidAccount, NotEnoughFunds{
        this.lock.lock();
        Conta cFrom = this.contas.get(from);
        Conta cTo = this.contas.get(to);
        this.lock.unlock();
        if(cFrom == null) throw new InvalidAccount(from);
        if(cTo == null) throw new InvalidAccount(to);

        cFrom.getLock().lock();
        cTo.getLock().lock();
        try{
            cFrom.levantar(amount);
            cTo.depositar(amount);
        }
        finally {
            cFrom.getLock().unlock();
            cTo.getLock().unlock();
        }

        // registar movimento
        cFrom.addMovimento(new Movimento("Transferencia", "Out to " + to, amount, cFrom.getSaldo()));
        cTo.addMovimento(new Movimento("Transferencia", "In from " + from, amount, cTo.getSaldo()));

    }


    public float totalBalance(int accounts[]) throws InvalidAccount{
        float total = 0;

        this.lock.lock();
        for(int i = 0; i < accounts.length; i++) {
            if (contas.get(accounts[i]) == null) throw new InvalidAccount(accounts[i]);
            contas.get(accounts[i]).getLock().lock();
        }

        for(Integer id : accounts){
            total += contas.get(id).getSaldo();
            contas.get(id).getLock().unlock();
        }
        this.lock.unlock();

        return total;
    }

    public float consultarSaldo(int id){
        return this.contas.get(id).getSaldo();
    }

    public ArrayList<Movimento> consultarMovimentos(int id){
        return this.contas.get(id).getMovimentos();
    }
}

