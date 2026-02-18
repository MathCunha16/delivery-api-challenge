package com.cocobambu.delivery.controller.api;

import com.cocobambu.delivery.dto.response.StoreResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

@Tag(name = "Gerenciamento de Lojas", description = "Endpoints para consulta de lojas cadastradas")
public interface StoreApi {

    @Operation(
            summary = "Lista todas as lojas",
            description = "Retorna uma lista paginada com todas as lojas disponíveis no sistema. Útil para a tela de seleção de unidade."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Listagem retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    ResponseEntity<Page<StoreResponse>> getAllStores(
            @Parameter(description = "Número da página (0..N)", example = "0") int page,
            @Parameter(description = "Quantidade de itens por página", example = "10") int size
    );

    @Operation(
            summary = "Busca loja por ID",
            description = "Retorna os detalhes de uma loja específica baseada no seu UUID."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Loja encontrada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Loja não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    ResponseEntity<StoreResponse> getStoreById(
            @Parameter(description = "UUID da loja", required = true) UUID id
    );
}
