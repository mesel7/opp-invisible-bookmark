package com.myapp.viewmodel;

import com.myapp.model.Book;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class BookViewModel {
    private List<Book> books;
    private final String filePath = "books.csv";

    public BookViewModel() {
        books = new ArrayList<>();
        loadBooksFromFile();
    }

    public void addBook(Book book) {
        books.add(book);
        saveBooksToFile();
    }

    public void updateBook(Book book) {
        saveBooksToFile();
    }

    public void deleteBook(Book book) {
        books.remove(book);
        saveBooksToFile();
    }

    public List<Book> searchBooks(String query, String searchType) {
        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            if ("제목".equals(searchType) && book.getTitle().contains(query)) {
                results.add(book);
            } else if ("저자".equals(searchType) && book.getAuthor().contains(query)) {
                results.add(book);
            }
        }
        return results;
    }

    public List<Book> getBooks() {
        return books;
    }

    private void saveBooksToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Book book : books) {
                writer.write(String.format("%s,%s,%s,%s,%s,%s,%s,%s,%s\n",
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getIsbn(),
                        book.getPublicationYear(),
                        book.getGenre(),
                        book.getDescription(),
                        book.getStatus(),
                        book.getImageUrl()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBooksFromFile() {
        File file = new File(filePath);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                // 데이터가 부족하면 기본값 할당
                String title = data.length > 0 ? data[0] : "";
                String author = data.length > 1 ? data[1] : "";
                String publisher = data.length > 2 ? data[2] : "";
                String isbn = data.length > 3 ? data[3] : "";
                String publicationYear = data.length > 4 ? data[4] : "";
                String genre = data.length > 5 ? data[5] : "";
                String description = data.length > 6 ? data[6] : "";
                String status = data.length > 7 ? data[7] : "읽을 예정";
                String imageUrl = data.length > 8 ? data[8] : "";

                Book book = new Book(title, author);
                book.setPublisher(publisher);
                book.setIsbn(isbn);
                book.setPublicationYear(publicationYear);
                book.setGenre(genre);
                book.setDescription(description);
                book.setStatus(status);
                book.setImageUrl(imageUrl);
                books.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}