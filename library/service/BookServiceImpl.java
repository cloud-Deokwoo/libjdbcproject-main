package service;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import dao.BookDAO;
import dao.BookDAOImpl;
import dto.BookDTO;
import util.DBUtil;


public class BookServiceImpl implements BookService {
    // 1. 여기서 일 처리
    private BookDAO bookDAO = new BookDAOImpl();

    // 2. 도서 등록
    public boolean registerBook(BookDTO book){
        // 제목 빈칸 안 됨
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()){
            System.out.println("제목을 입력해 주세요");
            return false;
        }

        // 저자 빈칸 안 됨
        if (book.getAuthor() == null || book.getAuthor().trim().isEmpty()){
            System.out.println("저자를 입력해 주세요");
            return false;
        }
        bookDAO.insert(book);
        System.out.println("성공적으로 저장되었습니다");
            return true;
    }

    // 3. 전체 목록
    public List<BookDTO> getAllBooks(){
        List<BookDTO> list = bookDAO.selectAll();
        if (list == null){
            return new ArrayList<>();
        }
        return list;
    }

    // 4. 대여 및 반납
    public void changeStatus(int bookId, String status){
        Connection conn = null;
        
    
    }
}
