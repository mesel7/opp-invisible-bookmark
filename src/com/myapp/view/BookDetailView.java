package com.myapp.view;

import com.myapp.model.Book;
import com.myapp.utils.UIStyles;
import com.myapp.utils.Utils;
import com.myapp.viewmodel.BookViewModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;

public class BookDetailView extends JFrame {
    private JLabel titleLabel, metadataLabel, isbnLabel, genreLabel;
    private JTextArea descriptionArea;
    private JComboBox<String> statusCombo;
    private JButton saveButton, deleteButton;

    private static BookDetailView instance;

    private BookDetailView(Book book, BookViewModel bookViewModel, MainView mainView) {
        Utils.playSound("resources/sounds/page_turning.wav");

        setTitle("도서 상세");
        setSize(400, 880);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        titleLabel = new JLabel(book.getTitle());
        titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
        titleLabel.setBounds(50, 20, 300, 30);
        add(titleLabel);

        // 이미지 라벨 설정
        JLabel imageLabel = new JLabel();
        imageLabel.setPreferredSize(new Dimension(300, 375)); // 크기 설정

        // 이미지가 있을 때
        if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
            String imagePath = book.getImageUrl().trim();
            File imageFile = new File(imagePath);

            // 이미지 파일이 존재하면 이미지 표시
            if (imageFile.exists()) {
                ImageIcon bookImage = new ImageIcon(imageFile.getAbsolutePath());
                Image scaledImage = bookImage.getImage().getScaledInstance(300, 375, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledImage));
            } else {
                // 이미지가 없으면 기본 이미지 표시
                String defaultImagePath = "resources/images/book_default.png";
                File defaultImageFile = new File(defaultImagePath);
                ImageIcon defaultBookImage = new ImageIcon(defaultImageFile.getAbsolutePath());
                Image scaledDefaultImage = defaultBookImage.getImage().getScaledInstance(300, 375, Image.SCALE_SMOOTH);
                imageLabel.setIcon(new ImageIcon(scaledDefaultImage));
            }
        } else {
            // 이미지가 없으면 기본 이미지 표시
            String defaultImagePath = "resources/images/book_default.png";
            File defaultImageFile = new File(defaultImagePath);
            ImageIcon defaultBookImage = new ImageIcon(defaultImageFile.getAbsolutePath());
            Image scaledDefaultImage = defaultBookImage.getImage().getScaledInstance(300, 375, Image.SCALE_SMOOTH);
            imageLabel.setIcon(new ImageIcon(scaledDefaultImage));

            /*
            imageLabel.setText("이미지 없음");
            imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imageLabel.setVerticalAlignment(SwingConstants.CENTER);
            imageLabel.setOpaque(true);
            imageLabel.setBackground(Color.decode("#ececec"));
            imageLabel.setForeground(Color.DARK_GRAY);
            */
        }

        // 위치 및 크기 설정
        imageLabel.setBounds(50, 50, 300, 375); // 위치와 크기 설정
        add(imageLabel);  // 레이아웃에 추가

        String metadata = String.format("%s (지은이) | %s | %s", book.getAuthor(), book.getPublisher(), book.getPublicationYear());
        metadataLabel = new JLabel(metadata);
        metadataLabel.setBounds(50, 450, 300, 30);
        add(metadataLabel);

        isbnLabel = new JLabel("ISBN: " + book.getIsbn());
        isbnLabel.setBounds(50, 490, 300, 30);
        add(isbnLabel);

        genreLabel = new JLabel("장르: " + book.getGenre());
        genreLabel.setBounds(50, 530, 300, 30);
        add(genreLabel);

        // JTextArea 생성 및 스타일 설정
        descriptionArea = new JTextArea(book.getDescription());
        descriptionArea.setLineWrap(true); // 줄 바꿈 허용
        descriptionArea.setWrapStyleWord(true); // 단어 단위로 줄 바꿈
        descriptionArea.setBackground(Color.decode("#ececec"));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // JTextArea를 JScrollPane으로 감싸기
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        scrollPane.setBounds(50, 570, 300, 100);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        add(scrollPane);

        statusCombo = new JComboBox<>(new String[]{"읽을 예정", "읽는 중", "읽음"});
        statusCombo.setSelectedItem(book.getStatus());
        statusCombo.setBounds(50, 690, 300, 30);
        add(statusCombo);

        saveButton = UIStyles.createStyledButton("저장", "#c3ebff", "#22abf3", "#22abf3");
        saveButton.setBounds(50, 770, 140, 30);
        saveButton.addActionListener(e -> {
            book.setDescription(descriptionArea.getText());
            book.setStatus((String) statusCombo.getSelectedItem());
            bookViewModel.updateBook(book); // ViewModel에 반영
            JOptionPane.showMessageDialog(this, "변경 내용이 저장되었습니다");
            mainView.updateBookList(); // MainView 갱신
            dispose();
        });
        add(saveButton);

        deleteButton = UIStyles.createStyledButton("삭제", "#ffbdbd", "#f27474", "#f27474");
        deleteButton.setBounds(210, 770, 140, 30);
        deleteButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(this, "도서를 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                // 이미지 파일 삭제
                String imageUrl = book.getImageUrl();
                if (imageUrl != null && !imageUrl.isEmpty()) {
                    File imageFile = new File(imageUrl);
                    if (imageFile.exists() && imageFile.isFile()) {
                        if (imageFile.delete()) {
                            System.out.println("이미지 파일 삭제 성공: " + imageUrl);
                        } else {
                            System.err.println("이미지 파일 삭제 실패: " + imageUrl);
                        }
                    }
                }

                bookViewModel.deleteBook(book); // ViewModel에서 삭제
                JOptionPane.showMessageDialog(this, "도서가 삭제되었습니다");
                mainView.updateBookList(); // MainView 갱신
                dispose();
            }
        });
        add(deleteButton);
    }

    // 싱글톤 인스턴스 반환
    public static BookDetailView getInstance(Book book, BookViewModel bookViewModel, MainView mainView) {
        if (instance == null || !instance.isVisible()) {
            instance = new BookDetailView(book, bookViewModel, mainView);
        }
        return instance;
    }
}