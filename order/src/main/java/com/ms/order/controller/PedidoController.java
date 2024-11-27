package com.ms.order.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ms.order.dtos.PedidoDTO;
import com.ms.order.dtos.PedidoRecebidoResponse;
import com.ms.order.model.Pedido;
import com.ms.order.producers.PedidoProducer;
import com.ms.order.service.PedidoService;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;
    
    @Autowired
    private PedidoProducer pedidoProducer;
    @PostMapping(value="/receber" ,produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> receberPedido(@RequestBody PedidoDTO pedidoDTO) {
        try {
        // Envia o pedido para a fila de pedidos recebidos (simula o recebimento do Sistema A)
        pedidoService.enviarPedidoParaFilaRecebidos(pedidoDTO);
        PedidoRecebidoResponse response = new PedidoRecebidoResponse(
            "Pedido recebido com sucesso",
            "O pedido foi colocado na fila e será processado em breve."
        );

        return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(response);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body("Erro ao processar o pedido: " + e.getMessage());
    }
    }

    // Criação de um pedido
       @PostMapping
    public ResponseEntity<PedidoDTO> criarPedido(@RequestBody PedidoDTO pedidoDTO) {
        try {
            // Cria o pedido no banco de dados e retorna o pedido salvo
            PedidoDTO pedidoCriado = pedidoService.criarPedido(pedidoDTO);

            // Envia o pedido para a fila de pedidos
            pedidoProducer.enviarPedidoParaFila(pedidoCriado);

            // Retorna o pedido criado com status 201
            return new ResponseEntity<>(pedidoCriado, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Listar pedidos (por status, se fornecido)
    @GetMapping
    public ResponseEntity<List<PedidoDTO>> listarPedidos(@RequestParam(required = false) String status) {
        return ResponseEntity.ok(pedidoService.listarPedidos(status));
    }

    // Atualizar o status de um pedido
    @PutMapping("/{id}/status")
    public ResponseEntity<?> atualizarStatus(@PathVariable Long id, @RequestParam String status) {
        pedidoService.atualizarStatusPedido(id, status);
        return ResponseEntity.ok("Status atualizado com sucesso!");
    }
}
