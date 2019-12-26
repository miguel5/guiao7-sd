import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class TaskRunner implements Runnable {

    private Socket socket;
    private Banco banco;
    private PrintWriter out;
    private BufferedReader in;

    public TaskRunner(Socket socket, Banco banco) throws IOException {
        this.socket = socket;
        this.banco = banco;
        this.out = new PrintWriter(this.socket.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void run(){

        try {
            String clInput = "";
            String[] tokens;

            while ((clInput = this.in.readLine()) != null) {

                if (clInput.equals("quit"))
                    break;

                // dividir por espaços
                tokens = clInput.trim().split("\\s+");

                this.runCommand(tokens);

                this.out.flush();
            }

            this.socket.shutdownOutput();
            this.socket.shutdownInput();
            this.socket.close();
            this.in.close();
            this.out.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void runCommand(String[] tokens){

        switch(tokens[0]){

            case "CRIAR_CONTA":
                this.criarConta(tokens[1]);
                // debug
                System.out.println("Saldo inicial: " + tokens[1]);
                break;

            case "FECHAR_CONTA":
                this.fecharConta(tokens[1]);
                // debug
                System.out.println("Fechar conta " + tokens[1]);
                break;

            case "TRANSFERIR":
                transferir(tokens[1], tokens[2], tokens[3]);
                // debug
                System.out.println("Transferir " + tokens[3] + " de " + tokens[1] + " para " + tokens[2]);
                break;

            case "CONSULTAR_SALDO":
                this.consultarSaldo(tokens[1]);
                // debug
                System.out.println("Saldo atual: " + banco.consultarSaldo(Integer.parseInt(tokens[1])));
                break;

            case "MOVIMENTOS":
                movimentos(tokens[1]);
                break;

            default:
                this.out.println("Comando inválido!");
                // debug
                System.out.println("Comando invalido!");
                break;
        }
    }

    private void criarConta(String saldoInicial){
        int id = this.banco.createAccount(Float.parseFloat(saldoInicial));
        this.out.println("ID: " + id + "; Saldo inicial: " + saldoInicial);
    }

    private void fecharConta(String id){
        try {
            banco.closeAccount(Integer.parseInt(id));
        } catch (InvalidAccount invalidAccount) {
            invalidAccount.printStackTrace();
        }
        this.out.println("A conta com o ID " + id + " foi fechada com sucesso!");
    }

    private void transferir(String from, String to, String amount){
        try {
            banco.transfer(Integer.parseInt(from), Integer.parseInt(to), Float.parseFloat(amount));
        } catch (InvalidAccount invalidAccount) {
            invalidAccount.printStackTrace();
        } catch (NotEnoughFunds notEnoughFunds) {
            notEnoughFunds.printStackTrace();
        }
        this.out.println("Transferir " + amount + " de " + from + " para " + to);
    }

    private void consultarSaldo(String id){
        this.out.println("Saldo atual: " + banco.consultarSaldo(Integer.parseInt(id)));
    }

    private void movimentos(String id){
        ArrayList<Movimento> l = banco.consultarMovimentos(Integer.parseInt(id));
        String s = l.get(0).toString();
        this.out.println(s);
    }
}
