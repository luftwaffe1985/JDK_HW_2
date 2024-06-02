package client;

public interface ClientView { // GUI abstraction interface

    void showMessage(String message); // GUI message display

    void disconnectedFromServer(); // Server-initiated server disconnection
}