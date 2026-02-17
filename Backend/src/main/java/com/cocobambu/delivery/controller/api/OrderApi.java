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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Gerenciamento de pedidos", description = "Endpoints para gerenciar pedidos")
public interface OrderApi {

    @Operation(
            summary = "Cria um novo pedido",
            description = "Cria um novo pedido, e gera um json de response conforme o de exemplo do desafio"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro de validação (campos inválidos)", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    ResponseEntity<OrderWrapperResponse> createOrder(
            @Parameter(
                    description = "Informações do pedido (JSON)",
                    required = true,
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
            )
            @RequestBody @Valid CreateOrderRequest request);
}
