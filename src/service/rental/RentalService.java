package service.rental;

import java.util.List;

import dto.RentalDTO;

public interface RentalService {
    boolean rentBook(RentalDTO rentalDto);

    boolean returnBook(long rentalId);

    List<RentalDTO> getUserRentalRecord(String libId);

    List<RentalDTO> getAllRentalRecord();

    List<RentalDTO> getCurrentRentalList();
}
