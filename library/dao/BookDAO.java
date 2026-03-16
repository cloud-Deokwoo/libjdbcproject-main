package dao;
import java.util.List;

import dto.BookDTO;

public interface BookDAO {

    // 1. 도서 등록
    int insert(BookDTO book);

    // 2. 전체 도서 목록 조회
    List<BookDTO> selectAll();

    // 3. 도서 상태 수정(ID로 찾아서 Y, N로 변경)
    int updateStatus(int BookId, String status);
}
