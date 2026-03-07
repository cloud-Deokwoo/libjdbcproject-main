package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import dbutil.DBUtil;
import domain.user.UserVO;
import dto.UserDTO;

public class UserDAOImpl implements UserRepository {

    // 1. 회원 가입 (Insert)
    public int insertUser(UserDTO dto) {
        // DTO의 변환 로직 사용 (생년월일 미래 날짜 검증 등 적용됨)
        UserVO vo = UserDTO.toUserVO(dto);
        
        String sql = "INSERT INTO lib_user (lib_id, last_name, first_name, birth_date, address, address_detailed, phone_num, reg_date) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, NOW())";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vo.getLibId());
            pstmt.setString(2, vo.getUserLastName());
            pstmt.setString(3, vo.getUserFirstName());
            pstmt.setDate(4, vo.getBirthDate());
            pstmt.setString(5, vo.getAddress());
            pstmt.setString(6, vo.getAddressDetailed());
            pstmt.setString(7, vo.getPhoneNum());

            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. 전체 회원 조회
    @Override
    public List<UserDTO> getAllUsers() { 
        List<UserDTO> userList = new ArrayList<>();
        String sql = "SELECT * FROM lib_user WHERE is_deleted = 0 ORDER BY id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                UserVO vo = UserVO.builder()
                        .id(rs.getLong("id"))
                        .libId(rs.getString("lib_id"))
                        .userLastName(rs.getString("last_name"))
                        .userFirstName(rs.getString("first_name"))
                        .birthDate(rs.getDate("birth_date"))
                        .address(rs.getString("address"))
                        .addressDetailed(rs.getString("address_detailed"))
                        .phoneNum(rs.getString("phone_num"))
                        .modDate(rs.getTimestamp("mod_date"))
                        .regDate(rs.getTimestamp("reg_date"))
                        .build();
                
                userList.add(UserDTO.toUserDTO(vo));
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    // 2. 회원 정보 조회
    public UserDTO getUserByLibId(String libId) {
        String sql = "SELECT * FROM lib_user WHERE lib_id = ? ";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, libId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserVO vo = UserVO.builder()
                            .id(rs.getLong("id"))
                            .libId(rs.getString("lib_id"))
                            .userLastName(rs.getString("last_name"))
                            .userFirstName(rs.getString("first_name"))
                            .birthDate(rs.getDate("birth_date"))
                            .address(rs.getString("address"))
                            .addressDetailed(rs.getString("address_detailed"))
                            .phoneNum(rs.getString("phone_num"))
                            .modDate(rs.getTimestamp("mod_date"))
                            .regDate(rs.getTimestamp("reg_date"))
                            .build();
                    
                    return UserDTO.toUserDTO(vo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 3. 회원 정보 수정 (Update)
    public int updateUser(UserDTO dto) {
        UserVO vo = UserDTO.toUserVO(dto);
        
        String sql = "UPDATE lib_user SET last_name=?, first_name=?, birth_date=?, address=?, address_detailed=?, phone_num=?, mod_date=NOW() "
                   + "WHERE lib_id=?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, vo.getUserLastName());
            pstmt.setString(2, vo.getUserFirstName());
            pstmt.setDate(3, vo.getBirthDate());
            pstmt.setString(4, vo.getAddress());
            pstmt.setString(5, vo.getAddressDetailed());
            pstmt.setString(6, vo.getPhoneNum());
            pstmt.setString(7, vo.getLibId());

            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 4. 회원 삭제 (Delete)
    @Override
    public int deleteUser(String libId) {
        // 실제 삭제 대신 is_deleted 상태를 1(true)로 업데이트합니다.
        String sql = "UPDATE lib_user SET is_deleted = 1 WHERE lib_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, libId);
            
            // 업데이트된 행의 수를 반환 (성공 시 1, 실패 시 0)
            return pstmt.executeUpdate();
        } catch (Exception e) {
            // 오류 추적을 위한 로그 출력
            e.printStackTrace();
        }
        return 0;
    }

    // 5. id 중복 체크 (Exists)
    @Override
    public boolean existsByLibId(String libId) {
        String sql = "SELECT COUNT(*) FROM lib_user WHERE lib_id = ?";
        
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, libId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // 결과 값이 0보다 크면 이미 존재하는 ID
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
