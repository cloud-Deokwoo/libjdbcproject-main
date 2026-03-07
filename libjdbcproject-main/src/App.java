
import java.util.List;
import java.util.Scanner;

import controller.LibManagerController;
import dto.BookDTO;
import dto.UserDTO;


public class App {
    private static Scanner scan = new Scanner(System.in, "cp949");
    // Controller 레이어
    private static LibManagerController controller = new LibManagerController();

    private static UserDTO userInfo = null; // 현재 로그인 정보
    public static void main(String[] args) throws Exception {
        System.out.println("도서 관리 시스템(ID: admin, PW: admin)");
        System.out.print("관리자 ID: ");
        String id = scan.nextLine();
        System.out.print("관리자 PW: ");
        String pw = scan.nextLine();

        // 관리자 id&pw 검증
        if(!id.equals("admin")) {
            System.out.println("아이디 혹은 비밀번호 틀림.");
            return;
        } else if(!pw.equals("admin")) {
            System.out.println("아이디 혹은 비밀번호 틀림.");
            return;
        } else {
            System.out.println("관리자 로그인 성공");
            menu();
        }
    }

    protected static void menu() {
        while(true) {
            System.out.println("\n" + "=====".repeat(4) + " 도서 관리 JDBC " + "=====".repeat(4));
            System.out.println("1. 회원 관리");
            System.out.println("2. 도서 관리");
            System.out.println("3. 대출 관리");
            System.out.println("0. 프로그램 종료");
            System.out.print("메뉴 선택: ");
            
            String input = scan.nextLine();
            if (input.isEmpty()) continue;
            char choice = input.charAt(0);

            // DB 접속이 필요한 메뉴(1, 2, 3)를 선택했을 때 연결 체크
            if (choice >= '1' && choice <= '3') {
                if (!dbutil.DBUtil.isConnected()) {
                    System.out.println("\n[시스템 오류] DB 서버와 연결 실패.");
                    continue;
                }
            }

            switch(choice) {
                case '1' -> {
                    System.out.println("\n[회원 관리 하위 메뉴]");
                    userManage();
                }
                case '2' -> {
                    System.out.println("\n[도서 관리 하위 메뉴]");
                    bookManage();
                }
                case '3' -> {
                    System.out.println("\n[대출 관리 하위 메뉴]");
                    rentalManage();
                }
                case '0' -> {
                    System.out.println("프로그램을 종료합니다.");
                    scan.close();
                    return;
                }
                default -> System.out.println("잘못된 메뉴 입력입니다.");
            }
        }
    }

