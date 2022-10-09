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
@Data
public class Category extends BaseEntity{

    private String name;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private List<Book> books;
}
