import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

class Client implements Runnable {
    String username;
    Socket socket;
    Scanner in;
    PrintStream out;
    ChatServer chatServer;

    public Client(Socket socket, ChatServer chatServer){

        this.socket = socket;
        this.chatServer = chatServer;
        // запускаем поток
        new Thread(this).start();

    }

    void receive(String message){

        out.println(message);
    }

    public void run() {
        try {
            // получаем потоки ввода и вывода
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();

            // создаем удобные средства ввода и вывода
            in = new Scanner(is);
            out = new PrintStream(os);

            // читаем из сети и пишем в сеть
            out.println("Enter your username: ");
            String input = in.nextLine();

            while(chatServer.isBusyUsername(input)){
                out.println("This username is taken. Enter again: ");
                input = in.nextLine();
            }
            username = input;
            chatServer.sendAll(username, " has joined to chat");
            out.println("Welcome to chat!");
            input = in.nextLine();

            while(true) {
                chatServer.sendAll(username, input);
                input = in.nextLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}