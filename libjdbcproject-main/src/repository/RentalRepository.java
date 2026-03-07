package repository;

import java.util.List;
import dto.RentalDTO;

public interface RentalRepository {
    // 새로운 대출 기록 등록
    int insertRental(RentalDTO rentalDTO);

    // 특정 사용자의 전체 대출 이력을 조회
    List<RentalDTO> findRentalListById(String libId);

    // 전체 대출 이력 조회
    List<RentalDTO> findAllRentalRecord();
    
    // rentalId를 통한 도서 검색
    RentalDTO findByRentalId(long rentalId);

    // 도서 반납 시 반납일을 현재 시간으로 업데이트
    int updateReturnDate(long rentalId);

    // 대출 현황 조회
    List<RentalDTO> getActiveRentals();
}