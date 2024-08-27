package src;

import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
    public void downloadFile(String host, int port, String filename) {
        try {
            Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(filename);

            InputStream is = socket.getInputStream();
            FileOutputStream fos = new FileOutputStream("downloads/" + filename);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = is.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            fos.close();
            is.close();
            socket.close();

            System.out.println("Downloaded file: " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
