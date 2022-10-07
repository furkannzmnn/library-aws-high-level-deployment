package com.example.lib.model;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "images")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Image extends BaseEntity{
    private String imageUrl;

    public String generateSqlTable() {
        return "CREATE TABLE images (id BIGINT NOT NULL, image_url VARCHAR(255), PRIMARY KEY (id))";
    }
}
