package com.example.lib.service;

import com.example.lib.dto.BookResponse;
import com.example.lib.model.Book;
import com.example.lib.model.BookStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;

import static com.example.lib.service.BookSaveService.OPENLIBRARY_SEARCH_REQUEST;
import static com.example.lib.service.BookSaveService.OPENLIBRARY_SEARCH_REQUEST_SUFFIX;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class BookSaveServiceTest {



}