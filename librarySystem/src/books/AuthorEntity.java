package books;

import java.text.DateFormat;
import java.time.LocalDate;
import java.util.UUID;

public record AuthorEntity(UUID id, String name, LocalDate dateOfBirth) {}
