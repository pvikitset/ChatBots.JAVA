package comp258_final_project;

import java.io.*;
import static java.util.UUID.randomUUID;
import java.util.Random;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 *
 */
public class EchoServer extends AbstractServer {
    //Class variables *************************************************

    /**
     * The default port to listen on.
     */
    final public static int DEFAULT_PORT = 5555;

    //Constructors ****************************************************
    /**
     * Constructs an instance of the echo server.
     *
     * @param port The port number to connect on.
     */
    public EchoServer(int port) {
        super(port);
    }

    //Instance methods ************************************************
    /**
     * This method handles any messages received from the client.
     *
     * @param msg The message received from the client.
     * @param client The connection from which the message originated.
     */
    public void handleMessageFromClient(Object msg, ConnectionToClient client) {
        System.out.println("Message received: " + msg + " from " + client);
        String message = msg.toString();

        if (message.charAt(0) == '#') {
            handleServerCommand(message, client);
        } else {

            //bot response goes here
            ChatBot bot = (ChatBot) client.getInfo("service_agent");
            String response = bot.getResponse(message);

            String room = client.getInfo("room").toString();
            this.sendToAllClientsInRoom(bot.getBot_name() + " : " + response, room);
        }

    }

    public void handleServerCommand(String message, ConnectionToClient client) {
        if (message.indexOf("#join") == 0) {
            joinRoom(message, client);
        } else if (message.indexOf("#signin") == 0) {
            signIn(message, client);
        } else if (message.indexOf("#yell") == 0) {
            yell(message, client);
        } else if (message.indexOf("#pm") == 0) {
            privateMessage(message, client);
        } else if (message.indexOf("#intercom") == 0) {
            intercom(message, client);
        } else if (message.indexOf("#ison") == 0) {
            isOn(message, client);
        } else if (message.indexOf("#who") == 0) {
            getUserList(client);
        }
    }

    public void getUserList(ConnectionToClient sender) {
        Thread[] clientThreadList = getClientConnections();
        String[] names = new String[clientThreadList.length];
        for (int i = 0; i < clientThreadList.length; i++) {
            ConnectionToClient client = ((ConnectionToClient) clientThreadList[i]);
            names[i] = client.getInfo("user").toString();
        }

        Envelope e = new Envelope("UserList", names);
        try {
            sender.sendToClient(e);
        } catch (Exception ex) {

        }
    }

