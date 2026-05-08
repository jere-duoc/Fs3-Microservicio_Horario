package DuocQuin.Horarios.rabbitmq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;

@Configuration
public class RabbitConfig {

    @Bean
    public Queue queue() {
        return new Queue("cola_notificaciones", true);
    }

    //exchange para recibir mensaje del productor
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("duocquin.exchange");
    }

    //binding es la relacion entre el exchange y una cola
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder
            .bind(queue)
            .to(exchange)
            .with("horario.creado");
    }

    //Serializador de json
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    //Rabbit templatr
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}
