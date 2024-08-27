package src;

import java.io.*;
import java.net.*;
import java.util.*;

public class Peer {
    private String peerId;
    private List<String> sharedFiles;
    private int port;

    public Peer(String peerId, int port) {
        this.peerId = peerId;
        this.port = port;
        this.sharedFiles = new ArrayList<>();
    }

    public void shareFile(String filename) {
        sharedFiles.add(filename);
    }

    public List<String> getSharedFiles() {
        return sharedFiles;
    }

    public int getPort() {
        return port;
    }

    public void startListening() throws IOException {
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Peer " + peerId + " is listening on port " + port);

        while (true) {
            Socket socket = serverSocket.accept();
            new Thread(new FileTransferHandler(socket)).start();
        }
    }

    private class FileTransferHandler implements Runnable {
        private Socket socket;

        public FileTransferHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String filename = in.readLine();

                File file = new File("files/" + filename);
                if (!file.exists()) {
                    System.out.println("File not found: " + filename);
                    socket.close();
                    return;
                }

                OutputStream os = socket.getOutputStream();
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[4096];
                int bytesRead;

                while ((bytesRead = fis.read(buffer)) != -1) {
                    os.write(buffer, 0, bytesRead);
                }

                fis.close();
                os.flush();
                os.close();
                socket.close();

                System.out.println("File sent: " + filename);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
