import java.io.*;

public class Main {
    public static void main(String[] args) {
        Peer peer1 = new Peer("Peer1", 5000);
        Peer peer2 = new Peer("Peer2", 5001);

        // Share files on each peer
        peer1.shareFile("file1.txt");
        peer2.shareFile("file2.txt");

        // Start listening on each peer
        new Thread(() -> {
            try {
                peer1.startListening();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                peer2.startListening();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        // Client to download files
        Client client = new Client();

        // Peer1 downloads a file from Peer2
        client.downloadFile("localhost", 5001, "file2.txt");
    }
}
