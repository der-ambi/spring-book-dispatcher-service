package de.bashburg.springbook.dispatcherservice

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux

private val logger = KotlinLogging.logger {}

@Configuration
class DispatchingFunctions {

    @Bean
    fun pack(): (OrderAcceptedMessage) -> Long = { orderAcceptedMessage ->
        logger.info { "The order with id ${orderAcceptedMessage.orderId} is packed" }
        orderAcceptedMessage.orderId
    }

    @Bean
    fun label(): (Flux<Long>) -> Flux<OrderDispatchedMessage> =
        { orderFlux ->
            orderFlux.map { orderId ->
                logger.info { "The order with id $orderId is labelled" }
                (OrderDispatchedMessage(orderId))
            }
        }
}