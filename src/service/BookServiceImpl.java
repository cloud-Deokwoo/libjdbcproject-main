package service;

import repository.*;
import dto.BookDTO;
import util.DBUtil;
import java.sql.*;
import java.util.List;

public class BookServiceImpl implements BookService {
    private BookDAOImpl bookDAO = new BookDAOImpl();

    @Override
    public void registerBook(BookDTO book) {
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            System.out.println("[알림] 제목이 없어 등록이 차단되었습니다.");
            return;
        }
        int result = bookDAO.insert(book);
        if (result > 0)
            System.out.println("도서 등록 성공!");
    }

    @Override
    public List<BookDTO> getAllBooks() {
        return bookDAO.selectAll();
    }

    @Override
    public void rentBook(Long id) {
        executeTransaction(id, false, "대여");
    }

    @Override
    public void returnBook(Long id) {
        executeTransaction(id, true, "반납");
    }

    private void executeTransaction(Long id, boolean status, String type) {
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            int res1 = bookDAO.updateStatus(conn, id, status);

            if (res1 <= 0) {
                throw new Exception("대상이 없거나 이미 " + type + "된 상태입니다.");
            }

            int res2 = status ? bookDAO.updateReturnDate(conn, id)
                    : bookDAO.insertRental(conn, id);

            if (res2 > 0) {
                conn.commit();
                System.out.println("[성공] " + type + " 처리가 완료되었습니다.");
            } else {
                throw new Exception(type + " 기록을 생성하거나 업데이트하는 데 실패했습니다.");
            }
        } catch (Exception e) {
            if (conn != null)
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                }
            System.out.println("[알림] " + e.getMessage());
        } finally {
            if (conn != null)
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException e) {
                }
        }
    }
}