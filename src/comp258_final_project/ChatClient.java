package comp258_final_project;

import java.io.*;
import comp258_final_project.GUIConsole;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 *
 *
 */
public class ChatClient extends AbstractClient {
    //Instance variables **********************************************

    /**
     * The interface type variable. It allows the implementation of the display
     * method in the client.
     */
    Displayable clientUI;

    //Constructors ****************************************************
    /**
     * Constructs an instance of the chat client.
     *
     * @param host The server to connect to.
     * @param port The port number to connect on.
     * @param clientUI The interface type variable.
     */
    public ChatClient(String host, int port, String userName, Integer botID, Displayable clientUI)
            throws IOException {
        super(host, port); //Call the superclass constructor
        this.clientUI = clientUI;
        openConnection();
        processClientCommand("#signin " + userName + " " + botID);
    }
    
    public ChatClient(String host, int port, String userName,Displayable clientUI)
            throws IOException {
        super(host, port); //Call the superclass constructor
        this.clientUI = clientUI;
        openConnection();
        processClientCommand("#signin " + userName);
    }

    public ChatClient(String host, int port, Displayable clientUI)
            throws IOException {
        super(host, port); //Call the superclass constructor
        this.clientUI = clientUI;
        openConnection();
    }

    //Instance methods ************************************************
    /**
     * This method handles all data that comes in from the server.
     *
     * @param msg The message from the server.
     */
    public void handleMessageFromServer(Object msg) {

        if (msg instanceof Envelope) {
            handleEnvelope((Envelope)msg);
        } else {

            clientUI.display(msg.toString());
        }
    }
    public void handleEnvelope(Envelope e){
        if(e.getName().equals("UserList")){
            ((GUIConsole)clientUI).displayList(e.getContents());
        }
    }
    /**
     * This method handles all data coming from the UI
     *
     * @param message The message from the UI.
     */
    public void handleMessageFromClientUI(String message) {
        try {
           /* if (message.charAt(0) == '#') {
                processClientCommand(message);

            } else {*/
                sendToServer(message);
           // }
        } catch (IOException e) {
            clientUI.display("Could not send message to server.  Terminating client.");
            quit();
        }
    }

    /*
  This method terminates the client
     */
    public void processClientCommand(String message) throws IOException {

        if (message.equals("#quit")) {
            quit();
        } else if (message.equals("#logoff")) {
            logoff();
        } else if (message.equals("#login")) {
            login();
        } else if (message.equals("#getHost")) {
            clientUI.display("Host: " + getHost());
        } else if (message.indexOf("#setHost") == 0) {
            setNewHost(message);
        } else if (message.equals("#getPort")) {
            clientUI.display("Port: " + getPort());

        } else if (message.equals("#setPort")) {
            setNewPort(message);

        } else if (isConnected()) {

            try {
                sendToServer(message);
            } catch (Exception ex) {
                clientUI.display("Failed to send commant to server");
            }
        }

    }

    /**
     * This method terminates the client.
     */
    public void quit() {
        try {
            closeConnection();
        } catch (IOException e) {
        }
        System.exit(0);
    }

    public void logoff() {
        try {
            closeConnection();
        } catch (IOException e) {
            System.out.println("Something terrible has happened");
        }
    }

    public void setNewHost(String message) {

        String host = message.substring(message.indexOf(" "), message.length());
        clientUI.display("New Host " + host);
        setHost(host.trim());

    }

    public void setNewPort(String message) {

        String port = message.substring(message.indexOf(""), message.length());
        clientUI.display("New Host " + port);
        setPort(Integer.parseInt(port.trim()));

    }

    public void login() {

        if (!isConnected()) {
            try {
                openConnection();
            } catch (IOException ioe) {
                System.out.println("Had to do it somewhere");

            }
        } else {
            clientUI.display("You are already connected");
        }

    }

    protected void connectionException(Exception exception) {
        clientUI.display("Connection to the server was lost");
        //quit();
    }
}




//End of ChatClient class
