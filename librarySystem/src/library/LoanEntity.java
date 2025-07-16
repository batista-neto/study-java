package library;

import books.BookEntity;

import java.time.LocalDateTime;

public record LoanEntity(BookEntity book, String clientName, LocalDateTime loanDate) {}
