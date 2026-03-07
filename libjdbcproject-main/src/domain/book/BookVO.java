package domain.book;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookVO {
    private long regId;
    private String isbn;
    private int bookIdx;
    private String bookName;
    private String bookWriter;
    private String bookPublisher;
    private Timestamp modDate;
    private Timestamp regDate;
    private String rentalStatus;
}
