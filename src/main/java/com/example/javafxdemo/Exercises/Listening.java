package com.example.javafxdemo.Exercises;

import javafx.fxml.FXML;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URISyntaxException;

public class Listening {

    @FXML
    private MediaView mediaView;

    @FXML
    protected void OnPlayAudio() {
        if (mediaView.getMediaPlayer() == null) {
            try {
                String path = getClass().getResource("/sample.mp3").toURI().toString();
                Media media = new Media(path);
                MediaPlayer player = new MediaPlayer(media);
                mediaView.setMediaPlayer(player);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
        mediaView.getMediaPlayer().seek(mediaView.getMediaPlayer().getStartTime());
        mediaView.getMediaPlayer().play();
    }
}
