package view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import dto.BookDTO;
import service.BookServiceImpl;

public class Main {

    public static void main(String[] args){
        BookServiceImpl service = new BookServiceImpl();
        Scanner sc = new Scanner(System.in);

        while (true){
            System.out.println("\n 도서 관리 시스템");
            System.out.println("1. 도서  등록 \n 2. 도서 목록 \n 3. 대여/반납 \n 0. 종료");
            System.out.println(" 메뉴를 선택해 주세요: ");

        int menu = -1;
        try{
            menu = sc.nextInt();
            sc.nextLine();
        } catch (InputMismatchException e){
            System.out.println("0~3번만 사용 가능합니다");
            sc.nextLine();
            continue;
        }

            if(menu == 1){
                // 1. 도서 등록
                    while(true){
                System.out.println("제목: ");
                String title = sc.nextLine();
                System.out.println("저자: ");
                String author = sc.nextLine();

                // 등록한 도서 정보 만들어서 전달
                boolean isSuccess = service.registerBook(new BookDTO(0, title, author, "대여 가능"));
                if (isSuccess){
                System.out.println("도서가 등록되었습니다.");
                    break;
            }
                System.out.println("다시 입력해 주세요");
            }
            } else if(menu == 2){
                // 2. 도서 목록
                System.out.println("\n [현재 보유 도서 리스트]");
                List<BookDTO> books = service.getAllBooks();

                if (books.isEmpty()){
                    System.out.println("등록된 도서가 없습니다");
                }else{
                for (BookDTO b : books){
                    System.out.println("ID: " + b.getBookId() + "\n 제목: " + b.getTitle() +
                "\n 저자: " + b.getAuthor() + "\n 상태: " + b.getStatus()) ;
                }
            }
            
                // 3. 대여/반납
                System.out.println("대여 및 반납 도서 번호");
                int id = sc.nextInt();
                sc.nextLine();
                System.out.println("대여/반납(Y/N)");
                String status = sc.nextLine();

                service.changeStatus(id, status);
                System.out.println("상태가 업데이트되었습니다.");
            } else if (menu == 0){
                // 0. 종료
                System.out.println("프로그램을 종료합니다. 안녕히 계세요.");
                break;
            } else{
                System.out.println("잘못된 번호입니다. 다시 선택해 주세요.");
            }

            

        }

        sc.close();


    }

}
