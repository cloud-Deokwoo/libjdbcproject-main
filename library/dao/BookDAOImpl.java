package dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dto.BookDTO;
import util.DBUtil;

public class BookDAOImpl  implements BookDAO{

    @Override
    public int insert(BookDTO book) {
        String sql = "INSERT INTO BOOKS(title, author) VALUES (?, ?)";

        // 작업이 끝나면 자동으로 닫아 줌
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)){

                // SQL에 ? 자리 값 채우기
                pstmt.setString(1, book.getTitle());
                pstmt.setString(2, book.getAuthor());

                // 실행 결과를 반환 성공하면 1
                return pstmt.executeUpdate();
            } catch (SQLException e){
                e.printStackTrace();
                return 0; //에러 혹은 실패 0
            }
    }

    @Override
    public List<BookDTO> selectAll() {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM BOOKS";

        // 자동으로 닫힘
        try (Connection conn = DBUtil.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql);
        ResultSet rs = pstmt.executeQuery()){ //조회 시 executeQuery()
            while (rs.next()) {
                BookDTO book = new BookDTO(
                    rs.getInt("bookID"), //DB 칼럼 이름**
                    rs.getString("title"),
                rs.getString("author"),
            rs.getString("status"));

            // 리스트 추가
            list.add(book);
                }
            }catch (SQLException e){
                e.printStackTrace();
            }
            return list; // 가득 차면 반환
        }


    @Override
    public int updateStatus(int BookId, String status) {
       String sql ="UPDATE BOOKS SET status = ? WHERE bookID = ?";
       try (Connection conn = DBUtil.getConnection();
    PreparedStatement pstmt = conn.prepareStatement(sql)){
    //   값 채우기
    pstmt.setString(1, status);
    pstmt.setInt(2, BookId);

    return pstmt.executeUpdate();

    } catch (SQLException e){
        e.printStackTrace();
        return 0;
    }

    }

}
