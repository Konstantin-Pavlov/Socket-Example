import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServerThreads {
    private final int port;
    private final ExecutorService pool = Executors.newCachedThreadPool();
    private boolean run = true;

    public EchoServerThreads(int port) {
        this.port = port;
    }

    public static EchoServerThreads bindToPort(int port) {
        return new EchoServerThreads(port);
    }

    public void run() {
        try (var server = new ServerSocket(port)) {
            System.out.println("Сервер with threads работает...");
            while (!server.isClosed()) {
                Socket clientSocket = server.accept();
                pool.submit(() -> handle(clientSocket));
            }
        } catch (IOException e) {
            System.out.printf("вероятнее всего порт  %s занят. %n", port);
            System.out.println(e.getMessage());
        }
    }

    private void handle(Socket socket) {
        System.out.printf("connected client: %s%n", socket);
        try (
                socket;
                Scanner reader = Util.getReader(socket);
                PrintWriter writer = Util.getWriter(socket);
        ) {
            sendResponse("Hello " + socket, writer);
            while (true) {
                String message = reader.nextLine();
                if (Util.isEmptyMessage(message) || Util.isExitMessage(message)) {
                    break;
                }
                if(message.equalsIgnoreCase("close")){
                    System.exit(0);
                }
                sendResponse(message.toUpperCase(), writer);
            }

        } catch (NoSuchElementException e) {
            System.out.println("The client closed the connection");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        System.out.printf("Client disconnected: %s%n", socket);
    }

    private void sendResponse(String response, Writer writer) throws IOException {
        System.out.printf("Got: %s %n", response);
        writer.write(response);
        writer.write(System.lineSeparator());
        writer.flush();
    }

}
