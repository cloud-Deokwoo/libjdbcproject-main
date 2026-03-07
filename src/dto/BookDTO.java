package dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private long regId;
    private String isbn;
    private int bookIdx;
    private String bookName;
    private String bookWriter;
    private String bookPublisher;
    private Timestamp modDate;
    private Timestamp regDate;
    private String rentalStatus;

    public static char rentalStatusIntegrity(String rentalStatus) {
        char status = rentalStatus.toUpperCase().charAt(0);
        return status;
    }
}
