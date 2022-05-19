package udp;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static udp.Util.HOST;
import static udp.Util.PORT;
import static udp.Util.getAudioFormat;

public class AudioReceiver extends Thread {

    public static void main(String[] args) {
        AudioReceiver r = new AudioReceiver();
        r.start();
    }

    @Override
    public void run() {
        System.out.println("Server listening on port: " + PORT);
        byte[] bytes = null;
        while (true) {
            bytes = receiveAudio();
            toSpeaker(bytes);
        }
    }

    public static byte[] receiveAudio() {
        try(DatagramSocket socket = new DatagramSocket(PORT)) {

            byte[] soundPacket = new byte[4096];
            DatagramPacket datagramPacket = new DatagramPacket(soundPacket, soundPacket.length, InetAddress.getByName(HOST), PORT);
            socket.receive(datagramPacket);

            return datagramPacket.getData();
        } catch (Exception e) {
            System.out.println(" Unable to send sound packet using UDP ");
            return null;
        }

    }

    public static void toSpeaker(byte[] soundBytes) {
        DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, getAudioFormat());
        try(SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);) {

            sourceDataLine.open(getAudioFormat());
            sourceDataLine.start();
            sourceDataLine.write(soundBytes, 0, soundBytes.length);
            sourceDataLine.drain();
            System.out.println("Audio is played");
        } catch (Exception e) {
            System.out.println("Unable to use the speakers!");
        }

    }

}
