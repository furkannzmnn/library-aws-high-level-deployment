package com.example.lib.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@SuperBuilder
public class BookResponse {
    private String url;
    private String key;
    private String title;
    private List<Author> authors;
    private float number_of_pages;
    @JsonIgnore
    private Identifiers identifiers;
    @JsonIgnore
    private List<Object> publishers = new ArrayList<>();
    private String publish_date;
    private String notes;
    private Cover cover;

}
