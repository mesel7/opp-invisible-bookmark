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
        saveBooksToFile(); // 파일에 저장
    }

    public void updateBook(Book book) {
        saveBooksToFile(); // 파일에 저장
    }

    public void deleteBook(Book book) {
        books.remove(book);
        saveBooksToFile(); // 파일에 저장
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
                writer.write(String.format("%s,%s,%s,%s,%d,%s,%s,%s\n",
                        book.getTitle(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getIsbn(),
                        book.getPublicationYear(),
                        book.getGenre(),
                        book.getDescription(),
                        book.getStatus()
                ));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadBooksFromFile() {
        File file = new File(filePath);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                Book book = new Book(data[0], data[1]);
                book.setPublisher(data[2]);
                book.setIsbn(data[3]);
                book.setPublicationYear(Integer.parseInt(data[4]));
                book.setGenre(data[5]);
                book.setDescription(data[6]);
                book.setStatus(data[7]);
                books.add(book);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}