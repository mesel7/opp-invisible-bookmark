package com.myapp.view;

import com.myapp.model.Book;
import com.myapp.utils.UIStyles;
import com.myapp.viewmodel.BookViewModel;

import javax.swing.*;
import java.io.File;

public class BookDetailView extends JFrame {
    private JLabel titleLabel, authorLabel, publisherLabel, isbnLabel, yearLabel, genreLabel, descriptionLabel, statusLabel;
    private JTextArea descriptionArea;
    private JComboBox<String> statusCombo;
    private JButton saveButton, deleteButton;

    public BookDetailView(Book book, BookViewModel bookViewModel, MainView mainView) {
        setTitle("도서 상세");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        titleLabel = new JLabel("제목: " + book.getTitle());
        titleLabel.setBounds(50, 20, 300, 30);
        add(titleLabel);

        authorLabel = new JLabel("저자: " + book.getAuthor());
        authorLabel.setBounds(50, 60, 300, 30);
        add(authorLabel);

        publisherLabel = new JLabel("출판사: " + book.getPublisher());
        publisherLabel.setBounds(50, 100, 300, 30);
        add(publisherLabel);

        isbnLabel = new JLabel("ISBN: " + book.getIsbn());
        isbnLabel.setBounds(50, 140, 300, 30);
        add(isbnLabel);

        yearLabel = new JLabel("출판연도: " + book.getPublicationYear());
        yearLabel.setBounds(50, 180, 300, 30);
        add(yearLabel);

        genreLabel = new JLabel("장르: " + book.getGenre());
        genreLabel.setBounds(50, 220, 300, 30);
        add(genreLabel);

        descriptionLabel = new JLabel("설명:");
        descriptionLabel.setBounds(50, 260, 300, 30);
        add(descriptionLabel);

        descriptionArea = new JTextArea(book.getDescription());
        descriptionArea.setBounds(50, 290, 300, 100);
        add(descriptionArea);

        statusLabel = new JLabel("상태:");
        statusLabel.setBounds(50, 400, 100, 30);
        add(statusLabel);

        statusCombo = new JComboBox<>(new String[]{"읽을 예정", "읽는 중", "읽음"});
        statusCombo.setSelectedItem(book.getStatus());
        statusCombo.setBounds(150, 400, 100, 30);
        add(statusCombo);

        saveButton = UIStyles.createStyledButton("저장", "#c3ebff", "#22abf3", "#22abf3");
        saveButton.setBounds(50, 440, 100, 30);
        saveButton.addActionListener(e -> {
            book.setDescription(descriptionArea.getText());
            book.setStatus((String) statusCombo.getSelectedItem());
            bookViewModel.updateBook(book); // ViewModel에 반영
            JOptionPane.showMessageDialog(this, "변경 내용이 저장되었습니다.");
            mainView.updateBookList(); // MainView 갱신
            dispose();
        });
        add(saveButton);

        deleteButton = UIStyles.createStyledButton("삭제", "#c3ebff", "#22abf3", "#22abf3");
        deleteButton.setBounds(200, 440, 100, 30);
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
                JOptionPane.showMessageDialog(this, "도서가 삭제되었습니다.");
                mainView.updateBookList(); // MainView 갱신
                dispose();
            }
        });
        add(deleteButton);
    }
}