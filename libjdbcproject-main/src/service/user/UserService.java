package service.user;

import dto.UserDTO;

public interface UserService {
    boolean join(UserDTO dto);
    UserDTO getUserInfo(String libId);
    boolean updateUser(UserDTO dto);
    boolean deleteUser(String libId);
}
