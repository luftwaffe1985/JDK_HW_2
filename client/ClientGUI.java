package client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class ClientGUI extends JFrame implements ClientView { // Application GUI operation description
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private JTextArea log;
    private JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    private JPasswordField password;
    private JButton btnLogin, btnSend;
    private JPanel headerPanel;

    private ClientController clientController; // Event response controller

    public ClientGUI() { // Constructor
        setting();
        createPanel();
        setVisible(true);
    }

    public void setClient(ClientController clientController) { // Setter
        this.clientController = clientController;
    }

    private void setting() { // GUI parameters setting
        setSize(WIDTH, HEIGHT);
        setResizable(false);
        setTitle("Client messaging field");
        // setLocation(serverWindow.getX() - 500, serverWindow.getY());
        setDefaultCloseOperation(HIDE_ON_CLOSE);
    }

    @Override
    public void showMessage(String msg) { // GUI message display
        log.append(msg + "\n");
    }

    @Override
    public void disconnectedFromServer() { // Client-server deviation
        hideHeaderPanel(true);
    }

    public void disconnectFromServer() { // Client-initiated server disconnection
        clientController.disconnectFromServer();
    }

    public void hideHeaderPanel(boolean visible) { // Top panel visibility
        headerPanel.setVisible(visible);
    }

    public void login() {
        if (clientController.connectToServer(tfLogin.getText())) { // Authorization
            headerPanel.setVisible(false);
        }
    }

    private void message() { // Message sending
        clientController.message(tfMessage.getText());
        tfMessage.setText("");
    }

    private void createPanel() { // Vidgets adding
        add(createHeaderPanel(), BorderLayout.NORTH);
        add(createLog());
        add(createFooter(), BorderLayout.SOUTH);
    }

    private Component createHeaderPanel() { // Authorization panel
        headerPanel = new JPanel(new GridLayout(2, 3));
        tfIPAddress = new JTextField("127.0.0.1");
        tfPort = new JTextField("5155");
        tfLogin = new JTextField("Peter Black");
        password = new JPasswordField("password");
        btnLogin = new JButton("login");
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });
        headerPanel.add(tfIPAddress);
        headerPanel.add(tfPort);
        headerPanel.add(new JPanel());
        headerPanel.add(tfLogin);
        headerPanel.add(password);
        headerPanel.add(btnLogin);
        return headerPanel;
    }

    private Component createLog() { // Message log panel
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    private Component createFooter() { // Message sending panel
        JPanel panel = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n') {
                    message();
                }
            }
        });

        btnSend = new JButton("send");
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                message();
            }
        });
        panel.add(tfMessage);
        panel.add(btnSend, BorderLayout.EAST);
        return panel;
    }

    @Override
    protected void processWindowEvent(WindowEvent e) { // Important event processor
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            this.disconnectedFromServer();
        }
    }
}
