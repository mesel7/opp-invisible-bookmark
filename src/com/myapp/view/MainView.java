package com.myapp.view;

import com.myapp.model.Book;
import com.myapp.viewmodel.BookViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class MainView extends JFrame {
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JButton searchButton, addBookButton;
    private JPanel bookPanel;
    private BookViewModel bookViewModel;
    private BookDetailView currentDetailView = null; // 현재 열려있는 상세보기 창을 추적

    // 생성자에서 BookViewModel을 전달받고 초기화
    public MainView(BookViewModel bookViewModel) {
        this.bookViewModel = bookViewModel;  // bookViewModel 초기화
        setTitle("도서 목록");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 상단 패널 (검색바)
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout());

        searchField = new JTextField(20);
        searchTypeCombo = new JComboBox<>(new String[]{"제목", "저자"});
        searchButton = new JButton("검색");
        addBookButton = new JButton("도서 추가");

        topPanel.add(searchField);
        topPanel.add(searchTypeCombo);
        topPanel.add(searchButton);
        topPanel.add(addBookButton);
        add(topPanel, BorderLayout.NORTH);

        // 도서 카드 목록
        bookPanel = new JPanel();
        bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(bookPanel);
        add(scrollPane, BorderLayout.CENTER);

        // 도서 목록 로드
        loadBooks(bookViewModel.getBooks());

        // 검색 버튼 액션
        searchButton.addActionListener(e -> {
            String query = searchField.getText();
            String searchType = (String) searchTypeCombo.getSelectedItem();
            List<Book> results = bookViewModel.searchBooks(query, searchType);
            loadBooks(results);
        });

        // 도서 추가 버튼 액션
        addBookButton.addActionListener(e -> {
            new BookAddView(bookViewModel, this).setVisible(true);
        });
    }

    private void loadBooks(List<Book> books) {
        bookPanel.removeAll(); // 기존 목록 제거
        bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.Y_AXIS)); // 카드들을 세로로 쌓도록 설정

        for (Book book : books) {
            JPanel bookCard = new JPanel();
            bookCard.setLayout(new BoxLayout(bookCard, BoxLayout.Y_AXIS)); // 카드 안의 텍스트도 세로로 배치
            bookCard.setPreferredSize(new Dimension(300, 250)); // 카드 크기 고정 (너비 300px, 높이 250px)
            bookCard.setBorder(BorderFactory.createLineBorder(Color.BLACK));

            // 첫 번째 줄: 책 제목, 저자, 출판사, 출판 연도
            JPanel titlePanel = new JPanel();
            titlePanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0)); // 텍스트 간 간격 좁게
            titlePanel.add(new JLabel("제목: " + book.getTitle()));
            titlePanel.add(new JLabel("저자: " + book.getAuthor()));
            titlePanel.add(new JLabel("출판사: " + book.getPublisher()));
            titlePanel.add(new JLabel("출판연도: " + book.getPublicationYear()));

            // 두 번째 줄: 읽을 예정 상태
            JLabel statusLabel = new JLabel("상태: " + book.getStatus());

            // 세 번째 줄: 설명 (길면 생략)
            String description = book.getDescription();
            if (description.length() > 60) {
                description = description.substring(0, 60) + "...";
            }
            JLabel descriptionLabel = new JLabel("설명: " + description);

            // 카드에 요소 추가
            bookCard.add(titlePanel);
            bookCard.add(statusLabel);
            bookCard.add(descriptionLabel);

            // 클릭 시 상세 화면 열기
            bookCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new BookDetailView(book, bookViewModel, MainView.this).setVisible(true);
                }
            });

            bookPanel.add(bookCard);

            // 카드 간의 간격 추가
            bookPanel.add(Box.createVerticalStrut(20));  // 카드 간 간격
        }

        bookPanel.revalidate();
        bookPanel.repaint();
    }

    public void updateBookList() {
        loadBooks(bookViewModel.getBooks());
    }

    private class BookDetailAction implements ActionListener {
        private Book book;

        public BookDetailAction(Book book) {
            this.book = book;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new BookDetailView(book, bookViewModel, MainView.this).setVisible(true);  // 여기서 bookViewModel을 사용
        }
    }
}