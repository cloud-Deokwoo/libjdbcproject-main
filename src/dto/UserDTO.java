package dto;

import java.sql.Date;
import java.sql.Timestamp;

import domain.user.UserVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"modDate", "regDate"})
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    // user 테이블 필드 생성
    private long id;
    private String libId;
    private String userLastName;
    private String userFirstName;
    private Date birthDate;
    private String address;
    private String addressDetailed;
    private String phoneNum;
    private Timestamp modDate;
    private Timestamp regDate;

    public static UserVO toUserVO(UserDTO userDTO) {
        return UserVO.builder()
                    .id(userDTO.id)
                    .libId(userDTO.libId)
                    .userLastName(userDTO.userLastName)
                    .userFirstName(userDTO.userFirstName)
                    .birthDate(toChangeAge(userDTO.getBirthDate()))
                    .address(userDTO.address)
                    .addressDetailed(userDTO.addressDetailed)
                    .phoneNum(userDTO.phoneNum)
                    .modDate(userDTO.getModDate())
                    .regDate(userDTO.getRegDate())
                    .build();
    }

    public static UserDTO toUserDTO(UserVO userVO) {
        return UserDTO.builder()
                    .id(userVO.getId())
                    .libId(userVO.getLibId())
                    .userLastName(userVO.getUserLastName())
                    .userFirstName(userVO.getUserFirstName())
                    .birthDate(toChangeAge(userVO.getBirthDate()))
                    .address(userVO.getAddress())
                    .addressDetailed(userVO.getAddressDetailed())
                    .phoneNum(userVO.getPhoneNum())
                    .modDate(userVO.getModDate())
                    .regDate(userVO.getRegDate())
                    .build();
    }

    private static Date toChangeAge(Date age) {
        Date now = new Date(System.currentTimeMillis());
        if(age.before(now)) {
            return age;
        } else {
            return now;
        }
    }
}
