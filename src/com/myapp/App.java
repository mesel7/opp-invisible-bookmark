package com.myapp;

import com.myapp.view.MainView;
import com.myapp.viewmodel.BookViewModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class App {
    public static void setUIFont(Font font) {
        UIManager.getLookAndFeelDefaults().keySet().stream()
                .filter(key -> key.toString().toLowerCase().contains("font"))
                .forEach(key -> UIManager.put(key, font));
    }

    public static void main(String[] args) {
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("resources/fonts/SCDream3.otf"));
            customFont = customFont.deriveFont(14f);

            // 전체 UI에 폰트 적용
            setUIFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("font load failed");
        }

        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        BookViewModel bookViewModel = new BookViewModel();
        MainView mainView = new MainView(bookViewModel);
        mainView.setVisible(true);
    }
}