package service;

import java.util.List;
import dto.BookDTO;

public interface BookService {

    void registerBook(BookDTO book);

    List<BookDTO> getAllBooks();

    void rentBook(Long id);

    void returnBook(Long id);
}