import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String args[]) throws IOException {
        ServerSocket s = new ServerSocket(12345);
        while(true){
            Socket socket = s.accept();
            Banco banco = new Banco();
            new Thread(new TaskRunner(socket, banco)).start();
        }
    }
}
