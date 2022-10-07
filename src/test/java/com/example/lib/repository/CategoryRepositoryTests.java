package com.example.lib.repository;

import com.example.lib.dto.CategoryType;
import com.example.lib.dto.ErrorCode;
import com.example.lib.exception.GenericException;
import com.example.lib.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class CategoryRepositoryTests extends BaseRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenCategoryObject_whenSave_thenReturnSavedCategory() {

        // given - precondition or setup
        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();

        // when -  action or the behaviour that we are going test
        Category savedCategory = categoryRepository.save(category);

        // then - verify the output
        assertThat(savedCategory.getId()).isGreaterThan(0);
        assertThat(savedCategory).isNotNull();
    }

    @Test
    public void givenCategoryObject_whenFindById_thenReturnCategoryObject() {

        // given - precondition or setup
        Category category = Category.builder().name(CategoryType.COMIC.getValue()).build();

        Category savedCategory = categoryRepository.save(category);

        // when -  action or the behaviour that we are going test
        Category returnedCategory = categoryRepository.findById(savedCategory.getId()).orElseThrow(() -> GenericException.builder().errorCode(ErrorCode.CATEGORY_NOT_FOUND).build());

        // then - verify the output
        assertThat(returnedCategory).isNotNull();
    }

    @Test
    public void givenCategoryList_whenFindAll_thenCategoryList(){

        // given - precondition or setup
        Category categoryComic = Category.builder().name(CategoryType.COMIC.getValue()).build();
        Category categoryScience = Category.builder().name(CategoryType.SCIENCE.getValue()).build();

        categoryRepository.save(categoryComic);
        categoryRepository.save(categoryScience);

        // when -  action or the behaviour that we are going test
        List<Category> categoryList = categoryRepository.findAll();

        // then - verify the output
        assertThat(categoryList).isNotNull();
        assertThat(categoryList.size()).isEqualTo(2);

    }

    @Test
    public void givenCategoryObject_whenFindByName_thenReturnCategoryObject() {

        // given - precondition or setup
        Category categoryComic = Category.builder().name(CategoryType.COMIC.getValue()).build();
        categoryRepository.save(categoryComic);

        String name = CategoryType.COMIC.getValue();

        // when -  action or the behaviour that we are going test
        Category category = categoryRepository.findByName(name).orElseThrow(() -> GenericException.builder().errorCode(ErrorCode.CATEGORY_NOT_FOUND).build());

        // then - verify the output
        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void givenCategoryObject_whenUpdateCategory_thenReturnUpdatedCategory(){

        // given - precondition or setup
        Category categoryComic = Category.builder().name(CategoryType.COMIC.getValue()).build();
        categoryRepository.save(categoryComic);

        String name = CategoryType.COMIC.getValue();

        // when -  action or the behaviour that we are going test
        Category category = categoryRepository.findByName(name).orElseThrow(() -> GenericException.builder().errorCode(ErrorCode.CATEGORY_NOT_FOUND).build());;

        Category changedCategory = category.toBuilder().name(CategoryType.RESEARCH_HISTORY.getValue()).build();

        Category updatedAddress =  categoryRepository.save(changedCategory);

        // then - verify the output
        assertThat(updatedAddress.getName()).isEqualTo(CategoryType.RESEARCH_HISTORY.getValue());
    }

    @Test
    public void givenCategoryObject_whenDelete_thenRemoveCategory(){

        // given - precondition or setup
        Category categoryComic = Category.builder().name(CategoryType.COMIC.getValue()).build();
        categoryRepository.save(categoryComic);

        System.out.println(categoryComic.getId());

        // when -  action or the behaviour that we are going test
        categoryRepository.deleteById(categoryComic.getId());
        Optional<Category> categoryOptional = categoryRepository.findById(categoryComic.getId());

        // then - verify the output
        assertThat(categoryOptional).isEmpty();
    }
}
