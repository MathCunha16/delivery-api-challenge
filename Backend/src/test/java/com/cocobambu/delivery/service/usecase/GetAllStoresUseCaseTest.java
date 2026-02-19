package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.response.StoreResponse;
import com.cocobambu.delivery.entity.Store;
import com.cocobambu.delivery.mapper.StoreMapper;
import com.cocobambu.delivery.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetAllStoresUseCaseTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper mapper;

    @InjectMocks
    private GetAllStoresUseCase getAllStoresUseCase;

    @Test
    @DisplayName("Should return a paginated list of mapped stores successfully")
    void shouldReturnPaginatedStoresSuccessfully() {
        // ARRANGE
        int page = 0;
        int size = 10;

        Pageable expectedPageable = PageRequest.of(page, size);

        Store store = new Store();
        StoreResponse responseDto = mock(StoreResponse.class);

        Page<Store> storePage = new PageImpl<>(List.of(store));

        when(storeRepository.findAll(expectedPageable)).thenReturn(storePage);
        when(mapper.toResponse(store)).thenReturn(responseDto);

        // ACT
        Page<StoreResponse> result = getAllStoresUseCase.execute(page, size);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().getFirst()).isEqualTo(responseDto);

        verify(storeRepository, times(1)).findAll(expectedPageable);
        verify(mapper, times(1)).toResponse(store);
    }

    @Test
    @DisplayName("Should return an empty page when no stores exist in the database")
    void shouldReturnEmptyPageWhenNoStoresExist() {
        // ARRANGE
        int page = 0;
        int size = 10;

        Pageable expectedPageable = PageRequest.of(page, size);

        Page<Store> emptyPage = new PageImpl<>(Collections.emptyList());

        when(storeRepository.findAll(expectedPageable)).thenReturn(emptyPage);

        // ACT
        Page<StoreResponse> result = getAllStoresUseCase.execute(page, size);

        // ASSERT
        assertThat(result).isNotNull();
        assertThat(result.isEmpty()).isTrue();

        verify(storeRepository, times(1)).findAll(expectedPageable);
        verify(mapper, never()).toResponse(any());
    }
}