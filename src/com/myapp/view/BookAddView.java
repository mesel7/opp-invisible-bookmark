package com.myapp.view;

import com.myapp.model.Book;
import com.myapp.utils.UIStyles;
import com.myapp.viewmodel.BookViewModel;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BookAddView extends JFrame {
    private JLabel titleLabel, authorLabel, publisherLabel, isbnLabel, yearLabel, genreLabel, descriptionLabel;
    private JTextField titleField, authorField, publisherField, isbnField, genreField;
    private JTextArea descriptionArea;
    private JButton uploadImageButton, saveButton, cancelButton;
    private String imagePath = "";
    private JSpinner yearSpinner;
    private JPanel imagePreviewPanel;

    public BookAddView(BookViewModel bookViewModel, MainView mainView) {
        setTitle("도서 추가");
        setSize(480, 720);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(null);
        getContentPane().setBackground(Color.WHITE);

        // 제목 입력 필드
        titleLabel = new JLabel("제목:");
        titleLabel.setBounds(50, 20, 100, 30);
        add(titleLabel);
        titleField = UIStyles.createStyledTextField();
        titleField.setBounds(150, 20, 200, 30);
        add(titleField);

        // 저자 입력 필드
        authorLabel = new JLabel("저자:");
        authorLabel.setBounds(50, 60, 100, 30);
        add(authorLabel);
        authorField = UIStyles.createStyledTextField();
        authorField.setBounds(150, 60, 200, 30);
        add(authorField);

        // 출판사 입력 필드
        publisherLabel = new JLabel("출판사:");
        publisherLabel.setBounds(50, 100, 100, 30);
        add(publisherLabel);
        publisherField = UIStyles.createStyledTextField();
        publisherField.setBounds(150, 100, 200, 30);
        add(publisherField);

        // ISBN 입력 필드
        isbnLabel = new JLabel("ISBN:");
        isbnLabel.setBounds(50, 140, 100, 30);
        add(isbnLabel);
        isbnField = UIStyles.createStyledTextField();
        isbnField.setBounds(150, 140, 200, 30);
        add(isbnField);

        // 출판연도 입력 필드
        yearLabel = new JLabel("출판연도:");
        yearLabel.setBounds(50, 180, 100, 30);
        add(yearLabel);

        // 연도 선택을 위한 Spinner 설정
        JPanel yearPanel = new JPanel();
        yearPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        Calendar calendar = Calendar.getInstance();
        SpinnerDateModel model = new SpinnerDateModel();
        model.setValue(calendar.getTime());
        yearSpinner = new JSpinner(model);
        JSpinner.DateEditor editor = new JSpinner.DateEditor(yearSpinner, "yyyy");
        yearSpinner.setEditor(editor);
        yearPanel.add(yearSpinner);
        yearPanel.setBackground(Color.WHITE);
        yearPanel.setBounds(150, 180, 200, 30);
        add(yearPanel);

        // 장르 입력 필드
        genreLabel = new JLabel("장르:");
        genreLabel.setBounds(50, 220, 100, 30);
        add(genreLabel);
        genreField = UIStyles.createStyledTextField();
        genreField.setBounds(150, 220, 200, 30);
        add(genreField);

        // 설명 필드
        descriptionLabel = new JLabel("설명:");
        descriptionLabel.setBounds(50, 260, 100, 30);
        add(descriptionLabel);
        descriptionArea = new JTextArea();
        descriptionArea.setBounds(150, 260, 200, 100);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setBackground(Color.decode("#ececec"));
        descriptionArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(descriptionArea);

        // 이미지 미리보기 패널 (기본 회색 박스)
        imagePreviewPanel = new JPanel();
        imagePreviewPanel.setBounds(150, 370, 200, 200);
        imagePreviewPanel.setBackground(Color.LIGHT_GRAY);
        imagePreviewPanel.setLayout(new BorderLayout());

        JLabel noImageLabel = new JLabel("이미지 없음", JLabel.CENTER);
        noImageLabel.setForeground(Color.DARK_GRAY);
        imagePreviewPanel.add(noImageLabel, BorderLayout.CENTER);

        add(imagePreviewPanel);

        // 이미지 업로드 버튼
        uploadImageButton = UIStyles.createStyledButton("이미지 업로드", "#c3ebff", "#22abf3", "#22abf3");
        uploadImageButton.setBounds(150, 580, 200, 30);
        uploadImageButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setAcceptAllFileFilterUsed(false); // All Files 필터링을 없앰
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("이미지 파일 (JPG, PNG)", "jpg", "png"));

            int returnValue = fileChooser.showOpenDialog(this);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                imagePath = file.getAbsolutePath(); // 선택한 이미지 경로 저장

                // 이미지를 images 폴더로 복사
                try {
                    File destinationDir = new File("images");
                    // images 폴더가 없으면 생성
                    if (!destinationDir.exists()) {
                        destinationDir.mkdir();
                    }
                    File destinationFile = new File(destinationDir, file.getName());
                    Files.copy(file.toPath(), destinationFile.toPath());

                    // 선택된 이미지 미리보기
                    ImageIcon imageIcon = new ImageIcon(destinationFile.getAbsolutePath());
                    Image image = imageIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                    imageIcon = new ImageIcon(image);

                    // "이미지 없음" 텍스트를 이미지로 교체
                    imagePreviewPanel.removeAll();
                    JLabel imageLabel = new JLabel(imageIcon);
                    imagePreviewPanel.add(imageLabel, BorderLayout.CENTER);
                    imagePreviewPanel.revalidate();
                    imagePreviewPanel.repaint();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });
        add(uploadImageButton);

        // 저장 버튼
        saveButton = UIStyles.createStyledButton("저장", "#c3ebff", "#22abf3", "#22abf3");
        saveButton.setBounds(50, 620, 100, 30);
        saveButton.addActionListener(e -> {
            // 제목과 저자가 비어 있는지 확인
            if (titleField.getText().isEmpty() || authorField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "제목과 저자는 필수 입력 사항입니다");
                return;
            }

            // 연도 값 얻기
            Date selectedDate = (Date) yearSpinner.getValue();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            String formattedYear = sdf.format(selectedDate);

            // Book 객체 생성 및 저장
            Book newBook = new Book(
                    titleField.getText(),
                    authorField.getText()
            );
            newBook.setPublisher(publisherField.getText());
            newBook.setIsbn(isbnField.getText());
            newBook.setPublicationYear(formattedYear);
            newBook.setGenre(genreField.getText());
            newBook.setDescription(descriptionArea.getText());

            // 선택한 이미지가 있는 경우 상대 경로로 이미지 저장
            if (imagePath.isEmpty()) {
                newBook.setImageUrl("");
            } else {
                newBook.setImageUrl("images/" + new File(imagePath).getName());
            }

            bookViewModel.addBook(newBook);
            JOptionPane.showMessageDialog(this, "도서가 저장되었습니다");

            // MainView 갱신
            mainView.updateBookList();
            dispose();
        });
        add(saveButton);

        // 취소 버튼
        cancelButton = UIStyles.createStyledButton("취소", "#ffffff", "#22abf3", "#22abf3");
        cancelButton.setBounds(200, 620, 100, 30);
        cancelButton.addActionListener(e -> {
            dispose();
        });
        add(cancelButton);
    }
}