package comp258_final_project;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;
import java.util.Random;

public class ChatBot {

    //Add more bots here
    Map<Integer, String> bot_instances_dict
            = new HashMap<Integer, String>() {
                {
                    put(171, "Help Bot"); //Categories: Business, Web, Help, Facebook, Slack, Telegram, Skype
                    put(667676, "Julie"); //Categories: Fun, Entertainment, Friends, Facebook, Twitter, Telegram, Apps, Skype
                    put(12332376, "Eddie"); //Categories: Fun, Dating, Entertainment, Friends
                    put(20873, "Alice"); //Categories: Fun, Dating, Entertainment, Friends
                    put(20152181, "May Lin"); //Categories: Fun, Dating, Entertainment, Friends
                    put(28322, "Chris"); //Categories: Dating
                    put(28406, "James"); //Categories: Tech
                    put(18123853, "Tax Info Assistant"); //Categories: Misc, Entertainment, Tech, Website assistant
                    put(132686, "Alley"); //Categories: Education
                    put(20742464, "Sexy Monika"); //Categories: Education, Fun, Dating, Entertainment, Tech, Famous People, Anime
                    put(13917194, "Braveheart"); //Categories: Education, Entertainment
                }
            };

    final private static String url_base = "https://www.botlibre.com/rest/api/form-chat?";
    final private static String api_key = "4025814554033669858";

    private String bot_instance;
    private String bot_name;
    private String message_from_client;
    public String response_from_bot;

    public ChatBot() {
        //set_up_random_bot();
    }

    public ChatBot(Integer botID) {
        this.bot_instance = botID.toString();
        this.bot_name = bot_instances_dict.get(botID);
    }

    public Map<Integer, String> getBot_instances_dict() {
        return bot_instances_dict;
    }

    private void set_up_random_bot() {
        Object[] keys = bot_instances_dict.keySet().toArray();
        Object key = keys[new Random().nextInt(keys.length)];
        this.bot_instance = key.toString();
        this.bot_name = bot_instances_dict.get(key);
    }

    public String getBot_name() {
        return bot_name;
    }

    private void sendRequest() throws MalformedURLException, ProtocolException, IOException, ParserConfigurationException, SAXException {
        String urlForm = buildURLRequest();
        URL urlForGetRequest = new URL(urlForm);
        String readLine = null;
        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        int responseCode = conection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(conection.getInputStream()));
            StringBuilder responseXML = new StringBuilder();

            while ((readLine = in.readLine()) != null) {
                responseXML.append(readLine);
            }
            in.close();

            ByteArrayInputStream input = new ByteArrayInputStream(
                    responseXML.toString().getBytes("UTF-8"));
            Element root = parseXml(input);
            String msg = root.getElementsByTagName("message").item(0).getTextContent();

            this.response_from_bot = msg;

        } else {
            this.response_from_bot = "Something went wrong. I can't see you message";
        }
    }

    private Element parseXml(ByteArrayInputStream input) throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
        DocumentBuilder b = f.newDocumentBuilder();
        Document document = b.parse(input);
        return document.getDocumentElement();
    }

    private String buildURLRequest() {

        message_from_client = message_from_client.replaceAll(" ", "+");
        StringBuilder urlRequest = new StringBuilder();
        urlRequest.append(url_base);
        urlRequest.append("instance=").append(bot_instance);
        urlRequest.append("&message=").append(message_from_client);
        urlRequest.append("&application=" + api_key);

        return urlRequest.toString();
    }

    private void processResponse(String msg) {
        this.message_from_client = msg;

        try {
            sendRequest();
        } catch (ProtocolException ex) {
            Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ParserConfigurationException | SAXException ex) {
            Logger.getLogger(ChatBot.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String getResponse(String msg) {
        processResponse(msg);
        return response_from_bot;
    }
}
