package com.cocobambu.delivery.controller.api;

import com.cocobambu.delivery.dto.request.CreateOrderRequest;
import com.cocobambu.delivery.dto.request.UpdateOrderRequest;
import com.cocobambu.delivery.dto.request.UpdateOrderStatusRequest;
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
            @ApiResponse(responseCode = "400", description = "Erro de validação (campos inválidos), erro de mismatch de valor pago, ou metódo de pagamento invalído", content = @Content),
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

    @Operation(summary = "Atualiza status do pedido", description = "Avança o status do pedido seguindo as regras da máquina de estados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Transição de status inválida", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    ResponseEntity<OrderWrapperResponse> updateOrderStatus(
            @Parameter(description = "ID do pedido") UUID id,
            @Parameter(description = "Novo status") UpdateOrderStatusRequest request
    );

    @Operation(
            summary = "Exclui um pedido",
            description = "Remove permanentemente um pedido do sistema. " +
                    "**Regra de Negócio:** Apenas pedidos finalizados (status **CANCELED** ou **DELIVERED**) podem ser excluídos. " +
                    "Pedidos em andamento (RECEIVED, CONFIRMED, DISPATCHED) gerarão erro."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pedido excluído com sucesso (sem conteúdo)", content = @Content),
            @ApiResponse(responseCode = "400", description = "Erro: Tentativa de excluir pedido em andamento", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content)
    })
    ResponseEntity<Void> deleteOrder(
            @Parameter(description = "UUID do pedido a ser excluído", required = true) UUID id
    );

    @Operation(
            summary = "Atualiza dados do pedido",
            description = "Atualiza informações cadastrais do pedido (cliente e endereço). " +
                    "**Regra de Negócio:** Só é permitido alterar pedidos que ainda não saíram para entrega (status **RECEIVED** ou **CONFIRMED**)."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Erro de validação ou status do pedido não permite alteração", content = @Content),
            @ApiResponse(responseCode = "404", description = "Pedido não encontrado", content = @Content)
    })
    ResponseEntity<OrderWrapperResponse> updateOrder(
            @Parameter(description = "UUID do pedido") UUID id,
            @Parameter(description = "Novos dados do pedido") UpdateOrderRequest request
    );
}
