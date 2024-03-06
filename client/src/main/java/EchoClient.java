import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class EchoClient {
    private final String host;
    private final int port;

    private EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public static EchoClient connectTo(int port) {
        var localhost = "127.0.0.1";
        return new EchoClient(localhost, port);
    }

    public void run() {
        System.out.println("напиши 'bye' чтоб закрыть программу \n\n\n");
        try (var socket = new Socket(host, port)) {
            var scanner = new Scanner(System.in, StandardCharsets.UTF_8);
            var output = socket.getOutputStream();
            var writer = new PrintWriter(output);
            try (scanner; writer) {
                while (true) {
                    var message = scanner.nextLine();
                    writer.write(message);
                    writer.write(System.lineSeparator());
                    writer.flush();
                    if ("bye".equalsIgnoreCase(message) || "close".equalsIgnoreCase(message))
                        return;
                }
            }
        } catch (NoSuchElementException e) {
            System.out.println("Connection dropped!");
        } catch (IOException e) {
            System.out.printf("Can't connect to %s:%s !%n", host, port);
        }
    }
}
