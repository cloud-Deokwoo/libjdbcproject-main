package repository;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import dto.BookDTO;

public interface BookDAO {

    int insert(BookDTO book);

    int updateStatus(Connection conn, Long id, boolean status) throws SQLException;

    List<BookDTO> selectAll();

    int insertRental(Connection conn, Long bookId) throws SQLException;

    int updateReturnDate(Connection conn, Long bookId) throws SQLException;
}