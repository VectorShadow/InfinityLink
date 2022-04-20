package org.vsdl.astral.link;

public interface LinkEventHandler {
    void handleMessageReception(String message, int linkId);
    void handleLinkException(Exception e, int linkId);
    void handleClosure(int linkId);
}
