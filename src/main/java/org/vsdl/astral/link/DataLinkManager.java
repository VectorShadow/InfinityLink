package org.vsdl.astral.link;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashMap;

public class DataLinkManager extends Thread {
    private final HashMap<Integer, DataLink> LINK_MAP = new HashMap<>();
    private final ServerSocket SOCK;
    private final LinkEventHandler HANDLER;

    private boolean isActive;

    public DataLinkManager(int port, LinkEventHandler handler) throws IOException {
        SOCK = new ServerSocket(port);
        HANDLER = handler;
    }

    @Override
    public void run() {
        try {
            isActive = true;
            do {
                DataLink link = new DataLink(SOCK.accept(), HANDLER);
                LINK_MAP.put(link.getID(), link);
                link.start();
            } while (isActive);
        } catch (IOException e) {
            //todo - logging
        }
    }

    public void removeLink(int linkId) {
        LINK_MAP.remove(linkId);
    }

    public void sendMessageOnLink(int linkId, String message) throws IOException {
        DataLink link = LINK_MAP.get(linkId);
        if (link == null) throw new IllegalArgumentException("Link ID " + linkId + " did not exist.");
        if (!link.isActive()) throw new IllegalStateException("Link ID " + linkId + " no longer active.");
        LINK_MAP.get(linkId).transmit(message);
    }

    public void close() throws IOException {
        isActive = false;
        SOCK.close();
        for (DataLink link : LINK_MAP.values()) {
            link.close();
        }
    }

}