    public void isOn(String message, ConnectionToClient client) {

        String userToLookFor = message.substring(message.indexOf(" "), message.length()).trim();

        Thread[] clientThreadList = getClientConnections();

        for (int i = 0; i < clientThreadList.length; i++) {
            ConnectionToClient clientToCheck = ((ConnectionToClient) clientThreadList[i]);
            if (clientToCheck.getInfo("user").equals(userToLookFor)) {

                try {
                    String room = clientToCheck.getInfo("room").toString();
                    String msg = userToLookFor + " is on and in room: " + room;
                    sendToClient(client.getInfo("user").toString(), msg);
                    break;
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            } else if (i >= clientThreadList.length - 1) {
                sendToClient(client.getInfo("user").toString(), userToLookFor + " is not on");
            }
        }
    }

    public void intercom(String message, ConnectionToClient client) {
        String user = client.getInfo("user").toString();
        String msgWithoutCommmand = message.substring(message.indexOf(" "), message.length());
        msgWithoutCommmand = msgWithoutCommmand.trim();
        String room = msgWithoutCommmand.substring(0, msgWithoutCommmand.indexOf(" "));
        String msg = msgWithoutCommmand.substring(msgWithoutCommmand.indexOf(" "), msgWithoutCommmand.length());

        msg = user + " : (intercom) " + msg;
        sendToAllClientsInRoom(msg, room);
    }

    public void yell(String message, ConnectionToClient client) {
        String user = client.getInfo("user").toString();
        String msg = message.substring(message.indexOf(" "), message.length());

        msg = user + " : (yell) " + msg;
        sendToAllClients(msg);
    }

    public void privateMessage(String message, ConnectionToClient client) {
        //String user = client.getInfo("user").toString();

        String msgWithoutCommmand = message.substring(message.indexOf(" "), message.length());
        msgWithoutCommmand = msgWithoutCommmand.trim();

        String user = msgWithoutCommmand.substring(0, msgWithoutCommmand.indexOf(" "));
        String msg = msgWithoutCommmand.substring(message.indexOf(" "), msgWithoutCommmand.length());

        sendToClient(user.trim(), msg.trim());
    }

    public void sendToClient(String user, String msg) {

        Thread[] clientThreadList = getClientConnections();

        for (int i = 0; i < clientThreadList.length; i++) {
            ConnectionToClient client = ((ConnectionToClient) clientThreadList[i]);
            if (client.getInfo("user").equals(user)) {

                try {
                    client.sendToClient(msg);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

    public void signIn(String message, ConnectionToClient client) {

        String msgWithoutCommmand = message.substring(message.indexOf(" "), message.length());
        msgWithoutCommmand = msgWithoutCommmand.trim();

        String userName = msgWithoutCommmand.substring(0, msgWithoutCommmand.indexOf(" "));
        userName = userName.trim();
        client.setInfo("user", userName);

        //generate guid, so we have unique room
        String guid = randomUUID().toString();
        joinRoom("#room " + guid, client);
        String room = client.getInfo("room").toString();

        //set up bot
        String bot_id = msgWithoutCommmand.substring(msgWithoutCommmand.indexOf(" "), msgWithoutCommmand.length());
        ChatBot bot_ins = new ChatBot(new Integer(bot_id.trim()));
        client.setInfo("service_agent", bot_ins);
        ChatBot bot = (ChatBot) client.getInfo("service_agent");
        String bot_name = bot.getBot_name();

        this.sendToAllClientsInRoom(bot_name + " : " + greeting(bot_name,
                client.getInfo("user").toString()), room);
    }

    private String greeting(String bot_name, String client_name) {

        String[] greet = {
            "Hi! ",
            "Hello ",
            "Greetings! ",
            "What's up? "};

        String[] introduce = {
            "My name's " + bot_name + ". ",
            "I'm " + bot_name + ". ",
            "Everybody calls me " + bot_name + ". "};

        String[] end_sentence = {
            "How can I help you today? " + client_name,
            "How are you today? " + client_name,
            "How is it going " + client_name + " ?",
            "How are you doing? " + client_name,
            "Nice to meet you " + client_name + ".",
            "How do you do?",
            "It is a pleasure to meet you " + client_name + ".",
            "What's up " + client_name + "?"};

        return greet[new Random().nextInt(greet.length)]
                + introduce[new Random().nextInt(introduce.length)]
                + end_sentence[new Random().nextInt(end_sentence.length)];
    }

    public void joinRoom(String message, ConnectionToClient client) {
        String roomName = message.substring(message.indexOf(" "), message.length());

        roomName = roomName.trim();

        client.setInfo("room", roomName);

    }

    public void sendToAllClientsInRoom(Object msg, String room) {

        Thread[] clientThreadList = getClientConnections();

        for (int i = 0; i < clientThreadList.length; i++) {
            ConnectionToClient client = ((ConnectionToClient) clientThreadList[i]);
            if (client.getInfo("room").equals(room)) {

                try {
                    client.sendToClient(msg);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        }
    }

    /**
     * This method overrides the one in the superclass. Called when the server
     * starts listening for connections.
     */
    protected void serverStarted() {
        System.out.println("Server listening for connections on port " + getPort());
    }

    /**
     * This method overrides the one in the superclass. Called when the server
     * stops listening for connections.
     */
    protected void serverStopped() {
        System.out.println("Server has stopped listening for connections.");
    }

    // Lab 1.5
    protected void clientConnected(ConnectionToClient client) {
        System.out.println("Client " + client + " connected");
    }

    // Lab 1.5
    synchronized protected void clientDisconnected(ConnectionToClient client) {
        System.out.println("Client " + client + " disconnected");
    }

    synchronized protected void clientException(
            ConnectionToClient client, Throwable exception) {
        clientDisconnected(client);
    }

    //Class methods ***************************************************
    /**
     * This method is responsible for the creation of the server instance (there
     * is no UI in this phase).
     *
     * @param args[0] The port number to listen on. Defaults to 5555 if no
     * argument is entered.
     */
    public static void main(String[] args) {
        int port = 0; //Port to listen on

        try {
            port = Integer.parseInt(args[0]); //Get port from command line
        } catch (Throwable t) {
            port = DEFAULT_PORT; //Set port to 5555
        }

        EchoServer sv = new EchoServer(port);

        try {
            sv.listen(); //Start listening for connections
        } catch (Exception ex) {
            System.out.println("ERROR - Could not listen for clients!");
        }
    }
}
//End of EchoServer class
