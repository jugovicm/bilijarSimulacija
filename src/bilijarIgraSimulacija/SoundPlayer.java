package bilijarIgraSimulacija;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundPlayer {
    private Clip clip;

    public SoundPlayer(String filePath) {
        try {
            File soundFile = new File ( filePath );
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream ( soundFile );

            // Smanjenje kvaliteta zvuka
            AudioFormat baseFormat = audioInputStream.getFormat ();
            AudioFormat decodedFormat = new AudioFormat (
                    AudioFormat.Encoding.PCM_SIGNED,
                    baseFormat.getSampleRate (),
                    16,
                    baseFormat.getChannels (),
                    baseFormat.getChannels () * 2,
                    baseFormat.getSampleRate (),
                    false
            );

            AudioInputStream decodedInputStream = AudioSystem.getAudioInputStream ( decodedFormat, audioInputStream );

            DataLine.Info info = new DataLine.Info ( Clip.class, decodedFormat );

            if (!AudioSystem.isLineSupported ( info )) {
                throw new LineUnavailableException ( "Unsupported audio format" );
            }

            clip = (Clip) AudioSystem.getLine ( info );
            clip.open ( decodedInputStream );
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace ();
        }
    }

    public void play() {
        if (clip != null) {
            clip.start ();
        }
    }
}