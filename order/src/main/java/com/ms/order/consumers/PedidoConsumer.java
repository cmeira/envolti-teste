package com.ms.order.consumers;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ms.order.config.RabbitMQConfig;
import com.ms.order.dtos.PedidoDTO;
import com.ms.order.producers.PedidoProducer;
import com.ms.order.service.PedidoService;

@Service
public class PedidoConsumer {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private PedidoProducer pedidoProducer; 

   @RabbitListener(queues = RabbitMQConfig.FILA_PEDIDOS, containerFactory = "rabbitListenerContainerFactory")
    public void processarPedido(PedidoDTO pedidoDTO) {
        System.out.println("a"+pedidoDTO);
        //Chama o serviço para criar e processar o pedido
        PedidoDTO pedidoProcessado = pedidoService.criarPedido(pedidoDTO);

        // Envia o pedido processado para o Sistema Externo B
        pedidoProducer.enviarPedidoParaSistemaExternoB(pedidoProcessado);
    }
}
