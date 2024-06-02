package client;

import server.ServerController;

public class ClientController { // Client logic class
    private boolean connected;
    private String name;
    private ClientView clientView;
    private ServerController server;

    public void setClientView(ClientView clientView) { // Setters
        this.clientView = clientView;
    }

    public void setServer(ServerController server) {
        this.server = server;
    }

    public boolean connectToServer(String name) { // Server connection
        this.name = name;

        if (server.connectUser(this)) {
            showOnWindow("Connection successful!\n");
            connected = true;
            String log = server.getHistory();
            if (log != null) {
                showOnWindow(log);
            }
            return true;
        } else {
            showOnWindow("Connection lost");
            return false;
        }
    }

    public void disconnectedFromServer() { // Server-initiated disconnection
        if (connected) {
            connected = false;
            clientView.disconnectedFromServer();
            showOnWindow("Disconnected from server");
        }
    }

    public void disconnectFromServer() { // User-initiated disconnection
        server.disconnectUser(this);
    }

    public void answerFromServer(String text) { // Client messaging
        showOnWindow(text);
    }

    public void message(String text) { // Server message intake
        if (connected) {
            if (!text.isEmpty()) {
                server.message(name + ": " + text);
            }
        } else {
            showOnWindow("No server connection");
        }
    }

    private void showOnWindow(String text) { // GUI message display
        clientView.showMessage(text);
    }

    public String getName() {
        return name;
    }
}