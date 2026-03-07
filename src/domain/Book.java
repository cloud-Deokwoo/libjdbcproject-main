package domain;

public class Book {
    private Long id;
    private String title;
    private String author;
    private int price;
    private String status;

    public Book(String title, String author, int price) {
        this.title = title;
        this.author = author;
        this.price = price;
        this.status = "Y";
    }

    public Book(Long id, String title, String author, int price, String status) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.price = price;
        this.status = status;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return String.format("[%d] %s - %s (%d원) | 대여가능: %s",
                id, title, author, price, status);
    }
}