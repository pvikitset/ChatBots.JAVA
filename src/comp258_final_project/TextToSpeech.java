/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package comp258_final_project;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import sun.audio.AudioData;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;
import sun.audio.ContinuousAudioDataStream;

/**
 *
 * @author pviki
 */
public class TextToSpeech {

    final private static String url_base = "http://api.voicerss.org/?";
    final private static String api_key = "f4002bf7d39f47d18b2a6bf120e58f87";
    final private static String language = "en-us";

    private String message_to_convert;

    public TextToSpeech() {
    }

    private String buildURLRequest() {
        message_to_convert = message_to_convert.replaceAll(" ", "%20");
        StringBuilder urlRequest = new StringBuilder();
        urlRequest.append(url_base);
        urlRequest.append("key=").append(api_key);
        urlRequest.append("&hl=").append(language);
        urlRequest.append("&src=").append(message_to_convert);

        return urlRequest.toString();
    }

    private void sendRequest() throws IOException {
        String urlForm = buildURLRequest();
        URL urlForGetRequest = new URL(urlForm);

        HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
        conection.setRequestMethod("GET");
        int responseCode = conection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            InputStream inputStream = conection.getInputStream();
            AudioStream audio = new AudioStream(inputStream);
            AudioPlayer.player.start(audio);
            try {
                Thread.sleep(audio.getLength() / 10); // need to sleep, so the audio can finish its self
            } catch (InterruptedException ex) {
                Logger.getLogger(TextToSpeech.class.getName()).log(Level.SEVERE, null, ex);
            }
            inputStream.close();
        } else {
            System.out.println("Server replied HTTP code: " + responseCode);
        }
        conection.disconnect();
    }

    private void process(String msg){
        this.message_to_convert = msg;

        try {
            sendRequest();
        } catch (IOException ex) {
        }
    }

    public void playTextToSpeech(String msg){
        msg = msg.substring(msg.indexOf(":"), msg.length()).trim();
        process(msg);
    }
}
