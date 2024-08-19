package de.bashburg.springbook.dispatcherservice

import com.fasterxml.jackson.databind.ObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.stream.binder.test.InputDestination
import org.springframework.cloud.stream.binder.test.OutputDestination
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration
import org.springframework.context.annotation.Import
import org.springframework.integration.support.MessageBuilder
import kotlin.test.Test

@SpringBootTest
@Import(TestChannelBinderConfiguration::class)
class FunctionsStreamIntegrationTests {

    @Autowired
    lateinit var input: InputDestination

    @Autowired
    lateinit var output: OutputDestination

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `when order is accepted then dispatch`() {
        val orderId = 121L;
        val inputMessage = MessageBuilder.withPayload(OrderAcceptedMessage(orderId)).build()
        val expectedOutputMessage = MessageBuilder.withPayload(OrderDispatchedMessage(orderId)).build()

        input.send(inputMessage)

        val receivedMessage = objectMapper.readValue(output.receive().payload, OrderDispatchedMessage::class.java)

        assertThat(receivedMessage).isEqualTo(expectedOutputMessage.payload)
    }
}