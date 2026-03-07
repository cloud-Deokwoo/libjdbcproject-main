package repository;

import java.util.List;
import dto.UserDTO;

public interface UserRepository {

    // 새로운 회원을 등록
    int insertUser(UserDTO dto);

    // 회원 아이디(libId)로 특정 회원의 상세 정보를 조회
    UserDTO getUserByLibId(String libId);

    // 시스템에 등록된 모든 회원 목록을 조회 (관리자용)
    List<UserDTO> getAllUsers();

    // 회원 정보를 수정합니다.
    int updateUser(UserDTO dto);

    // 회원을 탈퇴 처리하거나 삭제
    int deleteUser(String libId);

    // 아이디 중복 체크를 위한 메서드
    boolean existsByLibId(String libId);
}
