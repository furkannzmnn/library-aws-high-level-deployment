package com.example.lib.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "category")
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Category extends BaseEntity{

    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Book> books;

    public String generateSqlTable() {
        return "CREATE TABLE category (id BIGINT NOT NULL, name VARCHAR(255), PRIMARY KEY (id))";
    }
}
