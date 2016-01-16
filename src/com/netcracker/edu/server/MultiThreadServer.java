package com.netcracker.edu.server;

import com.netcracker.edu.commands.AbstractCommand;
import com.netcracker.edu.commands.CommandsEngine;
import com.netcracker.edu.dao.DAOFactory;
import com.netcracker.edu.session.SecurityContextHolder;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.security.AccessControlException;
import java.util.Arrays;

/**
 * Created by Zhassulan on 29.11.2015.
 */
public class MultiThreadServer implements Runnable {
    private static final Logger logger = LogManager.getLogger(MultiThreadServer.class);
    private Socket cSocket;

    public MultiThreadServer(Socket cSocket) {
        this.cSocket = cSocket;
    }

    @Override
    public void run() {
        try (PrintWriter out =
                     new PrintWriter(cSocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(cSocket.getInputStream()))) {

            String input;
            while ((input = in.readLine()) != null) {
                try {

                    String[] splittedCommand = input.toLowerCase().split(" ");
                    AbstractCommand command = CommandsEngine.getInstance().getCommand(splittedCommand[0].toLowerCase());
                    int executionCode;
                    if (command != null) {
                        executionCode = command.execute(Arrays.copyOfRange(splittedCommand, 1, splittedCommand.length), SecurityContextHolder.getLoggedHolder());
                        out.println(executionCode);
                        continue;
                    }
                    if (splittedCommand[0].equals("help")) {
                        CommandsEngine.getInstance().getHelp();
                        continue;
                    }
                    logger.warn("Unsupported command");
                    out.println(-5);
                } catch (IllegalArgumentException e) {
                    logger.error(e);
                    out.println(1);
                } catch (AccessControlException | AccessDeniedException ace) {
                    logger.warn(ace.toString());
                    out.println(1);
                }
            }
        } catch (SocketException e) {
            logger.error(e);
            try {
                DAOFactory.getDAObject().updateUser(SecurityContextHolder.getLoggedHolder());
                SecurityContextHolder.removeUserFromSignedUsers();
            } catch (Exception ex) {
                logger.error(ex);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                cSocket.close();
            } catch (IOException e) {
               logger.error(e);
            }
        }
    }
}
