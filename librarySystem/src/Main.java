import books.AuthorEntity;
import books.BookEntity;
import library.LibraryController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;


public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int opcao;

        LibraryController libraryController = new LibraryController();

        long timestamp = System.currentTimeMillis();
        Date dateNow = new Date(timestamp);
        AuthorEntity authorEntity = new AuthorEntity(UUID.randomUUID(), "Autor 1", LocalDate.parse("02/02/2000", DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        BookEntity bookEntity1 = new BookEntity("Livro 1", authorEntity, dateNow);
        BookEntity bookEntity2 = new BookEntity("Livro 2", authorEntity, dateNow);
        libraryController.addBook(bookEntity1);
        libraryController.addBook(bookEntity2);

        do {
            exibirMenu();
            opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    System.out.println("Digite o título do livro: ");
                    String bookTitle = scanner.nextLine();
                    System.out.println("Digite o nome do autor: ");
                    String authorName = scanner.nextLine();
                    System.out.println("Digite a data de nascimento do autor (ex: 01/02/2025): ");
                    String dateOfBirth = scanner.nextLine();

                    DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate dateTransformed = LocalDate.parse(dateOfBirth, format);

                    AuthorEntity author = new AuthorEntity(UUID.randomUUID(), authorName, dateTransformed);

                    long createTimestamp = System.currentTimeMillis();
                    Date date = new Date(createTimestamp);

                    BookEntity newBook = new BookEntity(bookTitle, author, date);

                    libraryController.addBook(newBook);


                    System.out.println(libraryController.listBooks());
                    break;
                case 2:
                    var books = libraryController.listBooks();
                    System.out.println("Lista de livros: " + books);
                    break;
                case 3:
                    System.out.println("Digite o título do livro que quer reservar: ");
                    String bookName = scanner.nextLine();
                    System.out.println("Digite seu nome: ");
                    String username = scanner.nextLine();
                    boolean reserved = libraryController.reserveBook(bookName, username);
                    if(!reserved) {
                        System.out.println("Livro não encrotrado ou ja reseervado");
                        break;
                    }
                    System.out.println("Reserva realizada com sucesso");
                    break;
                case 0:
                    System.out.println("Saindo do programa.");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);

        scanner.close();
    }

    public static void exibirMenu() {
        System.out.println("\n===== Menu =====");
        System.out.println("1. Cadastrar um livro");
        System.out.println("2. Listar todos os livros");
        System.out.println("3. Realizar empréstimo");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }
}