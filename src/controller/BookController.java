package controller;

import dto.BookDTO;
import service.BookService;
import service.BookServiceImpl;

import java.util.Scanner;

public class BookController {
    private BookService bookService = new BookServiceImpl();
    private Scanner sc = new Scanner(System.in, "MS949");

    public void start() {
        while (true) {
            System.out.println("\n--- 도서 관리 시스템 ---");
            System.out.println("1. 도서 등록 | 2. 전체 도서 조회 | 3. 도서 대여 | 4. 도서 반납 | 0. 종료");
            System.out.print("선택 > ");

            String menu = sc.nextLine();

            switch (menu) {
                case "1":
                    register();
                    break;
                case "2":
                    list();
                    break;
                case "3":
                    rent();
                    break;
                case "4":
                    returnBook();
                    break;
                case "0":
                    System.out.println("프로그램을 종료합니다.");
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }

    private void register() {
        System.out.print("제목: ");
        String title = sc.nextLine();
        System.out.print("저자: ");
        String author = sc.nextLine();

        BookDTO newBook = new BookDTO();
        newBook.setTitle(title);
        newBook.setAuthor(author);

        bookService.registerBook(newBook);
    }

    private void list() {
        bookService.getAllBooks().forEach(System.out::println);
    }

    private void rent() {
        System.out.print("대여할 도서 번호(ID): ");
        Long id = Long.parseLong(sc.nextLine());
        bookService.rentBook(id);
    }

    private void returnBook() {
        System.out.print("반납할 도서 번호(ID): ");
        Long id = Long.parseLong(sc.nextLine());
        bookService.returnBook(id);
    }
}