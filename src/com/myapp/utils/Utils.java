package com.myapp.utils;

import com.myapp.App;

import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Utils {
    public static void setUIFont(Font font) {
        UIManager.getLookAndFeelDefaults().keySet().stream()
                .filter(key -> key.toString().toLowerCase().contains("font"))
                .forEach(key -> UIManager.put(key, font));
    }

    public static void playSound(String filePath) {
        try (InputStream audioStream = App.class.getResourceAsStream(filePath)) {
            if (audioStream == null) {
                System.out.println("오디오 파일을 찾을 수 없습니다: " + filePath);
                return;
            }

            // InputStream 데이터를 메모리에 로드
            byte[] audioData = audioStream.readAllBytes();
            try (AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(audioData))) {
                // 클립 준비
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);

                // 재생
                clip.start();
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }


    public static void setBookImage(JLabel imageLabel, String imageUrl, int width, int height) {
        if (imageUrl != null && !imageUrl.isEmpty()) {
            String imagePath = imageUrl.trim();

            // 파일 시스템에서 이미지 읽기
            File imageFile = new File(imagePath);
            if (imageFile.exists() && imageFile.isFile()) {
                try {
                    ImageIcon bookImage = new ImageIcon(imageFile.getAbsolutePath());
                    Image scaledImage = bookImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } catch (Exception e) {
                    e.printStackTrace();
                    setDefaultImage(imageLabel, width, height); // 예외 발생 시 기본 이미지 처리
                }
            } else {
                setDefaultImage(imageLabel, width, height); // 파일이 없으면 기본 이미지 처리
            }
        } else {
            setDefaultImage(imageLabel, width, height); // 이미지 경로가 없으면 기본 이미지 처리
        }
    }

    public static void setDefaultImage(JLabel imageLabel, int width, int height) {
        try (InputStream defaultImageStream = Utils.class.getResourceAsStream("/images/book_default.png")) {
            if (defaultImageStream != null) {
                ImageIcon defaultBookImage = new ImageIcon(defaultImageStream.readAllBytes());
                Image scaledDefaultImage = defaultBookImage.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledDefaultImage));
            } else {
                // 기본 이미지도 없을 경우 기본 색상으로 표시
                imageLabel.setText("기본 이미지 없음");
                imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
                imageLabel.setVerticalAlignment(SwingConstants.CENTER);
                imageLabel.setOpaque(true);
                imageLabel.setBackground(Color.decode("#ececec"));
                imageLabel.setForeground(Color.DARK_GRAY);
            }
        } catch (IOException e) {
            e.printStackTrace();  // 기본 이미지도 로드 실패할 경우 예외 출력
            imageLabel.setText("기본 이미지 없음");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.decode("#ececec"));
            imageLabel.setForeground(Color.DARK_GRAY);
        }
    }

    public static void setBookStatusIcon(JPanel statusPanel, String status) {
        String iconPath = null;
        switch (status) {
            case "읽을 예정":
                iconPath = "icons/bookmark-regular.png";
                break;
            case "읽음":
                iconPath = "icons/book-solid.png";
                break;
            case "읽는 중":
                iconPath = "icons/book-open-solid.png";
                break;
        }

        if (iconPath != null) {
            try (InputStream statusIconStream = Utils.class.getResourceAsStream("/" + iconPath)) {
                if (statusIconStream != null) {
                    ImageIcon statusIcon = new ImageIcon(statusIconStream.readAllBytes());
                    Image scaledIcon = statusIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
                    JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
                    statusPanel.add(iconLabel);
                } else {
                    System.out.println("아이콘 파일을 찾을 수 없습니다: " + iconPath);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
