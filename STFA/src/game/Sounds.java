package game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Sounds {


    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
            // The wrapper thread is unnecessary, unless it blocks on the
            // Clip finishing; see comments.
            public void run() {
                try {
                    Clip clip = AudioSystem.getClip();
                    //AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                            //Game.class.getResourceAsStream("launchers.properties"));
                    clip.open(AudioSystem.getAudioInputStream(getClass().getResourceAsStream("resources/hit.wav")));
                    //clip.open(inputStream);
                    clip.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}