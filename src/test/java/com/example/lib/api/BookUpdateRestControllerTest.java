package com.example.lib.api;

import com.example.lib.dto.BookListItemResponse;
import com.example.lib.dto.BookUpdateRequest;
import com.example.lib.dto.CategoryType;
import com.example.lib.model.BookStatus;
import com.example.lib.service.BookUpdateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles(value = "integration")
class BookUpdateRestControllerTest {

    public final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private BookUpdateService bookUpdateService;

    @Autowired
    public MockMvc mvc;

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void itShouldUpdateTheBook_WhenValidBookUpdateRequestBody() throws Exception{

        // given - precondition or setup
        BookUpdateRequest updatedBookRequest = new BookUpdateRequest();
        updatedBookRequest.setTitle("Updated title");
        updatedBookRequest.setId(1L);
        updatedBookRequest.setCategoryId(1L);

        BookListItemResponse bookListItemResponse = BookListItemResponse.builder().bookStatus(BookStatus.READING)
                .title("Updated title")
                .categoryName(CategoryType.COMIC.name())
                .build();

        // when -  action or the behaviour that we are going test
        when(bookUpdateService.updateBook(updatedBookRequest)).thenReturn(bookListItemResponse);

        // then - verify the output
        mvc.perform(put("/api/v1/bookImage/update")
                        .content(asJsonString(updatedBookRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(jsonPath("$.title").value(updatedBookRequest.getTitle()));
    }
}