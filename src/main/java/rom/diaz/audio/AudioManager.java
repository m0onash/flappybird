package rom.diaz.audio;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import rom.diaz.config.GameSettings;

import java.net.URL;

public class AudioManager {

    private final GameSettings settings;

    private AudioClip jumpSound;
    private AudioClip scoreSound;
    private AudioClip collisionSound;

    private MediaPlayer musicPlayer;

    public AudioManager(GameSettings settings) {
        this.settings = settings;

        jumpSound = loadSound("/audio/jump.wav");
        scoreSound = loadSound("/audio/score.wav");
        collisionSound = loadSound("/audio/collision.wav");

        loadMusic("/audio/background.mp3");
    }

    private AudioClip loadSound(String path) {
        URL resource = getClass().getResource(path);

        if (resource == null) {
            System.err.println("Missing audio: " + path);
            return null;
        }

        try {
            return new AudioClip(resource.toExternalForm());
        } catch (RuntimeException exception) {
            System.err.println("Could not load audio: " + path);
            return null;
        }
    }

    private void loadMusic(String path) {
        URL resource = getClass().getResource(path);

        if (resource == null) {
            System.err.println("Missing music: " + path);
            return;
        }

        try {
            Media media = new Media(resource.toExternalForm());
            musicPlayer = new MediaPlayer(media);

            musicPlayer.setOnReady(() -> {
                System.out.println("Music is ready!");
                System.out.println("Duration: " + media.getDuration());
            });

            musicPlayer.setOnPlaying(() -> {
                System.out.println("Music is now playing!");
            });

            musicPlayer.setOnError(() -> {
                System.err.println(
                        "Music player error: " + musicPlayer.getError()
                );
            });

            media.setOnError(() -> {
                System.err.println(
                        "Media error: " + media.getError()
                );
            });
            musicPlayer.setCycleCount(MediaPlayer.INDEFINITE);
            musicPlayer.setVolume(0.3);
        } catch (RuntimeException exception) {
            System.err.println("Could not load music: " + path);
        }
    }

    public void playJump() {
        playSound(jumpSound);
    }

    public void playScore() {
        playSound(scoreSound);
    }

    public void playCollision() {
        playSound(collisionSound);
    }

    private void playSound(AudioClip sound) {
        if (settings.isSoundEnabled() && sound != null) {
            sound.play();
        }
    }


    public void startMusic() {
        System.out.println("startMusic() was called!");
        System.out.println("Music enabled: " + settings.isMusicEnabled());
        System.out.println("Player exists: " + (musicPlayer != null));
        if (settings.isMusicEnabled() && musicPlayer != null) {
            System.out.println("Attempting to play music...");
            musicPlayer.play();
        }
        System.out.println("Player status: " + musicPlayer.getStatus());
    }


    public void stopMusic() {
        if (musicPlayer != null) {
            musicPlayer.stop();
        }
    }

    public void updateMusic() {
        if (settings.isMusicEnabled()) {
            startMusic();
        } else {
            stopMusic();
        }
    }

    public void dispose() {
        System.out.println("AUDIO MANAGER IS BEING DISPOSED!");

        Thread.dumpStack();

        if (musicPlayer != null) {
            musicPlayer.dispose();
        }
    }
}
