package com.cocobambu.delivery.controller.api;

import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.response.OrderWrapperResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@Tag(name = "Gerenciamento de pedidos", description = "Endpoints para gerenciar pedidos")
public interface OrderApi {

    @Operation(
            summary = "Cria um novo pedido",
            description = "Cria um novo pedido, e gera um json de response conforme o de exemplo do desafio"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação (campos inválidos) ou erro de mismatch de valor pago", content = @Content),
            @ApiResponse(responseCode = "404", description = "Erro, não foi possível encontrar loja com o id fornecido", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    ResponseEntity<OrderWrapperResponse> createOrder(
            @Parameter(
                    description = "Informações do pedido (JSON)",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @RequestBody @Valid CreateOrderRequest request);

    @Operation(
            summary = "Lista pedidos paginados",
            description = "Retorna lista paginada ordenada por data de criação (decrescente)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    ResponseEntity<Page<OrderWrapperResponse>> getAllOrders(
            @Parameter(description = "Número da página (padrão 0)") int page,
            @Parameter(description = "Itens por página (padrão 10)") int size
    );

    @Operation(summary = "Busca pedido por ID", description = "Retorna os detalhes completos de um pedido específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado"),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    ResponseEntity<OrderWrapperResponse> getOrderById(
            @Parameter(description = "UUID do pedido") UUID id);

    @Operation(summary = "Lista pedidos de uma loja", description = "Retorna lista paginada de pedidos filtrados por loja.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Loja não encontrada", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    ResponseEntity<Page<OrderWrapperResponse>> getOrdersByStore(
            @Parameter(description = "UUID da loja") UUID storeId,
            @Parameter(description = "Página") int page,
            @Parameter(description = "Tamanho") int size
    );
}
