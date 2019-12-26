import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    public static void main(String args[]){
        try {
            Socket socket = new Socket("127.0.0.1", 12345);
            String userInput = "";
            String serverResp = "";

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            while(!userInput.equals("quit")){
                Scanner s = new Scanner(System.in);
                userInput = s.nextLine();

                // envia input do user
                out.println(userInput);
                out.flush();

                // recebe resposta do servidor
                serverResp = in.readLine();
                System.out.println(serverResp);
            }

            out.flush();
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
