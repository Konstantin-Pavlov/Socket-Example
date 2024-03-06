import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Util {
    private Util() {
    }

    public static PrintWriter getWriter(Socket socket) throws IOException {
        OutputStream outputStream = socket.getOutputStream();
        return new PrintWriter(outputStream);
    }

    public static Scanner getReader(Socket socket) throws IOException {
        InputStream inputStream = socket.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        return new Scanner(inputStreamReader);
    }

    public static boolean isExitMessage(String message){
        return "buy".equalsIgnoreCase(message);
    }

    public static boolean isEmptyMessage(String message){
        return message.isEmpty() || message.isBlank();
    }
}
