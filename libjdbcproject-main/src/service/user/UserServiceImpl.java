package service.user;

import java.util.List;

import dto.UserDTO;
import repository.UserRepository;
import repository.UserDAOImpl;

public class UserServiceImpl implements UserService {
    private final UserRepository userRepository = new UserDAOImpl();

    // 회원가입 서비스
    public boolean join(UserDTO dto) {
        // 1. 아이디 중복 체크
        if (userRepository.existsByLibId(dto.getLibId())) {
            return false;
        }
        // 2. 가입 진행
        return userRepository.insertUser(dto) > 0;
    }

    // 특정 유저 정보 보기
    public UserDTO getUserInfo(String libId) {
        return userRepository.getUserByLibId(libId);
    }
    // 모든 유저 보기
    public List<UserDTO> getUserList() {
        return userRepository.getAllUsers();
    }

    @Override
    public boolean updateUser(UserDTO dto) {
        // 수정하려는 회원이 존재하는지 먼저 체크
        if(!userRepository.existsByLibId(dto.getLibId())) return false;
        return userRepository.updateUser(dto) > 0;
    }

    @Override
    public boolean deleteUser(String libId) {
        // 대출 중인 책이 있는 회원은 삭제(탈퇴) 불가 로직
        return userRepository.deleteUser(libId) > 0;
    }
}
