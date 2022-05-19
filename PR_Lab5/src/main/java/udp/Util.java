package udp;

import javax.sound.sampled.AudioFormat;

public class Util {
    public static final String HOST = "localhost";
    public static final int PORT = 8000;

    public static AudioFormat getAudioFormat() {
        float sampleRate = 8000f;
        //8000,11025,16000,22050,44100 The number of samples played or recorded per second, for sounds that have this format.
        int sampleSizeInBits = 16;
        //8,16 The number of bits in each sample of a sound that has this format.
        int channels = 1;
        //1,2 The number of audio channels in this format (1 for mono, 2 for stereo).
        boolean signed = true;
        //true,false Indicates whether the data is signed or unsigned
        boolean bigEndian = false;
        //true,false Indicates whether the data for a single sample is stored in big-endian byte order (false means little-endian)
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

}
