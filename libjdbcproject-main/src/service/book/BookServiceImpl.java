package service.book;

import java.util.List;
import dto.BookDTO;
import repository.BookRepository;
import repository.BookDAOImpl;

public class BookServiceImpl implements BookService {
    
    private final BookRepository bookRepository = new BookDAOImpl();

    // 모든 도서 출력
    @Override
    public List<BookDTO> getBookList() {
        return bookRepository.findAllBooks();
    }

    // 도서 등록
    @Override
    public boolean registerBook(BookDTO dto) {
        dto.setRentalStatus("Y"); 
        
        // 동일한 ISBN과 소장번호(bookIdx)가 이미 있는지 체크
        if (bookRepository.existsByIsbnAndIdx(dto.getIsbn(), dto.getBookIdx())) return false;
        if (dto.getBookName() == null || dto.getBookName().trim().isEmpty()) {
            System.out.println("도서 제목 기입 필수.");
            return false;
        }
        return bookRepository.insertBook(dto) > 0;
    }

    // 도서 삭제
    public boolean deleteBook(long regId) {
        // 대출 중인 도서는 삭제할 수 없도록 검증
        BookDTO book = bookRepository.findBookByRegId(regId);
        if ("N".equals(book.getRentalStatus())) return false; 

        return bookRepository.deleteBook(regId) > 0;
    }

    // 도서 세부 정보
    @Override
    public BookDTO getBookDetail(long regId) {
        return bookRepository.findBookByRegId(regId);
    }
}