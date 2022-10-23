package com.example.lib.service;

import com.example.lib.dto.CategoryType;
import com.example.lib.model.Category;
import com.example.lib.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

public class CategoryServiceTest extends BaseServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private CategoryRepository categoryRepository;

    @Test
    void givenCategory_whenLoadCategory_thenReturnCategory() {

        // given - precondition or setup
        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();

        // when -  action or the behaviour that we are going test
        when(categoryRepository.findById(anyLong())).thenReturn(Optional.of(category));

        Category categoryResult = categoryService.loadCategory(anyLong());

        // then - verify the output
        assertEquals(category.getName(), categoryResult.getName());

    }

    @Test
    void givenCategory_whenFindByName_thenReturnCategory() {

        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();

        // when -  action or the behaviour that we are going test
        when(categoryRepository.findByName(CategoryType.COMIC.getValue())).thenReturn(Optional.of(category));

        Category categoryResult = categoryService.findByName(CategoryType.COMIC.getValue());

        // then - verify the output
        assertEquals(category.getName(), categoryResult.getName());
    }
}
