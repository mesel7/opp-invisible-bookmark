package com.myapp.view;

import com.myapp.model.Book;
import com.myapp.utils.UIStyles;
import com.myapp.viewmodel.BookViewModel;

import javax.swing.*;

public class BookAddView extends JFrame {
    private JTextField titleField, authorField, publisherField, isbnField, yearField, genreField;
    private JTextArea descriptionField;
    private JButton saveButton, cancelButton;

    public BookAddView(BookViewModel bookViewModel, JFrame parent) {
        setTitle("도서 추가");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);

        JLabel titleLabel = new JLabel("제목:");
        titleLabel.setBounds(50, 20, 100, 30);
        add(titleLabel);

        titleField = new JTextField();
        titleField.setBounds(150, 20, 200, 30);
        add(titleField);

        JLabel authorLabel = new JLabel("저자:");
        authorLabel.setBounds(50, 60, 100, 30);
        add(authorLabel);

        authorField = new JTextField();
        authorField.setBounds(150, 60, 200, 30);
        add(authorField);

        JLabel publisherLabel = new JLabel("출판사:");
        publisherLabel.setBounds(50, 100, 100, 30);
        add(publisherLabel);

        publisherField = new JTextField();
        publisherField.setBounds(150, 100, 200, 30);
        add(publisherField);

        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setBounds(50, 140, 100, 30);
        add(isbnLabel);

        isbnField = new JTextField();
        isbnField.setBounds(150, 140, 200, 30);
        add(isbnField);

        JLabel yearLabel = new JLabel("출판연도:");
        yearLabel.setBounds(50, 180, 100, 30);
        add(yearLabel);

        yearField = new JTextField();
        yearField.setBounds(150, 180, 200, 30);
        add(yearField);

        JLabel genreLabel = new JLabel("장르:");
        genreLabel.setBounds(50, 220, 100, 30);
        add(genreLabel);

        genreField = new JTextField();
        genreField.setBounds(150, 220, 200, 30);
        add(genreField);

        JLabel descriptionLabel = new JLabel("설명:");
        descriptionLabel.setBounds(50, 260, 100, 30);
        add(descriptionLabel);

        descriptionField = new JTextArea();
        descriptionField.setBounds(150, 260, 200, 100);
        add(descriptionField);

        saveButton = UIStyles.createStyledButton("저장", "#c3ebff", "#22abf3", "#22abf3");
        saveButton.setBounds(100, 400, 80, 30);
        saveButton.addActionListener(e -> {
            if (titleField.getText().isEmpty() || authorField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "제목과 저자는 필수 항목입니다.");
                return;
            }

            int publicationYear = 0;
            try {
                if (!yearField.getText().isEmpty()) {
                    publicationYear = Integer.parseInt(yearField.getText());
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "출판연도는 숫자여야 합니다.");
                return;
            }

            Book book = new Book(titleField.getText(), authorField.getText());
            book.setPublisher(publisherField.getText());
            book.setIsbn(isbnField.getText());
            book.setPublicationYear(publicationYear);
            book.setGenre(genreField.getText());
            book.setDescription(descriptionField.getText());

            bookViewModel.addBook(book);
            JOptionPane.showMessageDialog(this, "도서가 추가되었습니다.");
            ((MainView) parent).updateBookList(); // MainView 갱신
            dispose();
        });

        add(saveButton);

        cancelButton = UIStyles.createStyledButton("취소", "#c3ebff", "#22abf3", "#22abf3");
        cancelButton.setBounds(200, 400, 80, 30);
        cancelButton.addActionListener(e -> dispose());
        add(cancelButton);
    }
}