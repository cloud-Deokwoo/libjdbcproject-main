package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dbutil.DBUtil;
import dto.RentalDTO;

public class RentalDAOImpl implements RentalRepository {

    // 대출 기록 등록
    public int insertRental(RentalDTO dto) {
        String sql = "INSERT INTO rental_list (lib_id, reg_id, rent_date, return_date) VALUES (?, ?, NOW(), NULL)";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, dto.getLibId());
            pstmt.setLong(2, dto.getRegId());
            
            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 특정 유저 대출 기록 조회
    public List<RentalDTO> findRentalListById(String libId) {
        List<RentalDTO> list = new ArrayList<>();
        String sql = "SELECT r.*, b.isbn, b.book_idx, b.book_name, b.book_writer, b.book_publisher "
                    + "FROM rental_list r "
                    + "JOIN book b ON r.reg_id = b.reg_id "
                    + "WHERE r.lib_id = ? ORDER BY r.rent_date";

                    
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, libId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    RentalDTO dto = RentalDTO.builder()
                            .rentalId(rs.getLong("rental_id"))
                            .libId(rs.getString("lib_id"))
                            .regId(rs.getLong("reg_id"))
                            .rentDate(rs.getTimestamp("rent_date"))
                            .returnDate(rs.getTimestamp("return_date"))
                            .isbn(rs.getString("isbn"))
                            .bookIdx(rs.getInt("book_idx"))
                            .bookName(rs.getString("book_name"))
                            .bookWriter(rs.getString("book_writer"))
                            .bookPublisher(rs.getString("book_publisher"))
                            .build();
                    list.add(dto);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 모든 대출 목록 조회
    public List<RentalDTO> findAllRentalRecord() {
        List<RentalDTO> list = new ArrayList<>();
        String sql = "SELECT r.*, b.isbn, b.book_idx, b.book_name, b.book_writer, b.book_publisher "
                    + "FROM rental_list r "
                    + "JOIN book b ON r.reg_id = b.reg_id "
                    + "ORDER BY r.rental_id ";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                RentalDTO dto = RentalDTO.builder()
                        .rentalId(rs.getLong("rental_id"))
                        .libId(rs.getString("lib_id"))
                        .regId(rs.getLong("reg_id"))
                        .rentDate(rs.getTimestamp("rent_date"))
                        .returnDate(rs.getTimestamp("return_date"))
                        .isbn(rs.getString("isbn"))
                        .bookIdx(rs.getInt("book_idx"))
                        .bookName(rs.getString("book_name"))
                        .bookWriter(rs.getString("book_writer"))
                        .bookPublisher(rs.getString("book_publisher"))
                        .build();
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 반납 처리 (+ 반납일 업데이트)
    public int updateReturnDate(long rentalId) {
        // 1. 대출 이력에 반납일 업데이트
        String sql1 = "UPDATE rental_list SET return_date = NOW() WHERE rental_id = ?";
        // 2. 해당 대출건의 도서 상태를 'Y'로 변경
        String sql2 = "UPDATE book SET rental_status = 'Y' WHERE reg_id = (SELECT reg_id FROM rental_list WHERE rental_id = ?)";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try {
                // 대출 이력 업데이트
                PreparedStatement pstmt1 = conn.prepareStatement(sql1);
                pstmt1.setLong(1, rentalId);
                pstmt1.executeUpdate();

                // 도서 상태 업데이트
                PreparedStatement pstmt2 = conn.prepareStatement(sql2);
                pstmt2.setLong(1, rentalId);
                pstmt2.executeUpdate();

                conn.commit(); // 둘 다 성공하면 확정
                return 1;
            } catch (Exception e) {
                conn.rollback(); // 하나라도 실패하면 취소
                throw e;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 대출 현황 조회
    public List<RentalDTO> getActiveRentals() {
        List<RentalDTO> list = new ArrayList<>();
        String sql = "SELECT r.*, b.isbn, b.book_idx, b.book_name, b.book_writer, b.book_publisher, b.rental_status "
                    + "FROM rental_list r "
                    + "JOIN book b ON r.reg_id = b.reg_id "
                    + "WHERE r.return_date IS NULL "
                    + "ORDER BY r.rental_id ";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery()) {
            
            while (rs.next()) {
                RentalDTO dto = RentalDTO.builder()
                        .rentalId(rs.getLong("rental_id"))
                        .libId(rs.getString("lib_id"))
                        .regId(rs.getLong("reg_id"))
                        .rentDate(rs.getTimestamp("rent_date"))
                        .returnDate(rs.getTimestamp("return_date"))
                        .isbn(rs.getString("isbn"))
                        .bookIdx(rs.getInt("book_idx"))
                        .bookName(rs.getString("book_name"))
                        .bookWriter(rs.getString("book_writer"))
                        .bookPublisher(rs.getString("book_publisher"))
                        .build();
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // rentalId로 찾기
    @Override
    public RentalDTO findByRentalId(long rentalId) {
        RentalDTO dto = null;
        String sql = "SELECT * FROM rental_list WHERE rental_id = ?";

        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setLong(1, rentalId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // DB에서 꺼내온 정보로 DTO 조립
                    dto = RentalDTO.builder()
                            .rentalId(rs.getLong("rental_id"))
                            .libId(rs.getString("lib_id"))
                            .regId(rs.getLong("reg_id"))
                            .rentDate(rs.getTimestamp("rent_date"))
                            .returnDate(rs.getTimestamp("return_date"))
                            .build();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dto;
    }
}
