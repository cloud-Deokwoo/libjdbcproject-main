package dto;
public class BookDTO {

    private int book_id;
    private String title;
    private String author;
    private String status;

    // 기본 생성자
    public BookDTO(){}

    // 모든 필드를 매개변수로 받는 생성자
    public BookDTO(int book_id, String title, String author, String status){
    this.book_id = book_id; //책 번호
    this.title = title; // 책 제목
    this.author = author; // 저자s
    this.status = status; // 대여 기록
}

// 1. bookId getter/setter
public int getBookId(){return book_id;}
public void setBookId(int bookId) {this.book_id = bookId;}

// 2. title gettet/setter
public String getTitle(){return title;}
public void setTitle(String title){this.title = title;}

// 3. author getter setter
public String getAuthor(){return author;}
public void setAuthor(String author){this.author = author;}

// 4. status getter setter
public String getStatus(){return status;}
public void setStatus(String status){this.status = status;}



}


