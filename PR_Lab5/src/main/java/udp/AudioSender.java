package udp;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.Port;
import javax.sound.sampled.TargetDataLine;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static udp.Util.HOST;
import static udp.Util.PORT;
import static udp.Util.getAudioFormat;

public class AudioSender {

    public static void main(String[] args) {
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();
        for (int i = 0; i < mixerInfo.length; i++) {
            System.out.println(mixerInfo[i]);
        }

        if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
            try {

                DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, getAudioFormat());
                TargetDataLine targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
                targetDataLine.open(getAudioFormat());
                targetDataLine.start();
                byte[] audioBytes = new byte[4096];

                while (true) {
                    targetDataLine.read(audioBytes, 0, audioBytes.length);
                    sendAudio(audioBytes);
                }

            } catch (Exception e) {
                System.out.println(" Exception occurred while trying to use the microphone!");
                System.exit(0);
            }
        }
    }


    public static void sendAudio(byte[] soundPacket) {

        try(DatagramSocket datagramSocket = new DatagramSocket()) {
            datagramSocket.send(new DatagramPacket(soundPacket, soundPacket.length, InetAddress.getByName(HOST), PORT));
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(" Unable to send sound packet using UDP ");
        }
    }
}