    private static void userManage() {
        while (true) {
            System.out.println("\n--- 회원 관리 ---");
            System.out.println("1. 회원 등록");
            System.out.println("2. 전체 회원 목록");
            System.out.println("3. 회원 정보 수정");
            System.out.println("4. 회원 삭제(탈퇴)");
            System.out.println("0. 이전 메뉴로");
            System.out.print("메뉴 선택: ");
            
            char choice = scan.nextLine().charAt(0);

            if (choice == '0') break;

            switch (choice) {
                case '1' -> {
                    System.out.println("[신규 회원 등록]");
                    System.out.print("아이디: "); String id = scan.nextLine();
                    System.out.print("성: "); String last = scan.nextLine();
                    System.out.print("이름: "); String first = scan.nextLine();
                    
                    // 문자열로 입력을 받은 후 변환
                    System.out.print("생년월일(YYYY-MM-DD): "); 
                    String birthStr = scan.nextLine();
                    java.sql.Date birthDate = null;
                    
                    try {
                        birthDate = java.sql.Date.valueOf(birthStr); // "1995-05-20" 형식
                    } catch (IllegalArgumentException e) {
                        System.out.println(">> 날짜 형식이 올바르지 않습니다. (예: 1995-05-20)");
                        break;
                    }

                    System.out.print("주소: "); String address = scan.nextLine();
                    System.out.print("상세주소: "); String addressDetailed = scan.nextLine();
                    String phoneNum = "";  // 휴대폰 번호
                    while (true) {
                        System.out.print("휴대폰 번호(010XXXXXXXX): ");
                        phoneNum = scan.nextLine();

                        if (phoneNum.matches("^010\\d{8}$")) {
                            break;
                        } else {
                            System.out.println(">> 잘못된 형식입니다. '010'을 포함한 숫자 11자리를 입력해주세요. (예: 01012345678)");
                        }
                    }
                    
                    String result = controller.requestJoin(id, last, first, birthDate, address, addressDetailed, phoneNum);
                    System.out.println("결과: " + result);
                }
                case '2' -> {
                    System.out.println("\n[전체 회원 리스트]");
                    List<UserDTO> userList = controller.getLibuserList();
                    
                    if (userList == null || userList.isEmpty()) {
                        System.out.println("등록된 회원이 없습니다.");
                    } else {
                        System.out.println("ID | 도서관ID |  성명  | 생년월일 | 주소 | 상세 주소 |      휴대폰      | 수정일 | 등록일  ");
                        System.out.println("---------------------------------");
                        for (UserDTO user : userList) {
                            System.out.printf(" %s | %s | %s%s | %s | %s | %s | %s | %s | %s\n",
                                user.getId(),
                                user.getLibId(), 
                                user.getUserLastName(), 
                                user.getUserFirstName(),
                                user.getBirthDate(),
                                user.getAddress(),
                                user.getAddressDetailed(),
                                user.getPhoneNum(),
                                user.getModDate(),
                                user.getRegDate());
                        }
                    }
                }
                case '3' -> {
                    System.out.print("수정할 회원의 ID: ");
                    String id = scan.nextLine();
                    System.out.print("수정할 성: "); String last = scan.nextLine();
                    System.out.print("수정할 이름: "); String first = scan.nextLine();
                    // 문자열로 입력을 받은 후 변환
                    System.out.print("생년월일(YYYY-MM-DD): "); 
                    String birthStr = scan.nextLine();
                    java.sql.Date birthDate = null;
                    
                    try {
                        birthDate = java.sql.Date.valueOf(birthStr); // "1995-05-20" 형식
                    } catch (IllegalArgumentException e) {
                        System.out.println(">> 날짜 형식이 올바르지 않습니다. (예: 1995-05-20)");
                        break;
                    }

                    System.out.print("주소: "); String address = scan.nextLine();
                    System.out.print("상세주소: "); String addressDetailed = scan.nextLine();
                    String phoneNum = "";  // 휴대폰 번호
                    while (true) {
                        System.out.print("휴대폰 번호(010XXXXXXXX): ");
                        phoneNum = scan.nextLine();

                        if (phoneNum.matches("^010\\d{8}$")) {
                            break;
                        } else {
                            System.out.println(">> 잘못된 형식입니다. '010'을 포함한 숫자 11자리를 입력해주세요. (예: 01012345678)");
                        }
                    }
                    
                    String result = controller.requestUpdateUser(id, last, first, birthDate, address, addressDetailed, phoneNum);
                    System.out.println("결과: " + result);
                }
                case '4' -> {
                    System.out.print("삭제할 회원의 ID: ");
                    String id = scan.nextLine();
                    System.out.print("정말로 삭제하시겠습니까? (Y/N): ");
                    String confirm = scan.nextLine();
                    
                    if (confirm.equalsIgnoreCase("Y")) {
                        String result = controller.requestDeleteUser(id);
                        System.out.println("결과: " + result);
                        System.out.println(id + " 회원 삭제");
                    }
                }
                default -> System.out.println("잘못된 입력.");
            }
        }
    }

