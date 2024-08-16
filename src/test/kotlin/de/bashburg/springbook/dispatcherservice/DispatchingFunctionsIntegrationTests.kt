package de.bashburg.springbook.dispatcherservice

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cloud.function.context.FunctionCatalog
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import java.util.function.Function
import kotlin.test.Test

@FunctionalSpringBootTest
class DispatchingFunctionsIntegrationTests {

    @Autowired
    lateinit var functionCatalog: FunctionCatalog

    @Test
    fun `pack and label order`() {
        val packAndLabel: Function<OrderAcceptedMessage, Flux<OrderDispatchedMessage>> =
            functionCatalog.lookup(Function::class.java, "pack|label")

        val orderId = 123L

        StepVerifier.create(
            packAndLabel.apply(OrderAcceptedMessage(orderId))
        )
            .expectNextMatches { it -> it.equals(OrderDispatchedMessage(orderId)) }
            .verifyComplete()
    }
}