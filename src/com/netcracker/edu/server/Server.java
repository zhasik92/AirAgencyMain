package com.netcracker.edu.server;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Zhassulan on 29.11.2015.
 */
public class Server {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private static final int PORTNUMBER = 4444;

    public static void execute() throws IOException {


        ServerSocket sSock = new ServerSocket(PORTNUMBER);
        logger.info("Listening");
        while (true) {
            logger.info("Waiting for client");
            Socket sock = sSock.accept();
            System.out.println("Connected");
            new Thread(new MultiThreadServer(sock)).start();
        }
    }


}
