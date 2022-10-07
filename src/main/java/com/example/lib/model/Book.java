package com.example.lib.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "book")
@Getter
@Builder(toBuilder = true)
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book extends BaseEntity {
    private String title;
    private String authorName;
    @Enumerated(EnumType.STRING)
    private BookStatus bookStatus;
    private String publisher;
    private Integer lastPageNumber;
    private Integer totalPage;
    @OneToOne
    private Image image;
    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
    private Long userId;

    public String generateSqlTable() {
        return "CREATE TABLE book (id BIGINT NOT NULL, title VARCHAR(255), author_name VARCHAR(255), book_status VARCHAR(255), publisher VARCHAR(255), last_page_number INTEGER, total_page INTEGER, image_id BIGINT, category_id BIGINT, user_id BIGINT, PRIMARY KEY (id))";
    }
}
