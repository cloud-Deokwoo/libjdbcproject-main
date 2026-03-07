package dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Date;
import java.sql.Timestamp;

@Data
@Builder
public class RentalDTO {
    // rental_list 테이블 정보
    private long rentalId;
    private String libId;
    private long regId;
    private Timestamp rentDate;
    private Timestamp returnDate;

    // JOIN으로 가져온 book 테이블 정보 (화면 출력용)
    private String isbn;
    private int bookIdx;
    private String bookName;
    private String bookWriter;
    private String bookPublisher;
    private Date dueDate;
}
