import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

public class WebServer implements Runnable {
    private Socket clientSocket;

    public WebServer(Socket socket) {
        this.clientSocket = socket;
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(80);
        System.out.println("서버 시작.");

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new WebServer(socket)).start();
        }
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            OutputStream out = clientSocket.getOutputStream();
            String requestLine = in.readLine();
            if (requestLine != null && !requestLine.isEmpty()) {
                System.out.println("요청 받음: " + requestLine);
                StringTokenizer tokens = new StringTokenizer(requestLine);
                tokens.nextToken();

                String responseMessage = "<html><body><h1>연결 완료</h1></body></html>";

                out.write("HTTP/1.1 200 OK\r\n".getBytes());
                out.write("Content-Type: text/html\r\n".getBytes());
                out.write(("Content-Length: " + responseMessage.length() + "\r\n").getBytes());
                out.write("\r\n".getBytes()); // 헤더 끝

                out.write(responseMessage.getBytes());
            }

            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