    private static void bookManage() {
        while (true) {
            System.out.println("\n--- 도서 관리 ---");
            System.out.println("1. 도서 등록");
            System.out.println("2. 전체 도서 목록");
            System.out.println("3. 도서 삭제");
            System.out.println("0. 이전 메뉴로");
            System.out.print("메뉴 선택: ");
            char choice = scan.nextLine().charAt(0);

            if (choice == '0') break;

            switch (choice) {
                case '1' -> {
                    System.out.print("ISBN: "); String isbn = scan.nextLine();
                    System.out.print("책 번호: "); int bookIdx = scan.nextInt();
                    scan.nextLine();
                    System.out.print("제목: "); String title = scan.nextLine();
                    System.out.print("저자: "); String writer = scan.nextLine();
                    System.out.print("출판사: "); String publisher = scan.nextLine();
                    
                    // Controller에 도서 등록 요청
                    String result = controller.requestAddBook(isbn, bookIdx, title, writer, publisher);
                    System.out.println("결과: " + result);
                }
                case '2' -> {
                    List<BookDTO> books = controller.requestBookList();
                    System.out.println("\n[전체 도서 리스트]");
                    for (BookDTO b : books) {
                        System.out.printf("등록번호: %d | 책 번호: %s | 제목: %s | 저자: %s | 대여 가능 여부: %s\n",
                                b.getRegId(), b.getBookIdx(), b.getBookName(), b.getBookWriter(), b.getRentalStatus());
                    }
                }
                case '3' -> {
                    List<dto.BookDTO> books = controller.requestBookList();
                    System.out.println("\n[전체 도서 리스트]");
                    for (dto.BookDTO b : books) {
                        System.out.printf("등록번호: %d | 책 번호: %s | 제목: %s | 저자: %s | 대여 가능 여부: %s\n",
                                b.getRegId(), b.getBookIdx(), b.getBookName(), b.getBookWriter(), b.getRentalStatus());
                    }
                    System.out.println("=====".repeat(10));
                    System.out.print("삭제할 도서 등록번호(regId) 입력: ");
                    long regId = Long.parseLong(scan.nextLine());

                    // 1. 삭제 전 도서 정보 가져오기
                    dto.BookDTO book = controller.requestBookInfo(regId);

                    if (book == null) {
                        System.out.println("도서 정보가 존재하지 않음.");
                    } else {
                        // 2. 정보 출력
                        System.out.println("\n[삭제 대상 도서 정보]");
                        System.out.println("제목 : " + book.getBookName());
                        System.out.println("저자 : " + book.getBookWriter());
                        System.out.println("상태 : " + (book.getRentalStatus().equals("Y") ? "삭제가능" : "삭제불가(대출중)"));
                        System.out.println("-------------------------");
                        if(book.getRentalStatus().equals("N")) break;

                        // 3. 삭제 여부 재확인
                        System.out.print("정말로 이 도서를 삭제하시겠습니까? (Y/N): ");
                        String confirm = scan.nextLine();

                        if (confirm.equalsIgnoreCase("Y")) {
                            // 4. Controller에 삭제 요청
                            String result = controller.requestDeleteBook(regId);
                            System.out.println("결과: " + result);
                        } else {
                            System.out.println("삭제 취소.");
                        }
                    }
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private static void rentalManage() {
        while (true) {
            System.out.println("\n--- 대출 관리 ---");
            System.out.println("1. 도서 대출 실행");
            System.out.println("2. 도서 반납 실행");
            System.out.println("3. 유저별 대출 이력 조회");
            System.out.println("4. 전체 대출 이력 조회");
            System.out.println("5. 대출 중 도서 조회");
            System.out.println("0. 이전 메뉴로");
            System.out.print("메뉴 선택: ");
            char choice = scan.nextLine().charAt(0);

            if (choice == '0') break;

            switch (choice) {
                case '1' -> {
                    // 도서 목록 불러오기
                    List<BookDTO> books = controller.requestBookList();
                    System.out.println("\n[전체 도서 리스트]");
                    for (BookDTO b : books) {
                        System.out.printf("등록번호: %d | 책 번호: %s | 제목: %s | 저자: %s | 대여 가능 여부: %s\n",
                                b.getRegId(), b.getBookIdx(), b.getBookName(), b.getBookWriter(), b.getRentalStatus());
                    }

                    System.out.print("대출할 유저 ID: "); String libId = scan.nextLine();
                    System.out.print("대출할 도서 번호: "); 
                    long regId = Long.parseLong(scan.nextLine());
                    
                    String result = controller.requestRent(libId, regId);
                    System.out.println("결과: " + result);
                }
                case '2' -> {
                    // 현재 대출 중인 목록
                    System.out.println("\n[반납할 대출 기록을 선택하세요]");
                    controller.showRentalList();

                    System.out.print("반납할 대출 기록 번호(rentalId) 입력: ");
                    String input = scan.nextLine();
                    
                    try {
                        long rentalId = Long.parseLong(input);
                        
                        // regId 없이 rentalId만 전달
                        String result = controller.requestReturn(rentalId); 
                        System.out.println("\n>> 결과: " + result);
                    } catch (NumberFormatException e) {
                        System.out.println(">> 숫자로 입력해주세요.");
                    }
                }
                case '3' -> {
                    System.out.print("조회할 유저 ID(libId): ");
                    String libId = scan.nextLine();
                    
                    controller.showUserRentalHistory(libId); 
                }
                case '4' -> {
                    // 전체 대출 이력
                    controller.showAllRentalHistory();
                }
                case '5' -> {
                    // 대출 중 도서 이력
                    controller.showRentalList();
                }
                default -> System.out.println("잘못된 입력입니다.");
            }
        }
    }
}
