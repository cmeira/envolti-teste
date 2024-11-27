package com.ms.order.producers;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ms.order.dtos.PedidoDTO;

@Component
public class PedidoProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;  

    public PedidoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    
    public static final String FILA_PEDIDOS = "pedidos";
    private static final String FILA_EXTERNO_B = "pedidos_calculados";

    public void enviarPedidoParaFila(PedidoDTO pedidoDTO) {

        // Envia a mensagem para a fila RabbitMQ
        rabbitTemplate.convertAndSend(FILA_PEDIDOS, pedidoDTO);
        System.out.println("Pedido enviado para a fila: " + pedidoDTO);
    }

    /**
     * Envia um pedido calculado para a fila do sistema externo B.
     *
     * @param pedidoDTO Pedido calculado no formato DTO.
     */
    public void enviarPedidoParaSistemaExternoB(PedidoDTO pedidoDTO) {
        try {
            rabbitTemplate.convertAndSend(FILA_EXTERNO_B, pedidoDTO);
            System.out.println("Pedido enviado ao sistema externo B: " + pedidoDTO.getId());
        } catch (Exception e) {
            System.err.println("Erro ao enviar pedido para o sistema externo B: " + e.getMessage());
        }
    }
}
