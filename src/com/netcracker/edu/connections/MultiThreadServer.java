package com.netcracker.edu.connections;

import com.netcracker.edu.commands.AbstractCommand;
import com.netcracker.edu.commands.CommandsEngine;
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
    private Socket csocket;

    public MultiThreadServer(Socket csocket) {
        this.csocket = csocket;
    }

    @Override
    public void run() {
        try (PrintWriter out =
                     new PrintWriter(csocket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                     new InputStreamReader(csocket.getInputStream()))) {

            String input;
            while ((input = in.readLine()) != null) {
                try {

                    String[] splittedCommand = input.toLowerCase().split(" ");
                    AbstractCommand command = CommandsEngine.getInstance().getCommand(splittedCommand[0].toLowerCase());
                    int executionCode = -1;
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
                    logger.error(e.toString());
                    out.println(1);
                }catch (AccessControlException|AccessDeniedException ace){
                    logger.warn(ace.toString());
                    out.println(1);
                }

            }
            csocket.close();
        }catch (SocketException e){
            logger.error(e.toString());
            SecurityContextHolder.removeUserFromSignedUsers();
        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {

        }
    }
}
