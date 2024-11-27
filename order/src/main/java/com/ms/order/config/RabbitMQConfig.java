package com.ms.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Definição da fila "pedidos"
    public static final String FILA_PEDIDOS = "pedidos";
    
    // Definição da fila "pedidos_calculados"
    public static final String FILA_PEDIDOS_CALCULADOS = "pedidos_calculados";

    // Exchange para comunicação
    public static final String EXCHANGE = "pedidoExchange";

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Queue filaPedidos() {
        return new Queue(FILA_PEDIDOS, true);  // Persistente
    }

    @Bean
    public Queue filaPedidosCalculados() {
        return new Queue(FILA_PEDIDOS_CALCULADOS, true);  // Persistente
    }

    @Bean
    public Jackson2JsonMessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory,
                                         Jackson2JsonMessageConverter converter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory, 
            Jackson2JsonMessageConverter converter) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(converter); // Define o conversor de mensagens
        factory.setDefaultRequeueRejected(false);
        factory.setPrefetchCount(1);
        return factory;
    }
    

    @Bean
    public Binding bindingFilaPedidos() {
        return BindingBuilder.bind(filaPedidos()).to(exchange()).with("pedidos.*");
    }

    @Bean
    public Binding bindingFilaPedidosCalculados() {
        return BindingBuilder.bind(filaPedidosCalculados()).to(exchange()).with("pedidos_calculados.*");
    }
}
