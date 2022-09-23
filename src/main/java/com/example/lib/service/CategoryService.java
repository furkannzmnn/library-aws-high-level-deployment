package com.example.lib.service;

import com.example.lib.dto.ErrorCode;
import com.example.lib.exception.GenericException;
import com.example.lib.model.Category;
import com.example.lib.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public Category loadCategory(Long id) {
        return categoryRepository.findById(id).orElseThrow();
    }

    public Category findByName(String value) {
        return categoryRepository.findByName(value).orElseThrow(() -> GenericException.builder().errorCode(ErrorCode.CATEGORY_NOT_FOUND).build());
    }
}
