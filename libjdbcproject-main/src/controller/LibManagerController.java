package controller;

import java.sql.Date;
import java.util.List;

import dto.UserDTO;
import dto.BookDTO;
import dto.RentalDTO;
import service.user.UserServiceImpl;
import service.book.BookServiceImpl;
import service.rental.RentalServiceImpl;

public class LibManagerController {

    private UserServiceImpl userService = new UserServiceImpl();
    private BookServiceImpl bookService = new BookServiceImpl();
    private RentalServiceImpl rentalService = new RentalServiceImpl();

    // 1. 회원가입 요청 처리
    public String requestJoin(String id, String last, String first, Date birthDate, String address, String addressDetailed, String phoneNum) {
        UserDTO dto = UserDTO.builder()
                .libId(id)
                .userLastName(last)
                .userFirstName(first)
                .birthDate(birthDate)
                .address(address)
                .addressDetailed(addressDetailed)
                .phoneNum(phoneNum)
                .build();
        
        return userService.join(dto) ? "회원가입 성공" : "가입 실패 (아이디 중복 등)";
    }

    // 2. 회원 목록 조회
    public List<UserDTO> getLibuserList() {
        return userService.getUserList();
    }

    // 회원 정보 수정 요청
    public String requestUpdateUser(String id, String last, String first, Date birthDate, String address, String addressDetailed, String phoneNum) {
        UserDTO dto = UserDTO.builder()
                .libId(id)
                .userLastName(last)
                .userFirstName(first)
                .birthDate(birthDate)
                .address(address)
                .addressDetailed(addressDetailed)
                .phoneNum(phoneNum)
                .build();
        
        // userService.updateUser() 메서드가 필요합니다.
        return userService.updateUser(dto) ? "회원 정보 수정." : "수정 실패 (존재하지 않는 ID 등)";
    }

    // 회원 삭제 요청
    public String requestDeleteUser(String id) {
        // userService.deleteUser() 메서드가 필요합니다.
        return userService.deleteUser(id) ? "회원 탈퇴(삭제) 처리." : "삭제 실패 (ID 확인 필요)";
    }

    // 3. 도서 등록
    public String requestAddBook(String isbn, int bookIdx, String bookName, String bookWriter, String bookPublisher) {
        BookDTO dto = BookDTO.builder()
                .isbn(isbn)
                .bookIdx(bookIdx)
                .bookName(bookName)
                .bookWriter(bookWriter)
                .bookPublisher(bookPublisher)
                .rentalStatus("Y")
                .build();
        
        return bookService.registerBook(dto) ? "도서 등록 성공" : "도서 등록 실패";
    }

    // 4. 도서 목록 조회
    public List<BookDTO> requestBookList() {
        return bookService.getBookList();
    }

    // 4-1. 도서 상세 조회
    public BookDTO requestBookInfo(long regId) {
        return bookService.getBookDetail(regId);
    }

    // 5. 도서 삭제
    public String requestDeleteBook(long regId) {
        boolean result = bookService.deleteBook(regId);
        return result ? "도서 삭제" : "삭제 실패 (대출 중인 도서 혹은 존재하지 않는 번호)";
    }

    // 6. 대출 요청 처리
    public String requestRent(String libId, long regId) {
        RentalDTO dto = RentalDTO.builder()
                .libId(libId)
                .regId(regId)
                .build();
        
        return rentalService.rentBook(dto) ? "대출 처리 완료" : "대출 실패 ";
    }

    // 7. 반납 요청 처리
    public String requestReturn(long rentalId) {

        return rentalService.returnBook(rentalId) ? "반납 완료" : "반납 실패 (정보 불일치)";
    }

