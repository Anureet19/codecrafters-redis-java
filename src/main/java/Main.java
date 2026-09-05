import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final ExecutorService pool = Executors.newFixedThreadPool(100);
  public static void main(String[] args){
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    //  Uncomment the code below to pass the first stage
        ServerSocket serverSocket = null;
        int port = 6379;
        try {
          serverSocket = new ServerSocket(port);
          // Since the tester restarts your program quite often, setting SO_REUSEADDR
          // ensures that we don't run into 'Address already in use' errors
          serverSocket.setReuseAddress(true);
          while(true) {
              // Wait for connection from client.
              Socket clientSocket = serverSocket.accept();
              pool.execute(() -> handleClient(clientSocket));
          }
        } catch (IOException e) {
          System.out.println("IOException: " + e.getMessage());
        }
  }
    private static void handleClient(Socket clientSocket) {
      try {
          InputStream inputStream = clientSocket.getInputStream();
          byte[] buffer = new byte[1024];
          while ((inputStream.read(buffer)) != 1) {
              clientSocket.getOutputStream().write("+PONG\r\n".getBytes());
          }
      }catch (IOException e) {
          throw new RuntimeException(e);
      } finally {
          try {
              if (clientSocket != null) {
                  clientSocket.close();
              }
          } catch (IOException e) {
              System.out.println("IOException: " + e.getMessage());
          }
      }
    }
}
