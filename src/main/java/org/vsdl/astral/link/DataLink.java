package org.vsdl.astral.link;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class DataLink extends Thread {

    private static int nextId = 0;

    private final int ID;

    private final BufferedReader READER;

    private final LinkEventHandler HANDLER;

    private final Socket SOCK;

    private boolean isActive;

    public DataLink(Socket socket, LinkEventHandler handler) throws IOException {
        ID = nextId++;
        SOCK = socket;
        READER = new BufferedReader(new InputStreamReader(SOCK.getInputStream()));
        HANDLER = handler;
    }

    public void receive() {
        try {
            do {
                Thread.sleep(10);
                if (SOCK.getInputStream().available() <= 0) continue;
                HANDLER.handleMessageReception(READER.readLine(), ID);
            } while (isActive);
            READER.close();
        } catch (InterruptedException | IOException e) {
            HANDLER.handleLinkException(e, ID);
        }
        HANDLER.handleClosure(ID);
    }

    public void transmit(String message) throws IOException {
        SOCK.getOutputStream().write(message.getBytes());
    }

    @Override
    public void run() {
        isActive = true;
        receive();
    }

    public void close() {
        isActive = false;
    }

    public int getID() {
        return ID;
    }

    public boolean isActive() {
        return isActive;
    }
}
