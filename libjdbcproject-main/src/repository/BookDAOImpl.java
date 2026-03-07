package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dbutil.DBUtil;
import dto.BookDTO;

public class BookDAOImpl implements BookRepository {

    // 1. 도서 등록 (Create)
    public int insertBook(BookDTO dto) {
        String sql = "INSERT INTO book (isbn, book_idx, book_name, book_writer, book_publisher, reg_date, mod_date, rental_status) "
                   + "VALUES (?, ?, ?, ?, ?, NOW(), NOW(), ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getIsbn());
            pstmt.setInt(2, dto.getBookIdx());
            pstmt.setString(3, dto.getBookName());
            pstmt.setString(4, dto.getBookWriter());
            pstmt.setString(5, dto.getBookPublisher());
            
            // DTO의 무결성 체크 메서드 활용하여 'Y' 또는 'N' 저장
            char status = BookDTO.rentalStatusIntegrity(dto.getRentalStatus());
            pstmt.setString(6, String.valueOf(status));

            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. 전체 도서 목록 조회 (Read All)
    public List<BookDTO> findAllBooks() {
        List<BookDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM book WHERE is_deleted = FALSE ORDER BY reg_id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                BookDTO dto = BookDTO.builder()
                        .regId(rs.getLong("reg_id"))
                        .isbn(rs.getString("isbn"))
                        .bookIdx(rs.getInt("book_idx"))
                        .bookName(rs.getString("book_name"))
                        .bookWriter(rs.getString("book_writer"))
                        .bookPublisher(rs.getString("book_publisher"))
                        .regDate(rs.getTimestamp("reg_date"))
                        .modDate(rs.getTimestamp("mod_date"))
                        .rentalStatus(rs.getString("rental_status"))
                        .build();
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 3. 도서 대출 가능 여부(상태) 업데이트
    public int updateRentalStatus(long regId, String status) {
        String sql = "UPDATE book SET rental_status = ?, mod_date = NOW() WHERE reg_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            char validStatus = BookDTO.rentalStatusIntegrity(status);
            pstmt.setString(1, String.valueOf(validStatus));
            pstmt.setLong(2, regId);

            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 4. 도서 정보 삭제
    public int deleteBook(long regId) {
        String sql = "UPDATE book SET is_deleted = 1 WHERE reg_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, regId);
            
            // 데이터가 성공적으로 업데이트되면 1을 반환.
            return pstmt.executeUpdate();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 5. 특정 도서 상세 조회
    @Override
    public BookDTO findBookByRegId(long regId) {
        String sql = "SELECT * FROM book WHERE reg_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, regId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // DB에서 가져온 데이터를 BookDTO 객체로 빌드해서 반환
                    return BookDTO.builder()
                            .regId(rs.getLong("reg_id"))
                            .isbn(rs.getString("isbn"))
                            .bookIdx(rs.getInt("book_idx"))
                            .bookName(rs.getString("book_name"))
                            .bookWriter(rs.getString("book_writer"))
                            .bookPublisher(rs.getString("book_publisher"))
                            .regDate(rs.getTimestamp("reg_date"))
                            .modDate(rs.getTimestamp("mod_date"))
                            .rentalStatus(rs.getString("rental_status"))
                            .build();
                }
            }
        } catch (Exception e) {
            System.err.println("도서 상세 조회 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean existsByIsbnAndIdx(String isbn, int bookIdx) {
        // 1. 해당 ISBN과 소장번호가 일치하는 데이터의 개수를 세는 쿼리
        String sql = "SELECT COUNT(*) FROM book WHERE isbn = ? AND book_idx = ?";
        
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, isbn);
            pstmt.setInt(2, bookIdx);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.err.println("도서 중복 체크 중 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }
}
