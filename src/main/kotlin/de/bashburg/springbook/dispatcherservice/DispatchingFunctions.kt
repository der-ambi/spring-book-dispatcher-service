package de.bashburg.springbook.dispatcherservice

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import reactor.core.publisher.Flux
import java.util.function.Function

private val logger = KotlinLogging.logger {}

@Configuration
class DispatchingFunctions {

    @Bean
    fun pack() =
        Function<OrderAcceptedMessage, Long> { orderAcceptedMessage ->
            logger.info { "The order with id ${orderAcceptedMessage.orderId} is packed" }
            orderAcceptedMessage.orderId

        }

    @Bean
    fun label() =
        Function<Flux<Long>, Flux<OrderDispatchedMessage>> { orderFlux ->
            orderFlux.map { orderId ->
                logger.info { "The order with id $orderId is labelled" }
                (OrderDispatchedMessage(orderId))
            }
        }
}