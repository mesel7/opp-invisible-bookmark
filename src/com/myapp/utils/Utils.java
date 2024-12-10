package com.myapp.utils;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Utils {
    public static void playSound(String filePath) {
        try {
            // 오디오 파일 로드
            File soundFile = new File(filePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);

            // 클립 준비
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);

            // 재생
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }
}
