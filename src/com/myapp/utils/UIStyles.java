package com.myapp.utils;
import javax.swing.*;
import java.awt.*;

public class UIStyles {

    public static JTextField createStyledTextField() {
        JTextField textField = new JTextField(20);
        textField.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        textField.setBackground(Color.decode("#ececec"));
        textField.setOpaque(true);
        return textField;
    }

    public static JComboBox<String> createStyledComboBox(String[] items) {
        JComboBox<String> comboBox = new JComboBox<>(items);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        return comboBox;
    }

    public static JButton createStyledButton(String text, String bgColor, String fgColor, String hoverColor) {
        JButton button = new JButton(text);
        button.setBackground(Color.decode(bgColor)); // 배경 색상
        button.setForeground(Color.decode(fgColor)); // 글자 색상
        button.setFocusPainted(false); // 포커스 표시 제거
        button.setOpaque(true); // 배경색 표시
        button.setBorderPainted(false);
        button.setMargin(new Insets(10, 15, 10, 15));
        button.setFont(button.getFont().deriveFont(Font.BOLD));

        // hover 효과
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(Color.decode(hoverColor));
                button.setForeground(Color.white);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(Color.decode(bgColor));
                button.setForeground(Color.decode(fgColor));
            }
        });

        return button;
    }
}