package library;

import books.AuthorEntity;
import books.BookEntity;

import java.time.LocalDateTime;
import java.util.*;

public class LibraryController {

    private List<BookEntity> books = new ArrayList<>();
    private  List<AuthorEntity> authors = new ArrayList<>();
    private List<LoanEntity> loanList = new ArrayList<>();

    public List<BookEntity> listBooks() {
        return this.books.stream().toList();
    }

    public boolean addBook(BookEntity book) {
        var existbook = books.stream().anyMatch(b -> b.getTitle().trim().equalsIgnoreCase(book.getTitle().trim()));

        if (existbook) return false;

        books.add(book);
        return true;
    }

    public boolean reserveBook(String title, String borrowerName) {
        Optional<BookEntity> optionalBook = books.stream()
                .filter(b -> b.getTitle().trim().equalsIgnoreCase(title.trim()) && b.isAvailable())
                .findFirst();

        if (optionalBook.isEmpty()) {
            return false; // Livro não encontrado ou já reservado
        }

        BookEntity book = optionalBook.get();
        book.setAvailable(false);
        long timestamp = System.currentTimeMillis();
        Date date = new Date(timestamp);
        book.setUpdateDate(date);

        LoanEntity loan = new LoanEntity(book, borrowerName, LocalDateTime.now());

        loanList.add(loan);

        return true;
    }
}
