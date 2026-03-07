package repository;

import java.util.List;

import dto.BookDTO;

public interface BookRepository {
    // 새로운 도서 등록
    int insertBook(BookDTO dto);

    // 시스템에 등록된 모든 도서 목록을 조회
    List<BookDTO> findAllBooks();

    // 특정 도서의 대출 가능 상태를 변경
    int updateRentalStatus(long regId, String status);

    // 도서 정보를 시스템에서 삭제
    int deleteBook(long regId);

    // 특정 도서의 상세 정보를 조회
    BookDTO findBookByRegId(long regId);

    // 도서 중복 여부
    boolean existsByIsbnAndIdx(String isbn, int bookIdx);
}
