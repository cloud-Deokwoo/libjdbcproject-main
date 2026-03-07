package domain.rental;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RentalVO {
    private long rentalId;
    private String libId;
    private long regId;
    private Timestamp rentDate;
    private Timestamp returnDate;
}
