package domain.user;

import java.sql.Timestamp;
import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
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
}
