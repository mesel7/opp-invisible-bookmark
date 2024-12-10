package com.myapp.view;

import com.myapp.model.Book;
import com.myapp.utils.UIStyles;
import com.myapp.viewmodel.BookViewModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class MainView extends JFrame {
    private JTextField searchField;
    private JComboBox<String> searchTypeCombo;
    private JButton searchButton, resetButton, addBookButton;
    private JPanel bookPanel;
    private BookViewModel bookViewModel;

    public MainView(BookViewModel bookViewModel) {
        this.bookViewModel = bookViewModel;
        setTitle("도서 목록");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // 상단 패널
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // 로고 이미지 및 텍스트
        JLabel logoLabel = new JLabel();
        ImageIcon originalIcon = new ImageIcon("resources/images/logo.png");

        // 이미지 크기 비율 유지하며 크기 조정
        Image image = originalIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
        logoLabel.setIcon(new ImageIcon(image));
        logoLabel.setPreferredSize(new Dimension(50, 50));

        JLabel logoTextLabel = new JLabel("보이지 않는 책갈피");
        logoTextLabel.setFont(logoTextLabel.getFont().deriveFont(Font.BOLD));

        // 검색 필드
        searchField = UIStyles.createStyledTextField();
        searchField.setPreferredSize(new Dimension(200, 30));
        searchField.setMinimumSize(new Dimension(150, 30));

        // 드롭다운
        searchTypeCombo = UIStyles.createStyledComboBox(new String[]{"제목", "저자"});
        searchTypeCombo.setPreferredSize(new Dimension(100, 30));

        // 검색 버튼
        searchButton = UIStyles.createStyledButton("검색", "#c3ebff", "#22abf3", "#22abf3");
        searchButton.setPreferredSize(new Dimension(100, 30));

        // 초기화 버튼
        resetButton = UIStyles.createStyledButton("검색 초기화", "#ffffff", "#22abf3", "#22abf3");
        resetButton.setPreferredSize(new Dimension(120, 30));
        resetButton.addActionListener(e -> {
            searchField.setText("");
            loadBooks(bookViewModel.getBooks());
        });

        // 도서 추가 버튼
        addBookButton = UIStyles.createStyledButton("도서 추가", "#c3ebff", "#22abf3", "#22abf3");
        addBookButton.setPreferredSize(new Dimension(120, 30));
        addBookButton.addActionListener(e -> {
            // 기존 인스턴스가 열려 있으면 포커스 설정
            BookAddView bookAddView = BookAddView.getInstance(bookViewModel, this);
            bookAddView.setVisible(true);
            bookAddView.toFront(); // 창을 최상위로 이동
        });

        // 상단 패널에 컴포넌트 추가
        topPanel.add(logoLabel);
        topPanel.add(Box.createHorizontalStrut(15));
        topPanel.add(logoTextLabel);
        topPanel.add(Box.createHorizontalStrut(15));
        topPanel.add(searchField);
        topPanel.add(Box.createHorizontalStrut(15));
        topPanel.add(searchTypeCombo);
        topPanel.add(Box.createHorizontalStrut(15));
        topPanel.add(searchButton);
        topPanel.add(Box.createHorizontalStrut(15));
        topPanel.add(resetButton);
        topPanel.add(Box.createHorizontalStrut(15));
        topPanel.add(addBookButton);

        // 상단 패널 추가
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
    }

    private void loadBooks(List<Book> books) {
        bookPanel.removeAll();
        bookPanel.setLayout(new BoxLayout(bookPanel, BoxLayout.Y_AXIS));

        for (Book book : books) {
            JPanel bookCard = new JPanel();
            bookCard.setLayout(new BorderLayout());
            bookCard.setPreferredSize(new Dimension(0, 200));
            bookCard.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
            bookCard.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
            bookCard.setBackground(Color.WHITE);

            // 왼쪽: 책 이미지
            JLabel imageLabel = new JLabel();
            imageLabel.setPreferredSize(new Dimension(120, 150));

            if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                String imagePath = book.getImageUrl().trim();
                File imageFile = new File(imagePath);

                // 절대 경로로 변환
                /*
                if (!imageFile.isAbsolute()) {
                    // System.getProperty("user.dir")이 현재 실행 파일(.exe)이 위치한 디렉터리를 가리킴
                    imageFile = new File(System.getProperty("user.dir"), imagePath);
                    System.out.println(imageFile.getAbsolutePath());
                }
                */

                // 이미지 파일이 존재하면 이미지 표시
                if (imageFile.exists()) {
                    ImageIcon bookImage = new ImageIcon(imageFile.getAbsolutePath());
                    Image scaledImage = bookImage.getImage().getScaledInstance(120, 150, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                } else {
                    // 이미지가 없으면 기본 이미지 표시
                    String defaultImagePath = "resources/images/book_default.png";
                    File defaultImageFile = new File(defaultImagePath);
                    ImageIcon defaultBookImage = new ImageIcon(defaultImageFile.getAbsolutePath());
                    Image scaledDefaultImage = defaultBookImage.getImage().getScaledInstance(120, 150, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledDefaultImage));
                }
            } else {
                String defaultImagePath = "resources/images/book_default.png";
                File defaultImageFile = new File(defaultImagePath);
                ImageIcon defaultBookImage = new ImageIcon(defaultImageFile.getAbsolutePath());
                Image scaledDefaultImage = defaultBookImage.getImage().getScaledInstance(120, 150, Image.SCALE_SMOOTH);
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

            bookCard.add(imageLabel, BorderLayout.WEST);

            // 오른쪽: 책 정보
            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
            infoPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 0));
            infoPanel.setBackground(Color.WHITE);

            // 제목 (크게, 볼드 처리)
            JLabel titleLabel = new JLabel(book.getTitle());
            titleLabel.setFont(titleLabel.getFont().deriveFont(Font.BOLD, 18f));
            titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(titleLabel);
            infoPanel.add(Box.createVerticalStrut(10));

            // 저자 | 출판사 | 출판연도
            String metadata = String.format("%s (지은이) | %s | %s", book.getAuthor(), book.getPublisher(), book.getPublicationYear());
            JLabel metadataLabel = new JLabel(metadata);
            metadataLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(metadataLabel);
            infoPanel.add(Box.createVerticalStrut(10));

            // 읽을 예정 여부
            JPanel statusPanel = new JPanel();
            statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
            statusPanel.setBackground(Color.WHITE);
            statusPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

            // 상태에 따른 아이콘 설정
            String status = book.getStatus();
            String iconPath = null;
            switch (status) {
                case "읽을 예정":
                    iconPath = "resources/icons/bookmark-regular.png";
                    break;
                case "읽음":
                    iconPath = "resources/icons/book-solid.png";
                    break;
                case "읽는 중":
                    iconPath = "resources/icons/book-open-solid.png";
                    break;
            }

            if (iconPath != null) {
                ImageIcon statusIcon = new ImageIcon(iconPath);
                Image scaledIcon = statusIcon.getImage().getScaledInstance(14, 14, Image.SCALE_SMOOTH);
                JLabel iconLabel = new JLabel(new ImageIcon(scaledIcon));
                statusPanel.add(iconLabel);
            }

            JLabel statusLabel = new JLabel(status);
            statusPanel.add(statusLabel);

            infoPanel.add(statusPanel);
            infoPanel.add(Box.createVerticalStrut(10));

            // 가로 구분선
            JSeparator separator = new JSeparator();
            separator.setBackground(Color.decode("#ececec"));
            separator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
            separator.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(separator);
            infoPanel.add(Box.createVerticalStrut(10));

            // 설명
            JLabel descriptionLabel = new JLabel(book.getDescription());
            descriptionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            infoPanel.add(descriptionLabel);

            bookCard.add(infoPanel, BorderLayout.CENTER);

            bookCard.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    BookDetailView.getInstance(book, bookViewModel, MainView.this).setVisible(true);
                }
            });

            bookPanel.add(bookCard);
            bookPanel.add(Box.createVerticalStrut(10));
        }

        bookPanel.revalidate();
        bookPanel.repaint();
    }

    public void updateBookList() {
        loadBooks(bookViewModel.getBooks());
    }
}