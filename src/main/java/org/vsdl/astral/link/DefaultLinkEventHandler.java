package org.vsdl.astral.link;

public class DefaultLinkEventHandler implements LinkEventHandler {
    @Override
    public void handleMessageReception(String message, int linkId) {
        System.out.println("DataLink " + linkId + " received message: " + message);
    }

    @Override
    public void handleLinkException(Exception e, int linkId) {
        System.out.println("DataLink " + linkId + " threw exception: " + e);
    }

    @Override
    public void handleClosure(int linkId) {
        System.out.println("DataLink " + linkId + " closed.");
    }
}
