package com.myapp;

import com.myapp.utils.Utils;
import com.myapp.view.MainView;
import com.myapp.viewmodel.BookViewModel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

public class App {
    public static void main(String[] args) {
        try {
            InputStream fontStream = App.class.getResourceAsStream("/fonts/SCDream3.otf");
            if (fontStream == null) {
                System.out.println("Font file not found!");
                return;
            }

            // 폰트 생성 및 적용
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream);
            customFont = customFont.deriveFont(14f);

            // 전체 UI에 폰트 적용
            Utils.setUIFont(customFont);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
            System.out.println("Font load failed");
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