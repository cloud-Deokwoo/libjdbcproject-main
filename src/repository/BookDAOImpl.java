package repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dto.BookDTO;
import util.DBUtil;

public class BookDAOImpl implements BookDAO {

    @Override
    public int insert(BookDTO book) {
        String sql = "INSERT INTO BOOKS (title, author, is_available) VALUES (?, ?, true)";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    @Override
    public int updateStatus(Connection conn, Long id, boolean status) throws SQLException {
        String sql = "UPDATE BOOKS SET is_available = ? WHERE id = ? AND is_available = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setBoolean(1, status);
            pstmt.setLong(2, id);
            pstmt.setBoolean(3, !status);
            return pstmt.executeUpdate();
        }
    }

    public int insertRental(Connection conn, Long bookId) throws SQLException {
        String sql = "INSERT INTO RENTALS (book_id) VALUES (?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, bookId);
            return pstmt.executeUpdate();
        }
    }

    public int updateReturnDate(Connection conn, Long bookId) throws SQLException {
        String sql = "UPDATE RENTALS SET return_date = CURRENT_TIMESTAMP " +
                "WHERE book_id = ? AND return_date IS NULL";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setLong(1, bookId);
            return pstmt.executeUpdate();
        }
    }

    @Override
    public List<BookDTO> selectAll() {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BOOKS";
        try (Connection conn = DBUtil.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                BookDTO book = new BookDTO();
                book.setId(rs.getLong("id"));
                book.setTitle(rs.getString("title"));
                book.setAuthor(rs.getString("author"));
                book.setAvailable(rs.getBoolean("is_available"));
                list.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}