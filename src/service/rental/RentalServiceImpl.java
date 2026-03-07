package service.rental;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

import dto.BookDTO;
import dto.RentalDTO;
import dto.UserDTO;
import repository.RentalRepository;
import repository.UserDAOImpl;
import repository.UserRepository;
import repository.RentalDAOImpl;
import repository.BookRepository;
import repository.BookDAOImpl;

public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository = new RentalDAOImpl();
    private final BookRepository bookRepository = new BookDAOImpl();
    private final UserRepository userRepository = new UserDAOImpl();

    // 대출 실행
    @Override
    public boolean rentBook(RentalDTO rentalDTO) {
        // 트랜잭션 개념 적용
        try {
            // 유저 존재 여부 체크
            UserDTO user = userRepository.getUserByLibId(rentalDTO.getLibId());
            if(user == null) {
                System.out.println("존재하지 않는 ID");
                return false;
            }
            // 도서 존재 여부 체크
            BookDTO book = bookRepository.findBookByRegId(rentalDTO.getRegId());
            if (book == null) {
                System.out.println("존재하지 않는 도서 번호.");
                return false;
            }

            // 2. 추가 방어막: 이미 대출 중인지 체크
            if ("N".equals(book.getRentalStatus())) {
                System.out.println("이미 대출 중인 도서.");
                return false;
            }
            // 3. 대출 이력 생성
            int insertResult = rentalRepository.insertRental(rentalDTO);
            
            // 4. 도서 상태 업데이트
            int updateResult = bookRepository.updateRentalStatus(rentalDTO.getRegId(), "N");

            // 둘 다 성공해야 최종 성공
            return (insertResult > 0 && updateResult > 0);
            
        } catch(Exception e) {
            System.out.println("DB 오류 발생" + e.getMessage());
            return false;
        }
        
    }

    @Override
    public boolean returnBook(long rentalId) {
        // 1. 대출 번호로 정보 조회
        RentalDTO rental = rentalRepository.findByRentalId(rentalId);
        
        // 2. 검증
        if (rental == null) {
            System.out.println("해당 대출 번호를 찾을 수 없음.");
            return false;
        }
        if (rental.getReturnDate() != null) {
            System.out.println("이미 반납 처리가 완료된 도서.");
            return false;
        }

        // 3. 기록에서 찾아낸 regId로 반납 로직 수행
        long regId = rental.getRegId(); 
        
        int returnResult = rentalRepository.updateReturnDate(rentalId);
        int statusResult = bookRepository.updateRentalStatus(regId, "Y");

        return (returnResult > 0 && statusResult > 0);
    }

    // 특정 유저 대출 기록
    @Override
    public List<RentalDTO> getUserRentalRecord(String libId) {
        List<RentalDTO> list = rentalRepository.findRentalListById(libId);
    
        for (RentalDTO dto : list) {
            // 대출일(Timestamp)을 LocalDate로 변환하여 7일 더하기
            LocalDate rentDate = dto.getRentDate().toLocalDateTime().toLocalDate(); // Timestamp -> LocalDate 변환
            LocalDate dueDate = rentDate.plusDays(7);
            
            // DTO에 계산된 예정일 설정
            dto.setDueDate(Date.valueOf(dueDate));
        }
        
        return list;
    }

    // 전체 대출 기록
    @Override
    public List<RentalDTO> getAllRentalRecord() {
        List<RentalDTO> list = rentalRepository.findAllRentalRecord();
        
        for (RentalDTO dto : list) {
            // 대출일 기준 7일 뒤 예정일 계산 (Service 계층의 역할)
            if (dto.getRentDate() != null) {
                java.time.LocalDate dueDate = dto.getRentDate().toLocalDateTime().toLocalDate().plusDays(7);
                dto.setDueDate(java.sql.Date.valueOf(dueDate));
            }
        }
        return list;
    }

    // 대출 현황
    @Override
    public List<RentalDTO> getCurrentRentalList() {
        List<RentalDTO> list = rentalRepository.getActiveRentals();
        
        for (RentalDTO dto : list) {
            // 대출일 기준 7일 뒤 예정일 계산 (Service 계층의 역할)
            if (dto.getRentDate() != null) {
                java.time.LocalDate dueDate = dto.getRentDate().toLocalDateTime().toLocalDate().plusDays(7);
                dto.setDueDate(java.sql.Date.valueOf(dueDate));
            }
        }
        return list;
    }
}