package service;

import java.util.List;

import dto.BookDTO;

public interface BookService {

    // 도서 등록
    boolean registerBook(BookDTO bookDTO);

    // 전체 목록 조회
    List<BookDTO> getAllBooks();

    // 대여/반납
    void changeStatus(int bookId, String status);

}
