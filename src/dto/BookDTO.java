package dto;

public class BookDTO {
    private Long id;
    private String title;
    private String author;
    private boolean isAvailable;

    public BookDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        String status = isAvailable ? "대여 가능" : "대여 중";
        return String.format("번호: %d | 제목: %s | 저자: %s | 상태: %s",
                id, title, author, status);
    }
}