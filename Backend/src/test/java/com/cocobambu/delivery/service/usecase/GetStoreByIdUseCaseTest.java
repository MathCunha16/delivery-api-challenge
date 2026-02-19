package com.cocobambu.delivery.service.usecase;

import com.cocobambu.delivery.dto.response.StoreResponse;
import com.cocobambu.delivery.entity.Store;
import com.cocobambu.delivery.exception.ResourceNotFoundException;
import com.cocobambu.delivery.mapper.StoreMapper;
import com.cocobambu.delivery.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetStoreByIdUseCaseTest {

    @Mock
    private StoreRepository storeRepository;

    @Mock
    private StoreMapper mapper;

    @InjectMocks
    private GetStoreByIdUseCase getStoreByIdUseCase;

    @Test
    @DisplayName("Should return mapped store successfully when store exists")
    void shouldReturnMappedStoreSuccessfully() {
        // ARRANGE
        UUID storeId = UUID.randomUUID();
        Store store = new Store();
        StoreResponse expectedResponse = mock(StoreResponse.class);

        when(storeRepository.findById(storeId)).thenReturn(Optional.of(store));
        when(mapper.toResponse(store)).thenReturn(expectedResponse);

        // ACT
        StoreResponse actualResponse = getStoreByIdUseCase.execute(storeId);

        // ASSERT
        assertThat(actualResponse)
                .isNotNull()
                .isEqualTo(expectedResponse);

        verify(storeRepository, times(1)).findById(storeId);
        verify(mapper, times(1)).toResponse(store);
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when store does not exist")
    void shouldThrowExceptionWhenStoreNotFound() {
        // ARRANGE
        UUID storeId = UUID.randomUUID();

        when(storeRepository.findById(storeId)).thenReturn(Optional.empty());

        // ACT & ASSERT
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () ->
                getStoreByIdUseCase.execute(storeId)
        );

        assertThat(exception.getMessage()).isEqualTo("No store was found with id:" + storeId);

        verify(storeRepository, times(1)).findById(storeId);
        verifyNoInteractions(mapper);
    }
}