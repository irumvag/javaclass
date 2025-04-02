package game.component;

import javax.sound.sampled.*;
import java.io.InputStream;

public class SoundManager {
    public static void playSound(String path) {
        new Thread(() -> {
            try (InputStream is = SoundManager.class.getResourceAsStream(path);
                 AudioInputStream audioIn = AudioSystem.getAudioInputStream(is)) {
                
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
            } catch (Exception e) {
                System.err.println("Error playing sound: " + e.getMessage());
            }
        }).start();
    }
}