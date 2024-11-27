package com.ms.order.controller;

import com.ms.order.dtos.PedidoDTO;
import com.ms.order.dtos.PedidoRecebidoResponse;
import com.ms.order.producers.PedidoProducer;
import com.ms.order.service.PedidoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PedidoControllerTest {

    @InjectMocks
    private PedidoController pedidoController;

    @Mock
    private PedidoService pedidoService;

    @Mock
    private PedidoProducer pedidoProducer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testReceberPedido_Success() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        doNothing().when(pedidoService).enviarPedidoParaFilaRecebidos(any(PedidoDTO.class));

        ResponseEntity<?> response = pedidoController.receberPedido(pedidoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        PedidoRecebidoResponse responseBody = (PedidoRecebidoResponse) response.getBody();
        assertEquals("Pedido recebido com sucesso", responseBody.getMessage());
        verify(pedidoService, times(1)).enviarPedidoParaFilaRecebidos(pedidoDTO);
    }

    @Test
    void testReceberPedido_Failure() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        doThrow(new RuntimeException("Erro ao enviar pedido")).when(pedidoService).enviarPedidoParaFilaRecebidos(any(PedidoDTO.class));

        ResponseEntity<?> response = pedidoController.receberPedido(pedidoDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Erro ao processar o pedido: Erro ao enviar pedido", response.getBody());
    }

    @Test
    void testCriarPedido_Success() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        PedidoDTO pedidoCriado = new PedidoDTO();
        when(pedidoService.criarPedido(any(PedidoDTO.class))).thenReturn(pedidoCriado);
        doNothing().when(pedidoProducer).enviarPedidoParaFila(pedidoCriado);

        ResponseEntity<PedidoDTO> response = pedidoController.criarPedido(pedidoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pedidoCriado, response.getBody());
        verify(pedidoService, times(1)).criarPedido(pedidoDTO);
        verify(pedidoProducer, times(1)).enviarPedidoParaFila(pedidoCriado);
    }

    @Test
    void testCriarPedido_Failure() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        when(pedidoService.criarPedido(any(PedidoDTO.class))).thenThrow(new RuntimeException("Erro ao criar pedido"));

        ResponseEntity<PedidoDTO> response = pedidoController.criarPedido(pedidoDTO);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(pedidoService, times(1)).criarPedido(pedidoDTO);
    }

    @Test
    void testListarPedidos_Success() {
        PedidoDTO pedido1 = new PedidoDTO();
        PedidoDTO pedido2 = new PedidoDTO();
        List<PedidoDTO> pedidos = Arrays.asList(pedido1, pedido2);
        when(pedidoService.listarPedidos(any())).thenReturn(pedidos);

        ResponseEntity<List<PedidoDTO>> response = pedidoController.listarPedidos(null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pedidos, response.getBody());
        verify(pedidoService, times(1)).listarPedidos(null);
    }

    @Test
    void testAtualizarStatus_Success() {
        Long pedidoId = 1L;
        String novoStatus = "PROCESSADO";
        doNothing().when(pedidoService).atualizarStatusPedido(pedidoId, novoStatus);

        ResponseEntity<?> response = pedidoController.atualizarStatus(pedidoId, novoStatus);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Status atualizado com sucesso!", response.getBody());
        verify(pedidoService, times(1)).atualizarStatusPedido(pedidoId, novoStatus);
    }
}