    // 8. 특정 유저의 대출 목록 조회
    public void showUserRentalHistory(String libId) {
        UserDTO user = userService.getUserInfo(libId); 

        if (user == null) {
            System.out.println(">> [오류] '" + libId + "'님은 등록되지 않은 회원입니다.");
            return;
        }

        // 🛡️ 두 번째 방어막: 유저가 있다면 이력을 조회
        System.out.println("\n[" + user.getUserLastName() + user.getUserFirstName() + "(" + libId + ")] 님의 전체 대출 이력");
        List<RentalDTO> history = rentalService.getUserRentalRecord(libId);

        if (history == null || history.isEmpty()) {
            System.out.println(">> 해당 회원의 대출 이력이 존재하지 않음.");
            return;
        }

        // JOIN된 컬럼들을 포함하여 헤더 구성
        System.out.println("------------------------------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-15s | %-3s | %-15s | %-10s | %-10s | %-10s | %-10s\n", 
                          "대출번호", "ISBN", "책 번호", "도서명", "저자", "대출일자", "반납 예정일", "반납상태");
        System.out.println("------------------------------------------------------------------------------------------------------");
        
        for (dto.RentalDTO dto : history) {
            // 반납 여부 가공
            String status = (dto.getReturnDate() == null) ? "대출 중" : "반납완료";
            
            // DAO에서 JOIN으로 가져온 상세 정보들을 출력
            System.out.printf("%-8d | %-15s | %-3s | %-20s | %-10s | %-10s | %-10s | %-10s\n", 
                dto.getRentalId(),
                dto.getIsbn(),
                dto.getBookIdx(),
                dto.getBookName(),
                dto.getBookWriter(),
                dto.getRentDate().toString().substring(0, 10), // 날짜 부분
                dto.getDueDate(),
                status
            );
        }
        System.out.println("------------------------------------------------------------------------------------------------------");
    }

    // 9. 전체 대출 목록 조회
    public void showAllRentalHistory() {
        System.out.println("\n[도서관 전체 대출 기록 조회]");
        List<RentalDTO> history = rentalService.getAllRentalRecord();

        if (history == null || history.isEmpty()) {
            System.out.println(">> 대출 기록이 존재하지 않습니다.");
            return;
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-10s | %-15s | %-5s | %-20s | %-10s | %-12s | %-12s | %-8s\n", 
                        "대출번호", "회원ID", "ISBN", "책 번호", "도서명", "저자", "대출일", "반납예정일", "상태");
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        
        for (dto.RentalDTO dto : history) {
            String status = (dto.getReturnDate() == null) ? "대출 중" : "반납완료";
            
            System.out.printf("%-8d | %-10s | %-15s | %-5s | %-20s | %-10s | %-12s | %-12s | %-8s\n", 
                dto.getRentalId(),
                dto.getLibId(),
                dto.getIsbn(),
                dto.getBookIdx(),
                dto.getBookName(),
                dto.getBookWriter(),
                dto.getRentDate().toString().substring(0, 10),
                dto.getDueDate(), // 서비스에서 계산된 예정일
                status
            );
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");

    }
    
    // 10. 대출 중인 도서 조회
    public void showRentalList() {
        System.out.println("\n[도서관 전체 대출 현황 조회]");
        List<RentalDTO> history = rentalService.getCurrentRentalList();

        if (history == null || history.isEmpty()) {
            System.out.println(">> 대출 데이터가 하나도 없습니다.");
            return;
        }

        System.out.println("------------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-6s | %-10s | %-15s | %-5s | %-20s | %-10s | %-12s | %-12s | %-8s\n", 
                        "대출번호", "회원ID", "ISBN", "책 번호", "도서명", "저자", "대출일", "반납예정일", "상태");
        System.out.println("------------------------------------------------------------------------------------------------------------------");
        
        for (dto.RentalDTO dto : history) {
            String status = (dto.getReturnDate() == null) ? "대출 중" : "반납완료";
            
            System.out.printf("%-8d | %-10s | %-15s | %-5s | %-20s | %-10s | %-12s | %-12s | %-8s\n", 
                dto.getRentalId(),
                dto.getLibId(),
                dto.getIsbn(),
                dto.getBookIdx(),
                dto.getBookName(),
                dto.getBookWriter(),
                dto.getRentDate().toString().substring(0, 10),
                dto.getDueDate(), // 서비스에서 계산된 예정일
                status
            );
        }
        System.out.println("------------------------------------------------------------------------------------------------------------------");
    }
}