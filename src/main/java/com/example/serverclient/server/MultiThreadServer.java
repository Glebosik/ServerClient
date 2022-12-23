package com.example.serverclient.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.FileHandler;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class MultiThreadServer {

    static ExecutorService executeIt = Executors.newCachedThreadPool();
    static Database db = new Database();


    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(5548)) {
            LogManager.getLogManager().readConfiguration(
                    MultiThreadServer.class.getResourceAsStream("logging.properties"));
            FileHandler fh = new FileHandler("log.txt");
            Logger log = Logger.getLogger(MonoThreadClientHandler.class.getName());
            log.addHandler(fh);
            log.setUseParentHandlers(false);
            System.out.println("Server is up.");
            while (!server.isClosed()) {
                Socket client = server.accept();
                executeIt.execute(new MonoThreadClientHandler(client, log));
                System.out.println("Connection accepted.");
            }
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}