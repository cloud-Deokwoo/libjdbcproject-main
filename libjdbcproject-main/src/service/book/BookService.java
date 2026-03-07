package service.book;

import java.util.List;

import dto.BookDTO;

public interface BookService {
    List<BookDTO> getBookList();
    boolean registerBook(BookDTO dto);
    BookDTO getBookDetail(long regId);
    boolean deleteBook(long regId);
}
