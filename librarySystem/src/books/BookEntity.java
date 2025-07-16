package books;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class BookEntity {

    private UUID id;
    private String title;
    private AuthorEntity author;
    private boolean available;
    private Date registerDate;
    private Date updateDate;

    public BookEntity(String title, AuthorEntity author, Date date) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.author = author;
        this.available = true;
        this.registerDate = date;
        this.updateDate = date;
    }

    @Override
    public String toString() {
        return "title: " + title + '\'' +
                ", Autor: " + author.name() +
                ", Disponível: " + available +
                ", Data de registro: " + registerDate +
                ", Data de atualizacao: " + updateDate +
                '}' + "\n";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public AuthorEntity getAuthor() {
        return author;
    }

    public void setAuthor(AuthorEntity author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }
}
