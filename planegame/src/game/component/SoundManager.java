package game.component;

import javax.sound.sampled.*;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class SoundManager {
    private static final Map<String, Clip> activeClips = new HashMap<>();
    private static long pausePosition = 0;

    public static void playSound(String path) {
        new Thread(() -> {
            try (InputStream is = SoundManager.class.getResourceAsStream(path);
                 AudioInputStream audioIn = AudioSystem.getAudioInputStream(is)) {
                
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                
                // Store reference for pausing
                activeClips.put(path, clip);
                
                if(path.equals("/game/sound/game.wav")) { // Background music
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            } catch (Exception e) {
                System.err.println("Error playing sound: " + e.getMessage());
            }
        }).start();
    }
    public static boolean isGameSoundPlaying() {
        Clip gameClip = activeClips.get("/game/sound/game.wav");
        return gameClip != null && gameClip.isRunning();
    }
    public static void pauseAll() {
        activeClips.forEach((path, clip) -> {
            if(clip.isRunning()) {
                pausePosition = clip.getMicrosecondPosition();
                clip.stop();
            }
        });
    }

    public static void resumeAll() {
        activeClips.forEach((path, clip) -> {
            if(!clip.isRunning()) {
                clip.setMicrosecondPosition(pausePosition);
                if(path.equals("/game/sound/game.wav")) {
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } else {
                    clip.start();
                }
            }
        });
    }
}